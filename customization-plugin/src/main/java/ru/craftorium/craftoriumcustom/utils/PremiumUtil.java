package ru.craftorium.craftoriumcustom.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.InventoryView;
import ru.craftorium.craftoriumcustom.CraftoriumCustom;
import ru.craftorium.craftoriumcustom.db.DatabaseManager;
import ru.craftorium.craftoriumcustom.holders.PremPearlMenuHolder;

/**
 * Helpers around premium subscription handling:
 *  - parse durations like "1d", "30m", "2h", "1w";
 *  - read / write expiration timestamp in player data;
 *  - format remaining time (minute / hour / day);
 *  - revoke expired premium permissions.
 */
public final class PremiumUtil {

    private static final SimpleDateFormat DATE_FMT =
            new SimpleDateFormat("dd.MM.yyyy HH:mm", new Locale("ru"));

    private PremiumUtil() {
    }

    /** Parses 1d / 12h / 30m / 1w into milliseconds; returns -1 if invalid. */
    public static long parseDurationMillis(String input) {
        if (input == null || input.isEmpty()) {
            return -1L;
        }
        String s = input.trim().toLowerCase(Locale.ROOT);
        char unit = s.charAt(s.length() - 1);
        String numPart = s.substring(0, s.length() - 1);
        long n;
        try {
            n = Long.parseLong(numPart);
        } catch (NumberFormatException e) {
            return -1L;
        }
        if (n <= 0) {
            return -1L;
        }
        switch (unit) {
            case 'm':
                return n * 60_000L;
            case 'h':
                return n * 60L * 60_000L;
            case 'd':
                return n * 24L * 60L * 60_000L;
            case 'w':
                return n * 7L * 24L * 60L * 60_000L;
            default:
                return -1L;
        }
    }

    /** Reads the saved expiration epoch-ms for the player (0 if missing/permanent). */
    public static long readExpiration(Player player) {
        if (player == null) {
            return 0L;
        }
        HashMap<String, String> data = CraftoriumCustom.getPlayerData(player);
        if (data == null) {
            return 0L;
        }
        String raw = data.getOrDefault("premium_until", null);
        if (raw == null || raw.isEmpty()) {
            return 0L;
        }
        try {
            return Long.parseLong(raw);
        } catch (NumberFormatException e) {
            return 0L;
        }
    }

    /** Writes the expiration timestamp into player data. */
    public static void writeExpiration(Player player, long epochMillis) {
        if (player == null) {
            return;
        }
        if (epochMillis <= 0L) {
            CraftoriumCustom.removePlayerData(player, "premium_until");
        } else {
            CraftoriumCustom.setPlayerData(player, "premium_until", String.valueOf(epochMillis));
        }
    }

    /**
     * Returns a human-readable remaining time:
     *  - "" if no premium;
     *  - "бессрочно" if premium without expiration (granted manually);
     *  - "Xд Yч" / "Xч Yм" / "Xм" depending on magnitude;
     *  - "истекло" if expired.
     */
    public static String formatRemaining(Player player) {
        if (player == null || !player.hasPermission("stickhwcustom.prem")) {
            return "";
        }
        long until = readExpiration(player);
        if (until <= 0L) {
            return "бессрочно";
        }
        long now = System.currentTimeMillis();
        long left = until - now;
        if (left <= 0L) {
            return "истекло";
        }
        long minutes = left / 60_000L;
        long hours = minutes / 60L;
        long days = hours / 24L;
        if (days > 0L) {
            long remHours = hours % 24L;
            return days + "д " + remHours + "ч";
        }
        if (hours > 0L) {
            long remMin = minutes % 60L;
            return hours + "ч " + remMin + "м";
        }
        return minutes + "м";
    }

    /** Returns formatted date when premium will expire ("бессрочно" if no expiration set). */
    public static String formatUntil(Player player) {
        if (player == null || !player.hasPermission("stickhwcustom.prem")) {
            return "";
        }
        long until = readExpiration(player);
        if (until <= 0L) {
            return "бессрочно";
        }
        synchronized (DATE_FMT) {
            return DATE_FMT.format(new Date(until));
        }
    }

    /**
     * Splits remaining time into minutes / hours / days for the Information menu lore.
     *
     * @return long[3]{minutes,hours,days} of TOTAL remaining time; all zeros if expired.
     */
    public static long[] remainingMinutesHoursDays(Player player) {
        long[] result = new long[]{0L, 0L, 0L};
        if (player == null || !player.hasPermission("stickhwcustom.prem")) {
            return result;
        }
        long until = readExpiration(player);
        if (until <= 0L) {
            return new long[]{-1L, -1L, -1L};
        }
        long left = until - System.currentTimeMillis();
        if (left <= 0L) {
            return result;
        }
        long minutes = left / 60_000L;
        long hours = minutes / 60L;
        long days = hours / 24L;
        return new long[]{minutes, hours, days};
    }

    /**
     * Grants premium for the parsed duration. Calls LuckPerms to give the permission node
     * and stores expiration in player data. Works for online & offline targets.
     */
    public static boolean givePremium(CommandSender sender, String targetName, long durationMs) {
        if (durationMs <= 0L) {
            return false;
        }
        long until = System.currentTimeMillis() + durationMs;
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
                "lp user " + targetName + " permission set stickhwcustom.prem true");
        Player online = Bukkit.getPlayer(targetName);
        if (online != null) {
            writeExpiration(online, until);
        } else {
            // offline — read+update+write asynchronously
            DatabaseManager.getPlayerAsync(targetName).thenAccept(base64 -> {
                HashMap<String, String> data = base64 != null
                        ? DatabaseManager.fromBase64(base64)
                        : new HashMap<>();
                data.put("premium_until", String.valueOf(until));
                DatabaseManager.setPlayerAsync(targetName, DatabaseManager.toBase64(data));
            });
        }
        return true;
    }

    /**
     * Revokes premium permission and clears every premium-tied piece of state
     * for the player so the change is felt instantly:
     *  - removes the LuckPerms permission;
     *  - clears the expiration entry;
     *  - clears the saved pearl particle (premium-only effect);
     *  - closes the Premium menu if the player is currently looking at it.
     */
    public static void revoke(Player player) {
        if (player == null) {
            return;
        }
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
                "lp user " + player.getName() + " permission unset stickhwcustom.prem");
        writeExpiration(player, 0L);
        // Premium-only data: pearl particle effect.
        CraftoriumCustom.removePlayerData(player, "pearl_particle");
        // If the player is currently in the Premium menu, kick them out so the
        // UI reflects the change immediately.
        InventoryView open = player.getOpenInventory();
        if (open != null && open.getTopInventory() != null) {
            InventoryHolder holder = open.getTopInventory().getHolder();
            if (holder instanceof PremPearlMenuHolder) {
                player.closeInventory();
            }
        }
    }

    /**
     * Checks all online players and revokes premium for those whose expiration is reached.
     * Called from a repeating scheduler.
     */
    public static void enforceExpirations() {
        long now = System.currentTimeMillis();
        for (Player player : Bukkit.getOnlinePlayers()) {
            long until = readExpiration(player);
            if (until > 0L && until <= now && player.hasPermission("stickhwcustom.prem")) {
                revoke(player);
                player.sendMessage(HexUtil.translate(
                        "&#ff2222\u25b6 &f\u0421\u0440\u043e\u043a \u0434\u0435\u0439\u0441\u0442\u0432\u0438\u044f \u043f\u0440\u0435\u043c\u0438\u0443\u043c \u0438\u0441\u0442\u0451\u043a."));
            }
        }
    }
}
