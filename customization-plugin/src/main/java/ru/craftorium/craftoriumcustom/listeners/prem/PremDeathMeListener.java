package ru.craftorium.craftoriumcustom.listeners.prem;

import java.util.HashMap;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import ru.craftorium.craftoriumcustom.CraftoriumCustom;
import ru.craftorium.craftoriumcustom.holders.PremDeathMeMenuHolder;
import ru.craftorium.craftoriumcustom.items.MenuItems;
import ru.craftorium.craftoriumcustom.manager.ConfigManager;
import ru.craftorium.craftoriumcustom.menus.prem.PremDeathMeMenu;
import ru.craftorium.craftoriumcustom.menus.prem.PremDeathPlayerMenu;
import ru.craftorium.craftoriumcustom.menus.prem.PremPearlMenu;
import ru.craftorium.craftoriumcustom.utils.HexUtil;

public class PremDeathMeListener
implements Listener {
    private final CraftoriumCustom plugin;
    private final ConfigManager configManager;

    public PremDeathMeListener(CraftoriumCustom plugin, ConfigManager configManager) {
        this.plugin = plugin;
        this.configManager = configManager;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getClickedInventory() == null) {
            return;
        }
        if (!(e.getWhoClicked() instanceof Player)) {
            return;
        }
        if (!(e.getView().getTopInventory().getHolder() instanceof PremDeathMeMenuHolder)) {
            return;
        }
        e.setCancelled(true);
        if (e.getClickedInventory() != e.getView().getTopInventory()) {
            return;
        }
        Player player = (Player)e.getWhoClicked();
        int slot = e.getSlot();
        ClickType clickType = e.getClick();
        MenuItems menuItems = CraftoriumCustom.menuItems;
        if (slot == 45) {
            CraftoriumCustom.mainMenu.open(player);
            return;
        }
        if (slot == 53 && e.getCurrentItem() != null && e.getCurrentItem().getType() == Material.BARRIER) {
            CraftoriumCustom.removePlayerData(player, "death_me_particle");
            player.sendMessage("\u00a7a\u0427\u0430\u0441\u0442\u0438\u0446\u044b \u0441\u043c\u0435\u0440\u0442\u0438 \u0441\u0431\u0440\u043e\u0448\u0435\u043d\u044b!");
            PremDeathMeMenu menu = new PremDeathMeMenu(menuItems);
            menu.open(player);
            return;
        }
        if (slot == 2) {
            PremPearlMenu menu = new PremPearlMenu(menuItems);
            menu.open(player);
            return;
        }
        if (slot == 3) {
            PremDeathPlayerMenu menu = new PremDeathPlayerMenu(menuItems);
            menu.open(player);
            return;
        }
        if (this.plugin.getConfig().contains("deathparticles.items")) {
            for (String key : this.plugin.getConfig().getConfigurationSection("deathparticles.items").getKeys(false)) {
                String path = "deathparticles.items." + key;
                int configSlot = this.plugin.getConfig().getInt(path + ".slot");
                if (configSlot != slot) continue;
                String particleName = this.plugin.getConfig().getString(path + ".particle");
                try {
                    Particle particle = Particle.valueOf((String)particleName);
                    if (clickType == ClickType.LEFT) {
                        if (!player.hasPermission("stickhwcustom.prem")) {
                            String msg = this.plugin.getConfig().getString("messages.nopremium", "&#ff2222\u25b6 &f\u042d\u0442\u043e\u0442 \u044d\u0444\u0444\u0435\u043a\u0442 \u0432\u044b\u0434\u0430\u0451\u0442\u0441\u044f \u0437\u0430 \u0440\u0430\u0431\u043e\u0442\u0443 \u0432 \u043a\u043e\u043c\u0430\u043d\u0434\u0435 \u0441\u0435\u0440\u0432\u0435\u0440\u0430.");
                            player.sendMessage(HexUtil.translate(msg));
                            return;
                        }
                        String current = CraftoriumCustom.getPlayerData(player, "death_me_particle");
                        if (current.equalsIgnoreCase(particle.name())) {
                            HashMap<String, String> data = CraftoriumCustom.getPlayerData(player);
                            if (data != null) {
                                data.remove("death_me_particle");
                            }
                            String msg = this.plugin.getConfig().getString("messages.removeparticle", "&f\u0427\u0430\u0441\u0442\u0438\u0446\u044b \u043e\u0442\u043a\u043b\u044e\u0447\u0435\u043d\u044b.");
                            player.sendMessage(HexUtil.translate(msg));
                        } else {
                            CraftoriumCustom.setPlayerData(player, "death_me_particle", particle.name());
                            String displayN = this.plugin.getConfig().getString(path + ".name", particleName);
                            String msg = this.plugin.getConfig().getString("messages.acceptparticle", "&f\u0412\u044b\u0431\u0440\u0430\u043d\u044b \u0447\u0430\u0441\u0442\u0438\u0446\u044b: {particle}").replace("{particle}", displayN);
                            player.sendMessage(HexUtil.translate(msg));
                        }
                        PremDeathMeMenu menu = new PremDeathMeMenu(menuItems);
                        menu.open(player);
                        continue;
                    }
                    if (clickType != ClickType.RIGHT) continue;
                    Location loc = player.getLocation().add(0.0, 2.0, 0.0);
                    player.spawnParticle(particle, loc, 20, 0.5, 0.5, 0.5, 0.0);
                }
                catch (IllegalArgumentException ex) {
                    player.sendMessage("\u00a7c\u041e\u0448\u0438\u0431\u043a\u0430: \u0447\u0430\u0441\u0442\u0438\u0446\u0430 " + particleName + " \u043d\u0435 \u043d\u0430\u0439\u0434\u0435\u043d\u0430!");
                }
            }
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        Player player = e.getEntity();
        if (!player.hasPermission("stickhwcustom.prem")) {
            return;
        }
        HashMap<String, String> data = CraftoriumCustom.getPlayerData(player);
        if (data == null) {
            return;
        }
        String particleName = data.get("death_me_particle");
        if (particleName == null || particleName.isEmpty()) {
            return;
        }
        try {
            Particle particle = Particle.valueOf((String)particleName);
            Location loc = player.getLocation().add(0.0, 1.0, 0.0);
            player.getWorld().spawnParticle(particle, loc, 30, 0.5, 0.8, 0.5, 0.0);
        }
        catch (IllegalArgumentException illegalArgumentException) {
            // empty catch block
        }
    }
}

