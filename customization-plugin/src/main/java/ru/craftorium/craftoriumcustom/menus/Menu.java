package ru.craftorium.craftoriumcustom.menus;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;
import ru.craftorium.craftoriumcustom.menus.Button;

public abstract class Menu
implements InventoryHolder {
    protected final List<Button> buttons = new ArrayList<Button>();

    public void onClick(InventoryClickEvent e) {
        this.buttons.forEach(b -> b.onClick(e));
    }

    public abstract void open(Player var1);
}

