package ru.craftorium.craftoriumcustom.items;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import ru.craftorium.craftoriumcustom.CraftoriumCustom;
import ru.craftorium.craftoriumcustom.manager.ConfigManager;
import ru.craftorium.craftoriumcustom.utils.HexUtil;

public class MenuItems {
    public ItemStack nickcolor() {
        ItemStack nickcolor = new ItemStack(Material.RED_DYE);
        ItemMeta itemMeta = nickcolor.getItemMeta();
        itemMeta.setDisplayName(HexUtil.translate("&#FF0000 \u274f \u0426\u0432\u0435\u0442 \u043d\u0438\u043a\u043d\u0435\u0439\u043c\u0430 \u274f"));
        ArrayList<String> lore = new ArrayList<String>();
        lore.add(HexUtil.translate(" &f"));
        lore.add(HexUtil.translate(" &#FF0000&n\u258d&f \u0412 \u0434\u0430\u043d\u043d\u043e\u043c \u0440\u0430\u0437\u0434\u0435\u043b\u0435 \u043c\u043e\u0436\u043d\u043e \u043d\u0430\u0441\u0442\u0440\u043e\u0438\u0442\u044c"));
        lore.add(HexUtil.translate(" &#FF0000\u258d&f \u0446\u0432\u0435\u0442 \u0432\u0430\u0448\u0435\u0433\u043e &6\u1d18\u0280\u1d07\u1d0d\u026a\u1d1c\u1d0d-\u0433\u0440\u0430\u0434\u0438\u0435\u043d\u0442\u0430&f."));
        lore.add(HexUtil.translate(" &f"));
        lore.add(HexUtil.translate(" &#FF0000\u25b6&f \u041d\u0430\u0436\u043c\u0438\u0442\u0435, \u0447\u0442\u043e\u0431\u044b \u043e\u0442\u043a\u0440\u044b\u0442\u044c \u0440\u0430\u0437\u0434\u0435\u043b."));
        itemMeta.setLore(lore);
        nickcolor.setItemMeta(itemMeta);
        return nickcolor;
    }

    public ItemStack sounds() {
        ItemStack sounds = new ItemStack(Material.MUSIC_DISC_STRAD);
        ItemMeta itemMeta = sounds.getItemMeta();
        itemMeta.setDisplayName(HexUtil.translate("&#FF7000 \u274f \u0417\u0432\u0443\u043a\u0438 \u274f"));
        ArrayList<String> lore = new ArrayList<String>();
        lore.add(HexUtil.translate(" &f"));
        lore.add(HexUtil.translate(" &#FF7000&n\u258d&f \u0412 \u0434\u0430\u043d\u043d\u043e\u043c \u0440\u0430\u0437\u0434\u0435\u043b\u0435 \u043c\u043e\u0436\u043d\u043e \u043d\u0430\u0441\u0442\u0440\u043e\u0438\u0442\u044c"));
        lore.add(HexUtil.translate(" &#FF7000&n\u258d&f \u043f\u0440\u043e\u0438\u0433\u0440\u044b\u0432\u0430\u043d\u0438\u0435 \u0437\u0432\u0443\u043a\u043e\u0432 \u043f\u0440\u0438 \u0441\u043e\u0432\u0435\u0440\u0448\u0435\u043d\u0438\u0438"));
        lore.add(HexUtil.translate(" &#FF7000\u258d&f \u0432\u0430\u043c\u0438 \u043e\u043f\u0440\u0435\u0434\u0435\u043b\u0451\u043d\u043d\u044b\u0445 \u0434\u0435\u0439\u0441\u0442\u0432\u0438\u0439"));
        lore.add(HexUtil.translate(" &f"));
        lore.add(HexUtil.translate(" &#FF7000\u25b6&f \u041d\u0430\u0436\u043c\u0438\u0442\u0435, \u0447\u0442\u043e\u0431\u044b \u043e\u0442\u043a\u0440\u044b\u0442\u044c \u0440\u0430\u0437\u0434\u0435\u043b."));
        itemMeta.setLore(lore);
        sounds.setItemMeta(itemMeta);
        return sounds;
    }

    public ItemStack particle() {
        ItemStack particle = new ItemStack(Material.MELON_SEEDS);
        ItemMeta itemMeta = particle.getItemMeta();
        itemMeta.setDisplayName(HexUtil.translate("&#00D8FF \u274f \u0427\u0430\u0441\u0442\u0438\u0446\u044b \u274f"));
        ArrayList<String> lore = new ArrayList<String>();
        lore.add(HexUtil.translate(" &f"));
        lore.add(HexUtil.translate(" &#00D8FF&n\u258d&f \u0412 \u0434\u0430\u043d\u043d\u043e\u043c \u0440\u0430\u0437\u0434\u0435\u043b\u0435 \u043c\u043e\u0436\u043d\u043e \u043d\u0430\u0441\u0442\u0440\u043e\u0438\u0442\u044c"));
        lore.add(HexUtil.translate(" &#00D8FF&n\u258d&f \u043e\u0442\u043e\u0431\u0440\u0430\u0436\u0435\u043d\u0438\u0435 \u0447\u0430\u0441\u0442\u0438\u0446 \u043f\u0440\u0438 \u0441\u043e\u0432\u0435\u0440\u0448\u0435\u043d\u0438\u0438"));
        lore.add(HexUtil.translate(" &#00D8FF\u258d&f \u0432\u0430\u043c\u0438 \u043e\u043f\u0440\u0435\u0434\u0435\u043b\u0451\u043d\u043d\u044b\u0445 \u0434\u0435\u0439\u0441\u0442\u0432\u0438\u0439"));
        lore.add(HexUtil.translate(" &f"));
        lore.add(HexUtil.translate(" &#00D8FF\u25b6&f \u041d\u0430\u0436\u043c\u0438\u0442\u0435, \u0447\u0442\u043e\u0431\u044b \u043e\u0442\u043a\u0440\u044b\u0442\u044c \u0440\u0430\u0437\u0434\u0435\u043b."));
        itemMeta.setLore(lore);
        particle.setItemMeta(itemMeta);
        return particle;
    }

