package ru.craftorium.craftoriumcustom.listeners.traps;

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
import ru.craftorium.craftoriumcustom.holders.SkinMenuHolder;
import ru.craftorium.craftoriumcustom.items.MenuItems;
import ru.craftorium.craftoriumcustom.menus.skins.SkinsMenu;
import ru.craftorium.craftoriumcustom.utils.HexUtil;

public class TrapsListener
implements Listener {
    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getClickedInventory() == null) {
            return;
        }
        if (!(e.getWhoClicked() instanceof Player)) {
            return;
        }
        if (!(e.getView().getTopInventory().getHolder() instanceof SkinMenuHolder)) {
            return;
        }
        e.setCancelled(true);
        if (e.getClickedInventory() != e.getView().getTopInventory()) {
            return;
        }
        Player player = (Player)e.getWhoClicked();
        int slot = e.getSlot();
        MenuItems menuItems = CraftoriumCustom.menuItems;
        if (slot == 53) {
            CraftoriumCustom.mainMenu.open(player);
            return;
        }
        ItemStack item = e.getCurrentItem();
        if (item == null || !item.hasItemMeta()) {
            return;
        }
        ItemMeta meta = item.getItemMeta();
        NamespacedKey trapKey = new NamespacedKey((Plugin)CraftoriumCustom.getInstance(), "trap-type");
        NamespacedKey stanKey = new NamespacedKey((Plugin)CraftoriumCustom.getInstance(), "stan-type");
        NamespacedKey premKey = new NamespacedKey((Plugin)CraftoriumCustom.getInstance(), "premium");
        if (meta.getPersistentDataContainer().has(trapKey, PersistentDataType.STRING)) {
            String premium = (String)meta.getPersistentDataContainer().getOrDefault(premKey, PersistentDataType.STRING, "false");
            if ("true".equalsIgnoreCase(premium) && !player.hasPermission("stickhwcustom.prem")) {
                String msg = CraftoriumCustom.getInstance().getConfig().getString("messages.nopremium", "&#ff2222\u25b6 &f\u042d\u0442\u043e\u0442 \u044d\u0444\u0444\u0435\u043a\u0442 \u0432\u044b\u0434\u0430\u0451\u0442\u0441\u044f \u0437\u0430 \u0440\u0430\u0431\u043e\u0442\u0443 \u0432 \u043a\u043e\u043c\u0430\u043d\u0434\u0435 \u0441\u0435\u0440\u0432\u0435\u0440\u0430.");
                player.sendMessage(HexUtil.translate(msg));
                return;
            }
            String tag = (String)meta.getPersistentDataContainer().get(trapKey, PersistentDataType.STRING);
            CraftoriumCustom.setPlayerData(player, "trap_skin", tag);
            player.sendMessage(HexUtil.translate("&#00d8ff\u25b6 &f\u0421\u043a\u0438\u043d \u0442\u0440\u0430\u043f\u043a\u0438 \u0443\u0441\u0442\u0430\u043d\u043e\u0432\u043b\u0435\u043d: &6" + tag));
            SkinsMenu skinsMenu = new SkinsMenu(menuItems);
            skinsMenu.open(player);
        } else if (meta.getPersistentDataContainer().has(stanKey, PersistentDataType.STRING)) {
            String premium = (String)meta.getPersistentDataContainer().getOrDefault(premKey, PersistentDataType.STRING, "false");
            if ("true".equalsIgnoreCase(premium) && !player.hasPermission("stickhwcustom.prem")) {
                String msg = CraftoriumCustom.getInstance().getConfig().getString("messages.nopremium", "&#ff2222\u25b6 &f\u042d\u0442\u043e\u0442 \u044d\u0444\u0444\u0435\u043a\u0442 \u0432\u044b\u0434\u0430\u0451\u0442\u0441\u044f \u0437\u0430 \u0440\u0430\u0431\u043e\u0442\u0443 \u0432 \u043a\u043e\u043c\u0430\u043d\u0434\u0435 \u0441\u0435\u0440\u0432\u0435\u0440\u0430.");
                player.sendMessage(HexUtil.translate(msg));
                return;
            }
            String tag = (String)meta.getPersistentDataContainer().get(stanKey, PersistentDataType.STRING);
            CraftoriumCustom.setPlayerData(player, "stan_skin", tag);
            player.sendMessage(HexUtil.translate("&#00d8ff\u25b6 &f\u0421\u043a\u0438\u043d \u0441\u0442\u0430\u043d\u0430 \u0443\u0441\u0442\u0430\u043d\u043e\u0432\u043b\u0435\u043d: &6" + tag));
            SkinsMenu skinsMenu = new SkinsMenu(menuItems);
            skinsMenu.open(player);
        }
    }
}

