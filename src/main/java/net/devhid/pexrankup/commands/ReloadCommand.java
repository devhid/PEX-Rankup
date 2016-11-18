package net.devhid.pexrankup.commands;

import net.devhid.pexrankup.RankupPlugin;
import net.devhid.pexrankup.util.ChatUtil;
import net.devhid.pexrankup.util.CommandUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ReloadCommand implements CommandExecutor {
    private final RankupPlugin plugin;

    public ReloadCommand(RankupPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String prefix = plugin.getPrefix();

        try {
            CommandUtil.checkPerm(sender, "pexrankup.reload");
            CommandUtil.checkArgs(args, 0);
        } catch(CommandUtil.CommandException ex) {
            sender.sendMessage(prefix + ex.getMessage());
            return true;
        }

        plugin.reloadConfig();
        sender.sendMessage(ChatUtil.color(prefix + "&aYou have successfully reloaded the configuration file."));

        return true;
    }
}
