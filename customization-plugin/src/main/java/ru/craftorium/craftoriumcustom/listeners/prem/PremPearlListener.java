package ru.craftorium.craftoriumcustom.listeners.prem;

import java.util.HashMap;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import ru.craftorium.craftoriumcustom.CraftoriumCustom;
import ru.craftorium.craftoriumcustom.holders.PremPearlMenuHolder;
import ru.craftorium.craftoriumcustom.items.MenuItems;
import ru.craftorium.craftoriumcustom.manager.ConfigManager;
import ru.craftorium.craftoriumcustom.menus.prem.PremPearlMenu;
import ru.craftorium.craftoriumcustom.utils.HexUtil;

public class PremPearlListener
implements Listener {
    private final CraftoriumCustom plugin;
    private final ConfigManager configManager;

    public PremPearlListener(CraftoriumCustom plugin, ConfigManager configManager) {
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
        if (!(e.getView().getTopInventory().getHolder() instanceof PremPearlMenuHolder)) {
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
            CraftoriumCustom.removePlayerData(player, "pearl_particle");
            player.sendMessage("\u00a7a\u0427\u0430\u0441\u0442\u0438\u0446\u044b \u0436\u0435\u043c\u0447\u0443\u0433\u0430 \u0441\u0431\u0440\u043e\u0448\u0435\u043d\u044b!");
            PremPearlMenu menu = new PremPearlMenu(menuItems);
            menu.open(player);
            return;
        }
        // Banner navigation to DeathPlayer / DeathMe sections removed — the
        // Premium tab now only contains pearl-particle effects.
        if (this.plugin.getConfig().contains("pearlparticles.items")) {
            for (String key : this.plugin.getConfig().getConfigurationSection("pearlparticles.items").getKeys(false)) {
                String path = "pearlparticles.items." + key;
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
                        String current = CraftoriumCustom.getPlayerData(player, "pearl_particle");
                        if (current.equalsIgnoreCase(particle.name())) {
                            HashMap<String, String> data = CraftoriumCustom.getPlayerData(player);
                            if (data != null) {
                                data.remove("pearl_particle");
                            }
                            String msg = this.plugin.getConfig().getString("messages.removeparticle", "&f\u0427\u0430\u0441\u0442\u0438\u0446\u044b \u043e\u0442\u043a\u043b\u044e\u0447\u0435\u043d\u044b.");
                            player.sendMessage(HexUtil.translate(msg));
                        } else {
                            CraftoriumCustom.setPlayerData(player, "pearl_particle", particle.name());
                            String displayN = this.plugin.getConfig().getString(path + ".name", particleName);
                            String msg = this.plugin.getConfig().getString("messages.acceptparticle", "&f\u0412\u044b\u0431\u0440\u0430\u043d\u044b \u0447\u0430\u0441\u0442\u0438\u0446\u044b: {particle}").replace("{particle}", displayN);
                            player.sendMessage(HexUtil.translate(msg));
                        }
                        PremPearlMenu menu = new PremPearlMenu(menuItems);
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
    public void onPearlThrow(ProjectileLaunchEvent e) {
        if (!(e.getEntity() instanceof EnderPearl)) {
            return;
        }
        if (!(e.getEntity().getShooter() instanceof Player)) {
            return;
        }
        Player player = (Player)e.getEntity().getShooter();
        if (!player.hasPermission("stickhwcustom.prem")) {
            return;
        }
        HashMap<String, String> data = CraftoriumCustom.getPlayerData(player);
        if (data == null) {
            return;
        }
        String particleName = data.get("pearl_particle");
        if (particleName == null || particleName.isEmpty()) {
            return;
        }
        try {
            final Particle particle = Particle.valueOf((String)particleName);
            final EnderPearl pearl = (EnderPearl)e.getEntity();
            new BukkitRunnable(){

                public void run() {
                    if (pearl.isDead() || !pearl.isValid()) {
                        this.cancel();
                        return;
                    }
                    pearl.getWorld().spawnParticle(particle, pearl.getLocation(), 5, 0.1, 0.1, 0.1, 0.0);
                }
            }.runTaskTimer((Plugin)this.plugin, 0L, 1L);
        }
        catch (IllegalArgumentException illegalArgumentException) {
            // empty catch block
        }
    }
}

