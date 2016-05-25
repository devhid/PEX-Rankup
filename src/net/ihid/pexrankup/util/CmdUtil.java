package net.ihid.pexrankup.util;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Mikey on 5/23/2016.
 */
public class CmdUtil {

    private static class CommandException extends RuntimeException {
        CommandException(String msg) {
            super(msg);
        }
    }

    public static void checkPerm(CommandSender cs, String perm) {
        if (!cs.hasPermission(perm))
            throw new CommandException("&cYou do not have permission to execute this command.");
    }

    public static void checkArgs(String[] args, int min) {
        if (args.length < min)
            throw new CommandException("&cYou did not specify enough arguments for this command.");
    }

    public static void checkPlayer(CommandSender cs) {
        if (!(cs instanceof Player))
            throw new CommandException("&cThis command is not usable from the console.");
    }
}
