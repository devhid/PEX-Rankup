package net.devhid.pexrankup.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.ChatColor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ChatUtil {

    public static String color(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }
}
