package dev.sunccois.economy.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import com.velocitypowered.api.proxy.messages.ChannelIdentifier;
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;
import dev.sunccois.economy.common.Channels;
import org.slf4j.Logger;

import java.nio.file.Path;

/**
 * Velocity side of the plugin.
 * Its only responsibility is to rebroadcast INVALIDATE messages from any
 * backend server to every other backend, so cached balances stay in sync.
 *
 * No database access here — the Velocity plugin is stateless.
 */
@Plugin(
        id = "sunccois-economy",
        name = "SunccoisEconomy (Velocity)",
        version = "1.0.0",
        authors = {"sunccois"},
        description = "Rebroadcasts economy cache-invalidations to all backends"
)
public final class SunccoisEconomyVelocity {

    private final ProxyServer proxy;
    private final Logger logger;
    private final ChannelIdentifier channel;

    @Inject
    public SunccoisEconomyVelocity(ProxyServer proxy, Logger logger,
                                   @DataDirectory Path dataDir) {
        this.proxy = proxy;
        this.logger = logger;
        String[] parts = Channels.CHANNEL.split(":", 2);
        this.channel = MinecraftChannelIdentifier.create(parts[0], parts[1]);
    }

    @Subscribe
    public void onInit(ProxyInitializeEvent e) {
        proxy.getChannelRegistrar().register(channel);
        logger.info("SunccoisEconomy (Velocity) ready. Channel = {}", Channels.CHANNEL);
    }

    @Subscribe
    public void onPluginMessage(PluginMessageEvent event) {
        if (!event.getIdentifier().getId().equals(Channels.CHANNEL)) return;

        // Only handle messages coming from a backend server, never from a client.
        if (!(event.getSource() instanceof ServerConnection source)) return;

        // Drop from further processing; we are routing it ourselves.
        event.setResult(PluginMessageEvent.ForwardResult.handled());

        byte[] data = event.getData();
        // Fan-out: forward to every other backend server currently registered.
        for (RegisteredServer target : proxy.getAllServers()) {
            if (target.getServerInfo().getName().equals(source.getServerInfo().getName())) {
                continue; // skip the sender
            }
            // Velocity can only send a plugin message on a backend channel if
            // there is at least one player connection through which to pipe it.
            // sendPluginMessage returns false when nobody is on that server.
            target.sendPluginMessage(channel, data);
        }
    }
}
