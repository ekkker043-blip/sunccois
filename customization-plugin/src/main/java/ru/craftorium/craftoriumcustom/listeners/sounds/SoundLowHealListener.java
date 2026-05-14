package ru.craftorium.craftoriumcustom.listeners.sounds;

import java.util.HashMap;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import ru.craftorium.craftoriumcustom.CraftoriumCustom;
import ru.craftorium.craftoriumcustom.holders.SoundLowHealMenuHolder;
import ru.craftorium.craftoriumcustom.items.MenuItems;
import ru.craftorium.craftoriumcustom.manager.ConfigManager;
import ru.craftorium.craftoriumcustom.menus.sounds.SoundDeathMeMenu;
import ru.craftorium.craftoriumcustom.menus.sounds.SoundDeathPlayerMenu;
import ru.craftorium.craftoriumcustom.menus.sounds.SoundHitMenu;
import ru.craftorium.craftoriumcustom.menus.sounds.SoundLowHealMenu;
import ru.craftorium.craftoriumcustom.menus.sounds.SoundVolumeMenu;
import ru.craftorium.craftoriumcustom.utils.HexUtil;

public class SoundLowHealListener
implements Listener {
    private final CraftoriumCustom plugin;
    private final ConfigManager configManager;

    public SoundLowHealListener(CraftoriumCustom plugin, ConfigManager configManager) {
        this.plugin = plugin;
        this.configManager = configManager;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        int configSlot;
        String path;
        if (e.getClickedInventory() == null) {
            return;
        }
        if (!(e.getWhoClicked() instanceof Player)) {
            return;
        }
        if (!(e.getView().getTopInventory().getHolder() instanceof SoundLowHealMenuHolder)) {
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
        if (slot == 2) {
            SoundHitMenu menu = new SoundHitMenu(menuItems);
            menu.open(player);
            return;
        }
        if (slot == 3) {
            SoundDeathMeMenu menu = new SoundDeathMeMenu(menuItems);
            menu.open(player);
            return;
        }
        if (slot == 5) {
            SoundDeathPlayerMenu menu = new SoundDeathPlayerMenu(menuItems);
            menu.open(player);
            return;
        }
        if (clickType == ClickType.LEFT && this.plugin.getConfig().contains("lowheal.items")) {
            for (String key : this.plugin.getConfig().getConfigurationSection("lowheal.items").getKeys(false)) {
                path = "lowheal.items." + key;
                configSlot = this.plugin.getConfig().getInt(path + ".slot");
                if (configSlot != slot) continue;
                boolean premium = this.plugin.getConfig().getBoolean(path + ".premium", false);
                if (premium && !player.hasPermission("stickhwcustom.prem")) {
                    String msg = this.plugin.getConfig().getString("messages.nopremium", "&#ff2222\u25b6 &f\u042d\u0442\u043e\u0442 \u044d\u0444\u0444\u0435\u043a\u0442 \u0432\u044b\u0434\u0430\u0451\u0442\u0441\u044f \u0437\u0430 \u0440\u0430\u0431\u043e\u0442\u0443 \u0432 \u043a\u043e\u043c\u0430\u043d\u0434\u0435 \u0441\u0435\u0440\u0432\u0435\u0440\u0430.");
                    player.sendMessage(HexUtil.translate(msg));
                    return;
                }
                String soundName = this.plugin.getConfig().getString(path + ".sound");
                try {
                    Sound sound = Sound.valueOf((String)soundName);
                    if (CraftoriumCustom.getPlayerData(player, "lowhealth_sound").equalsIgnoreCase(sound.name())) {
                        HashMap<String, String> data2 = CraftoriumCustom.getPlayerData(player);
                        if (data2 != null) {
                            data2.remove("lowhealth_sound");
                        }
                        String msg = this.plugin.getConfig().getString("messages.removesound", "&f\u0417\u0432\u0443\u043a \u043e\u0442\u043a\u043b\u044e\u0447\u0435\u043d.");
                        player.sendMessage(HexUtil.translate(msg));
                    } else {
                        CraftoriumCustom.setPlayerData(player, "lowhealth_sound", sound.name());
                        CraftoriumCustom.setPlayerData(player, "sound_volume", "1.0");
                        CraftoriumCustom.setPlayerData(player, "sound_pitch", "1.0");
                        player.playSound(player.getLocation(), sound, 1.0f, 1.0f);
                        String displayN = this.plugin.getConfig().getString(path + ".name", soundName);
                        String msg = this.plugin.getConfig().getString("messages.acceptsound", "&f\u0412\u044b\u0431\u0440\u0430\u043d \u0437\u0432\u0443\u043a: {sound}").replace("{sound}", displayN);
                        player.sendMessage(HexUtil.translate(msg));
                    }
                    SoundLowHealMenu menu = new SoundLowHealMenu(menuItems);
                    menu.open(player);
                    return;
                }
                catch (IllegalArgumentException ex) {
                    player.sendMessage("\u00a7c\u041e\u0448\u0438\u0431\u043a\u0430: \u0437\u0432\u0443\u043a " + soundName + " \u043d\u0435 \u043d\u0430\u0439\u0434\u0435\u043d!");
                }
            }
        }
        if (clickType == ClickType.RIGHT && this.plugin.getConfig().contains("lowheal")) {
            for (String key : this.plugin.getConfig().getConfigurationSection("lowheal.items").getKeys(false)) {
                path = "lowheal.items." + key;
                configSlot = this.plugin.getConfig().getInt(path + ".slot");
                if (configSlot != slot) continue;
                String soundName = this.plugin.getConfig().getString(path + ".sound");
                try {
                    Sound sound = Sound.valueOf((String)soundName);
                    if (!CraftoriumCustom.getPlayerData(player, "lowhealth_sound").equalsIgnoreCase(sound.name())) continue;
                    SoundVolumeMenu volumeMenu = new SoundVolumeMenu(menuItems);
                    volumeMenu.open(player);
                }
                catch (IllegalArgumentException illegalArgumentException) {}
            }
        }
    }

    @EventHandler
    public void onLowHealth(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player)) {
            return;
        }
        final Player player = (Player)e.getEntity();
        new BukkitRunnable(){

            public void run() {
                if (player.getHealth() > 4.5) {
                    return;
                }
                HashMap<String, String> data = CraftoriumCustom.getPlayerData(player);
                if (data == null) {
                    return;
                }
                String soundName = data.get("lowhealth_sound");
                if (soundName == null || soundName.isEmpty()) {
                    return;
                }
                try {
                    Sound sound = Sound.valueOf((String)soundName.toUpperCase());
                    float volume = 1.0f;
                    float pitch = 1.0f;
                    try {
                        volume = Float.parseFloat(data.getOrDefault("sound_volume", "1.0"));
                        pitch = Float.parseFloat(data.getOrDefault("sound_pitch", "1.0"));
                    }
                    catch (NumberFormatException numberFormatException) {
                        // empty catch block
                    }
                    player.playSound(player.getLocation(), sound, volume, pitch);
                }
                catch (Exception exception) {
                    // empty catch block
                }
            }
        }.runTaskLater((Plugin)this.plugin, 1L);
    }
}

