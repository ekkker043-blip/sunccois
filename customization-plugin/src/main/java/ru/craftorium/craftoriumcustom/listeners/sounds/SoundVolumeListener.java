package ru.craftorium.craftoriumcustom.listeners.sounds;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import ru.craftorium.craftoriumcustom.CraftoriumCustom;
import ru.craftorium.craftoriumcustom.holders.SoundVolumeMenuHolder;
import ru.craftorium.craftoriumcustom.items.MenuItems;
import ru.craftorium.craftoriumcustom.menus.sounds.SoundVolumeMenu;

public class SoundVolumeListener
implements Listener {
    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getClickedInventory() == null) {
            return;
        }
        if (!(e.getWhoClicked() instanceof Player)) {
            return;
        }
        if (!(e.getView().getTopInventory().getHolder() instanceof SoundVolumeMenuHolder)) {
            return;
        }
        e.setCancelled(true);
        if (e.getClickedInventory() != e.getView().getTopInventory()) {
            return;
        }
        Player player = (Player)e.getWhoClicked();
        int slot = e.getSlot();
        if (slot == 49) {
            player.closeInventory();
            player.sendMessage("\u00a7a\u041d\u0430\u0441\u0442\u0440\u043e\u0439\u043a\u0438 \u0437\u0432\u0443\u043a\u0430 \u0441\u043e\u0445\u0440\u0430\u043d\u0435\u043d\u044b!");
            return;
        }
        if (slot >= 9 && slot < 36) {
            double volume = 0.1 + (double)(slot - 9) * 0.1;
            volume = (double)Math.round(volume * 10.0) / 10.0;
            CraftoriumCustom.setPlayerData(player, "sound_pitch", String.valueOf(volume));
            MenuItems menuItems = CraftoriumCustom.menuItems;
            SoundVolumeMenu soundVolumeMenu = new SoundVolumeMenu(menuItems);
            soundVolumeMenu.open(player);
        }
    }
}

