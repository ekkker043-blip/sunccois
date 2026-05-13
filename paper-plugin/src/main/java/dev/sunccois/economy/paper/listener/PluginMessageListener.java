package dev.sunccois.economy.paper.listener;

import dev.sunccois.economy.common.Channels;
import dev.sunccois.economy.paper.economy.EconomyService;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * Receives INVALIDATE messages that Velocity rebroadcasts to every backend
 * whenever some other server changes a balance.
 */
public final class PluginMessageListener implements org.bukkit.plugin.messaging.PluginMessageListener {

    private final EconomyService economy;

    public PluginMessageListener(EconomyService economy) {
        this.economy = economy;
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (!Channels.CHANNEL.equals(channel)) return;

        try (DataInputStream in = new DataInputStream(new ByteArrayInputStream(message))) {
            int version = in.readByte() & 0xFF;
            if (version != Channels.PROTOCOL_VERSION) return;

            String sub = in.readUTF();
            if (!Channels.SUB_INVALIDATE.equals(sub)) return;

            String uuidStr = in.readUTF();
            // source server, not used here, but kept in the frame for debugging.
            String source = in.readUTF();

            try {
                UUID uuid = UUID.fromString(uuidStr);
                economy.invalidate(uuid);
            } catch (IllegalArgumentException ignored) {}
        } catch (IOException ignored) {
            // Malformed packet — drop silently.
        }
    }
}
