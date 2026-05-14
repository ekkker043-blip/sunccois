package ru.craftorium.craftoriumcustom.listeners.star;

import java.util.HashMap;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import ru.craftorium.craftoriumcustom.CraftoriumCustom;
import ru.craftorium.craftoriumcustom.holders.StarMenuHolder;
import ru.craftorium.craftoriumcustom.items.MenuItems;
import ru.craftorium.craftoriumcustom.items.StarItem;
import ru.craftorium.craftoriumcustom.menus.star.StarMenu;
import ru.craftorium.craftoriumcustom.utils.HexUtil;

public class StarListener
implements Listener {
    private static final int[] STAR_SLOTS = new int[]{12, 13, 14, 20, 21, 22, 23, 24, 39, 41};

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getClickedInventory() == null) {
            return;
        }
        if (!(e.getWhoClicked() instanceof Player)) {
            return;
        }
        if (!(e.getView().getTopInventory().getHolder() instanceof StarMenuHolder)) {
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
        for (int i = 0; i < STAR_SLOTS.length; ++i) {
            double points;
            int currentLevel;
            if (STAR_SLOTS[i] != slot) continue;
            int level = i + 1;
            int cost = CraftoriumCustom.getInstance().getConfig().getInt("stars.levels." + level + ".cost", 1000 * level);
            HashMap<String, String> data = CraftoriumCustom.getPlayerData(player);
            if (data == null) {
                return;
            }
            try {
                currentLevel = Integer.parseInt(data.getOrDefault("star_level", "0"));
                points = Double.parseDouble(data.getOrDefault("star_points", "0"));
            }
            catch (NumberFormatException ex) {
                return;
            }
            if (currentLevel >= level) {
                player.sendMessage(HexUtil.translate("&#ff2222\u25b6 &f\u042d\u0442\u0430 \u0437\u0432\u0435\u0437\u0434\u0430 \u0443\u0436\u0435 \u043f\u043e\u043b\u0443\u0447\u0435\u043d\u0430!"));
                return;
            }
            if (points < (double)cost) {
                double remaining = (double)cost - points;
                player.sendMessage(HexUtil.translate("&#ff2222\u25b6 &f\u041d\u0435\u043e\u0431\u0445\u043e\u0434\u0438\u043c\u043e \u0435\u0449\u0451 &6" + String.format("%.0f", remaining) + "\u20bd&f."));
                return;
            }
            data.put("star_level", String.valueOf(level));
            String hex = CraftoriumCustom.getInstance().getConfig().getString("stars.levels." + level + ".hex", "#FFFFFF");
            player.sendMessage(HexUtil.translate("&#ffc900\u25b6 &f\u0417\u0432\u0435\u0437\u0434\u0430 &#" + hex.replace("#", "") + MenuItems.convertInt(level) + " &f(" + level + " \u0443\u0440.) \u043f\u043e\u043b\u0443\u0447\u0435\u043d\u0430!"));
            StarItem starItem = new StarItem();
            StarMenu starMenu = new StarMenu(menuItems, starItem);
            starMenu.open(player);
            return;
        }
    }
}

