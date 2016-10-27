package net.ihid.pexrankup.commands;

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
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class RankupCommand implements CommandExecutor {
    private final YamlConfiguration config;

    public RankupCommand(RankupPlugin plugin) {
        this.config = plugin.getConfig();
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

        BigDecimal rawCost = getCostOfNextRank(user);
        BigDecimal balance = getBalance(ps);
        String cost = getCostOfNextRankFormatted(user);

        if(balance.doubleValue() < rawCost.doubleValue()) {
            ps.sendMessage(prefix + ChatUtil.color(config.getString("RANKUP" + ".not-enough-money").replace("{cost}", cost).replace("{rank}", getNextRank(user))));
            return true;
        }

        if(RankupPlugin.economy.withdrawPlayer(ps, rawCost.doubleValue()).transactionSuccess()) {
            executeCommands(user, getNextRank(user));

            user.setParentsIdentifier(udpateRank(user));
            user.save();

            group = getCurrentGroup(user);
            Bukkit.broadcastMessage(prefix + ChatUtil.color(config.getString("RANKUP" + ".rank-up-broadcast").replace("{username}", ps.getName()).replace("{rank}", group)));
        }
        return true;
    }

    public BigDecimal getBalance(Player player) {
        return BigDecimal.valueOf(RankupPlugin.economy.getBalance(player));
    }

    public BigDecimal getCostOfNextRank(PermissionUser user) {
        return BigDecimal.valueOf(config.getDouble("LADDER." + getNextRank(user)));
    }

    public String getCostOfNextRankString(PermissionUser user) {
        return getCostOfNextRank(user).toString();
    }

    public String getCostOfNextRankFormatted(PermissionUser user) {
        return new DecimalFormat("#,###").format(getCostOfNextRank(user));
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
        List<String> parents = user.getParentIdentifiers();

        for(int i = 0; i < parents.size(); i++) {
            if(parents.get(i).equalsIgnoreCase(getCurrentGroup(user))) {
                newParents.add(getNextRank(user));
                continue;
            }
            newParents.add(parents.get(i));
        }
        return newParents;
    }

    public String getCurrentGroup(PermissionUser user) {
        List<String> ranks = new ArrayList<>(config.getConfigurationSection("LADDER").getKeys(false));

        for(int i = ranks.size()-1; i >= 0; i--) {
            for(String group: user.getParentIdentifiers()) {
                if(group.equalsIgnoreCase(ranks.get(i))) {
                    return ranks.get(i);
                }
            }
        }
        System.out.println("Could not find current group of " + user.getName() + ".");
        return null;
    }

    public String getNextRank(PermissionUser user)  {
        List<String> ranks = new ArrayList<>(config.getConfigurationSection("LADDER").getKeys(false));

        for(int i = ranks.size()-1; i >= 0; i--) {
            for(String group: user.getParentIdentifiers()) {
                if(group.equalsIgnoreCase(ranks.get(i))) {
                    return ranks.get(i+1) != null ? ranks.get(i+1) : "N/A";
                }
            }
        }
        System.out.println("Could not find next rank of " + user.getName() + ".");
        return null;
    }
}