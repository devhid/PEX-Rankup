package net.devhid.pexrankup.commands;

import net.devhid.pexrankup.RankupPlugin;
import net.devhid.pexrankup.util.ChatUtil;
import net.devhid.pexrankup.util.CommandUtil;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import java.math.BigDecimal;
import java.text.DecimalFormat;

public class RanksCommand implements CommandExecutor {
    private final RankupPlugin plugin;
    private final FileConfiguration config;

    public RanksCommand(RankupPlugin plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
    }

    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        String prefix = plugin.getPrefix();

        try {
            CommandUtil.checkPerm(cs, "pexrankup.ranks");
            CommandUtil.checkArgs(args, 0);
        } catch(CommandUtil.CommandException ex) {
            cs.sendMessage(prefix + ex.getMessage());
            return true;
        }

        cs.sendMessage(prefix + ChatUtil.color(config.getString("RANKUP" + ".rank-list")));

        for (String rank : config.getConfigurationSection("LADDER").getKeys(false)) {
            String cost = new DecimalFormat("#,###").format(BigDecimal.valueOf(config.getDouble("LADDER." + rank)));

            cs.sendMessage(ChatUtil.color(config.getString("RANKUP" + ".rank-list-format")
                    .replace("{rank}", rank)
                    .replace("{cost}", cost)));
        }
        return true;
    }
}