package ru.craftorium.craftoriumcustom.menus.info;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ru.craftorium.craftoriumcustom.CraftoriumCustom;
import ru.craftorium.craftoriumcustom.holders.InfoMenuHolder;
import ru.craftorium.craftoriumcustom.items.MenuItems;
import ru.craftorium.craftoriumcustom.utils.HexUtil;
import ru.craftorium.craftoriumcustom.utils.PremiumUtil;

/**
 * "Информация" tab — shows premium expiration with minute / hour / day breakdown.
 * Mirrors the visual style (colors, header/footer banners) of the other menus.
 */
public class InfoMenu {
    private final MenuItems items;

    public InfoMenu(MenuItems items) {
        this.items = items;
    }

    public void open(Player player) {
        Inventory inventory = Bukkit.createInventory((InventoryHolder) new InfoMenuHolder(), 45,
                "\u0418\u043d\u0444\u043e\u0440\u043c\u0430\u0446\u0438\u044f");
        // Frame: same colors as other menus (blue top, orange bottom, gray edges).
        for (int i = 0; i <= 8; i++) {
            inventory.setItem(i, this.items.blue());
        }
        for (int i = 9; i <= 17; i++) {
            inventory.setItem(i, this.items.gray());
        }
        for (int i = 27; i <= 35; i++) {
            inventory.setItem(i, this.items.gray());
        }
        for (int i = 36; i <= 44; i++) {
            inventory.setItem(i, this.items.orange());
        }
        inventory.setItem(36, this.items.back());

        inventory.setItem(22, premiumInfoItem(player));
        inventory.setItem(20, starInfoItem(player));
        inventory.setItem(24, colorInfoItem(player));

        player.openInventory(inventory);
    }

    private ItemStack premiumInfoItem(Player player) {
        ItemStack item = new ItemStack(Material.CLOCK);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(HexUtil.translate("&#00D8FF \u274f \u041f\u0440\u0435\u043c\u0438\u0443\u043c \u274f"));
        List<String> lore = new ArrayList<>();
        lore.add(HexUtil.translate(" &f"));
        boolean hasPrem = player.hasPermission("stickhwcustom.prem");
        if (!hasPrem) {
            lore.add(HexUtil.translate(" &#00D8FF\u258d&f \u0421\u0442\u0430\u0442\u0443\u0441: &cне выдан"));
        } else {
            long[] mhd = PremiumUtil.remainingMinutesHoursDays(player);
            long minutes = mhd[0];
            long hours = mhd[1];
            long days = mhd[2];
            if (minutes < 0L) {
                lore.add(HexUtil.translate(" &#00D8FF\u258d&f \u0421\u0442\u0430\u0442\u0443\u0441: &aбессрочно"));
            } else if (minutes == 0L) {
                lore.add(HexUtil.translate(" &#00D8FF\u258d&f \u0421\u0442\u0430\u0442\u0443\u0441: &cистёк"));
            } else {
                lore.add(HexUtil.translate(" &#00D8FF\u258d&f \u0421\u0442\u0430\u0442\u0443\u0441: &aактивен"));
                lore.add(HexUtil.translate(" &#00D8FF\u258d&f \u0418\u0441\u0442\u0435\u043a\u0430\u0435\u0442: &6" + PremiumUtil.formatUntil(player)));
                lore.add(HexUtil.translate(" &f"));
                lore.add(HexUtil.translate(" &#00D8FF\u258d&f \u041e\u0441\u0442\u0430\u043b\u043e\u0441\u044c \u0432\u0440\u0435\u043c\u0435\u043d\u0438:"));
                lore.add(HexUtil.translate(" &#00D8FF\u258d&f - \u043c\u0438\u043d\u0443\u0442: &6" + minutes));
                lore.add(HexUtil.translate(" &#00D8FF\u258d&f - \u0447\u0430\u0441\u043e\u0432: &6" + hours));
                lore.add(HexUtil.translate(" &#00D8FF\u258d&f - \u0434\u043d\u0435\u0439: &6" + days));
            }
        }
        lore.add(HexUtil.translate(" &f"));
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    private ItemStack starInfoItem(Player player) {
        ItemStack item = new ItemStack(Material.NETHER_STAR);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(HexUtil.translate("&#FFC900 \u274f \u0417\u0432\u0435\u0437\u0434\u0430 \u0441\u0442\u0430\u0442\u0443\u0441\u0430 \u274f"));
        List<String> lore = new ArrayList<>();
        HashMap<String, String> data = CraftoriumCustom.getPlayerData(player);
        int level = 0;
        double points = 0.0;
        if (data != null) {
            try {
                level = Integer.parseInt(data.getOrDefault("star_level", "0"));
            } catch (NumberFormatException ignored) {
            }
            try {
                points = Double.parseDouble(data.getOrDefault("star_points", "0"));
            } catch (NumberFormatException ignored) {
            }
        }
        lore.add(HexUtil.translate(" &f"));
        if (level <= 0) {
            lore.add(HexUtil.translate(" &#FFC900\u258d&f \u0423\u0440\u043e\u0432\u0435\u043d\u044c: &cнет"));
        } else {
            lore.add(HexUtil.translate(" &#FFC900\u258d&f \u0423\u0440\u043e\u0432\u0435\u043d\u044c: &6" + MenuItems.convertInt(level) + " &f(" + level + " \u0443\u0440.)"));
        }
        lore.add(HexUtil.translate(" &#FFC900\u258d&f \u041f\u043e\u0436\u0435\u0440\u0442\u0432\u043e\u0432\u0430\u043d\u043e: &6" + String.format("%.0f", points) + "\u20bd"));
        lore.add(HexUtil.translate(" &f"));
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    private ItemStack colorInfoItem(Player player) {
        ItemStack item = new ItemStack(Material.RED_DYE);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(HexUtil.translate("&#FF0000 \u274f \u0426\u0432\u0435\u0442 \u043d\u0438\u043a\u043d\u0435\u0439\u043c\u0430 \u274f"));
        List<String> lore = new ArrayList<>();
        lore.add(HexUtil.translate(" &f"));
        HashMap<String, String> data = CraftoriumCustom.getPlayerData(player);
        String color = data != null ? data.getOrDefault("name_color", null) : null;
        if (color == null) {
            lore.add(HexUtil.translate(" &#FF0000\u258d&f \u0426\u0432\u0435\u0442: &cне выбран"));
        } else {
            lore.add(HexUtil.translate(" &#FF0000\u258d&f \u0426\u0432\u0435\u0442: &#" + color.replace("#", "") + color));
        }
        lore.add(HexUtil.translate(" &f"));
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }
}
