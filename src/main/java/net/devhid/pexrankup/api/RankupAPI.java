package net.devhid.pexrankup.api;

import net.devhid.pexrankup.RankupManager;
import net.devhid.pexrankup.RankupPlugin;
import org.bukkit.entity.Player;
import ru.tehkode.permissions.PermissionUser;

import java.math.BigDecimal;

public class RankupAPI {
    private final RankupManager rankupManager;

    public RankupAPI() {
        this.rankupManager = new RankupManager(RankupPlugin.getPlugin());
    }

    /**
     * Returns the balance of the specified player.
     *
     * @param player the Player whose balance is being grabbed.
     * @return       the balance of the user in BigDecimal representation.
     **/
    public BigDecimal getBalance(Player player) {
        return rankupManager.getBalance(player);
    }

    /**
     * Returns the specified user's current PEX group.
     *
     * @param user the PermissionUser whose group is being grabbed.
     * @return     the user's current PEX group name in String representation.
     **/
    public String getCurrentGroup(PermissionUser user) {
        return rankupManager.getCurrentGroup(user);
    }

    /**
     * Returns the specified user's next rank in the rankup ladder.
     *
     * @param user the PermissionUser whose next rank is being grabbed.
     * @return     the user's next rank name in the ladder in String representation.
     **/
    public String getNextRank(PermissionUser user) {
        return rankupManager.getNextRank(user);
    }

    /**
     * Returns the money required to rank up to the next rank.
     *
     * @param user the PermissionUser whose remaining rank price is being calculated.
     * @return     the difference between the next rank's cost and the user's balance in BigDecimal representation.
     **/
    public BigDecimal getRemainingPrice(PermissionUser user) {
        return rankupManager.getRemainingPrice(user);
    }

    /**
     * Returns the money required to rank up to the next rank.
     * Formatted version adds commas and removes scientific notation.
     *
     * @param user the PermissionUser whose remaining rank price is being calculated.
     * @return     the difference between the next rank's cost and the user's balance in formatted String representation.
     **/
    public String getRemainingPriceFormatted(PermissionUser user) {
        return rankupManager.getRemainingPriceFormatted(user);
    }

    /**
     * Returns the cost of the specified user's next rank in the rankup ladder.
     *
     * @param user the PermissionUser whose next rank cost is being grabbed.
     * @return     the cost of the user's next rank in the ladder in BigDecimal representation.
     **/
    public BigDecimal getCostOfNextRank(PermissionUser user) {
        return rankupManager.getCostOfNextRank(user);
    }

    /**
     * Returns the cost of the specified user's next rank in the rankup ladder.
     *
     * @param user the PermissionUser whose next rank cost is being grabbed.
     * @return     the cost of the user's next rank in the ladder in String representation.
     **/
    public String getCostOfNextRankString(PermissionUser user) {
        return rankupManager.getCostOfNextRankString(user);
    }

    /**
     * Returns the cost of the specified user's next rank in the rankup ladder.
     * The formatted cost will be separated by commas.
     *
     * @param user the PermissionUser whose next rank cost is being grabbed.
     * @return     the cost of the user's next rank in the ladder in a formatted String representation.
     **/
    public String getCostOfNextRankFormatted(PermissionUser user) {
        return rankupManager.getCostOfNextRankFormatted(user);
    }
}