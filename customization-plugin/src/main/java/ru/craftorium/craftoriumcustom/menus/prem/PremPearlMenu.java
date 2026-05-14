package ru.craftorium.craftoriumcustom.menus.prem;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import ru.craftorium.craftoriumcustom.CraftoriumCustom;
import ru.craftorium.craftoriumcustom.holders.PremPearlMenuHolder;
import ru.craftorium.craftoriumcustom.items.MenuItems;

public class PremPearlMenu {
    private final MenuItems items;

    public PremPearlMenu(MenuItems items) {
        this.items = items;
    }

    public void open(Player player) {
        int i;
        Inventory inventory = Bukkit.createInventory((InventoryHolder)new PremPearlMenuHolder(), (int)54, (String)"\u041f\u0440\u0435\u043c\u0438\u0443\u043c");
        // Only one banner-tab now \u2014 the DeathPlayer / DeathMe sub-tabs were removed
        // and this menu owns the only premium effect list.
        for (i = 0; i <= 1; ++i) {
            inventory.setItem(i, this.items.blue());
        }
        for (i = 3; i <= 8; ++i) {
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
        inventory.setItem(2, this.items.banner());
        inventory.setItem(45, this.items.back());
        inventory.setItem(53, this.items.resetItem());
        if (CraftoriumCustom.getInstance().getConfig().contains("pearlparticles.items")) {
            for (String key : CraftoriumCustom.getInstance().getConfig().getConfigurationSection("pearlparticles.items").getKeys(false)) {
                String path = "pearlparticles.items." + key;
                int slot = CraftoriumCustom.getInstance().getConfig().getInt(path + ".slot");
                String particleName = CraftoriumCustom.getInstance().getConfig().getString(path + ".particle");
                String displayName = CraftoriumCustom.getInstance().getConfig().getString(path + ".name");
                String materialName = CraftoriumCustom.getInstance().getConfig().getString(path + ".material");
                if (materialName == null) {
                    CraftoriumCustom.getInstance().getLogger().warning("\u041d\u0435\u043f\u0440\u0430\u0432\u0438\u043b\u044c\u043d\u044b\u0439 \u043c\u0430\u0442\u0435\u0440\u0438\u0430\u043b: " + key);
                    continue;
                }
                Material material = Material.valueOf((String)materialName);
                inventory.setItem(slot, this.items.particleItem(player, "pearl_particle", particleName, displayName, material));
            }
        }
        player.openInventory(inventory);
    }
}

