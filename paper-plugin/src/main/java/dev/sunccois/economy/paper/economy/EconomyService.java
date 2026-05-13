package dev.sunccois.economy.paper.economy;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import dev.sunccois.economy.common.Channels;
import dev.sunccois.economy.paper.SunccoisEconomyPlugin;
import dev.sunccois.economy.paper.db.Database;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;

/**
 * Core economy logic. All mutating operations are transactional:
 *   BEGIN;
 *     SELECT ... FOR UPDATE;   -- row lock, blocks concurrent writers
 *     UPDATE ...;
 *     INSERT INTO eco_transactions ...;
 *   COMMIT;
 *
 * That guarantees:
 *   - no dupe if two servers try to give the same player at the same time
 *     (one waits for the lock; both effects apply in serial order);
 *   - no lost money on crash: every successful operation is durably committed
 *     before the method returns.
 *
 * The only cache is a short-lived read-cache for balance lookups, invalidated
 * on every local write AND on every remote write via Velocity-routed plugin
 * messages (see {@link dev.sunccois.economy.paper.listener.PluginMessageListener}).
 */
public final class EconomyService {

    public enum OpResult {
        OK,
        PLAYER_NOT_FOUND,
        NOT_ENOUGH_FUNDS,
        CAP_EXCEEDED,
        INVALID_AMOUNT,
        DB_ERROR
    }

    /** Result of a mutation, including the resulting balance when OK. */
    public record MutationOutcome(OpResult result, long newBalance) {
        public static MutationOutcome of(OpResult r) { return new MutationOutcome(r, -1L); }
        public static MutationOutcome ok(long nb) { return new MutationOutcome(OpResult.OK, nb); }
    }

    private final SunccoisEconomyPlugin plugin;
    private final Database database;
    private final String serverName;
    private final long maxBalance;
    private final long startingBalance;

    private final Cache<UUID, Long> readCache;
    private final ExecutorService ioPool;

    public EconomyService(SunccoisEconomyPlugin plugin, Database database,
                          String serverName, int cacheTtlSeconds,
                          long maxBalance, long startingBalance) {
        this.plugin = plugin;
        this.database = database;
        this.serverName = serverName;
        this.maxBalance = Math.min(maxBalance, Long.MAX_VALUE / 2);
        this.startingBalance = Math.max(0L, startingBalance);

        this.readCache = Caffeine.newBuilder()
                .expireAfterWrite(java.time.Duration.ofSeconds(Math.max(1, cacheTtlSeconds)))
                .maximumSize(10_000)
                .build();

        final AtomicInteger id = new AtomicInteger();
        ThreadFactory tf = r -> {
            Thread t = new Thread(r, "SunccoisEconomy-IO-" + id.incrementAndGet());
            t.setDaemon(true);
            return t;
        };
        this.ioPool = Executors.newFixedThreadPool(4, tf);
    }

    public void shutdown() {
        ioPool.shutdown();
    }

    /** Invalidate the read cache. Called locally after writes and on inbound messages. */
    public void invalidate(UUID uuid) {
        if (uuid != null) readCache.invalidate(uuid);
    }

    /* ---------------- Lookups ---------------- */

    /** Resolve an online OR offline player name -> UUID. Returns empty if never seen. */
    public Optional<UUID> resolveUuid(String name) {
        Player online = Bukkit.getPlayerExact(name);
        if (online != null) return Optional.of(online.getUniqueId());

        // Check DB first to respect what the economy knows.
        try (Connection c = database.getConnection();
             PreparedStatement ps = c.prepareStatement(
                     "SELECT uuid FROM eco_accounts WHERE username = ? LIMIT 1")) {
            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(UUID.fromString(rs.getString(1)));
            }
        } catch (SQLException e) {
            plugin.getLogger().log(Level.WARNING, "resolveUuid failed", e);
            return Optional.empty();
        }

