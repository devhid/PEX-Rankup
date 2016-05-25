package net.ihid.pexrankup.util;

import org.bukkit.ChatColor;

/**
 * Created by Mikey on 5/24/2016.
 */
public class ChatUtil {
    public static String color(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }
}
