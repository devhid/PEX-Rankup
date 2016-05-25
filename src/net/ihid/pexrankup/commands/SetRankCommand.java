package net.ihid.pexrankup.commands;

import net.ihid.pexrankup.RankupPlugin;
import net.ihid.pexrankup.util.ChatUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 * Created by Mikey on 5/23/2016.
 */
public class SetRankCommand {
    private RankupPlugin plugin;

    private YamlConfiguration config;

    public SetRankCommand(RankupPlugin instance) {
        plugin = instance;
    }

    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        String prefix = (config.getBoolean("MAIN" + ".prefix-enabled")) ? ChatUtil.color(config.getString("MAIN" + ".prefix")) : "";

        return true;
    }
}
