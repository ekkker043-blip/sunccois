package ru.craftorium.craftoriumcustom.commands;

import java.util.HashMap;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import ru.craftorium.craftoriumcustom.CraftoriumCustom;
import ru.craftorium.craftoriumcustom.db.DatabaseManager;
import ru.craftorium.craftoriumcustom.menus.MainMenu;
import ru.craftorium.craftoriumcustom.utils.HexUtil;
import ru.craftorium.craftoriumcustom.utils.PremiumUtil;

public class CustomCommand implements CommandExecutor {
    private final MainMenu menu;

    public CustomCommand(MainMenu menu) {
        this.menu = menu;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // /cust give star <nick> <level>
        // /cust give premium <nick> <duration>  e.g. 1d, 12h, 30m, 1w
        if (args.length >= 4 && args[0].equalsIgnoreCase("give")) {
            String kind = args[1].toLowerCase();
            String targetName = args[2];
            String value = args[3];
            if (kind.equals("star")) {
                if (!sender.hasPermission("stickhwcustom.star.give")) {
                    return noPermission(sender);
                }
                return giveStarLevel(sender, targetName, value);
            }
            if (kind.equals("premium")) {
                if (!sender.hasPermission("stickhwcustom.premium.give")) {
                    return noPermission(sender);
                }
                return givePremium(sender, targetName, value);
            }
            return usage(sender);
        }
        // /cust star <nick> <rubles>  (existing behaviour: adds rubles to star_points)
        if (args.length >= 3 && args[0].equalsIgnoreCase("star")) {
            if (!sender.hasPermission("stickhwcustom.star.give")) {
                return noPermission(sender);
            }
            return addStarPoints(sender, args[1], args[2]);
        }
        // /cust premium <nick>  (legacy toggle — kept for backwards compatibility)
        if (args.length >= 2 && args[0].equalsIgnoreCase("premium")
                && sender.hasPermission("stickhwcustom.premium.give")) {
            return legacyTogglePremium(sender, args[1]);
        }
        if (args.length >= 1 && args[0].equalsIgnoreCase("reload")
                && sender.hasPermission("stickhwcustom.reload")) {
            CraftoriumCustom.getInstance().reloadConfig();
            sender.sendMessage(HexUtil.translate(
                    "&#00d8ff\u25b6 &f\u041a\u043e\u043d\u0444\u0438\u0433 \u043f\u0435\u0440\u0435\u0437\u0430\u0433\u0440\u0443\u0436\u0435\u043d!"));
            return true;
        }
        if (!(sender instanceof Player)) {
            sender.sendMessage(HexUtil.translate(
                    "&#FFB000\u25b6 &f\u041a\u043e\u043c\u0430\u043d\u0434\u0443 \u043c\u043e\u0433\u0443\u0442 \u0438\u0441\u043f\u043e\u043b\u044c\u0437\u043e\u0432\u0430\u0442\u044c \u0442\u043e\u043b\u044c\u043a\u043e \u0438\u0433\u0440\u043e\u043a\u0438."));
            return true;
        }
        Player player = (Player) sender;
        this.menu.open(player);
        return true;
    }

    private boolean noPermission(CommandSender sender) {
        sender.sendMessage(HexUtil.translate(
                "&#ff2222\u25b6 &f\u0423 \u0432\u0430\u0441 \u043d\u0435\u0442 \u043f\u0440\u0430\u0432 \u043d\u0430 \u044d\u0442\u0443 \u043a\u043e\u043c\u0430\u043d\u0434\u0443."));
        return true;
    }

    private boolean usage(CommandSender sender) {
        sender.sendMessage(HexUtil.translate("&#FFB000\u25b6 &f\u0418\u0441\u043f\u043e\u043b\u044c\u0437\u043e\u0432\u0430\u043d\u0438\u0435:"));
        sender.sendMessage(HexUtil.translate("&7- &f/cust give star <\u043d\u0438\u043a> <\u0443\u0440\u043e\u0432\u0435\u043d\u044c>"));
        sender.sendMessage(HexUtil.translate("&7- &f/cust give premium <\u043d\u0438\u043a> <1d|12h|30m|1w>"));
        sender.sendMessage(HexUtil.translate("&7- &f/cust star <\u043d\u0438\u043a> <\u0440\u0443\u0431\u043b\u0438>"));
        return true;
    }

