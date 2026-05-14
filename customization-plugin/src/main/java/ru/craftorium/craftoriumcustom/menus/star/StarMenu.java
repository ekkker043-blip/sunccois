package ru.craftorium.craftoriumcustom.menus.star;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import ru.craftorium.craftoriumcustom.CraftoriumCustom;
import ru.craftorium.craftoriumcustom.holders.StarMenuHolder;
import ru.craftorium.craftoriumcustom.items.MenuItems;
import ru.craftorium.craftoriumcustom.items.StarItem;

public class StarMenu {
    private final MenuItems items;
    private final StarItem starItem;

    public StarMenu(MenuItems items, StarItem starItem) {
        this.items = items;
        this.starItem = starItem;
    }

    public void open(Player player) {
        Inventory inventory = Bukkit.createInventory((InventoryHolder)new StarMenuHolder(), (int)54, (String)"\u0417\u0432\u0435\u0437\u0434\u0430 \u0441\u0442\u0430\u0442\u0443\u0441\u0430");
        int cost1 = CraftoriumCustom.getInstance().getConfig().getInt("stars.levels.1.cost", 1000);
        int cost2 = CraftoriumCustom.getInstance().getConfig().getInt("stars.levels.2.cost", 5000);
        int cost3 = CraftoriumCustom.getInstance().getConfig().getInt("stars.levels.3.cost", 15000);
        int cost4 = CraftoriumCustom.getInstance().getConfig().getInt("stars.levels.4.cost", 25000);
        int cost5 = CraftoriumCustom.getInstance().getConfig().getInt("stars.levels.5.cost", 50000);
        int cost6 = CraftoriumCustom.getInstance().getConfig().getInt("stars.levels.6.cost", 75000);
        int cost7 = CraftoriumCustom.getInstance().getConfig().getInt("stars.levels.7.cost", 100000);
        int cost8 = CraftoriumCustom.getInstance().getConfig().getInt("stars.levels.8.cost", 125000);
        int cost9 = CraftoriumCustom.getInstance().getConfig().getInt("stars.levels.9.cost", 200000);
        int cost10 = CraftoriumCustom.getInstance().getConfig().getInt("stars.levels.10.cost", 500000);
        inventory.setItem(12, this.starItem.getStar(Material.RED_SHULKER_BOX, "&#FF5555", cost1, 1, player));
        inventory.setItem(13, this.starItem.getStar(Material.ORANGE_SHULKER_BOX, "&#FFAA00", cost2, 2, player));
        inventory.setItem(14, this.starItem.getStar(Material.YELLOW_SHULKER_BOX, "&#FFFF55", cost3, 3, player));
        inventory.setItem(20, this.starItem.getStar(Material.LIME_SHULKER_BOX, "&#55FF55", cost4, 4, player));
        inventory.setItem(21, this.starItem.getStar(Material.MAGENTA_SHULKER_BOX, "&#FF55FF", cost5, 5, player));
        inventory.setItem(22, this.starItem.getStar(Material.PURPLE_SHULKER_BOX, "&#AA00AA", cost6, 6, player));
        inventory.setItem(23, this.starItem.getStar(Material.LIGHT_BLUE_SHULKER_BOX, "&#55FFFF", cost7, 7, player));
        inventory.setItem(24, this.starItem.getStar(Material.CYAN_SHULKER_BOX, "&#00AAAA", cost8, 8, player));
        inventory.setItem(39, this.starItem.getStar(Material.BLUE_SHULKER_BOX, "&#0000AA", cost9, 9, player));
        inventory.setItem(41, this.starItem.getStar(Material.WHITE_SHULKER_BOX, "&#FFFFFF", cost10, 10, player));
        inventory.setItem(45, this.items.getStarHelp());
        inventory.setItem(53, this.items.back());
        player.openInventory(inventory);
    }
}

