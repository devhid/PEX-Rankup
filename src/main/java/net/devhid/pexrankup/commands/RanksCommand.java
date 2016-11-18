package net.devhid.pexrankup.commands;

import net.devhid.pexrankup.RankupPlugin;
import net.devhid.pexrankup.util.ChatUtil;
import net.devhid.pexrankup.util.CommandUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.math.BigDecimal;
import java.text.NumberFormat;

public class RanksCommand implements CommandExecutor {
    private final RankupPlugin plugin;

    public RanksCommand(RankupPlugin plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        String prefix = plugin.getPrefix();

        try {
            CommandUtil.checkPerm(sender, "pexrankup.ranks");
            CommandUtil.checkArgs(args, 0);
        } catch(CommandUtil.CommandException ex) {
            sender.sendMessage(prefix + ex.getMessage());
            return true;
        }

        sender.sendMessage(prefix + ChatUtil.color(plugin.getConfig().getString("RANKUP" + ".rank-list")));

        for (String rank : plugin.getConfig().getConfigurationSection("LADDER").getKeys(false)) {
            String cost = NumberFormat.getInstance().format(BigDecimal.valueOf(plugin.getConfig().getDouble("LADDER." + rank)));

            sender.sendMessage(ChatUtil.color(plugin.getConfig().getString("RANKUP" + ".rank-list-format")
                    .replace("{rank}", rank)
                    .replace("{cost}", cost)));
        }
        return true;
    }
}