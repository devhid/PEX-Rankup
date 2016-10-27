package net.devhid.pexrankup.commands;

import net.devhid.pexrankup.RankupPlugin;
import net.devhid.pexrankup.util.ChatUtil;
import net.devhid.pexrankup.util.CommandUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;
import java.util.ArrayList;
import java.util.List;

public class SetRankCommand implements CommandExecutor {
    private final RankupPlugin plugin;
    private final FileConfiguration config;

    public SetRankCommand(RankupPlugin plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
    }

    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        String prefix = plugin.getPrefix();

        Player target = Bukkit.getPlayer(args[0]);

        try {
            CommandUtil.checkPerm(cs, "pexrankup.setrank");
            CommandUtil.checkArgs(args, 2);
            CommandUtil.checkOnline(target);
        } catch(CommandUtil.CommandException ex) {
            cs.sendMessage(prefix + ex.getMessage());
            return true;
        }

        String group = args[1];

        if(!isValid(group)) {
            cs.sendMessage(prefix + ChatUtil.color(config.getString("RANKUP" + ".setrank-invalid-rank")));
            return true;
        }

        final PermissionUser user = PermissionsEx.getUser(target);
        executeCommands(user, group);

        user.setParentsIdentifier(setRank(user, group));
        user.save();

        cs.sendMessage(prefix + ChatUtil.color(config.getString("RANKUP" + ".setrank-success")
                .replace("{username}", target.getName())
                .replace("{rank}", group)));
        target.sendMessage(prefix + ChatUtil.color(config.getString("RANKUP" + ".setrank-received")
                .replace("{rank}", group)));

        return true;
    }

    private void executeCommands(PermissionUser user, String rank) {
        String old = getCurrentGroup(user);

        for(String cmd: config.getStringList("RANKUP" + ".execute-commands-on-setrank")) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd
                    .replace("{username}", user.getName())
                    .replace("{rank}", rank)
                    .replace("{oldrank}", old));
        }
    }

    private String getCurrentGroup(PermissionUser user) {
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

    private List<String> setRank(PermissionUser user, String group) {
        List<String> newParents = new ArrayList<>();
        List<String> parents = user.getParentIdentifiers();

        for(int i = 0; i < parents.size(); i++) {
            if(isValid(parents.get(i))) {
                newParents.add(group);
                continue;
            }
            newParents.add(parents.get(i));
        }
        return newParents;
    }

    private boolean isValid(String group) {
        for(String rank: config.getConfigurationSection("LADDER").getKeys(false)) {
            if(rank.equalsIgnoreCase(group)) {
                return true;
            }
        }
        return false;
    }
}