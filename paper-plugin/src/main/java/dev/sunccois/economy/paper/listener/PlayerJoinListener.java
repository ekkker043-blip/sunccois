package dev.sunccois.economy.paper.listener;

import dev.sunccois.economy.paper.economy.EconomyService;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/** Creates an account row on first join and keeps username up-to-date. */
public final class PlayerJoinListener implements Listener {

    private final EconomyService economy;

    public PlayerJoinListener(EconomyService economy) {
        this.economy = economy;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        economy.ensureAccount(e.getPlayer().getUniqueId(), e.getPlayer().getName());
        // Drop any stale cached value from before this join.
        economy.invalidate(e.getPlayer().getUniqueId());
    }
}
