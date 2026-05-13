package dev.sunccois.economy.paper.placeholder;

import dev.sunccois.economy.common.NumberFormatter;
import dev.sunccois.economy.paper.SunccoisEconomyPlugin;
import dev.sunccois.economy.paper.economy.EconomyService;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Available placeholders:
 *   %sunccois_balance%             -> raw number (e.g. 1000)
 *   %sunccois_balance_formatted%   -> with commas (e.g. 1,000)
 */
public final class EconomyPlaceholders extends PlaceholderExpansion {

    private final SunccoisEconomyPlugin plugin;
    private final EconomyService economy;

    public EconomyPlaceholders(SunccoisEconomyPlugin plugin, EconomyService economy) {
        this.plugin = plugin;
        this.economy = economy;
    }

    @Override public @NotNull String getIdentifier() { return "sunccois"; }
    @Override public @NotNull String getAuthor()     { return "sunccois"; }
    @Override public @NotNull String getVersion()    { return plugin.getDescription().getVersion(); }
    @Override public boolean persist()               { return true; }

    @Override
    public @Nullable String onRequest(OfflinePlayer player, @NotNull String params) {
        if (player == null) return "";
        long bal = economy.getBalance(player.getUniqueId());
        return switch (params.toLowerCase()) {
            case "balance" -> Long.toString(bal);
            case "balance_formatted", "balance_fmt" -> NumberFormatter.format(bal);
            default -> null;
        };
    }
}
