package net.devhid.pexrankup.commands;

import net.devhid.pexrankup.RankupManager;
import net.devhid.pexrankup.RankupPlugin;
import net.devhid.pexrankup.util.ChatUtil;
import net.devhid.pexrankup.util.CommandUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class SetRankCommand implements CommandExecutor {
    private final RankupPlugin plugin;
    private final RankupManager rankupManager;

    public SetRankCommand(RankupPlugin plugin) {
        this.plugin = plugin;
        this.rankupManager = plugin.getRankupManager();
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        String prefix = plugin.getPrefix();

        Player target;
        try {
            CommandUtil.checkPerm(sender, "pexrankup.setrank");
            CommandUtil.checkArgs(args, 2);

            target = Bukkit.getPlayer(args[0]);
            CommandUtil.checkOnline(target);
        } catch(CommandUtil.CommandException ex) {
            sender.sendMessage(prefix + ex.getMessage());
            return true;
        }

        String group = args[1];

        if(!rankupManager.isValid(group)) {
            sender.sendMessage(prefix + ChatUtil.color(
                    plugin.getConfig().getString("RANKUP" + ".setrank-invalid-rank")));
            return true;
        }

        PermissionUser user = PermissionsEx.getUser(target);
        rankupManager.executeCommands(user, group, "setrank");

        user.setParentsIdentifier(rankupManager.setRank(user, group));
        user.save();

        sender.sendMessage(prefix + ChatUtil.color(
                plugin.getConfig().getString("RANKUP" + ".setrank-success")
                .replace("{username}", target.getName())
                .replace("{rank}", group)));

        target.sendMessage(prefix + ChatUtil.color(
                plugin.getConfig().getString("RANKUP" + ".setrank-received")
                .replace("{rank}", group)));

        return true;
    }

}