        // Fallback: OfflinePlayer with hasPlayedBefore.
        @SuppressWarnings("deprecation")
        OfflinePlayer op = Bukkit.getOfflinePlayer(name);
        if (op.hasPlayedBefore() || op.isOnline()) return Optional.of(op.getUniqueId());
        return Optional.empty();
    }

    /** Cached read. Safe to call from async; blocks briefly on cache miss. */
    public long getBalance(UUID uuid) {
        Long cached = readCache.getIfPresent(uuid);
        if (cached != null) return cached;

        try (Connection c = database.getConnection();
             PreparedStatement ps = c.prepareStatement(
                     "SELECT balance FROM eco_accounts WHERE uuid = ?")) {
            ps.setString(1, uuid.toString());
            try (ResultSet rs = ps.executeQuery()) {
                long bal = rs.next() ? rs.getLong(1) : 0L;
                readCache.put(uuid, bal);
                return bal;
            }
        } catch (SQLException e) {
            plugin.getLogger().log(Level.WARNING, "getBalance failed", e);
            return 0L;
        }
    }

    /** Make sure a row exists for this player. Called on join / before writes. */
    public void ensureAccount(UUID uuid, String username) {
        try (Connection c = database.getConnection();
             PreparedStatement ps = c.prepareStatement(
                     "INSERT INTO eco_accounts (uuid, username, balance) VALUES (?,?,?) " +
                             "ON DUPLICATE KEY UPDATE username = VALUES(username)")) {
            ps.setString(1, uuid.toString());
            ps.setString(2, username);
            ps.setLong(3, startingBalance);
            ps.executeUpdate();
        } catch (SQLException e) {
            plugin.getLogger().log(Level.WARNING, "ensureAccount failed", e);
        }
    }

    /* ---------------- Mutations ---------------- */

    public CompletableFuture<MutationOutcome> giveAsync(UUID uuid, long amount, String actor) {
        return CompletableFuture.supplyAsync(() -> give(uuid, amount, actor), ioPool);
    }

    public CompletableFuture<MutationOutcome> takeAsync(UUID uuid, long amount, String actor) {
        return CompletableFuture.supplyAsync(() -> take(uuid, amount, actor), ioPool);
    }

    public CompletableFuture<MutationOutcome> resetAsync(UUID uuid, String actor) {
        return CompletableFuture.supplyAsync(() -> reset(uuid, actor), ioPool);
    }

    public CompletableFuture<Long> getBalanceAsync(UUID uuid) {
        return CompletableFuture.supplyAsync(() -> getBalance(uuid), ioPool);
    }

    private MutationOutcome give(UUID uuid, long amount, String actor) {
        if (amount <= 0) return MutationOutcome.of(OpResult.INVALID_AMOUNT);
        return mutate(uuid, amount, "GIVE", actor);
    }

    private MutationOutcome take(UUID uuid, long amount, String actor) {
        if (amount <= 0) return MutationOutcome.of(OpResult.INVALID_AMOUNT);
        return mutate(uuid, -amount, "TAKE", actor);
    }

    private MutationOutcome reset(UUID uuid, String actor) {
        try (Connection c = database.getConnection()) {
            c.setAutoCommit(false);
            try {
                Long current = lockRowForUpdate(c, uuid);
                if (current == null) {
                    c.rollback();
                    return MutationOutcome.of(OpResult.PLAYER_NOT_FOUND);
                }
                long delta = -current;
                try (PreparedStatement u = c.prepareStatement(
                        "UPDATE eco_accounts SET balance = 0 WHERE uuid = ?")) {
                    u.setString(1, uuid.toString());
                    u.executeUpdate();
                }
                writeTxn(c, uuid, "RESET", delta, 0L, actor);
                c.commit();
                onLocalChange(uuid);
                return MutationOutcome.ok(0L);
            } catch (SQLException ex) {
                safeRollback(c);
                plugin.getLogger().log(Level.SEVERE, "reset failed for " + uuid, ex);
                return MutationOutcome.of(OpResult.DB_ERROR);
            } finally {
                c.setAutoCommit(true);
            }
        } catch (SQLException e) {
            plugin.getLogger().log(Level.SEVERE, "reset: cannot acquire connection", e);
            return MutationOutcome.of(OpResult.DB_ERROR);
        }
    }

    /** Core transactional mutation for give (+delta) and take (-delta). */
    private MutationOutcome mutate(UUID uuid, long delta, String type, String actor) {
        try (Connection c = database.getConnection()) {
            c.setAutoCommit(false);
            try {
                Long current = lockRowForUpdate(c, uuid);
                if (current == null) {
                    c.rollback();
                    return MutationOutcome.of(OpResult.PLAYER_NOT_FOUND);
                }

                long next;
                try {
                    next = Math.addExact(current, delta);
                } catch (ArithmeticException overflow) {
                    c.rollback();
                    return MutationOutcome.of(OpResult.CAP_EXCEEDED);
                }
                if (next < 0) {
                    c.rollback();
                    return MutationOutcome.of(OpResult.NOT_ENOUGH_FUNDS);
                }
                if (next > maxBalance) {
                    c.rollback();
                    return MutationOutcome.of(OpResult.CAP_EXCEEDED);
                }

                try (PreparedStatement u = c.prepareStatement(
                        "UPDATE eco_accounts SET balance = ? WHERE uuid = ?")) {
                    u.setLong(1, next);
                    u.setString(2, uuid.toString());
                    u.executeUpdate();
                }
                writeTxn(c, uuid, type, delta, next, actor);
                c.commit();
                onLocalChange(uuid);
                return MutationOutcome.ok(next);
            } catch (SQLException ex) {
                safeRollback(c);
                plugin.getLogger().log(Level.SEVERE, "mutate failed for " + uuid, ex);
                return MutationOutcome.of(OpResult.DB_ERROR);
            } finally {
                c.setAutoCommit(true);
            }
        } catch (SQLException e) {
            plugin.getLogger().log(Level.SEVERE, "mutate: cannot acquire connection", e);
            return MutationOutcome.of(OpResult.DB_ERROR);
        }
    }

    private Long lockRowForUpdate(Connection c, UUID uuid) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement(
                "SELECT balance FROM eco_accounts WHERE uuid = ? FOR UPDATE")) {
            ps.setString(1, uuid.toString());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getLong(1);
                return null;
            }
        }
    }

    private void writeTxn(Connection c, UUID uuid, String type, long delta,
                          long balanceAfter, String actor) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement(
                "INSERT INTO eco_transactions " +
                "(account_uuid, type, delta, balance_after, source_server, actor) " +
                "VALUES (?,?,?,?,?,?)")) {
            ps.setString(1, uuid.toString());
            ps.setString(2, type);
            ps.setLong(3, delta);
            ps.setLong(4, balanceAfter);
            ps.setString(5, serverName);
            ps.setString(6, actor != null ? actor : "CONSOLE");
            ps.executeUpdate();
        }
    }

    private static void safeRollback(Connection c) {
        try { c.rollback(); } catch (SQLException ignored) {}
    }

    /**
     * Called after every successful local write:
     *   1. invalidate local cache,
     *   2. broadcast an INVALIDATE message through Velocity so other
     *      backends drop their cached copy.
     */
    private void onLocalChange(UUID uuid) {
        invalidate(uuid);
        broadcastInvalidate(uuid);
    }

    private void broadcastInvalidate(UUID uuid) {
        // We need an online player to pipe the message through Velocity's
        // BungeeCord channel. If nobody is online, we simply skip — other
        // backends will pick up the new value on their next cache expiry.
        Player any = Bukkit.getOnlinePlayers().stream().findAny().orElse(null);
        if (any == null) return;

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(baos);
            out.writeByte(Channels.PROTOCOL_VERSION);
            out.writeUTF(Channels.SUB_INVALIDATE);
            out.writeUTF(uuid.toString());
            out.writeUTF(serverName);
            any.sendPluginMessage(plugin, Channels.CHANNEL, baos.toByteArray());
        } catch (IOException e) {
            plugin.getLogger().log(Level.WARNING, "broadcastInvalidate failed", e);
        }
    }
}
