package net.ihid.pexrankup.util;

import net.ihid.pexrankup.Lang;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Mikey on 5/23/2016.
 */
public class CmdUtil {

    public static class CommandException extends RuntimeException {
        CommandException(String msg) {
            super(msg);
        }
    }

    public static void checkPerm(CommandSender cs, String perm) {
        if (!cs.hasPermission(perm))
            throw new CommandException(ChatUtil.color(Lang.NO_PERMISSION.toString()));
    }

    public static void checkArgs(String[] args, int min) {
        if (args.length < min)
            throw new CommandException(ChatUtil.color(Lang.IMPROPER_USAGE.toString()));
    }

    public static void checkPlayer(CommandSender cs) {
        if (!(cs instanceof Player))
            throw new CommandException(ChatUtil.color(Lang.INVALID_SENDER.toString()));
    }

    public static void checkOnline(Player target) {
        if(target == null || !target.isOnline()) {
            throw new CommandException(ChatUtil.color(Lang.PLAYER_OFFLINE.toString()));
        }
    }
}
