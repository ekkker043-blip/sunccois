package ru.craftorium.craftoriumcustom;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import ru.craftorium.craftoriumcustom.api.CustomPlaceholder;
import ru.craftorium.craftoriumcustom.commands.CustomCommand;
import ru.craftorium.craftoriumcustom.db.DatabaseManager;
import ru.craftorium.craftoriumcustom.items.MenuItems;
import ru.craftorium.craftoriumcustom.items.StarItem;
import ru.craftorium.craftoriumcustom.listeners.EventListener;
import ru.craftorium.craftoriumcustom.listeners.colorname.ColorNameListener;
import ru.craftorium.craftoriumcustom.listeners.info.InfoListener;
import ru.craftorium.craftoriumcustom.listeners.particles.ParticleListener;
import ru.craftorium.craftoriumcustom.listeners.prem.PremDeathMeListener;
import ru.craftorium.craftoriumcustom.listeners.prem.PremDeathPlayerListener;
import ru.craftorium.craftoriumcustom.listeners.prem.PremPearlListener;
import ru.craftorium.craftoriumcustom.listeners.sounds.SoundDeathMeListener;
import ru.craftorium.craftoriumcustom.listeners.sounds.SoundDeathPlayerListener;
import ru.craftorium.craftoriumcustom.listeners.sounds.SoundHitListener;
import ru.craftorium.craftoriumcustom.listeners.sounds.SoundLowHealListener;
import ru.craftorium.craftoriumcustom.listeners.sounds.SoundVolumeListener;
import ru.craftorium.craftoriumcustom.listeners.star.StarListener;
import ru.craftorium.craftoriumcustom.listeners.traps.TrapsListener;
import ru.craftorium.craftoriumcustom.manager.ConfigManager;
import ru.craftorium.craftoriumcustom.menus.MainMenu;
import ru.craftorium.craftoriumcustom.utils.PremiumUtil;

public final class CraftoriumCustom
extends JavaPlugin
implements Listener {
    private static CraftoriumCustom instance;
    public static MenuItems menuItems;
    public static StarItem starItem;
    public static MainMenu mainMenu;
    public DatabaseManager dbm;
    public static final Map<UUID, HashMap<String, String>> playerData;
    public static CustomPlaceholder customPlaceholder;
    private static ConfigManager configManager;

    public void onEnable() {
        this.saveDefaultConfig();
        configManager = new ConfigManager(this);
        this.dbm = new DatabaseManager(this, configManager);
        instance = this;
        menuItems = new MenuItems();
        mainMenu = new MainMenu(menuItems);
        this.getCommand("custom").setExecutor((CommandExecutor)new CustomCommand(mainMenu));
        this.registerEvents();
        customPlaceholder = new CustomPlaceholder();
        customPlaceholder.register();
        Bukkit.getScheduler().runTaskAsynchronously((Plugin)this, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                DatabaseManager.getPlayerAsync(player.getName()).thenAccept(data -> {
                    HashMap playerMap = data != null ? DatabaseManager.fromBase64(data) : new HashMap();
                    Bukkit.getScheduler().runTask((Plugin)this, () -> playerData.put(player.getUniqueId(), playerMap));
                });
            }
        });
        new BukkitRunnable(){

            public void run() {
                CraftoriumCustom.this.saveAllPlayerDataAsync();
            }
        }.runTaskTimerAsynchronously((Plugin)this, 0L, 2400L);
        // Check premium expirations once a second so the revocation feels
        // instant. The loop just compares two longs per online player, so the
        // tick cost is negligible.
        new BukkitRunnable() {

            public void run() {
                PremiumUtil.enforceExpirations();
            }
        }.runTaskTimer((Plugin) this, 20L, 20L);
    }

    private void registerEvents() {
        this.getServer().getPluginManager().registerEvents((Listener)new TrapsListener(), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new ParticleListener(this), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new PremPearlListener(this, configManager), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new PremDeathMeListener(this, configManager), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new ColorNameListener(), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new InfoListener(), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new PremDeathPlayerListener(this, configManager), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new EventListener(), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new StarListener(), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)this, (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new SoundLowHealListener(this, configManager), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new SoundDeathPlayerListener(this, configManager), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new SoundDeathMeListener(this, configManager), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new SoundHitListener(this, configManager), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new SoundVolumeListener(), (Plugin)this);
    }

    private void saveAllPlayerDataAsync() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            HashMap<String, String> data = playerData.get(player.getUniqueId());
            if (data == null) continue;
            DatabaseManager.setPlayerAsync(player.getName(), DatabaseManager.toBase64(data));
        }
    }

    public void onDisable() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            HashMap<String, String> data = playerData.get(player.getUniqueId());
            if (data == null) continue;
            DatabaseManager.setPlayerAsync(player.getName(), DatabaseManager.toBase64(data)).join();
        }
    }

    public static CraftoriumCustom getInstance() {
        return instance;
    }

    public static HashMap<String, String> getPlayerData(Player player) {
        return playerData.get(player.getUniqueId());
    }

    public static String getPlayerData(Player player, String key) {
        HashMap<String, String> data = playerData.get(player.getUniqueId());
        return data != null ? data.getOrDefault(key, "null") : "null";
    }

    public static void setPlayerData(Player player, String key, String value) {
        playerData.computeIfAbsent(player.getUniqueId(), k -> new HashMap()).put(key, value);
    }

    public static void removePlayerData(Player player, String key) {
        HashMap<String, String> data = playerData.get(player.getUniqueId());
        if (data != null) {
            data.remove(key);
        }
    }

    static {
        playerData = new ConcurrentHashMap<UUID, HashMap<String, String>>();
    }
}

