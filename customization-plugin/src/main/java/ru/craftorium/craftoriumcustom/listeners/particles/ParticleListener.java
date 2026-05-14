package ru.craftorium.craftoriumcustom.listeners.particles;

import java.util.HashMap;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import ru.craftorium.craftoriumcustom.CraftoriumCustom;
import ru.craftorium.craftoriumcustom.holders.ParticleMenuHolder;
import ru.craftorium.craftoriumcustom.items.MenuItems;
import ru.craftorium.craftoriumcustom.menus.MainMenu;
import ru.craftorium.craftoriumcustom.menus.particle.ParticleMenu;
import ru.craftorium.craftoriumcustom.utils.HexUtil;

public class ParticleListener
implements Listener {
    private final CraftoriumCustom plugin;

    public ParticleListener(CraftoriumCustom plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getClickedInventory() == null) {
            return;
        }
        if (!(e.getWhoClicked() instanceof Player)) {
            return;
        }
        if (!(e.getView().getTopInventory().getHolder() instanceof ParticleMenuHolder)) {
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
            MainMenu mainMenu = new MainMenu(menuItems);
            mainMenu.open(player);
            return;
        }
        if (slot == 53 && e.getCurrentItem() != null && e.getCurrentItem().getType() == Material.BARRIER) {
            CraftoriumCustom.removePlayerData(player, "crit_particle");
            player.sendMessage("\u00a7a\u0427\u0430\u0441\u0442\u0438\u0446\u044b \u0441\u0431\u0440\u043e\u0448\u0435\u043d\u044b!");
            ParticleMenu particleMenu = new ParticleMenu(menuItems);
            particleMenu.open(player);
            return;
        }
        if (this.plugin.getConfig().contains("particles.items")) {
            for (String key : this.plugin.getConfig().getConfigurationSection("particles.items").getKeys(false)) {
                String path = "particles.items." + key;
                int configSlot = this.plugin.getConfig().getInt(path + ".slot");
                if (configSlot != slot) continue;
                String particleName = this.plugin.getConfig().getString(path + ".particle");
                try {
                    Particle particle = Particle.valueOf((String)particleName);
                    if (clickType == ClickType.LEFT) {
                        // Admin-only effect: if the particle entry is marked "admin: true" in
                        // config, only players with the admin permission can equip it.
                        boolean adminOnly = this.plugin.getConfig().getBoolean(path + ".admin", false);
                        if (adminOnly && !player.hasPermission("stickhwcustom.admin.particle")) {
                            String msg = this.plugin.getConfig().getString("messages.adminonly",
                                "&#ff2222\u25b6 &f\u042d\u0442\u043e\u0442 \u044d\u0444\u0444\u0435\u043a\u0442 \u0434\u043e\u0441\u0442\u0443\u043f\u0435\u043d \u0442\u043e\u043b\u044c\u043a\u043e \u0430\u0434\u043c\u0438\u043d\u0438\u0441\u0442\u0440\u0430\u0442\u043e\u0440\u0443.");
                            player.sendMessage(HexUtil.translate(msg));
                            return;
                        }
                        String current = CraftoriumCustom.getPlayerData(player, "crit_particle");
                        if (current.equalsIgnoreCase(particle.name())) {
                            HashMap<String, String> data = CraftoriumCustom.getPlayerData(player);
                            if (data != null) {
                                data.remove("crit_particle");
                            }
                            String msg = this.plugin.getConfig().getString("messages.removeparticle", "&f\u0427\u0430\u0441\u0442\u0438\u0446\u044b \u043e\u0442\u043a\u043b\u044e\u0447\u0435\u043d\u044b.");
                            player.sendMessage(HexUtil.translate(msg));
                        } else {
                            CraftoriumCustom.setPlayerData(player, "crit_particle", particle.name());
                            String displayN = this.plugin.getConfig().getString(path + ".name", particleName);
                            String msg = this.plugin.getConfig().getString("messages.acceptparticle", "&f\u0412\u044b\u0431\u0440\u0430\u043d\u044b \u0447\u0430\u0441\u0442\u0438\u0446\u044b: {particle}").replace("{particle}", displayN);
                            player.sendMessage(HexUtil.translate(msg));
                        }
                        ParticleMenu particleMenu = new ParticleMenu(menuItems);
                        particleMenu.open(player);
                        continue;
                    }
                    if (clickType != ClickType.RIGHT) continue;
                    this.spawnDemoParticles(player, particle);
                }
                catch (IllegalArgumentException ex) {
                    player.sendMessage("\u00a7c\u041e\u0448\u0438\u0431\u043a\u0430: \u0447\u0430\u0441\u0442\u0438\u0446\u0430 " + particleName + " \u043d\u0435 \u043d\u0430\u0439\u0434\u0435\u043d\u0430!");
                }
            }
        }
    }

    private void spawnDemoParticles(Player player, Particle particle) {
        Location loc = player.getLocation().add(0.0, 2.0, 0.0);
        if (particle == Particle.REDSTONE) {
            Color color = this.getColorFromConfig(player);
            Particle.DustOptions dustOptions = new Particle.DustOptions(color, 1.0f);
            player.spawnParticle(particle, loc, 20, 0.5, 0.5, 0.5, 0.0, (Object)dustOptions);
        } else {
            player.spawnParticle(particle, loc, 20, 0.5, 0.5, 0.5, 0.0);
        }
    }

    private Color getColorFromConfig(Player player) {
        String colorStr = CraftoriumCustom.getPlayerData(player, "particle_color");
        if (colorStr != null && !colorStr.equalsIgnoreCase("null")) {
            try {
                return Color.fromRGB((int)Integer.parseInt(colorStr));
            }
            catch (NumberFormatException numberFormatException) {
                // empty catch block
            }
        }
        String configColor = this.plugin.getConfig().getString("particles.items.redstone.color", "RED");
        switch (configColor.toUpperCase()) {
            case "RED": {
                return Color.RED;
            }
            case "BLUE": {
                return Color.BLUE;
            }
            case "GREEN": {
                return Color.GREEN;
            }
            case "YELLOW": {
                return Color.YELLOW;
            }
            case "WHITE": {
                return Color.WHITE;
            }
            case "PURPLE": {
                return Color.PURPLE;
            }
        }
        return Color.RED;
    }

    @EventHandler
    public void onCritHit(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player)) {
            return;
        }
        Player player = (Player)e.getDamager();
        if (!(player.getFallDistance() > 0.0f)) {
            return;
        }
        HashMap<String, String> data = CraftoriumCustom.getPlayerData(player);
        if (data == null) {
            return;
        }
        String particleName = data.get("crit_particle");
        if (particleName == null || particleName.isEmpty()) {
            return;
        }
        try {
            Particle particle = Particle.valueOf((String)particleName);
            Location loc = e.getEntity().getLocation().add(0.0, 1.0, 0.0);
            if (particle == Particle.REDSTONE) {
                Color color = this.getColorFromConfig(player);
                Particle.DustOptions dustOptions = new Particle.DustOptions(color, 1.0f);
                player.getWorld().spawnParticle(particle, loc, 15, 0.3, 0.5, 0.3, 0.0, (Object)dustOptions);
            } else {
                player.getWorld().spawnParticle(particle, loc, 15, 0.3, 0.5, 0.3, 0.0);
            }
        }
        catch (IllegalArgumentException illegalArgumentException) {
            // empty catch block
        }
    }
}

