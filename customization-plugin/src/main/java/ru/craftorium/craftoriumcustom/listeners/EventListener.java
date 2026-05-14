package ru.craftorium.craftoriumcustom.listeners;

import java.util.HashMap;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.plugin.Plugin;
import ru.craftorium.craftoriumcustom.CraftoriumCustom;
import ru.craftorium.craftoriumcustom.db.DatabaseManager;
import ru.craftorium.craftoriumcustom.holders.ColorNameMenuHolder;
import ru.craftorium.craftoriumcustom.holders.MainMenuHolder;
import ru.craftorium.craftoriumcustom.holders.ParticleMenuHolder;
import ru.craftorium.craftoriumcustom.holders.PremDeathMeMenuHolder;
import ru.craftorium.craftoriumcustom.holders.PremDeathPlayerMenuHolder;
import ru.craftorium.craftoriumcustom.holders.PremPearlMenuHolder;
import ru.craftorium.craftoriumcustom.holders.SkinMenuHolder;
import ru.craftorium.craftoriumcustom.holders.SoundDeathMeMenuHolder;
import ru.craftorium.craftoriumcustom.holders.SoundDeathPlayerHolder;
import ru.craftorium.craftoriumcustom.holders.SoundHitMenuHolder;
import ru.craftorium.craftoriumcustom.holders.SoundLowHealMenuHolder;
import ru.craftorium.craftoriumcustom.holders.SoundVolumeMenuHolder;
import ru.craftorium.craftoriumcustom.holders.StarMenuHolder;

public class EventListener
implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        DatabaseManager.getPlayerAsync(player.getName()).thenAccept(data -> {
            HashMap playerMap = data != null ? DatabaseManager.fromBase64(data) : new HashMap();
            Bukkit.getScheduler().runTask((Plugin)CraftoriumCustom.getInstance(), () -> CraftoriumCustom.playerData.put(player.getUniqueId(), playerMap));
        });
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        HashMap<String, String> data = CraftoriumCustom.playerData.get(player.getUniqueId());
        if (data != null) {
            String base64 = DatabaseManager.toBase64(data);
            DatabaseManager.setPlayerAsync(player.getName(), base64).thenRun(() -> CraftoriumCustom.playerData.remove(player.getUniqueId()));
        }
    }

    private boolean isCustomHolder(InventoryHolder holder) {
        return holder instanceof MainMenuHolder || holder instanceof ParticleMenuHolder || holder instanceof PremPearlMenuHolder || holder instanceof PremDeathMeMenuHolder || holder instanceof PremDeathPlayerMenuHolder || holder instanceof ColorNameMenuHolder || holder instanceof SkinMenuHolder || holder instanceof SoundHitMenuHolder || holder instanceof SoundDeathMeMenuHolder || holder instanceof SoundDeathPlayerHolder || holder instanceof SoundLowHealMenuHolder || holder instanceof SoundVolumeMenuHolder || holder instanceof StarMenuHolder;
    }

    @EventHandler(priority=EventPriority.LOWEST)
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getView().getTopInventory() == null) {
            return;
        }
        InventoryHolder holder = e.getView().getTopInventory().getHolder();
        if (!this.isCustomHolder(holder)) {
            return;
        }
        e.setCancelled(true);
        if (e.getClickedInventory() == null) {
            return;
        }
        if (e.getClickedInventory() != e.getView().getTopInventory()) {
            return;
        }
        if (!(e.getWhoClicked() instanceof Player)) {
            return;
        }
        if (holder instanceof MainMenuHolder) {
            CraftoriumCustom.mainMenu.onClick(e);
        }
    }

    @EventHandler(priority=EventPriority.LOWEST)
    public void onInventoryDrag(InventoryDragEvent e) {
        if (e.getView().getTopInventory() == null) {
            return;
        }
        InventoryHolder holder = e.getView().getTopInventory().getHolder();
        if (!this.isCustomHolder(holder)) {
            return;
        }
        e.setCancelled(true);
    }
}

