package ru.craftorium.craftoriumcustom.menus.sounds;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import ru.craftorium.craftoriumcustom.holders.SoundVolumeMenuHolder;
import ru.craftorium.craftoriumcustom.items.MenuItems;

public class SoundVolumeMenu {
    private final MenuItems items;

    public SoundVolumeMenu(MenuItems items) {
        this.items = items;
    }

    public void open(Player player) {
        Inventory inventory = Bukkit.createInventory((InventoryHolder)new SoundVolumeMenuHolder(), (int)54, (String)"\u041d\u0430\u0441\u0442\u0440\u043e\u0439\u043a\u0438");
        for (int slot = 9; slot < 36; ++slot) {
            double volume = 0.1 + (double)(slot - 9) * 0.1;
            inventory.setItem(slot, this.items.green(volume, player));
        }
        inventory.setItem(49, this.items.saveSound());
        player.openInventory(inventory);
    }
}

