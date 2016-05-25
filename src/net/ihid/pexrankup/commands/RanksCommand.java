package net.ihid.pexrankup.commands;

import net.ihid.pexrankup.RankupPlugin;
import net.ihid.pexrankup.util.ChatUtil;
import net.ihid.pexrankup.util.CmdUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;

import java.math.BigDecimal;
import java.text.NumberFormat;

/**
 * Created by Mikey on 5/23/2016.
 */
public class RanksCommand {
    private RankupPlugin plugin;

    private YamlConfiguration config;

    public RanksCommand(RankupPlugin instance) {
        plugin = instance;
        config = instance.getConfig();
    }

    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        String prefix = (config.getBoolean("MAIN" + ".prefix-enabled")) ? ChatUtil.color(config.getString("MAIN" + ".prefix")) : "";

        CmdUtil.checkPerm(cs, "pexrankup.ranks");
        CmdUtil.checkArgs(args, 0);

        cs.sendMessage(prefix + ChatUtil.color(config.getString("RANKUP" + ".rank-list")));
        for (String rank : plugin.getConfig().getConfigurationSection("LADDER").getKeys(false)) {
            String cost = NumberFormat.getInstance().format(BigDecimal.valueOf(config.getDouble("LADDER." + rank)));
            cs.sendMessage(ChatUtil.color(config.getString("RANKUP" + ".rank-list-format")
                    .replace("{rank}", rank)
                    .replace("{cost}", cost)));
        }
        return true;
    }
}