    /** /cust give star <nick> <level> — set the player's star level directly. */
    private boolean giveStarLevel(CommandSender sender, String targetName, String levelStr) {
        int level;
        try {
            level = Integer.parseInt(levelStr);
        } catch (NumberFormatException e) {
            sender.sendMessage(HexUtil.translate(
                    "&#FFB000\u25b6 &f\u0423\u043a\u0430\u0436\u0438\u0442\u0435 \u0443\u0440\u043e\u0432\u0435\u043d\u044c \u0447\u0438\u0441\u043b\u043e\u043c (1-10)."));
            return true;
        }
        if (level < 0 || level > 10) {
            sender.sendMessage(HexUtil.translate(
                    "&#FFB000\u25b6 &f\u0423\u0440\u043e\u0432\u0435\u043d\u044c \u0434\u043e\u043b\u0436\u0435\u043d \u0431\u044b\u0442\u044c \u043e\u0442 0 \u0434\u043e 10."));
            return true;
        }
        Player target = Bukkit.getPlayer(targetName);
        if (target != null) {
            HashMap<String, String> data = CraftoriumCustom.getPlayerData(target);
            if (data == null) {
                data = new HashMap<>();
            }
            data.put("star_level", String.valueOf(level));
            CraftoriumCustom.playerData.put(target.getUniqueId(), data);
            sender.sendMessage(HexUtil.translate(
                    "&#ffc900\u25b6 &f\u0418\u0433\u0440\u043e\u043a\u0443 &6" + targetName + " &f\u0432\u044b\u0434\u0430\u043d\u0430 \u0437\u0432\u0435\u0437\u0434\u0430 &6" + level + " \u0443\u0440."));
            target.sendMessage(HexUtil.translate(
                    "&#ffc900\u25b6 &f\u0412\u044b \u043f\u043e\u043b\u0443\u0447\u0438\u043b\u0438 \u0437\u0432\u0435\u0437\u0434\u0443 &6" + level + " \u0443\u0440."));
        } else {
            DatabaseManager.getPlayerAsync(targetName).thenAccept(base64 -> {
                HashMap<String, String> data = base64 != null
                        ? DatabaseManager.fromBase64(base64)
                        : new HashMap<>();
                data.put("star_level", String.valueOf(level));
                DatabaseManager.setPlayerAsync(targetName, DatabaseManager.toBase64(data))
                        .thenRun(() -> Bukkit.getScheduler().runTask((Plugin) CraftoriumCustom.getInstance(), () ->
                                sender.sendMessage(HexUtil.translate(
                                        "&#ffc900\u25b6 &f\u0418\u0433\u0440\u043e\u043a\u0443 &6" + targetName + " &f\u0432\u044b\u0434\u0430\u043d\u0430 \u0437\u0432\u0435\u0437\u0434\u0430 &6" + level + " \u0443\u0440. &7(\u043e\u0444\u0444\u043b\u0430\u0439\u043d)"))));
            });
        }
        return true;
    }

    /** /cust give premium <nick> <duration> — give premium for parsed duration. */
    private boolean givePremium(CommandSender sender, String targetName, String duration) {
        long millis = PremiumUtil.parseDurationMillis(duration);
        if (millis <= 0L) {
            sender.sendMessage(HexUtil.translate(
                    "&#FFB000\u25b6 &f\u041d\u0435\u0432\u0435\u0440\u043d\u0430\u044f \u0434\u043b\u0438\u0442\u0435\u043b\u044c\u043d\u043e\u0441\u0442\u044c. \u041f\u0440\u0438\u043c\u0435\u0440: 1d, 12h, 30m, 1w"));
            return true;
        }
        PremiumUtil.givePremium(sender, targetName, millis);
        String msg = CraftoriumCustom.getInstance().getConfig().getString("messages.premiumgiven",
                "&#00d8ff\u25b6 &f\u041f\u0440\u0435\u043c\u0438\u0443\u043c \u0432\u044b\u0434\u0430\u043d \u0438\u0433\u0440\u043e\u043a\u0443 &6{player}&f.");
        sender.sendMessage(HexUtil.translate(msg.replace("{player}", targetName)
                + " &7\u043d\u0430 &6" + duration));
        return true;
    }

