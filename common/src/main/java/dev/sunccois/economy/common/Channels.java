package dev.sunccois.economy.common;

/**
 * Shared constants for cross-platform plugin messaging.
 */
public final class Channels {
    private Channels() {}

    /** Plugin-messaging channel used for balance invalidation broadcasts. */
    public static final String CHANNEL = "sunccois:eco";

    /** Magic bytes / protocol version sent in every packet. */
    public static final int PROTOCOL_VERSION = 1;

    /** Subchannel: "INVALIDATE" — one player's cache must be refreshed from DB. */
    public static final String SUB_INVALIDATE = "INVALIDATE";
}
