package ru.craftorium.craftoriumcustom.menus;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import ru.craftorium.craftoriumcustom.holders.MainMenuHolder;
import ru.craftorium.craftoriumcustom.items.MenuItems;
import ru.craftorium.craftoriumcustom.items.StarItem;
import ru.craftorium.craftoriumcustom.menus.Button;
import ru.craftorium.craftoriumcustom.menus.colorname.ColorNameMenu;
import ru.craftorium.craftoriumcustom.menus.info.InfoMenu;
import ru.craftorium.craftoriumcustom.menus.particle.ParticleMenu;
import ru.craftorium.craftoriumcustom.menus.prem.PremPearlMenu;
import ru.craftorium.craftoriumcustom.menus.skins.SkinsMenu;
import ru.craftorium.craftoriumcustom.menus.sounds.SoundHitMenu;
import ru.craftorium.craftoriumcustom.menus.star.StarMenu;
import ru.craftorium.craftoriumcustom.utils.HexUtil;
import ru.craftorium.craftoriumcustom.CraftoriumCustom;

public class MainMenu {
    private final MenuItems items;
    private final List<Button> buttons = new ArrayList<Button>();

    public MainMenu(MenuItems items) {
        this.items = items;
        this.setupButtons();
    }

    private void setupButtons() {
        this.buttons.add(new Button(11, this.items.sounds(), e -> {
            SoundHitMenu soundMenu = new SoundHitMenu(this.items);
            soundMenu.open((Player)e.getWhoClicked());
        }));
        this.buttons.add(new Button(33, this.items.traps(), e -> {
            if (Bukkit.getPluginManager().getPlugin("HolyLiteItems") != null) {
                SkinsMenu skinsMenu = new SkinsMenu(this.items);
                skinsMenu.open((Player)e.getWhoClicked());
            } else {
                e.getWhoClicked().sendMessage("\u0414\u0430\u043d\u043d\u044b\u0439 \u0440\u0430\u0437\u0434\u0435\u043b \u043d\u0435\u0434\u043e\u0441\u0442\u0443\u043f\u0435\u043d");
                Bukkit.getLogger().info("HolyLiteItems \u043d\u0435 \u043d\u0430\u0439\u0434\u0435\u043d!");
            }
        }));
        this.buttons.add(new Button(15, this.items.particle(), e -> {
            ParticleMenu particleMenu = new ParticleMenu(this.items);
            particleMenu.open((Player)e.getWhoClicked());
        }));
        // Premium tab is gated by the stickhwcustom.prem permission. Players
        // without premium get a clear feedback message; effects themselves are
        // also locked behind the same permission inside PremPearlListener.
        this.buttons.add(new Button(13, this.items.premka(), e -> {
            Player p = (Player)e.getWhoClicked();
            if (p.hasPermission("stickhwcustom.prem")) {
                PremPearlMenu premPearlMenu = new PremPearlMenu(this.items);
                premPearlMenu.open(p);
            } else {
                String msg = CraftoriumCustom.getInstance().getConfig().getString(
                        "messages.nopremium",
                        "&#ff2222\u25b6 &f\u042d\u0442\u043e\u0442 \u0440\u0430\u0437\u0434\u0435\u043b \u0442\u0440\u0435\u0431\u0443\u0435\u0442 \u043f\u0440\u0435\u043c\u0438\u0443\u043c.");
                p.sendMessage(HexUtil.translate(msg));
            }
        }));
        // The nickname-colour menu opens for everyone, but each colour inside
        // is gated by the player's star level only (premium does not bypass).
        this.buttons.add(new Button(29, this.items.nickcolor(), e -> {
            ColorNameMenu colorNameMenu = new ColorNameMenu(this.items);
            colorNameMenu.open((Player)e.getWhoClicked());
        }));
        this.buttons.add(new Button(31, this.items.star(), e -> {
            StarItem starItem = new StarItem();
            StarMenu starMenu = new StarMenu(this.items, starItem);
            starMenu.open((Player)e.getWhoClicked());
        }));
        this.buttons.add(new Button(35, this.items.info(), e -> {
            InfoMenu infoMenu = new InfoMenu(this.items);
            infoMenu.open((Player)e.getWhoClicked());
        }));
    }

    public void open(Player player) {
        Inventory inventory = Bukkit.createInventory((InventoryHolder)new MainMenuHolder(), (int)45, (String)"\u041a\u0430\u0441\u0442\u043e\u043c\u0438\u0437\u0430\u0446\u0438\u044f");
        this.buttons.forEach(button -> inventory.setItem(button.getSlot(), button.getItem()));
        player.openInventory(inventory);
    }

    public void onClick(InventoryClickEvent e) {
        this.buttons.forEach(b -> b.onClick(e));
    }
}

