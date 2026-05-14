package ru.craftorium.craftoriumcustom.listeners.colorname;

import java.util.HashMap;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import ru.craftorium.craftoriumcustom.CraftoriumCustom;
import ru.craftorium.craftoriumcustom.holders.ColorNameMenuHolder;
import ru.craftorium.craftoriumcustom.items.MenuItems;
import ru.craftorium.craftoriumcustom.menus.colorname.ColorNameMenu;
import ru.craftorium.craftoriumcustom.utils.HexUtil;

public class ColorNameListener
implements Listener {
    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getClickedInventory() == null) {
            return;
        }
        if (!(e.getWhoClicked() instanceof Player)) {
            return;
        }
        if (!(e.getView().getTopInventory().getHolder() instanceof ColorNameMenuHolder)) {
            return;
        }
        e.setCancelled(true);
        if (e.getClickedInventory() != e.getView().getTopInventory()) {
            return;
        }
        Player player = (Player)e.getWhoClicked();
        int slot = e.getSlot();
        if (slot == 45) {
            CraftoriumCustom.mainMenu.open(player);
            return;
        }
        if (slot == 53) {
            // Reset color — clears the stored name_color and re-opens the menu.
            CraftoriumCustom.removePlayerData(player, "name_color");
            player.sendMessage(HexUtil.translate(
                    "&#ff2222\u25b6 &f\u0426\u0432\u0435\u0442 \u043d\u0438\u043a\u043d\u0435\u0439\u043c\u0430 \u0441\u0431\u0440\u043e\u0448\u0435\u043d."));
            new ColorNameMenu(CraftoriumCustom.menuItems).open(player);
            return;
        }
        ItemStack item = e.getCurrentItem();
        if (item == null || !item.hasItemMeta()) {
            return;
        }
        ItemMeta meta = item.getItemMeta();
        NamespacedKey colorKey = new NamespacedKey((Plugin)CraftoriumCustom.getInstance(), "color");
        NamespacedKey levelKey = new NamespacedKey((Plugin)CraftoriumCustom.getInstance(), "required_star");
        if (meta.getPersistentDataContainer().has(colorKey, PersistentDataType.STRING)) {
            String color = (String)meta.getPersistentDataContainer().get(colorKey, PersistentDataType.STRING);
            int requiredStar = 1;
            if (meta.getPersistentDataContainer().has(levelKey, PersistentDataType.INTEGER)) {
                requiredStar = (Integer)meta.getPersistentDataContainer().get(levelKey, PersistentDataType.INTEGER);
            }
            HashMap<String, String> pData = CraftoriumCustom.getPlayerData(player);
            int playerStar = 0;
            if (pData != null) {
                try {
                    playerStar = Integer.parseInt(pData.getOrDefault("star_level", "0"));
                }
                catch (NumberFormatException numberFormatException) {
                    // empty catch block
                }
            }
            // Star-only gating: a player can pick a colour only if their star
            // level is >= the colour's required level. Premium no longer
            // bypasses this requirement.
            if (playerStar < requiredStar) {
                String msg = CraftoriumCustom.getInstance().getConfig().getString("messages.needstar", "&#ff2222\u2716 &f\u0414\u043b\u044f \u0430\u043a\u0442\u0438\u0432\u0430\u0446\u0438\u0438 \u0446\u0432\u0435\u0442\u0430 \u043d\u0435\u043e\u0431\u0445\u043e\u0434\u0438\u043c\u0430 &6{level} \u0437\u0432\u0435\u0437\u0434\u0430 \u0441\u0442\u0430\u0442\u0443\u0441\u0430&f.");
                msg = msg.replace("{level}", MenuItems.convertInt(requiredStar) + " (" + requiredStar + " \u0443\u0440.)");
                player.sendMessage(HexUtil.translate(msg));
                return;
            }
            CraftoriumCustom.setPlayerData(player, "name_color", color);
            player.sendMessage(HexUtil.translate("&#00d8ff\u25b6 &f\u0426\u0432\u0435\u0442 \u043d\u0438\u043a\u043d\u0435\u0439\u043c\u0430 \u0443\u0441\u0442\u0430\u043d\u043e\u0432\u043b\u0435\u043d!"));
            new ColorNameMenu(CraftoriumCustom.menuItems).open(player);
        }
    }
}

