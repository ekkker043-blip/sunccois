package ru.craftorium.craftoriumcustom.listeners.sounds;

import java.util.HashMap;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.potion.PotionEffectType;
import ru.craftorium.craftoriumcustom.CraftoriumCustom;
import ru.craftorium.craftoriumcustom.holders.SoundHitMenuHolder;
import ru.craftorium.craftoriumcustom.items.MenuItems;
import ru.craftorium.craftoriumcustom.manager.ConfigManager;
import ru.craftorium.craftoriumcustom.menus.sounds.SoundDeathMeMenu;
import ru.craftorium.craftoriumcustom.menus.sounds.SoundDeathPlayerMenu;
import ru.craftorium.craftoriumcustom.menus.sounds.SoundHitMenu;
import ru.craftorium.craftoriumcustom.menus.sounds.SoundLowHealMenu;
import ru.craftorium.craftoriumcustom.menus.sounds.SoundVolumeMenu;
import ru.craftorium.craftoriumcustom.utils.HexUtil;

public class SoundHitListener
implements Listener {
    private final CraftoriumCustom plugin;
    private final ConfigManager configManager;

    public SoundHitListener(CraftoriumCustom plugin, ConfigManager configManager) {
        this.plugin = plugin;
        this.configManager = configManager;
    }

    @EventHandler
    public void on(InventoryClickEvent e) {
        Player player = (Player)e.getWhoClicked();
        if (e.getClickedInventory() != null && e.getClickedInventory().getHolder() != null && e.getWhoClicked() instanceof Player && e.getClickedInventory().getHolder() instanceof SoundHitMenuHolder) {
            e.setCancelled(true);
            this.inventoryClick(player, e.getSlot(), e.getClick());
        }
    }

    private void inventoryClick(Player player, int slot, ClickType clickType) {
        int configSlot;
        String path;
        MenuItems menuItems = CraftoriumCustom.menuItems;
        switch (slot) {
            case 3: {
                new SoundDeathMeMenu(menuItems).open(player);
                return;
            }
            case 4: {
                new SoundLowHealMenu(menuItems).open(player);
                return;
            }
            case 5: {
                new SoundDeathPlayerMenu(menuItems).open(player);
                return;
            }
            case 45: {
                CraftoriumCustom.mainMenu.open(player);
                return;
            }
        }
        if (clickType == ClickType.LEFT && this.plugin.getConfig().contains("hitsounds")) {
            for (String key : this.plugin.getConfig().getConfigurationSection("hitsounds.items").getKeys(false)) {
                path = "hitsounds.items." + key;
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
                    if (CraftoriumCustom.getPlayerData(player, "hit_sound").equalsIgnoreCase(sound.name())) {
                        HashMap<String, String> data = CraftoriumCustom.getPlayerData(player);
                        if (data != null) {
                            data.remove("hit_sound");
                        }
                        String msg = this.plugin.getConfig().getString("messages.removesound", "&f\u0417\u0432\u0443\u043a \u043e\u0442\u043a\u043b\u044e\u0447\u0435\u043d.");
                        player.sendMessage(HexUtil.translate(msg));
                    } else {
                        CraftoriumCustom.setPlayerData(player, "hit_sound", sound.name());
                        CraftoriumCustom.setPlayerData(player, "sound_volume", "1.0");
                        CraftoriumCustom.setPlayerData(player, "sound_pitch", "1.0");
                        player.playSound(player.getLocation(), sound, 1.0f, 1.0f);
                        String displayN = this.plugin.getConfig().getString(path + ".name", soundName);
                        String msg = this.plugin.getConfig().getString("messages.acceptsound", "&f\u0412\u044b\u0431\u0440\u0430\u043d \u0437\u0432\u0443\u043a: {sound}").replace("{sound}", displayN);
                        player.sendMessage(HexUtil.translate(msg));
                    }
                }
                catch (IllegalArgumentException ex) {
                    player.sendMessage("\u00a7c\u041e\u0448\u0438\u0431\u043a\u0430: \u0437\u0432\u0443\u043a " + soundName + " \u043d\u0435 \u043d\u0430\u0439\u0434\u0435\u043d!");
                }
                SoundHitMenu soundHitMenu = new SoundHitMenu(menuItems);
                soundHitMenu.open(player);
                return;
            }
        }
        if (clickType == ClickType.RIGHT && this.plugin.getConfig().contains("hitsounds")) {
            for (String key : this.plugin.getConfig().getConfigurationSection("hitsounds.items").getKeys(false)) {
                path = "hitsounds.items." + key;
                configSlot = this.plugin.getConfig().getInt(path + ".slot");
                if (configSlot != slot) continue;
                String soundName = this.plugin.getConfig().getString(path + ".sound");
                try {
                    Sound sound = Sound.valueOf((String)soundName);
                    if (!CraftoriumCustom.getPlayerData(player, "hit_sound").equalsIgnoreCase(sound.name())) continue;
                    SoundVolumeMenu soundVolumeMenu = new SoundVolumeMenu(menuItems);
                    soundVolumeMenu.open(player);
                }
                catch (IllegalArgumentException illegalArgumentException) {}
            }
        }
    }

    @EventHandler
    public void on(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player && this.isCriticalHit((Player)e.getDamager())) {
            Sound sound;
            Player damager = (Player)e.getDamager();
            HashMap<String, String> data = CraftoriumCustom.getPlayerData(damager);
            if (data == null) {
                return;
            }
            String hitSound = data.get("hit_sound");
            if (hitSound == null || hitSound.isEmpty()) {
                return;
            }
            try {
                sound = Sound.valueOf((String)hitSound.toUpperCase());
            }
            catch (IllegalArgumentException ex) {
                return;
            }
            float volume = 1.0f;
            float pitch = 1.0f;
            try {
                volume = Float.parseFloat(data.getOrDefault("sound_volume", "1.0"));
                pitch = Float.parseFloat(data.getOrDefault("sound_pitch", "1.0"));
            }
            catch (NumberFormatException numberFormatException) {
                // empty catch block
            }
            damager.playSound(damager.getLocation(), sound, volume, pitch);
        }
    }

    private boolean isCriticalHit(Player player) {
        return player.getFallDistance() > 0.0f && !player.isOnGround() && !player.hasPotionEffect(PotionEffectType.BLINDNESS) && !player.isInsideVehicle();
    }
}

