package net.devhid.pexrankup.commands;

import net.devhid.pexrankup.RankupManager;
import net.devhid.pexrankup.RankupPlugin;
import net.devhid.pexrankup.api.events.PostRankupEvent;
import net.devhid.pexrankup.api.events.PreRankupEvent;
import net.devhid.pexrankup.util.ChatUtil;
import net.devhid.pexrankup.util.CommandUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import java.math.BigDecimal;


public class RankupCommand implements CommandExecutor {
    private final RankupPlugin plugin;
    private final RankupManager rankupManager;

    public RankupCommand(RankupPlugin plugin) {
        this.plugin = plugin;
        this.rankupManager = plugin.getRankupManager();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        String prefix = plugin.getPrefix();

        try {
            CommandUtil.checkPlayer(sender);
            CommandUtil.checkPerm(sender, "pexrankup.rankup");
            CommandUtil.checkArgs(args, 0);
        } catch(CommandUtil.CommandException ex) {
            sender.sendMessage(prefix + ex.getMessage());
            return true;
        }

        Player ps = (Player) sender;
        PermissionUser user = PermissionsEx.getUser(ps);
        String group = rankupManager.getCurrentGroup(user);

        if(group.equalsIgnoreCase(plugin.getConfig().getString("RANKUP" + ".last-rank"))) {
            ps.sendMessage(prefix + ChatUtil.color(plugin.getConfig().getString("RANKUP" + ".last-rank-message")));
            return true;
        }

        BigDecimal rawCost = rankupManager.getCostOfNextRank(user);
        BigDecimal balance = rankupManager.getBalance(ps);
        String cost = rankupManager.getCostOfNextRankFormatted(user);

        if(balance.doubleValue() < rawCost.doubleValue()) {
            ps.sendMessage(prefix + ChatUtil.color(plugin.getConfig().getString("RANKUP" + ".not-enough-money")
                    .replace("{cost}", cost)
                    .replace("{remaining-price}", rankupManager.getRemainingPriceFormatted(user))
                    .replace("{rank}", rankupManager.getNextRank(user))));
            return true;
        }

        if(RankupPlugin.getEconomy().getBalance(ps) >= rawCost.doubleValue()) {
            String nextRank = rankupManager.getNextRank(user);

            PreRankupEvent preRankupEvent = new PreRankupEvent(ps, group, nextRank, balance.doubleValue(), rawCost.doubleValue());
            plugin.getServer().getPluginManager().callEvent(preRankupEvent);

            if(!preRankupEvent.isCancelled()) {
                if (RankupPlugin.getEconomy().withdrawPlayer(ps, rawCost.doubleValue()).transactionSuccess()) {

                    rankupManager.executeCommands(user, nextRank, "rankup");

                    user.setParentsIdentifier(rankupManager.udpateRank(user));
                    user.save();

                    group = rankupManager.getCurrentGroup(user);

                    if(plugin.getConfig().getBoolean("RANKUP" + ".rank-up-message.broadcast")) {
                        plugin.getServer().broadcastMessage(prefix + ChatUtil.color(plugin.getConfig().getString("RANKUP" + ".rank-up-message.broadcast-message")
                                .replace("{username}", ps.getName()).replace("{rank}", group)));
                    }

                    sender.sendMessage(ChatUtil.color(prefix + plugin.getConfig().getString("RANKUP" + ".rank-up-message.player-message")
                            .replace("{rank}", group)));

                    plugin.getServer().getPluginManager().callEvent(new PostRankupEvent(ps, nextRank, balance.doubleValue()));
                }
            }
        }
        return true;
    }
}