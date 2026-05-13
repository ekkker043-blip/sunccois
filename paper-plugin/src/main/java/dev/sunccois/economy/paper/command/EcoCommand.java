package dev.sunccois.economy.paper.command;

import dev.sunccois.economy.common.NumberFormatter;
import dev.sunccois.economy.paper.SunccoisEconomyPlugin;
import dev.sunccois.economy.paper.economy.EconomyService;
import dev.sunccois.economy.paper.economy.EconomyService.MutationOutcome;
import dev.sunccois.economy.paper.economy.EconomyService.OpResult;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * /eco give|take|reset|get <player> [amount]
 *
 * Input parsing and messaging only. All economy work is done asynchronously
 * inside {@link EconomyService} and results are delivered back on the main
 * thread where user feedback is sent.
 */
public final class EcoCommand implements CommandExecutor, TabCompleter {

    private final SunccoisEconomyPlugin plugin;
    private final EconomyService economy;

    public EcoCommand(SunccoisEconomyPlugin plugin, EconomyService economy) {
        this.plugin = plugin;
        this.economy = economy;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sendUsage(sender);
            return true;
        }
        String sub = args[0].toLowerCase();

        switch (sub) {
            case "give" -> handleGive(sender, args);
            case "take" -> handleTake(sender, args);
            case "reset" -> handleReset(sender, args);
            case "get" -> handleGet(sender, args);
            default -> sendUsage(sender);
        }
        return true;
    }

    /* ---------------- Subcommands ---------------- */

    private void handleGive(CommandSender sender, String[] args) {
        if (!sender.hasPermission("sunccois.eco.admin")) { noPerm(sender); return; }
        if (args.length < 3) { sendUsage(sender); return; }

        Optional<UUID> uuid = economy.resolveUuid(args[1]);
        if (uuid.isEmpty()) { msg(sender, "player-not-found", "player", args[1]); return; }

        long amount = NumberFormatter.parsePositive(args[2]);
        if (amount < 0) { msg(sender, "invalid-amount"); return; }

        String actor = actorOf(sender);
        economy.giveAsync(uuid.get(), amount, actor).thenAccept(outcome ->
                Bukkit.getScheduler().runTask(plugin, () -> feedback(sender, args[1], amount, outcome, "given")));
    }

    private void handleTake(CommandSender sender, String[] args) {
        if (!sender.hasPermission("sunccois.eco.admin")) { noPerm(sender); return; }
        if (args.length < 3) { sendUsage(sender); return; }

        Optional<UUID> uuid = economy.resolveUuid(args[1]);
        if (uuid.isEmpty()) { msg(sender, "player-not-found", "player", args[1]); return; }

        long amount = NumberFormatter.parsePositive(args[2]);
        if (amount < 0) { msg(sender, "invalid-amount"); return; }

        String actor = actorOf(sender);
        economy.takeAsync(uuid.get(), amount, actor).thenAccept(outcome ->
                Bukkit.getScheduler().runTask(plugin, () -> feedback(sender, args[1], amount, outcome, "taken")));
    }

    private void handleReset(CommandSender sender, String[] args) {
        if (!sender.hasPermission("sunccois.eco.admin")) { noPerm(sender); return; }
        if (args.length < 2) { sendUsage(sender); return; }

        Optional<UUID> uuid = economy.resolveUuid(args[1]);
        if (uuid.isEmpty()) { msg(sender, "player-not-found", "player", args[1]); return; }

        String actor = actorOf(sender);
        economy.resetAsync(uuid.get(), actor).thenAccept(outcome ->
                Bukkit.getScheduler().runTask(plugin, () -> {
                    if (outcome.result() == OpResult.OK) {
                        msg(sender, "reset", "player", args[1]);
                    } else {
                        errorFeedback(sender, args[1], 0, outcome);
                    }
                }));
    }

    private void handleGet(CommandSender sender, String[] args) {
        // "/eco get" alone = self balance
        if (args.length == 1) {
            if (!(sender instanceof Player p)) { sendUsage(sender); return; }
            if (!p.hasPermission("sunccois.eco.self")) { noPerm(sender); return; }
            economy.getBalanceAsync(p.getUniqueId()).thenAccept(bal ->
                    Bukkit.getScheduler().runTask(plugin, () ->
                            msg(sender, "balance-self", "amount", NumberFormatter.format(bal))));
            return;
        }

        // "/eco get <name>" — self or other
        boolean self = sender instanceof Player pp && pp.getName().equalsIgnoreCase(args[1]);
        if (!self && !sender.hasPermission("sunccois.eco.get")) { noPerm(sender); return; }

        Optional<UUID> uuid = economy.resolveUuid(args[1]);
        if (uuid.isEmpty()) { msg(sender, "player-not-found", "player", args[1]); return; }

        economy.getBalanceAsync(uuid.get()).thenAccept(bal ->
                Bukkit.getScheduler().runTask(plugin, () -> {
                    if (self) {
                        msg(sender, "balance-self", "amount", NumberFormatter.format(bal));
                    } else {
                        msg(sender, "balance-other",
                                "player", args[1],
                                "amount", NumberFormatter.format(bal));
                    }
                }));
    }

    /* ---------------- Feedback helpers ---------------- */

    private void feedback(CommandSender sender, String playerName, long amount,
                          MutationOutcome outcome, String key) {
        if (outcome.result() == OpResult.OK) {
            msg(sender, key,
                    "player", playerName,
                    "amount", NumberFormatter.format(amount),
                    "new", NumberFormatter.format(outcome.newBalance()));
        } else {
            errorFeedback(sender, playerName, amount, outcome);
        }
    }

    private void errorFeedback(CommandSender sender, String player, long amount, MutationOutcome outcome) {
        switch (outcome.result()) {
            case PLAYER_NOT_FOUND -> msg(sender, "player-not-found", "player", player);
            case NOT_ENOUGH_FUNDS -> {
                long have = Math.max(0, outcome.newBalance());
                msg(sender, "not-enough",
                        "player", player,
                        "have", NumberFormatter.format(have));
            }
            case CAP_EXCEEDED -> msg(sender, "cap-exceeded");
            case INVALID_AMOUNT -> msg(sender, "invalid-amount");
            default -> msg(sender, "db-error");
        }
    }

    private String actorOf(CommandSender sender) {
        return sender instanceof Player p ? p.getName() : "CONSOLE";
    }

    private void noPerm(CommandSender sender) { msg(sender, "no-permission"); }

    private void sendUsage(CommandSender sender) {
        for (String line : plugin.getConfig().getString("messages.usage", "").split("\n")) {
            sendRaw(sender, line);
        }
    }

    private void msg(CommandSender sender, String key, String... kv) {
        String raw = plugin.getConfig().getString("messages." + key, "");
        for (int i = 0; i + 1 < kv.length; i += 2) {
            raw = raw.replace("%" + kv[i] + "%", kv[i + 1]);
        }
        sendRaw(sender, raw);
    }

    private void sendRaw(CommandSender sender, String raw) {
        String prefix = plugin.getConfig().getString("messages.prefix", "");
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + raw));
    }

    /* ---------------- Tab complete ---------------- */

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return filter(Arrays.asList("give", "take", "reset", "get"), args[0]);
        }
        if (args.length == 2) {
            List<String> names = new ArrayList<>();
            Bukkit.getOnlinePlayers().forEach(p -> names.add(p.getName()));
            return filter(names, args[1]);
        }
        return Collections.emptyList();
    }

    private List<String> filter(List<String> src, String prefix) {
        String p = prefix.toLowerCase();
        List<String> out = new ArrayList<>();
        for (String s : src) if (s.toLowerCase().startsWith(p)) out.add(s);
        return out;
    }
}