    /** /cust star <nick> <rubles> — adds rubles to star_points (kept from previous version). */
    private boolean addStarPoints(CommandSender sender, String targetName, String amountStr) {
        int amount;
        try {
            amount = Integer.parseInt(amountStr);
        } catch (NumberFormatException e) {
            sender.sendMessage(HexUtil.translate("&#FFB000\u25b6 &f\u041d\u0435\u043f\u0440\u0430\u0432\u0438\u043b\u044c\u043d\u043e\u0435 \u0447\u0438\u0441\u043b\u043e!"));
            return true;
        }
        Player target = Bukkit.getPlayer(targetName);
        CraftoriumCustom plugin = CraftoriumCustom.getInstance();
        if (target != null) {
            HashMap<String, String> data = CraftoriumCustom.getPlayerData(target);
            if (data == null) {
                data = new HashMap<>();
            }
            double current;
            try {
                current = Double.parseDouble(data.getOrDefault("star_points", "0"));
            } catch (NumberFormatException e) {
                current = 0.0;
            }
            data.put("star_points", String.valueOf(current + amount));
            CraftoriumCustom.playerData.put(target.getUniqueId(), data);
            String msg = plugin.getConfig().getString("messages.stargiven",
                    "&#ffc900\u25b6 &f\u0418\u0433\u0440\u043e\u043a\u0443 &6{player} &f\u043d\u0430\u0447\u0438\u0441\u043b\u0435\u043d\u043e &6{amount}\u20bd &f\u043f\u043e\u0436\u0435\u0440\u0442\u0432\u043e\u0432\u0430\u043d\u0438\u0439.");
            msg = msg.replace("{player}", targetName).replace("{amount}", String.valueOf(amount));
            sender.sendMessage(HexUtil.translate(msg));
        } else {
            DatabaseManager.getPlayerAsync(targetName).thenAccept(base64 -> {
                double cur;
                HashMap<String, String> data = base64 != null
                        ? DatabaseManager.fromBase64(base64)
                        : new HashMap<>();
                try {
                    cur = Double.parseDouble(data.getOrDefault("star_points", "0"));
                } catch (NumberFormatException e) {
                    cur = 0.0;
                }
                data.put("star_points", String.valueOf(cur + amount));
                DatabaseManager.setPlayerAsync(targetName, DatabaseManager.toBase64(data))
                        .thenRun(() -> Bukkit.getScheduler().runTask((Plugin) plugin, () -> {
                            String msg = plugin.getConfig().getString("messages.stargiven",
                                    "&#ffc900\u25b6 &f\u0418\u0433\u0440\u043e\u043a\u0443 &6{player} &f\u043d\u0430\u0447\u0438\u0441\u043b\u0435\u043d\u043e &6{amount}\u20bd &f\u043f\u043e\u0436\u0435\u0440\u0442\u0432\u043e\u0432\u0430\u043d\u0438\u0439.");
                            msg = msg.replace("{player}", targetName).replace("{amount}", String.valueOf(amount));
                            sender.sendMessage(HexUtil.translate(msg + " &7(\u043e\u0444\u0444\u043b\u0430\u0439\u043d)"));
                        }));
            });
        }
        return true;
    }

    /** Legacy /cust premium <nick> — toggles permission, no duration tracking. */
    private boolean legacyTogglePremium(CommandSender sender, String targetName) {
        Player target = Bukkit.getPlayer(targetName);
        if (target != null) {
            if (target.hasPermission("stickhwcustom.prem")) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
                        "lp user " + targetName + " permission unset stickhwcustom.prem");
                PremiumUtil.writeExpiration(target, 0L);
                String msg = CraftoriumCustom.getInstance().getConfig().getString("messages.premiumremoved",
                        "&#ff2222\u25b6 &f\u041f\u0440\u0435\u043c\u0438\u0443\u043c \u0443\u0431\u0440\u0430\u043d \u0443 \u0438\u0433\u0440\u043e\u043a\u0430 &6{player}&f.");
                sender.sendMessage(HexUtil.translate(msg.replace("{player}", targetName)));
            } else {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
                        "lp user " + targetName + " permission set stickhwcustom.prem true");
                String msg = CraftoriumCustom.getInstance().getConfig().getString("messages.premiumgiven",
                        "&#00d8ff\u25b6 &f\u041f\u0440\u0435\u043c\u0438\u0443\u043c \u0432\u044b\u0434\u0430\u043d \u0438\u0433\u0440\u043e\u043a\u0443 &6{player}&f.");
                sender.sendMessage(HexUtil.translate(msg.replace("{player}", targetName)));
            }
        } else {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
                    "lp user " + targetName + " permission set stickhwcustom.prem true");
            String msg = CraftoriumCustom.getInstance().getConfig().getString("messages.premiumgiven",
                    "&#00d8ff\u25b6 &f\u041f\u0440\u0435\u043c\u0438\u0443\u043c \u0432\u044b\u0434\u0430\u043d \u0438\u0433\u0440\u043e\u043a\u0443 &6{player}&f.");
            sender.sendMessage(HexUtil.translate(msg.replace("{player}", targetName)
                    + " &7(\u043e\u0444\u0444\u043b\u0430\u0439\u043d)"));
        }
        return true;
    }
}
