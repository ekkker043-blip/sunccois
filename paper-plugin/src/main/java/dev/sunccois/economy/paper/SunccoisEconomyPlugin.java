package dev.sunccois.economy.paper;

import dev.sunccois.economy.common.Channels;
import dev.sunccois.economy.paper.command.EcoCommand;
import dev.sunccois.economy.paper.db.Database;
import dev.sunccois.economy.paper.economy.EconomyService;
import dev.sunccois.economy.paper.listener.PlayerJoinListener;
import dev.sunccois.economy.paper.listener.PluginMessageListener;
import dev.sunccois.economy.paper.placeholder.EconomyPlaceholders;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Entry point for the Paper-side plugin.
 *
 * Lifecycle guarantees:
 *  - Every write ({@code /eco give|take|reset}) is persisted to MySQL inside a
 *    transaction BEFORE the command returns. No in-memory deferral exists,
 *    so a server crash / kill -9 cannot lose money.
 *  - The read-cache is invalidated via a Velocity-routed plugin message,
 *    so balances stay consistent across servers.
 */
public final class SunccoisEconomyPlugin extends JavaPlugin {

    private Database database;
    private EconomyService economy;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        reloadConfig();

        try {
            this.database = new Database(this);
            this.database.initialize();
        } catch (Exception ex) {
            getLogger().severe("Не удалось подключиться к MySQL: " + ex.getMessage());
            ex.printStackTrace();
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        String serverName = getConfig().getString("server-name", "unknown");
        int cacheTtl = getConfig().getInt("economy.read-cache-ttl-seconds", 5);
        long maxBalance = getConfig().getLong("economy.maximum-balance", 1_000_000_000_000L);
        long startingBalance = getConfig().getLong("economy.starting-balance", 0);

        this.economy = new EconomyService(this, database, serverName,
                cacheTtl, maxBalance, startingBalance);

        // Plugin messaging channel — outgoing and incoming.
        Bukkit.getMessenger().registerOutgoingPluginChannel(this, Channels.CHANNEL);
        Bukkit.getMessenger().registerIncomingPluginChannel(this, Channels.CHANNEL,
                new PluginMessageListener(economy));

        // Commands and listeners.
        EcoCommand ecoCommand = new EcoCommand(this, economy);
        getCommand("eco").setExecutor(ecoCommand);
        getCommand("eco").setTabCompleter(ecoCommand);

        getServer().getPluginManager().registerEvents(
                new PlayerJoinListener(economy), this);

        // PlaceholderAPI is optional.
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new EconomyPlaceholders(this, economy).register();
            getLogger().info("Интеграция с PlaceholderAPI включена.");
        }

        getLogger().info("SunccoisEconomy запущен. Server ID = " + serverName);
    }

    @Override
    public void onDisable() {
        // No cache flushing needed — all writes are already in MySQL.
        if (economy != null) economy.shutdown();
        if (database != null) database.close();
    }

    public EconomyService economy() {
        return economy;
    }
}
