package ru.craftorium.craftoriumcustom.items;

import java.util.ArrayList;
import java.util.HashMap;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ru.craftorium.craftoriumcustom.CraftoriumCustom;
import ru.craftorium.craftoriumcustom.items.MenuItems;
import ru.craftorium.craftoriumcustom.utils.HexUtil;

public class StarItem {
    public ItemStack getStar(Material material, String color, int cost, int level, Player player) {
        int currentStar;
        ItemStack shulker = new ItemStack(material);
        ItemMeta itemMeta = shulker.getItemMeta();
        itemMeta.setDisplayName(HexUtil.translate(color + MenuItems.convertInt(level) + " \u0417\u0432\u0435\u0437\u0434\u0430 \u0441\u0442\u0430\u0442\u0443\u0441\u0430 (" + level + " \u0443\u0440.)"));
        ArrayList<String> lore = new ArrayList<String>();
        lore.add(HexUtil.translate(""));
        HashMap<String, String> pData = CraftoriumCustom.getPlayerData(player);
        double starPoints = 0.0;
        if (pData != null) {
            try {
                starPoints = Double.parseDouble(pData.getOrDefault("star_points", "0"));
            }
            catch (NumberFormatException numberFormatException) {
                // empty catch block
            }
        }
        String currentStarStr = pData != null ? pData.getOrDefault("star_level", "0") : "0";
        try {
            currentStar = Integer.parseInt(currentStarStr);
        }
        catch (NumberFormatException e) {
            currentStar = 0;
        }
        double percent = Math.min(100.0, starPoints / (double)cost * 100.0);
        String progressStr = String.format("%.1f\u20bd / %d\u20bd [%.1f%%]", starPoints, cost, percent);
        lore.add(HexUtil.translate(color + " &n\u258d" + color + " \u041f\u0440\u043e\u0433\u0440\u0435\u0441\u0441:"));
        lore.add(HexUtil.translate(color + " \u258d&f " + progressStr));
        lore.add(HexUtil.translate(""));
        String bonus = CraftoriumCustom.getInstance().getConfig().getString("stars.levels." + level + ".bonus", "");
        if (!bonus.isEmpty()) {
            lore.add(HexUtil.translate(color + " &n\u258d" + color + " \u0411\u043e\u043d\u0443\u0441\u044b:"));
            lore.add(HexUtil.translate(color + " \u258d&f " + bonus));
            lore.add(HexUtil.translate(""));
        }
        if (currentStar >= level) {
            lore.add(HexUtil.translate(color + " &l\u25b6 &a\u0423\u0436\u0435 \u043f\u043e\u043b\u0443\u0447\u0435\u043d\u043e!"));
            itemMeta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ENCHANTS});
            itemMeta.addEnchant(Enchantment.LUCK, 1, true);
        } else if (starPoints >= (double)cost) {
            lore.add(HexUtil.translate(color + " &l\u25b6 &f\u041d\u0430\u0436\u043c\u0438\u0442\u0435, \u0447\u0442\u043e\u0431\u044b \u043f\u043e\u043b\u0443\u0447\u0438\u0442\u044c."));
        } else {
            double remaining = (double)cost - starPoints;
            lore.add(HexUtil.translate(color + " &l\u25b6 &f\u041d\u0435\u043e\u0431\u0445\u043e\u0434\u0438\u043c\u043e \u0435\u0449\u0451 " + String.format("%.0f", remaining) + "\u20bd"));
        }
        itemMeta.setLore(lore);
        shulker.setItemMeta(itemMeta);
        return shulker;
    }
}