    public ItemStack premka() {
        ItemStack premka = new ItemStack(Material.END_CRYSTAL);
        ItemMeta itemMeta = premka.getItemMeta();
        itemMeta.setDisplayName(HexUtil.translate("&#00D8FF \u274f \u041f\u0440\u0435\u043c\u0438\u0443\u043c \u274f"));
        ArrayList<String> lore = new ArrayList<String>();
        lore.add(HexUtil.translate(" &f"));
        lore.add(HexUtil.translate(" &#00D8FF&n\u258d&f \u0412 \u0434\u0430\u043d\u043d\u043e\u043c \u0440\u0430\u0437\u0434\u0435\u043b\u0435 \u043c\u043e\u0436\u043d\u043e \u043d\u0430\u0441\u0442\u0440\u043e\u0438\u0442\u044c"));
        lore.add(HexUtil.translate(" &#00D8FF&n\u258d&f \u043e\u0442\u043e\u0431\u0440\u0430\u0436\u0435\u043d\u0438\u0435 \u043f\u0440\u0438 \u0441\u043e\u0432\u0435\u0440\u0448\u0435\u043d\u0438\u0438"));
        lore.add(HexUtil.translate(" &#00D8FF\u258d&f \u0432\u0430\u043c\u0438 \u043e\u043f\u0440\u0435\u0434\u0435\u043b\u0451\u043d\u043d\u044b\u0445 \u0434\u0435\u0439\u0441\u0442\u0432\u0438\u0439"));
        lore.add(HexUtil.translate(" &f"));
        lore.add(HexUtil.translate(" &#00D8FF\u25b6&f \u041d\u0430\u0436\u043c\u0438\u0442\u0435, \u0447\u0442\u043e\u0431\u044b \u043e\u0442\u043a\u0440\u044b\u0442\u044c \u0440\u0430\u0437\u0434\u0435\u043b."));
        itemMeta.setLore(lore);
        premka.setItemMeta(itemMeta);
        return premka;
    }

    public ItemStack blue() {
        ItemStack blue = new ItemStack(Material.BLUE_STAINED_GLASS_PANE);
        ItemMeta itemMeta = blue.getItemMeta();
        itemMeta.setDisplayName(" ");
        blue.setItemMeta(itemMeta);
        return blue;
    }

    public ItemStack orange() {
        ItemStack orange = new ItemStack(Material.ORANGE_STAINED_GLASS_PANE);
        ItemMeta itemMeta = orange.getItemMeta();
        itemMeta.setDisplayName(" ");
        orange.setItemMeta(itemMeta);
        return orange;
    }

    public ItemStack gray() {
        ItemStack gray = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta itemMeta = gray.getItemMeta();
        itemMeta.setDisplayName(" ");
        gray.setItemMeta(itemMeta);
        return gray;
    }

