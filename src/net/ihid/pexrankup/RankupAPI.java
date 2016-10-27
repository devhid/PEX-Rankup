package net.ihid.pexrankup;

import net.ihid.pexrankup.commands.RankupCommand;
import org.bukkit.entity.Player;
import ru.tehkode.permissions.PermissionUser;
import java.math.BigDecimal;

public class RankupAPI {
    private final RankupCommand rankupCommand;
    
    public RankupAPI() {
        this.rankupCommand = new RankupCommand(RankupPlugin.getPlugin());
    }

    /**
     * Returns the balance of the specified player.
     *
     * @param player the Player whose balance is being grabbed.
     * @return       the balance of the user in BigDecimal representation.
     **/
    public BigDecimal getBalance(Player player) {
        return rankupCommand.getBalance(player);
    }

    /**
     * Returns the specified user's current PEX group.
     *
     * @param user the PermissionUser whose group is being grabbed.
     * @return     the user's current PEX group name in String representation.
     **/
    public String getCurrentGroup(PermissionUser user) {
        return rankupCommand.getCurrentGroup(user);
    }

    /**
     * Returns the specified user's next rank in the rankup ladder.
     *
     * @param user the PermissionUser whose next rank is being grabbed.
     * @return     the user's next rank name in the ladder in String representation.
     **/
    public String getNextRank(PermissionUser user) {
        return rankupCommand.getNextRank(user);
    }

    /**
     * Returns the cost of the specified user's next rank in the rankup ladder.
     *
     * @param user the PermissionUser whose next rank cost is being grabbed.
     * @return     the cost of the user's next rank in the ladder in BigDecimal representation.
     **/
    public BigDecimal getCostOfNextRank(PermissionUser user) {
        return rankupCommand.getCostOfNextRank(user);
    }

    /**
     * Returns the cost of the specified user's next rank in the rankup ladder.
     *
     * @param user the PermissionUser whose next rank cost is being grabbed.
     * @return     the cost of the user's next rank in the ladder in String representation.
     **/
    public String getCostOfNextRankString(PermissionUser user) {
        return rankupCommand.getCostOfNextRankString(user);
    }

    /**
     * Returns the cost of the specified user's next rank in the rankup ladder.
     * The formatted cost will be separated by commas.
     *
     * @param user the PermissionUser whose next rank cost is being grabbed.
     * @return     the cost of the user's next rank in the ladder in a formatted String representation.
     **/
    public String getCostOfNextRankFormatted(PermissionUser user) {
        return rankupCommand.getCostOfNextRankFormatted(user);
    }
}
