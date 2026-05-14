package ru.craftorium.craftoriumcustom.menus.skins;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import ru.craftorium.craftoriumcustom.holders.SkinMenuHolder;
import ru.craftorium.craftoriumcustom.items.MenuItems;

public class SkinsMenu {
    private final MenuItems items;

    public SkinsMenu(MenuItems items) {
        this.items = items;
    }

    public void open(Player player) {
        Inventory inventory = Bukkit.createInventory((InventoryHolder)new SkinMenuHolder(), (int)54, (String)"\u0422\u0440\u0430\u043f\u043a\u0438 \u0438 \u0441\u0442\u0430\u043d\u044b");
        for (int i = 0; i < 54; ++i) {
            inventory.setItem(i, this.items.gray());
        }
        inventory.setItem(2, this.items.getTrapSkin(player, Material.STRUCTURE_BLOCK, "\u0420\u0430\u043d\u0434\u043e\u043c\u043d\u044b\u0439", "&x&f&b&0&f&d&4", true, "random"));
        inventory.setItem(10, this.items.getTrapSkin(player, Material.NETHER_BRICK_STAIRS, "\u041d\u0435\u0437\u0435\u0440", "&x&d&3&6&b&7&e", true, "nether"));
        inventory.setItem(11, this.items.getTrapSkin(player, Material.RED_NETHER_BRICK_STAIRS, "\u041a\u0440\u0430\u0441\u043d\u044b\u0439 \u043d\u0435\u0437\u0435\u0440", "&c", true, "rednether"));
        inventory.setItem(12, this.items.getTrapSkin(player, Material.POLISHED_BLACKSTONE_STAIRS, "\u0427\u0435\u0440\u043d\u0438\u0442", "&x&0&0&d&8&f&f", true, "blackstone"));
        inventory.setItem(19, this.items.getTrapSkin(player, Material.QUARTZ_STAIRS, "\u041a\u0432\u0430\u0440\u0446", "&f", true, "quartz"));
        inventory.setItem(20, this.items.getTrapSkin(player, Material.CRIMSON_STAIRS, "\u0411\u0430\u0433\u0440\u043e\u0432\u044b\u0439", "&x&d&d&6&1&c&4", true, "crimson"));
        inventory.setItem(21, this.items.getTrapSkin(player, Material.WARPED_STAIRS, "\u0418\u0441\u043a\u0430\u0436\u0451\u043d\u043d\u044b\u0439", "&x&4&e&d&8&c&9", true, "warped"));
        inventory.setItem(28, this.items.getTrapSkin(player, Material.RED_SANDSTONE_STAIRS, "\u041a\u0440\u0430\u0441\u043d\u044b\u0439 \u043f\u0435\u0441\u0447\u0430\u043d\u0438\u043a", "&x&e&7&7&e&2&a", true, "redsand"));
        inventory.setItem(29, this.items.getTrapSkin(player, Material.PRISMARINE_STAIRS, "\u041f\u0440\u0438\u0437\u043c\u0430\u0440\u0438\u043d", "&x&9&b&d&f&b&e", true, "prismarine"));
        inventory.setItem(30, this.items.getTrapSkin(player, Material.PURPUR_STAIRS, "\u041f\u0443\u0440\u043f\u0443\u0440", "&x&e&2&a&2&e&2", true, "purpur"));
        inventory.setItem(37, this.items.getTrapSkin(player, Material.STONE_BRICK_STAIRS, "\u041a\u0430\u043c\u0435\u043d\u043d\u044b\u0439 \u043a\u0438\u0440\u043f\u0438\u0447", "&x&c&1&b&f&c&1", false, "stone"));
        inventory.setItem(38, this.items.getTrapSkin(player, Material.SPRUCE_STAIRS, "\u0415\u043b\u044c", "&x&d&5&9&f&5&e", false, "spruce"));
        inventory.setItem(39, this.items.getTrapSkin(player, Material.END_STONE_BRICK_STAIRS, "\u042d\u043d\u0434\u0435\u0440\u043d\u044f\u043a", "&x&e&0&e&9&a&9", false, "endstone"));
        inventory.setItem(6, this.items.getStanSkin(player, Material.STRUCTURE_BLOCK, "\u0420\u0430\u043d\u0434\u043e\u043c\u043d\u044b\u0439", "&x&f&b&0&f&d&4", true, "random"));
        inventory.setItem(14, this.items.getStanSkin(player, Material.BLACK_DYE, "\u0427\u0435\u0440\u043d\u044b\u0439", "&x&0&0&d&8&f&f", true, "black"));
        inventory.setItem(15, this.items.getStanSkin(player, Material.RED_DYE, "\u041a\u0440\u0430\u0441\u043d\u044b\u0439", "&c", true, "red"));
        inventory.setItem(16, this.items.getStanSkin(player, Material.WHITE_DYE, "\u0411\u0435\u043b\u044b\u0439", "&f", true, "white"));
        inventory.setItem(23, this.items.getStanSkin(player, Material.ORANGE_DYE, "\u041e\u0440\u0430\u043d\u0436\u0435\u0432\u0430\u044f", "&6", true, "orange"));
        inventory.setItem(24, this.items.getStanSkin(player, Material.PURPLE_DYE, "\u0424\u0438\u043e\u043b\u0435\u0442\u043e\u0432\u044b\u0439", "&x&b&2&5&7&f&9", true, "purple"));
        inventory.setItem(25, this.items.getStanSkin(player, Material.BLUE_DYE, "\u0421\u0438\u043d\u0438\u0439", "&x&6&6&9&2&f&f", true, "blue"));
        inventory.setItem(32, this.items.getStanSkin(player, Material.LIME_DYE, "\u041b\u0430\u0439\u043c\u043e\u0432\u044b\u0439", "&a", true, "lime"));
        inventory.setItem(33, this.items.getStanSkin(player, Material.LIGHT_BLUE_DYE, "\u0421\u0432\u0435\u0442\u043b\u043e-\u0433\u043e\u043b\u0443\u0431\u043e\u0439", "&b", true, "light_blue"));
        inventory.setItem(34, this.items.getStanSkin(player, Material.MAGENTA_DYE, "\u041f\u0443\u0440\u043f\u0443\u0440\u043d\u044b\u0439", "&d", true, "magenta"));
        inventory.setItem(41, this.items.getStanSkin(player, Material.YELLOW_DYE, "\u0416\u0451\u043b\u0442\u044b\u0439", "&e", false, "yellow"));
        inventory.setItem(42, this.items.getStanSkin(player, Material.CYAN_DYE, "\u0413\u043e\u043b\u0443\u0431\u043e\u0439", "&x&4&c&c&3&f&4", false, "cyan"));
        inventory.setItem(43, this.items.getStanSkin(player, Material.GREEN_DYE, "\u0417\u0435\u043b\u0451\u043d\u044b\u0439", "&x&9&a&d&d&3&6", false, "green"));
        inventory.setItem(53, this.items.back());
        player.openInventory(inventory);
    }
}

