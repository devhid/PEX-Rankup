package net.ihid.pexrankup.commands;

import lombok.NonNull;
import net.ihid.pexrankup.util.ChatUtil;
import net.ihid.pexrankup.util.CmdUtil;
import net.ihid.pexrankup.RankupPlugin;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Mikey on 5/23/2016.
 */
public class RankupCommand implements CommandExecutor {
    private RankupPlugin plugin;

    private YamlConfiguration config;

    public RankupCommand(RankupPlugin instance) {
        plugin = instance;
        config = plugin.getConfig();
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        String prefix = (config.getBoolean("MAIN" + ".prefix-enabled")) ? ChatUtil.color(config.getString("MAIN" + ".prefix")) : "";

        try {
            CmdUtil.checkPlayer(cs);
            CmdUtil.checkPerm(cs, "pexrankup.rankup");
            CmdUtil.checkArgs(args, 0);
        } catch(CmdUtil.CommandException ex) {
            cs.sendMessage(prefix + ex.getMessage());
            return true;
        }

        Player ps = (Player) cs;
        PermissionUser user = PermissionsEx.getUser(ps);
        String group = getCurrentGroup(user);

        if(group.equalsIgnoreCase(config.getString("RANKUP" + ".last-rank"))) {
            ps.sendMessage(prefix + ChatUtil.color(config.getString("RANKUP" + ".last-rank-message")));
            return true;
        }

        BigDecimal rawCost = BigDecimal.valueOf(config.getDouble("LADDER." + getNextRank(user)));
        BigDecimal balance = BigDecimal.valueOf(plugin.economy.getBalance(ps));
        String cost = NumberFormat.getInstance().format(rawCost);

        if(balance.doubleValue() < rawCost.doubleValue()) {
            ps.sendMessage(prefix + ChatUtil.color(config.getString("RANKUP" + ".not-enough-money").replace("{cost}", cost).replace("{rank}", getNextRank(user))));
            return true;
        }

        if(plugin.economy.withdrawPlayer(ps, rawCost.doubleValue()).transactionSuccess()) {
            executeCommands(user, getNextRank(user));

            user.setParentsIdentifier(udpateRank(user));
            user.save();

            group = getCurrentGroup(user);
            Bukkit.broadcastMessage(prefix + ChatUtil.color(config.getString("RANKUP" + ".rank-up-broadcast").replace("{username}", ps.getName()).replace("{rank}", group)));
        }
        return true;
    }

    private void executeCommands(PermissionUser user, String rank) {
        String old = getCurrentGroup(user);

        for(String cmd: config.getStringList("RANKUP" + ".execute-commands-on-rankup")) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd
                    .replace("{username}", user.getName())
                    .replace("{rank}", rank)
                    .replace("{oldrank}", old));
        }
    }

    private List<String> udpateRank(PermissionUser user) {
        List<String> newParents = new ArrayList<>();
        final List<String> parents = user.getParentIdentifiers();

        for(int i = 0; i < parents.size(); i++) {
            if(parents.get(i).equalsIgnoreCase(getCurrentGroup(user))) {
                newParents.add(getNextRank(user));
                continue;
            }
            newParents.add(parents.get(i));
        }
        return newParents;
    }

    private String getCurrentGroup(PermissionUser user) {
        for(String rank: config.getConfigurationSection("LADDER").getKeys(false)) {
            for(String group: user.getParentIdentifiers()) {
                if(rank.equalsIgnoreCase(group)) {
                    return rank;
                }
            }
        }
        System.out.println("Could not find current group of " + user.getName() + ".");
        return null;
    }

    private String getNextRank(PermissionUser user) {
        final Set<String> ranks = config.getConfigurationSection("LADDER").getKeys(false);

        for(int i = 0; i < ranks.size(); i++) {
            for(String group: user.getParentIdentifiers()) {
                if(group.equalsIgnoreCase((String) ranks.toArray()[i])) {
                    return (String) ranks.toArray()[i+1];
                }
            }
        }
        System.out.println("Could not find next rank of " + user.getName() + ".");
        return null;
    }
}