    public ItemStack green(double volume, Player player) {
        ItemStack gray = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
        ItemMeta itemMeta = gray.getItemMeta();
        itemMeta.setDisplayName(" ");
        ArrayList<String> lore = new ArrayList<String>();
        lore.add(HexUtil.translate(" &#00D8FF&n\u258d&f"));
        lore.add(HexUtil.translate(" &#00D8FF&n\u258d&f \u0412\u044b\u0441\u043e\u0442\u0430 \u0437\u0432\u0443\u043a\u0430: " + String.format("%.1f", volume)));
        lore.add(HexUtil.translate(" &#00D8FF\u258d&f"));
        lore.add(HexUtil.translate(" &f"));
        lore.add(HexUtil.translate(" &#00D8FF&l\u25b6&f \u041d\u0430\u0436\u043c\u0438\u0442\u0435, \u0447\u0442\u043e\u0431\u044b \u0438\u0437\u043c\u0435\u043d\u0438\u0442\u044c \u0432\u044b\u0441\u043e\u0442\u0443 \u0437\u0432\u0443\u043a\u0430."));
        itemMeta.setLore(lore);
        String pitchValue = CraftoriumCustom.getPlayerData(player, "sound_pitch");
        if (!pitchValue.equalsIgnoreCase("null")) {
            try {
                double playerVolume = Double.parseDouble(pitchValue);
                if (Math.abs(playerVolume - volume) < 1.0E-4) {
                    itemMeta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ENCHANTS});
                    itemMeta.addEnchant(Enchantment.LUCK, 1, true);
                }
            }
            catch (NumberFormatException numberFormatException) {
                // empty catch block
            }
        }
        itemMeta.getPersistentDataContainer().set(new NamespacedKey((Plugin)CraftoriumCustom.getInstance(), "double"), PersistentDataType.DOUBLE, volume);
        gray.setItemMeta(itemMeta);
        return gray;
    }

    public ItemStack saveSound() {
        ItemStack gray = new ItemStack(Material.GREEN_CONCRETE);
        ItemMeta itemMeta = gray.getItemMeta();
        itemMeta.setDisplayName(HexUtil.translate("&#05FB00\u25b6 \u0421\u043e\u0445\u0440\u0430\u043d\u0438\u0442\u044c"));
        gray.setItemMeta(itemMeta);
        return gray;
    }

    public ItemStack particleItem(Player player, String category, String particleName, String displayName, Material material) {
        ConfigManager configManager = new ConfigManager(CraftoriumCustom.getInstance());
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(HexUtil.translate(configManager.getString("items.particle.name", " &6{name}").replace("{name}", displayName)));
        ArrayList<String> translatedLore = new ArrayList<String>();
        String currentParticle = CraftoriumCustom.getPlayerData(player, category);
        if (currentParticle.equalsIgnoreCase(particleName)) {
            for (String line : configManager.getStringList("items.particle.lore_active")) {
                translatedLore.add(HexUtil.translate(line));
            }
            meta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ENCHANTS});
            meta.addEnchant(Enchantment.LUCK, 1, true);
        } else {
            for (String line : configManager.getStringList("items.particle.lore")) {
                translatedLore.add(HexUtil.translate(line));
            }
        }
        meta.setLore(translatedLore);
        item.setItemMeta(meta);
        return item;
    }

    public ItemStack soundItem(Player player, String category, String soundName, String displayName, Material material) {
        return this.soundItem(player, category, soundName, displayName, material, false);
    }

    public ItemStack soundItem(Player player, String category, String soundName, String displayName, Material material, boolean premium) {
        ConfigManager configManager = new ConfigManager(CraftoriumCustom.getInstance());
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        Object name = configManager.getString("items.sound.name", " &x&f&f&7&0&0&0{name}").replace("{name}", displayName);
        if (premium) {
            name = (String)name + " &6&l(\u1d18\u0280\u1d07\u1d0d\u026a\u1d1c\u1d0d)";
        }
        meta.setDisplayName(HexUtil.translate((String)name));
        ArrayList<String> translatedLore = new ArrayList<String>();
        if (CraftoriumCustom.getPlayerData(player, category).equalsIgnoreCase(soundName)) {
            for (String line : configManager.getStringList("items.sound.lore")) {
                translatedLore.add(HexUtil.translate(line));
            }
            meta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ENCHANTS});
            meta.addEnchant(Enchantment.LUCK, 1, true);
        } else {
            for (String line : configManager.getStringList("items.sound.lore2")) {
                translatedLore.add(HexUtil.translate(line));
            }
        }
        if (premium) {
            meta.getPersistentDataContainer().set(new NamespacedKey((Plugin)CraftoriumCustom.getInstance(), "premium"), PersistentDataType.STRING, "true");
        }
        meta.setLore(translatedLore);
        item.setItemMeta(meta);
        return item;
    }

    public ItemStack banner_crit() {
        ItemStack banner = new ItemStack(Material.CREEPER_BANNER_PATTERN);
        ItemMeta itemMeta = banner.getItemMeta();
        itemMeta.setDisplayName(HexUtil.translate("&#00D8FF \u274f \u041a\u0440\u0438\u0442\u0438\u0447\u0435\u0441\u043a\u0438\u0439 \u0443\u0434\u0430\u0440 \u274f"));
        ArrayList<String> lore = new ArrayList<String>();
        lore.add(HexUtil.translate(" &f"));
        lore.add(HexUtil.translate(" &#00D8FF&n\u258d&f \u0414\u0435\u0439\u0441\u0442\u0432\u0438\u0435 \u043f\u0440\u0438 \u0441\u0440\u043e\u0431\u0430\u0442\u044b\u0432\u0430\u043d\u0438\u0438 \u0441\u043e\u0431\u044b\u0442\u0438\u044f:"));
        lore.add(HexUtil.translate(" &#00D8FF\u258d&f - \u043d\u0430\u043d\u0435\u0441\u0435\u043d\u0438\u0435 \u043a\u0440\u0438\u0442\u0438\u0447\u0435\u0441\u043a\u043e\u0433\u043e \u0443\u0434\u0430\u0440\u0430"));
        lore.add(HexUtil.translate(" &f"));
        lore.add(HexUtil.translate(" &#00D8FF\u25b6&f \u041d\u0430\u0436\u043c\u0438\u0442\u0435, \u0447\u0442\u043e\u0431\u044b \u043d\u0430\u0441\u0442\u0440\u043e\u0438\u0442\u044c \u0434\u0435\u0439\u0441\u0442\u0432\u0438\u0435."));
        itemMeta.setLore(lore);
        banner.setItemMeta(itemMeta);
        return banner;
    }

    public ItemStack banner_kill() {
        ItemStack banner = new ItemStack(Material.CREEPER_BANNER_PATTERN);
        ItemMeta itemMeta = banner.getItemMeta();
        itemMeta.setDisplayName(HexUtil.translate("&#00D8FF \u274f \u0423\u0431\u0438\u0439\u0441\u0442\u0432\u043e \u0438\u0433\u0440\u043e\u043a\u0430 \u274f"));
        ArrayList<String> lore = new ArrayList<String>();
        lore.add(HexUtil.translate(" &f"));
        lore.add(HexUtil.translate(" &#00D8FF&n\u258d&f \u0414\u0435\u0439\u0441\u0442\u0432\u0438\u0435 \u043f\u0440\u0438 \u0441\u0440\u043e\u0431\u0430\u0442\u044b\u0432\u0430\u043d\u0438\u0438 \u0441\u043e\u0431\u044b\u0442\u0438\u044f:"));
        lore.add(HexUtil.translate(" &#00D8FF\u258d&f - \u0443\u0431\u0438\u0439\u0441\u0442\u0432\u043e \u0434\u0440\u0443\u0433\u043e\u0433\u043e \u0438\u0433\u0440\u043e\u043a\u0430"));
        lore.add(HexUtil.translate(" &f"));
        lore.add(HexUtil.translate(" &#00D8FF\u25b6&f \u041d\u0430\u0436\u043c\u0438\u0442\u0435, \u0447\u0442\u043e\u0431\u044b \u043d\u0430\u0441\u0442\u0440\u043e\u0438\u0442\u044c \u0434\u0435\u0439\u0441\u0442\u0432\u0438\u044f."));
        itemMeta.setLore(lore);
        banner.setItemMeta(itemMeta);
        return banner;
    }

    public ItemStack banner_lowheal() {
        ItemStack banner = new ItemStack(Material.CREEPER_BANNER_PATTERN);
        ItemMeta itemMeta = banner.getItemMeta();
        itemMeta.setDisplayName(HexUtil.translate("&#00D8FF \u274f \u041e\u0441\u0442\u0430\u0451\u0442\u0441\u044f \u043c\u0430\u043b\u043e \u0437\u0434\u043e\u0440\u043e\u0432\u044c\u044f \u274f"));
        ArrayList<String> lore = new ArrayList<String>();
        lore.add(HexUtil.translate(" &f"));
        lore.add(HexUtil.translate(" &#00D8FF&n\u258d&f \u0414\u0435\u0439\u0441\u0442\u0432\u0438\u0435 \u043f\u0440\u0438 \u0441\u0440\u043e\u0431\u0430\u0442\u044b\u0432\u0430\u043d\u0438\u0438 \u0441\u043e\u0431\u044b\u0442\u0438\u044f:"));
        lore.add(HexUtil.translate(" &#00D8FF\u258d&f - \u043d\u0438\u0437\u043a\u0438\u0439 \u0443\u0440\u043e\u0432\u0435\u043d\u044c (\u0440\u0430\u0437\u0432\u0438\u0442\u0438\u044f) \u0437\u0434\u043e\u0440\u043e\u0432\u044c\u044f ( < 2.5 )"));
        lore.add(HexUtil.translate(" &f"));
        lore.add(HexUtil.translate(" &#00D8FF\u25b6&f \u041d\u0430\u0436\u043c\u0438\u0442\u0435, \u0447\u0442\u043e\u0431\u044b \u043d\u0430\u0441\u0442\u0440\u043e\u0438\u0442\u044c \u0434\u0435\u0439\u0441\u0442\u0432\u0438\u044f."));
        itemMeta.setLore(lore);
        banner.setItemMeta(itemMeta);
        return banner;
    }

    public ItemStack banner_death_me() {
        ItemStack banner = new ItemStack(Material.CREEPER_BANNER_PATTERN);
        ItemMeta itemMeta = banner.getItemMeta();
        itemMeta.setDisplayName(HexUtil.translate("&#00D8FF \u274f \u041f\u0440\u0438 \u0432\u0430\u0448\u0435\u0439 \u0441\u043c\u0435\u0440\u0442\u0438 \u274f"));
        ArrayList<String> lore = new ArrayList<String>();
        lore.add(HexUtil.translate(" &f"));
        lore.add(HexUtil.translate(" &#00D8FF&n\u258d&f \u0414\u0435\u0439\u0441\u0442\u0432\u0438\u0435 \u043f\u0440\u0438 \u0441\u0440\u043e\u0431\u0430\u0442\u044b\u0432\u0430\u043d\u0438\u0438 \u0441\u043e\u0431\u044b\u0442\u0438\u044f:"));
        lore.add(HexUtil.translate(" &#00D8FF\u258d&f - \u0441\u043c\u0435\u0440\u0442\u044c \u0438\u0433\u0440\u043e\u043a\u0430"));
        lore.add(HexUtil.translate(" &f"));
        lore.add(HexUtil.translate(" &#00D8FF\u25b6&f \u041d\u0430\u0436\u043c\u0438\u0442\u0435, \u0447\u0442\u043e\u0431\u044b \u043d\u0430\u0441\u0442\u0440\u043e\u0438\u0442\u044c \u0434\u0435\u0439\u0441\u0442\u0432\u0438\u044f."));
        itemMeta.setLore(lore);
        banner.setItemMeta(itemMeta);
        return banner;
    }

    public ItemStack banner() {
        ItemStack banner = new ItemStack(Material.CREEPER_BANNER_PATTERN);
        ItemMeta itemMeta = banner.getItemMeta();
        itemMeta.setDisplayName(HexUtil.translate("&#00D8FF \u274f \u0422\u0435\u043b\u0435\u043f\u043e\u0440\u0442\u0430\u0446\u0438\u044f \u0436\u0435\u043c\u0447\u0443\u0433\u043e\u043c \u043a\u0440\u0430\u044f \u274f"));
        ArrayList<String> lore = new ArrayList<String>();
        lore.add(HexUtil.translate(" &f"));
        lore.add(HexUtil.translate(" &#00D8FF&n\u258d&f \u0414\u0435\u0439\u0441\u0442\u0432\u0438\u0435 \u043f\u0440\u0438 \u0441\u0440\u043e\u0431\u0430\u0442\u044b\u0432\u0430\u043d\u0438\u0438 \u0441\u043e\u0431\u044b\u0442\u0438\u044f:"));
        lore.add(HexUtil.translate(" &#00D8FF\u258d&f - \u0442\u0435\u043b\u0435\u043f\u043e\u0440\u0442\u0430\u0446\u0438\u044f \u0436\u0435\u043c\u0447\u0443\u0433\u043e\u043c \u043a\u0440\u0430\u044f"));
        lore.add(HexUtil.translate(" &f"));
        lore.add(HexUtil.translate(" &#00D8FF\u25b6 &f\u041d\u0430\u0436\u043c\u0438\u0442\u0435, \u0447\u0442\u043e\u0431\u044b \u043d\u0430\u0441\u0442\u0440\u043e\u0438\u0442\u044c \u0434\u0435\u0439\u0441\u0442\u0432\u0438\u044f."));
        itemMeta.setLore(lore);
        banner.setItemMeta(itemMeta);
        return banner;
    }

    public ItemStack banner1() {
        ItemStack banner = new ItemStack(Material.CREEPER_BANNER_PATTERN);
        ItemMeta itemMeta = banner.getItemMeta();
        itemMeta.setDisplayName(HexUtil.translate("&#00D8FF \u274f \u0423\u0431\u0438\u0439\u0441\u0442\u0432\u043e \u0438\u0433\u0440\u043e\u043a\u0430 \u274f"));
        ArrayList<String> lore = new ArrayList<String>();
        lore.add(HexUtil.translate(" &f"));
        lore.add(HexUtil.translate(" &#00D8FF&n\u258d&f \u0414\u0435\u0439\u0441\u0442\u0432\u0438\u0435 \u043f\u0440\u0438 \u0441\u0440\u043e\u0431\u0430\u0442\u044b\u0432\u0430\u043d\u0438\u0438 \u0441\u043e\u0431\u044b\u0442\u0438\u044f:"));
        lore.add(HexUtil.translate(" &#00D8FF\u258d&f - \u0443\u0431\u0438\u0439\u0441\u0442\u0432\u043e \u0434\u0440\u0443\u0433\u043e\u0433\u043e \u0438\u0433\u0440\u043e\u043a\u0430"));
        lore.add(HexUtil.translate(" &f"));
        lore.add(HexUtil.translate(" &#00D8FF\u25b6 &f\u041d\u0430\u0436\u043c\u0438\u0442\u0435, \u0447\u0442\u043e\u0431\u044b \u043d\u0430\u0441\u0442\u0440\u043e\u0438\u0442\u044c \u0434\u0435\u0439\u0441\u0442\u0432\u0438\u044f."));
        itemMeta.setLore(lore);
        banner.setItemMeta(itemMeta);
        return banner;
    }

    public ItemStack banner2() {
        ItemStack banner = new ItemStack(Material.CREEPER_BANNER_PATTERN);
        ItemMeta itemMeta = banner.getItemMeta();
        itemMeta.setDisplayName(HexUtil.translate("&#00D8FF \u274f \u041f\u0440\u0438 \u0432\u0430\u0448\u0435\u0439 \u0441\u043c\u0435\u0440\u0442\u0438 \u274f"));
        ArrayList<String> lore = new ArrayList<String>();
        lore.add(HexUtil.translate(" &f"));
        lore.add(HexUtil.translate(" &#00D8FF&n\u258d&f \u0414\u0435\u0439\u0441\u0442\u0432\u0438\u0435 \u043f\u0440\u0438 \u0441\u0440\u043e\u0431\u0430\u0442\u044b\u0432\u0430\u043d\u0438\u0438 \u0441\u043e\u0431\u044b\u0442\u0438\u044f:"));
        lore.add(HexUtil.translate(" &#00D8FF\u258d&f - \u0441\u043c\u0435\u0440\u0442\u044c \u0438\u0433\u0440\u043e\u043a\u0430"));
        lore.add(HexUtil.translate(" &f"));
        lore.add(HexUtil.translate(" &#00D8FF\u25b6 &f\u041d\u0430\u0436\u043c\u0438\u0442\u0435, \u0447\u0442\u043e\u0431\u044b \u043d\u0430\u0441\u0442\u0440\u043e\u0438\u0442\u044c \u0434\u0435\u0439\u0441\u0442\u0432\u0438\u044f."));
        itemMeta.setLore(lore);
        banner.setItemMeta(itemMeta);
        return banner;
    }

    public ItemStack star() {
        ItemStack star = new ItemStack(Material.NETHER_STAR);
        ItemMeta itemMeta = star.getItemMeta();
        itemMeta.setDisplayName(HexUtil.translate("&#FFC900&l\u2605 &#FFAA00&l\u0417\u0432\u0435\u0437\u0434\u0430 &f\u0441\u0442\u0430\u0442\u0443\u0441\u0430 &#FFC900&l\u2605"));
        ArrayList<String> lore = new ArrayList<String>();
        lore.add(HexUtil.translate(" &f"));
        lore.add(HexUtil.translate(" &#FF5555\u258d &#FF7700\u258d &#FFAA00\u258d &#FFC900\u258d &#FFFF55\u258d &#FFFFFF\u258d"));
        lore.add(HexUtil.translate(" &f"));
        lore.add(HexUtil.translate(" &#FFC900&n\u258d&f \u0412 \u0434\u0430\u043d\u043d\u043e\u043c \u0440\u0430\u0437\u0434\u0435\u043b\u0435 \u043c\u043e\u0436\u043d\u043e \u043d\u0430\u0441\u0442\u0440\u043e\u0438\u0442\u044c"));
        lore.add(HexUtil.translate(" &#FFC900\u258d&f \u0432\u043e\u0437\u043c\u043e\u0436\u043d\u043e\u0441\u0442\u0438 \u0437\u0432\u0435\u0437\u0434\u044b \u0441\u0442\u0430\u0442\u0443\u0441\u0430"));
        lore.add(HexUtil.translate(" &f"));
        lore.add(HexUtil.translate(" &#FFC900\u25b6 &f\u041d\u0430\u0436\u043c\u0438\u0442\u0435, \u0447\u0442\u043e\u0431\u044b \u043e\u0442\u043a\u0440\u044b\u0442\u044c \u0440\u0430\u0437\u0434\u0435\u043b."));
        itemMeta.setLore(lore);
        itemMeta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ENCHANTS});
        itemMeta.addEnchant(Enchantment.LUCK, 1, true);
        star.setItemMeta(itemMeta);
        return star;
    }

    public ItemStack getNameItem(Material material, String color, String color2, int level, String playerName) {
        ItemStack shulker = new ItemStack(material);
        ItemMeta itemMeta = shulker.getItemMeta();
        itemMeta.setDisplayName(HexUtil.translate(color + MenuItems.convertInt(level) + " \u0417\u0432\u0435\u0437\u0434\u0430 \u0441\u0442\u0430\u0442\u0443\u0441\u0430 (" + level + " \u0443\u0440.)"));
        ArrayList<String> lore = new ArrayList<String>();
        lore.add(HexUtil.translate(""));
        lore.add(HexUtil.translate(color + " &n\u258d" + color + " \u041f\u0440\u0435\u0434\u043f\u0440\u043e\u0441\u043c\u043e\u0442\u0440:"));
        lore.add(HexUtil.translate(color + " \u258d&r " + this.colorizeNickname(playerName, color2, "#FFFFFF", true)));
        lore.add(HexUtil.translate(""));
        lore.add(HexUtil.translate(color + " &l\u25b6 &f\u041d\u0430\u0436\u043c\u0438\u0442\u0435, \u0447\u0442\u043e\u0431\u044b \u0432\u044b\u0431\u0440\u0430\u0442\u044c \u0446\u0432\u0435\u0442."));
        lore.add(HexUtil.translate(color + "   &7\u0422\u0440\u0435\u0431\u0443\u0435\u0442\u0441\u044f " + MenuItems.convertInt(level) + " \u0437\u0432\u0435\u0437\u0434\u0430 (" + level + " \u0443\u0440.)"));
        itemMeta.setLore(lore);
        itemMeta.getPersistentDataContainer().set(new NamespacedKey((Plugin)CraftoriumCustom.getInstance(), "color"), PersistentDataType.STRING, color2);
        itemMeta.getPersistentDataContainer().set(new NamespacedKey((Plugin)CraftoriumCustom.getInstance(), "required_star"), PersistentDataType.INTEGER, level);
        shulker.setItemMeta(itemMeta);
        return shulker;
    }

    public static String convertInt(int i) {
        if (i >= 1 && i <= 3999) {
            int[] arabicValues = new int[]{1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
            String[] romanSymbols = new String[]{"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};
            StringBuilder result = new StringBuilder();
            for (int j = 0; j < arabicValues.length; ++j) {
                while (i >= arabicValues[j]) {
                    result.append(romanSymbols[j]);
                    i -= arabicValues[j];
                }
            }
            return result.toString();
        }
        throw new IllegalArgumentException("\u0427\u0438\u0441\u043b\u043e \u0434\u043e\u043b\u0436\u043d\u043e \u0431\u044b\u0442\u044c \u0432 \u0434\u0438\u0430\u043f\u0430\u0437\u043e\u043d\u0435 \u043e\u0442 1 \u0434\u043e 3999");
    }

    private String colorizeNickname(String playerName, String color1, String color2, Boolean bold) {
        int length = playerName.length();
        if (length == 0) {
            return "";
        }
        net.md_5.bungee.api.ChatColor startColor = net.md_5.bungee.api.ChatColor.of((String)color1);
        net.md_5.bungee.api.ChatColor endColor = net.md_5.bungee.api.ChatColor.of((String)color2);
        TextComponent coloredNickname = new TextComponent();
        for (int i = 0; i < length; ++i) {
            double ratio = length > 1 ? (double)i / (double)(length - 1) : 0.0;
            int red = (int)((double)startColor.getColor().getRed() * (1.0 - ratio) + (double)endColor.getColor().getRed() * ratio);
            int green = (int)((double)startColor.getColor().getGreen() * (1.0 - ratio) + (double)endColor.getColor().getGreen() * ratio);
            int blue = (int)((double)startColor.getColor().getBlue() * (1.0 - ratio) + (double)endColor.getColor().getBlue() * ratio);
            net.md_5.bungee.api.ChatColor color = net.md_5.bungee.api.ChatColor.of((Color)new Color(red, green, blue));
            TextComponent letter = new TextComponent(String.valueOf(playerName.charAt(i)));
            letter.setColor(color);
            if (bold.booleanValue()) {
                letter.setBold(Boolean.valueOf(true));
            }
            coloredNickname.addExtra((BaseComponent)letter);
        }
        return TextComponent.toLegacyText((BaseComponent[])new BaseComponent[]{coloredNickname});
    }

    public ItemStack back() {
        ItemStack dragon = new ItemStack(Material.ARROW);
        ItemMeta itemMeta = dragon.getItemMeta();
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes((char)'&', (String)"&x&F&F&7&0&0&0\u25c0 \u041d\u0430\u0437\u0430\u0434"));
        dragon.setItemMeta(itemMeta);
        return dragon;
    }

    public ItemStack traps() {
        ItemStack dye = new ItemStack(Material.POPPED_CHORUS_FRUIT);
        ItemMeta itemMeta = dye.getItemMeta();
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes((char)'&', (String)"&x&f&b&0&f&d&4 \u274f \u0421\u043a\u0438\u043d\u044b \u043d\u0430 \u043f\u0438\u0440\u043e\u0442\u0435\u0445\u043d\u0438\u043a\u0443 \u274f"));
        ArrayList<String> lore = new ArrayList<String>();
        lore.add(ChatColor.translateAlternateColorCodes((char)'&', (String)""));
        lore.add(ChatColor.translateAlternateColorCodes((char)'&', (String)"&x&f&b&0&f&d&4 &n\u258d&f \u0412 \u0434\u0430\u043d\u043d\u043e\u043c \u0440\u0430\u0437\u0434\u0435\u043b\u0435 \u043c\u043e\u0436\u043d\u043e \u043d\u0430\u0441\u0442\u0440\u043e\u0438\u0442\u044c \u0441\u043a\u0438\u043d\u044b"));
        lore.add(ChatColor.translateAlternateColorCodes((char)'&', (String)"&x&f&b&0&f&d&4 \u258d&f \u0434\u043b\u044f \u0440\u0430\u0437\u043b\u0438\u0447\u043d\u043e\u0439 \u043f\u0438\u0440\u043e\u0442\u0435\u0445\u043d\u0438\u043a\u0438 (\u0442\u0440\u0430\u043f\u043a\u0438, \u0441\u0442\u0430\u043d\u044b)."));
        lore.add(ChatColor.translateAlternateColorCodes((char)'&', (String)""));
        lore.add(ChatColor.translateAlternateColorCodes((char)'&', (String)"&x&f&b&0&f&d&4 &l\u25b6 &f\u041d\u0430\u0436\u043c\u0438\u0442\u0435, \u0447\u0442\u043e\u0431\u044b \u043e\u0442\u043a\u0440\u044b\u0442\u044c \u0440\u0430\u0437\u0434\u0435\u043b."));
        itemMeta.setLore(lore);
        dye.setItemMeta(itemMeta);
        return dye;
    }

    public ItemStack getTrapSkin(Player player, Material material, String skinName, String color, Boolean premium, String tag) {
        String trapSkin;
        ItemStack dye = new ItemStack(material);
        ItemMeta itemMeta = dye.getItemMeta();
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes((char)'&', (String)(color + "\u274f \u0421\u043a\u0438\u043d: " + skinName + " \u274f")));
        ArrayList<String> lore = new ArrayList<String>();
        lore.add(ChatColor.translateAlternateColorCodes((char)'&', (String)""));
        lore.add(ChatColor.translateAlternateColorCodes((char)'&', (String)(color + " &n\u258d&f " + color + "\u0426\u0435\u043b\u044c:")));
        lore.add(ChatColor.translateAlternateColorCodes((char)'&', (String)(color + " \u258d&f &f\u0422\u0440\u0430\u043f\u043a\u0438")));
        lore.add(ChatColor.translateAlternateColorCodes((char)'&', (String)""));
        lore.add(ChatColor.translateAlternateColorCodes((char)'&', (String)(color + " &l\u25b6 &f\u041d\u0430\u0436\u043c\u0438\u0442\u0435, \u0447\u0442\u043e\u0431\u044b \u0432\u044b\u0431\u0440\u0430\u0442\u044c \u0441\u043a\u0438\u043d." + (premium != false ? " &6&l(\u1d18\u0280\u1d07\u1d0d\u026a\u1d1c\u1d0d)" : ""))));
        itemMeta.setLore(lore);
        itemMeta.getPersistentDataContainer().set(new NamespacedKey((Plugin)CraftoriumCustom.getInstance(), "trap-type"), PersistentDataType.STRING, tag);
        itemMeta.getPersistentDataContainer().set(new NamespacedKey((Plugin)CraftoriumCustom.getInstance(), "premium"), PersistentDataType.STRING, premium.toString());
        HashMap<String, String> pData = CraftoriumCustom.getPlayerData(player);
        String string = trapSkin = pData != null ? pData.getOrDefault("trap_skin", "none") : "none";
        if (trapSkin.equalsIgnoreCase(tag)) {
            itemMeta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ENCHANTS});
            itemMeta.addEnchant(Enchantment.LUCK, 1, false);
        }
        dye.setItemMeta(itemMeta);
        return dye;
    }

    public ItemStack getStanSkin(Player player, Material material, String skinName, String color, Boolean premium, String tag) {
        String stanSkin;
        ItemStack dye = new ItemStack(material);
        ItemMeta itemMeta = dye.getItemMeta();
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes((char)'&', (String)(color + "\u274f " + skinName + " \u0446\u0432\u0435\u0442 \u274f")));
        ArrayList<String> lore = new ArrayList<String>();
        lore.add(ChatColor.translateAlternateColorCodes((char)'&', (String)""));
        lore.add(ChatColor.translateAlternateColorCodes((char)'&', (String)(color + " &n\u258d&f " + color + "\u0426\u0435\u043b\u044c:")));
        lore.add(ChatColor.translateAlternateColorCodes((char)'&', (String)(color + " \u258d&f &f\u0421\u0442\u0430\u043d\u044b")));
        lore.add(ChatColor.translateAlternateColorCodes((char)'&', (String)""));
        lore.add(ChatColor.translateAlternateColorCodes((char)'&', (String)(color + " &l\u25b6 &f\u041d\u0430\u0436\u043c\u0438\u0442\u0435, \u0447\u0442\u043e\u0431\u044b \u0432\u044b\u0431\u0440\u0430\u0442\u044c \u0441\u043a\u0438\u043d." + (premium != false ? " &6&l(\u1d18\u0280\u1d07\u1d0d\u026a\u1d1c\u1d0d)" : ""))));
        itemMeta.setLore(lore);
        itemMeta.getPersistentDataContainer().set(new NamespacedKey((Plugin)CraftoriumCustom.getInstance(), "stan-type"), PersistentDataType.STRING, tag);
        itemMeta.getPersistentDataContainer().set(new NamespacedKey((Plugin)CraftoriumCustom.getInstance(), "premium"), PersistentDataType.STRING, premium.toString());
        HashMap<String, String> pData = CraftoriumCustom.getPlayerData(player);
        String string = stanSkin = pData != null ? pData.getOrDefault("stan_skin", "none") : "none";
        if (stanSkin.equalsIgnoreCase(tag)) {
            itemMeta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ENCHANTS});
            itemMeta.addEnchant(Enchantment.LUCK, 1, false);
        }
        dye.setItemMeta(itemMeta);
        return dye;
    }

    public ItemStack getStarHelp() {
        ItemStack itemStack = new ItemStack(Material.BOOK);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes((char)'&', (String)" &x&0&0&D&8&F&F\u041f\u043e\u043c\u043e\u0449\u044c \u043f\u043e \u0441\u0438\u0441\u0442\u0435\u043c\u0435 \u0437\u0432\u0451\u0437\u0434 \u0441\u0442\u0430\u0442\u0443\u0441\u0430"));
        ArrayList<String> lore = new ArrayList<String>();
        lore.add(ChatColor.translateAlternateColorCodes((char)'&', (String)""));
        lore.add(ChatColor.translateAlternateColorCodes((char)'&', (String)" &x&0&0&D&8&F&F&l&n\u258d&x&0&0&D&8&F&F \u0427\u0442\u043e \u0442\u0430\u043a\u043e\u0435 \u0417\u0432\u0451\u0437\u0434\u044b \u0421\u0442\u0430\u0442\u0443\u0441\u0430?"));
        lore.add(ChatColor.translateAlternateColorCodes((char)'&', (String)" &x&0&0&D&8&F&F&l&n\u258d&f "));
        lore.add(ChatColor.translateAlternateColorCodes((char)'&', (String)" &x&0&0&D&8&F&F&l&n\u258d&f \u0417\u0432\u0451\u0437\u0434\u044b \u0441\u0442\u0430\u0442\u0443\u0441\u0430 - \u043f\u0440\u0435\u0444\u0438\u043a\u0441 \u043f\u0435\u0440\u0435\u0434 \u043f\u0440\u0438\u0432\u0438\u043b\u0435\u0433\u0438\u0435\u0439,"));
        lore.add(ChatColor.translateAlternateColorCodes((char)'&', (String)" &x&0&0&D&8&F&F&l&n\u258d&f \u043a\u043e\u0442\u043e\u0440\u044b\u0439 \u043e\u043f\u0440\u0435\u0434\u0435\u043b\u044f\u0435\u0442 \u0441\u0442\u0430\u0442\u0443\u0441\u043d\u043e\u0441\u0442\u044c \u0438\u0433\u0440\u043e\u043a\u0430 \u0432 \u0422\u0430\u0431\u0435."));
        lore.add(ChatColor.translateAlternateColorCodes((char)'&', (String)" &x&0&0&D&8&F&F&l&n\u258d&f "));
        lore.add(ChatColor.translateAlternateColorCodes((char)'&', (String)" &x&0&0&D&8&F&F&l&n\u258d&x&0&0&D&8&F&F \u041a\u0430\u043a \u043f\u043e\u043b\u0443\u0447\u0438\u0442\u044c \u0437\u0432\u0435\u0437\u0434\u0443 \u0441\u0442\u0430\u0442\u0443\u0441\u0430?"));
        lore.add(ChatColor.translateAlternateColorCodes((char)'&', (String)" &x&0&0&D&8&F&F&l&n\u258d&f "));
        lore.add(ChatColor.translateAlternateColorCodes((char)'&', (String)" &x&0&0&D&8&F&F&l&n\u258d&f \u0417\u0432\u0451\u0437\u0434\u044b \u0441\u0442\u0430\u0442\u0443\u0441\u0430 \u0432\u044b\u0434\u0430\u044e\u0442\u0441\u044f \u0430\u0432\u0442\u043e\u043c\u0430\u0442\u0438\u0447\u0435\u0441\u043a\u0438,"));
        lore.add(ChatColor.translateAlternateColorCodes((char)'&', (String)" &x&0&0&D&8&F&F&l&n\u258d&f \u0432 \u0437\u0430\u0432\u0438\u0441\u0438\u043c\u043e\u0441\u0442\u0438 \u043e\u0442 \u0442\u043e\u0433\u043e, \u0441\u043a\u043e\u043b\u044c\u043a\u043e \u0432\u044b \u043f\u043e\u0436\u0435\u0440\u0442\u0432\u043e\u0432\u0430\u043b\u0438"));
        lore.add(ChatColor.translateAlternateColorCodes((char)'&', (String)" &x&0&0&D&8&F&F&l&n\u258d&f \u0441\u0440\u0435\u0434\u0441\u0442\u0432 \u0437\u0430 \u0432\u0441\u0435 \u0432\u0440\u0435\u043c\u044f \u0441\u0443\u0449\u0435\u0441\u0442\u0432\u043e\u0432\u0430\u043d\u0438\u044f \u041b\u0430\u0439\u0442 \u0430\u043d\u0430\u0440\u0445\u0438\u0438."));
        lore.add(ChatColor.translateAlternateColorCodes((char)'&', (String)" &x&0&0&D&8&F&F&l&n\u258d&f "));
        lore.add(ChatColor.translateAlternateColorCodes((char)'&', (String)" &x&0&0&D&8&F&F&l&n\u258d&x&0&0&D&8&F&F \u041a\u0430\u043a\u0438\u0435 \u043f\u0440\u0435\u0438\u043c\u0443\u0449\u0435\u0441\u0442\u0432\u0430 \u0443 \u0437\u0432\u0451\u0437\u0434 \u0441\u0442\u0430\u0442\u0443\u0441\u0430?"));
        lore.add(ChatColor.translateAlternateColorCodes((char)'&', (String)" &x&0&0&D&8&F&F&l&n\u258d&f "));
        lore.add(ChatColor.translateAlternateColorCodes((char)'&', (String)" &x&0&0&D&8&F&F&l&n\u258d&f \u0417\u0432\u0435\u0437\u0434\u044b \u043f\u0440\u0438\u0434\u0430\u044e\u0442 \u0441\u0442\u0430\u0442\u0443\u0441\u043d\u043e\u0441\u0442\u0438 \u0432\u0430\u0448\u0435\u043c\u0443 \u043d\u0438\u043a\u043d\u0435\u0439\u043c\u0443,"));
        lore.add(ChatColor.translateAlternateColorCodes((char)'&', (String)" &x&0&0&D&8&F&F&l&n\u258d&f \u0430 \u0442\u0430\u043a\u0436\u0435 \u043f\u0440\u0438\u0431\u0430\u0432\u043b\u044f\u044e\u0442 \u043a\u043e\u043b\u0438\u0447\u0435\u0441\u0442\u0432\u043e \u0433\u043e\u043b\u043e\u0441\u043e\u0432 \u0437\u0430 \u0438\u0432\u0435\u043d\u0442"));
        lore.add(ChatColor.translateAlternateColorCodes((char)'&', (String)" &x&0&0&D&8&F&F&l\u258d&f \u0437\u0430 \u043a\u043e\u0442\u043e\u0440\u044b\u0439 \u0432\u044b \u043f\u0440\u043e\u0433\u043e\u043b\u043e\u0441\u043e\u0432\u0430\u043b\u0438 (\u0447\u0435\u0440\u0435\u0437 /vote)."));
        lore.add(ChatColor.translateAlternateColorCodes((char)'&', (String)""));
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public ItemStack info() {
        ItemStack item = new ItemStack(Material.BOOK);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(HexUtil.translate("&#00D8FF \u274f \u0418\u043d\u0444\u043e\u0440\u043c\u0430\u0446\u0438\u044f \u274f"));
        ArrayList<String> lore = new ArrayList<String>();
        lore.add(HexUtil.translate(" &f"));
        lore.add(HexUtil.translate(" &#00D8FF&n\u258d&f \u0412 \u0434\u0430\u043d\u043d\u043e\u043c \u0440\u0430\u0437\u0434\u0435\u043b\u0435 \u043c\u043e\u0436\u043d\u043e \u0443\u0432\u0438\u0434\u0435\u0442\u044c"));
        lore.add(HexUtil.translate(" &#00D8FF\u258d&f \u0441\u0440\u043e\u043a \u0434\u0435\u0439\u0441\u0442\u0432\u0438\u044f \u0432\u0430\u0448\u0435\u0433\u043e &6\u041f\u0440\u0435\u043c\u0438\u0443\u043c&f"));
        lore.add(HexUtil.translate(" &#00D8FF\u258d&f \u0438 \u0434\u0440\u0443\u0433\u0443\u044e \u0438\u043d\u0444\u043e\u0440\u043c\u0430\u0446\u0438\u044e."));
        lore.add(HexUtil.translate(" &f"));
        lore.add(HexUtil.translate(" &#00D8FF\u25b6&f \u041d\u0430\u0436\u043c\u0438\u0442\u0435, \u0447\u0442\u043e\u0431\u044b \u043e\u0442\u043a\u0440\u044b\u0442\u044c \u0440\u0430\u0437\u0434\u0435\u043b."));
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        return item;
    }

    public ItemStack resetItem() {
        ItemStack item = new ItemStack(Material.BARRIER);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(HexUtil.translate("&#FF4444 \u274f \u0421\u0431\u0440\u043e\u0441\u0438\u0442\u044c \u274f"));
        ArrayList<String> lore = new ArrayList<String>();
        lore.add(HexUtil.translate(" &f"));
        lore.add(HexUtil.translate(" &#FF4444\u25b6&f \u041d\u0430\u0436\u043c\u0438\u0442\u0435, \u0447\u0442\u043e\u0431\u044b \u0441\u0431\u0440\u043e\u0441\u0438\u0442\u044c \u044d\u0444\u0444\u0435\u043a\u0442."));
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }
}

