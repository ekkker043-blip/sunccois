package dev.sunccois.economy.common;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * Thread-safe number formatting with thousands separators (e.g. 1,000).
 * A single formatter instance is NOT reused across threads because DecimalFormat
 * is not thread-safe; we create per-call formatters. For hot paths that's still
 * cheap compared to a MySQL query, so it's fine.
 */
public final class NumberFormatter {
    private NumberFormatter() {}

    private static final DecimalFormatSymbols SYMBOLS;

    static {
        SYMBOLS = new DecimalFormatSymbols(Locale.ROOT);
        SYMBOLS.setGroupingSeparator(',');
    }

    /** Format a long with a comma as thousands separator: 1000 -> "1,000". */
    public static String format(long value) {
        DecimalFormat df = new DecimalFormat("#,##0", SYMBOLS);
        return df.format(value);
    }

    /**
     * Parse a user-supplied amount. Accepts digits, commas and underscores as
     * thousands separators. Rejects negatives, NaN, overflow and zero.
     * Returns the parsed positive long, or -1 on invalid input.
     */
    public static long parsePositive(String raw) {
        if (raw == null) return -1;
        String cleaned = raw.replace(",", "").replace("_", "").trim();
        if (cleaned.isEmpty()) return -1;
        try {
            long v = Long.parseLong(cleaned);
            if (v <= 0) return -1;
            return v;
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
