package net.devhid.pexrankup.util;

import org.bukkit.ChatColor;

public final class ChatUtil {
    private ChatUtil() {}

    public static String color(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }
}
