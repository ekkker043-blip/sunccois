package ru.craftorium.craftoriumcustom.api;

import java.awt.Color;
import java.util.HashMap;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import ru.craftorium.craftoriumcustom.CraftoriumCustom;
import ru.craftorium.craftoriumcustom.utils.HexUtil;
import ru.craftorium.craftoriumcustom.utils.PremiumUtil;

public class CustomPlaceholder extends PlaceholderExpansion {
    // Cached colour palette for the animated star (level 11). Built lazily on
    // the first request and reused on every subsequent placeholder evaluation.
    // The placeholder is in the hot path (called for every tab refresh / chat
    // message), so we must NOT allocate a new array per call.
    private static volatile String[] rainbowPalette;
    // How fast the rainbow cycles. 250ms = 4 colour steps per second.
    private static final long RAINBOW_STEP_MS = 250L;

    private static String[] palette() {
        String[] p = rainbowPalette;
        if (p != null) {
            return p;
        }
        synchronized (CustomPlaceholder.class) {
            if (rainbowPalette != null) {
                return rainbowPalette;
            }
            String[] built = new String[10];
            for (int i = 0; i < 10; i++) {
                built[i] = CraftoriumCustom.getInstance().getConfig()
                        .getString("stars.levels." + (i + 1) + ".hex", "#FFFFFF");
            }
            rainbowPalette = built;
            return built;
        }
    }

    /** Invalidates the cached palette so /cust reload can pick up new colours. */
    public static void invalidatePaletteCache() {
        rainbowPalette = null;
    }

    private static String currentRainbowHex() {
        String[] p = palette();
        int idx = (int) ((System.currentTimeMillis() / RAINBOW_STEP_MS) % (long) p.length);
        if (idx < 0) {
            idx = 0;
        }
        return p[idx];
    }

    public String getIdentifier() {
        return "stickhwcustom";
    }

    public String getAuthor() {
        return "Craftorium";
    }

    public String getVersion() {
        return "1.6";
    }

    public boolean persist() {
        return true;
    }

    public String onPlaceholderRequest(Player player, String params) {
        if (params == null || player == null) {
            return "";
        }
        if (params.equalsIgnoreCase("name")) {
            return player.getName();
        }
        if (params.equalsIgnoreCase("colorname")) {
            HashMap<String, String> data = CraftoriumCustom.getPlayerData(player);
            if (data == null) {
                return player.getName();
            }
            String color = data.getOrDefault("name_color", null);
            if (color == null) {
                return player.getName();
            }
            int starLevel = 0;
            try {
                starLevel = Integer.parseInt(data.getOrDefault("star_level", "0"));
            } catch (NumberFormatException ignored) {
            }
            // Colour is gated by star level only \u2014 premium no longer
            // auto-grants the bold colour.
            if (starLevel <= 0) {
                return player.getName();
            }
            return this.colorizeNickname(player.getName(), color, "#FFFFFF", true);
        }
        // Numeric star level: hide entirely (empty) if no star.
        if (params.equalsIgnoreCase("star")) {
            HashMap<String, String> data = CraftoriumCustom.getPlayerData(player);
            if (data == null) {
                return "";
            }
            String level = data.getOrDefault("star_level", "0");
            try {
                if (Integer.parseInt(level) <= 0) {
                    return "";
                }
            } catch (NumberFormatException e) {
                return "";
            }
            return level;
        }
        if (params.equalsIgnoreCase("star_points")) {
            HashMap<String, String> data = CraftoriumCustom.getPlayerData(player);
            if (data == null) {
                return "0";
            }
            return data.getOrDefault("star_points", "0");
        }
        if (params.equalsIgnoreCase("star_symbol")) {
            int level = readStarLevel(player);
            if (level <= 0) {
                return "";
            }
            String hex = starHex(level);
            return HexUtil.translate("&#" + hex.replace("#", "") + "\u2b50");
        }
        // Star prefix that includes its own trailing space if the player has a star.
        // Used to avoid "empty leading space" in chat formats like "%stickhwcustom_star_prefix%%player_name%".
        if (params.equalsIgnoreCase("star_prefix")) {
            int level = readStarLevel(player);
            if (level <= 0) {
                return "";
            }
            String hex = starHex(level);
            return HexUtil.translate("&#" + hex.replace("#", "") + "\u2b50 ");
        }
        if (params.equalsIgnoreCase("premium")) {
            return player.hasPermission("stickhwcustom.prem") ? "\u2b50" : "";
        }
        if (params.equalsIgnoreCase("premium_remaining")) {
            return PremiumUtil.formatRemaining(player);
        }
        if (params.equalsIgnoreCase("premium_until")) {
            return PremiumUtil.formatUntil(player);
        }
        return "";
    }

    private String colorizeNickname(String playerName, String color1, String color2, boolean bold) {
        int length = playerName.length();
        if (length == 0) {
            return "";
        }
        ChatColor startColor = ChatColor.of(color1);
        ChatColor endColor = ChatColor.of(color2);
        TextComponent coloredNickname = new TextComponent();
        for (int i = 0; i < length; ++i) {
            double ratio = length > 1 ? (double) i / (double) (length - 1) : 0.0;
            int red = (int) ((double) startColor.getColor().getRed() * (1.0 - ratio) + (double) endColor.getColor().getRed() * ratio);
            int green = (int) ((double) startColor.getColor().getGreen() * (1.0 - ratio) + (double) endColor.getColor().getGreen() * ratio);
            int blue = (int) ((double) startColor.getColor().getBlue() * (1.0 - ratio) + (double) endColor.getColor().getBlue() * ratio);
            ChatColor color = ChatColor.of(new Color(red, green, blue));
            TextComponent letter = new TextComponent(String.valueOf(playerName.charAt(i)));
            letter.setColor(color);
            letter.setBold(Boolean.valueOf(bold));
            coloredNickname.addExtra((BaseComponent) letter);
        }
        return TextComponent.toLegacyText(new BaseComponent[]{coloredNickname});
    }

    /** Reads the player's star level from their cached data; 0 on any issue. */
    private static int readStarLevel(Player player) {
        HashMap<String, String> data = CraftoriumCustom.getPlayerData(player);
        if (data == null) {
            return 0;
        }
        try {
            return Integer.parseInt(data.getOrDefault("star_level", "0"));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /**
     * Returns the hex colour for a given star level. Level 11 is animated and
     * cycles through the colours of levels 1-10; every other level reads its
     * fixed hex from config.
     */
    private static String starHex(int level) {
        if (level == 11) {
            return currentRainbowHex();
        }
        return CraftoriumCustom.getInstance().getConfig()
                .getString("stars.levels." + level + ".hex", "#FFFFFF");
    }
}
