package ru.craftorium.craftoriumcustom.manager;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class ConfigManager {
    private final JavaPlugin plugin;
    private File configFile;
    private FileConfiguration config;

    public ConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.configFile = new File(plugin.getDataFolder(), "config.yml");
        this.reloadConfig();
    }

    public void reloadConfig() {
        if (!this.configFile.exists()) {
            this.plugin.saveResource("config.yml", false);
        }
        this.config = YamlConfiguration.loadConfiguration((File)this.configFile);
        if (!this.config.contains("database.type")) {
            this.config.set("database.type", (Object)"sqlite");
            this.saveConfig();
        }
    }

    public void saveConfig() {
        try {
            this.config.save(this.configFile);
        }
        catch (IOException e) {
            this.plugin.getLogger().severe("Could not save config file: " + e.getMessage());
        }
    }

    public int getInt(String path, int defaultValue) {
        if (this.config.contains(path)) {
            return this.config.getInt(path, defaultValue);
        }
        this.config.set(path, (Object)defaultValue);
        this.saveConfig();
        return defaultValue;
    }

    public double getDouble(String path, double defaultValue) {
        if (this.config.contains(path)) {
            return this.config.getDouble(path, defaultValue);
        }
        this.config.set(path, (Object)defaultValue);
        this.saveConfig();
        return defaultValue;
    }

    public String getString(String path, String defaultValue) {
        if (this.config.contains(path)) {
            return this.config.getString(path, defaultValue);
        }
        this.config.set(path, (Object)defaultValue);
        this.saveConfig();
        return defaultValue;
    }

    public List<String> getStringList(String path) {
        return this.config.contains(path) ? this.config.getStringList(path) : Collections.emptyList();
    }

    public void set(String path, Object value) {
        this.config.set(path, value);
        this.saveConfig();
    }
}

