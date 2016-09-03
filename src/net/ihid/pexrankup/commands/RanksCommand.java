package net.ihid.pexrankup.commands;

import net.ihid.pexrankup.RankupPlugin;
import net.ihid.pexrankup.util.ChatUtil;
import net.ihid.pexrankup.util.CmdUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import java.math.BigDecimal;
import java.text.NumberFormat;

public class RanksCommand implements CommandExecutor {
    private final YamlConfiguration config;

    public RanksCommand(RankupPlugin plugin) {
        this.config = plugin.getConfig();
    }

    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        String prefix = (config.getBoolean("MAIN" + ".prefix-enabled")) ? ChatUtil.color(config.getString("MAIN" + ".prefix")) : "";

        try {
            CmdUtil.checkPerm(cs, "pexrankup.ranks");
            CmdUtil.checkArgs(args, 0);
        } catch(CmdUtil.CommandException ex) {
            cs.sendMessage(prefix + ex.getMessage());
            return true;
        }

        cs.sendMessage(prefix + ChatUtil.color(config.getString("RANKUP" + ".rank-list")));

        for (String rank : config.getConfigurationSection("LADDER").getKeys(false)) {
            String cost = NumberFormat.getInstance().format(BigDecimal.valueOf(config.getDouble("LADDER." + rank)));
            cs.sendMessage(ChatUtil.color(config.getString("RANKUP" + ".rank-list-format")
                    .replace("{rank}", rank)
                    .replace("{cost}", cost)));
        }
        return true;
    }
}
