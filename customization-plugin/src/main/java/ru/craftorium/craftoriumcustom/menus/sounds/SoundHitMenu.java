package ru.craftorium.craftoriumcustom.menus.sounds;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import ru.craftorium.craftoriumcustom.CraftoriumCustom;
import ru.craftorium.craftoriumcustom.holders.SoundHitMenuHolder;
import ru.craftorium.craftoriumcustom.items.MenuItems;

public class SoundHitMenu {
    private final MenuItems items;

    public SoundHitMenu(MenuItems items) {
        this.items = items;
    }

    public void open(Player player) {
        int i;
        Inventory inventory = Bukkit.createInventory((InventoryHolder)new SoundHitMenuHolder(), (int)54, (String)"\u0417\u0432\u0443\u043a\u0438");
        for (i = 0; i <= 1; ++i) {
            inventory.setItem(i, this.items.blue());
        }
        for (i = 7; i <= 8; ++i) {
            inventory.setItem(i, this.items.blue());
        }
        for (i = 9; i <= 17; ++i) {
            inventory.setItem(i, this.items.gray());
        }
        for (i = 36; i <= 44; ++i) {
            inventory.setItem(i, this.items.gray());
        }
        for (i = 45; i <= 53; ++i) {
            inventory.setItem(i, this.items.orange());
        }
        inventory.setItem(2, this.items.banner_crit());
        inventory.setItem(3, this.items.banner_death_me());
        inventory.setItem(4, this.items.banner_lowheal());
        inventory.setItem(5, this.items.banner_kill());
        inventory.setItem(45, this.items.back());
        if (CraftoriumCustom.getInstance().getConfig().contains("hitsounds.items")) {
            for (String key : CraftoriumCustom.getInstance().getConfig().getConfigurationSection("hitsounds.items").getKeys(false)) {
                String path = "hitsounds.items." + key;
                int slot = CraftoriumCustom.getInstance().getConfig().getInt(path + ".slot");
                String soundName = CraftoriumCustom.getInstance().getConfig().getString(path + ".sound");
                String displayName = CraftoriumCustom.getInstance().getConfig().getString(path + ".name");
                String materialName = CraftoriumCustom.getInstance().getConfig().getString(path + ".material");
                if (materialName == null) {
                    CraftoriumCustom.getInstance().getLogger().warning("\u041d\u0435\u043f\u0440\u0430\u0432\u0438\u043b\u044c\u043d\u044b\u0439 \u043c\u0430\u0442\u0435\u0440\u0438\u0430\u043b: " + key);
                    continue;
                }
                Material material = Material.valueOf((String)materialName);
                boolean premium = CraftoriumCustom.getInstance().getConfig().getBoolean(path + ".premium", false);
                inventory.setItem(slot, this.items.soundItem(player, "hit_sound", soundName, displayName, material, premium));
            }
        }
        player.openInventory(inventory);
    }
}

