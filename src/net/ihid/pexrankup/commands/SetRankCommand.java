package net.ihid.pexrankup.commands;

import net.ihid.pexrankup.RankupPlugin;
import net.ihid.pexrankup.util.ChatUtil;
import net.ihid.pexrankup.util.CmdUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Mikey on 5/23/2016.
 */
public class SetRankCommand implements CommandExecutor {
    private RankupPlugin plugin;

    private YamlConfiguration config;

    public SetRankCommand(RankupPlugin instance) {
        plugin = instance;
        config = instance.getConfig();
    }

    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        String prefix = (config.getBoolean("MAIN" + ".prefix-enabled")) ? ChatUtil.color(config.getString("MAIN" + ".prefix")) : "";

        Player target = Bukkit.getPlayer(args[0]);

        try {
            CmdUtil.checkPerm(cs, "pexrankup.setrank");
            CmdUtil.checkArgs(args, 2);
            CmdUtil.checkOnline(target);
        } catch(CmdUtil.CommandException ex) {
            cs.sendMessage(prefix + ex.getMessage());
            return true;
        }

        String group = args[1];

        if(!isValid(group)) {
            cs.sendMessage(prefix + ChatUtil.color(config.getString("RANKUP" + ".setrank-invalid-rank")));
            return true;
        }

        final PermissionUser user = PermissionsEx.getUser((Player) cs);
        user.setParentsIdentifier(setRank(user, group));

        return true;
    }

    private List<String> setRank(PermissionUser user, String group) {
        List<String> newParents = new ArrayList<>();
        final List<String> parents = user.getParentIdentifiers();

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
