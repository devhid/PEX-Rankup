package net.devhid.pexrankup;

import net.devhid.pexrankup.util.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import ru.tehkode.permissions.PermissionUser;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class RankupManager {
    private final RankupPlugin plugin;

    public RankupManager(RankupPlugin plugin) {
        this.plugin = plugin;
    }

    public String getNextRank(PermissionUser user)  {
        List<String> ranks = new ArrayList<>(plugin.getConfig().getConfigurationSection("LADDER").getKeys(false));

        for(int i = ranks.size() - 1; i >= 0; i--) {
            for(String group: user.getParentIdentifiers()) {
                if(group.equalsIgnoreCase(ranks.get(i))) {
                    try { return ranks.get(i + 1); }
                    catch(IndexOutOfBoundsException | NullPointerException ex) {
                        return ChatUtil.color(plugin.getConfig().getString("RANKUP" + ".scoreboard-next-rank-none"));
                    }
                }
            }
        }
        System.out.println("Could not find next rank of " + user.getName() + ".");
        return null;
    }

    public List<String> udpateRank(PermissionUser user) {
        List<String> newParents = new ArrayList<>();
        List<String> parents = user.getParentIdentifiers();

        for(String parent: parents) {
            if(parent.equalsIgnoreCase(getCurrentGroup(user))) {
                newParents.add(getNextRank(user));
                continue;
            }
            newParents.add(parent);
        }
        return newParents;
    }

    public String getCurrentGroup(PermissionUser user) {
        List<String> ranks = new ArrayList<>(plugin.getConfig().getConfigurationSection("LADDER").getKeys(false));

        for(int i = ranks.size() - 1; i >= 0; i--) {
            for(String group: user.getParentIdentifiers()) {
                if(group.equalsIgnoreCase(ranks.get(i))) {
                    return ranks.get(i);
                }
            }
        }
        System.out.println("Could not find current group of " + user.getName() + ".");
        return null;
    }

    public void executeCommands(PermissionUser user, String rank, String type) {
        String old = getCurrentGroup(user);

        for(String cmd: plugin.getConfig().getStringList("RANKUP" + ".execute-commands-upon-" + type)) {
            plugin.getServer().dispatchCommand(Bukkit.getConsoleSender(), cmd
                    .replace("{username}", user.getName())
                    .replace("{rank}", rank)
                    .replace("{oldrank}", old));
        }
    }

    public List<String> setRank(PermissionUser user, String group) {
        List<String> newParents = new ArrayList<>();
        List<String> parents = user.getParentIdentifiers();

        for(String parent: parents) {
            if(isValid(parent)) {
                newParents.add(group);
                continue;
            }
            newParents.add(parent);
        }
        return newParents;
    }

    public boolean isValid(String group) {
        for(String rank: plugin.getConfig().getConfigurationSection("LADDER").getKeys(false)) {
            if(rank.equalsIgnoreCase(group)) {
                return true;
            }
        }
        return false;
    }

    public boolean checkLastRank(PermissionUser user) {
        return getCurrentGroup(user).equalsIgnoreCase(plugin.getConfig().getString("RANKUP" + ".last-rank"));
    }

    public BigDecimal getBalance(Player player) {
        return BigDecimal.valueOf(RankupPlugin.getEconomy().getBalance(player));
    }

    public BigDecimal getCostOfNextRank(PermissionUser user) {
        return BigDecimal.valueOf(plugin.getConfig().getDouble("LADDER." + getNextRank(user)));
    }

    public BigDecimal getRemainingPrice(PermissionUser user) {
        BigDecimal diff = getCostOfNextRank(user).subtract(getBalance(user.getPlayer()));
        return  (diff.doubleValue() < 0 || checkLastRank(user)) ? BigDecimal.ZERO : diff;
    }

    public String getBalanceString(Player player) {
        return getBalance(player).toString();
    }

    public String getBalanceFormatted(Player player) {
        return NumberFormat.getInstance().format(getBalance(player));
    }

    public String getCostOfNextRankString(PermissionUser user) {
        return getCostOfNextRank(user).toString();
    }

    public String getCostOfNextRankFormatted(PermissionUser user) {
        return NumberFormat.getInstance().format(getCostOfNextRank(user));
    }

    public String getRemainingPriceString(PermissionUser user) {
        return getRemainingPrice(user).toString();
    }

    public String getRemainingPriceFormatted(PermissionUser user) {
        return NumberFormat.getInstance().format(getRemainingPrice(user));
    }
}
