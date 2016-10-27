package net.devhid.pexrankup.api.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public final class PreRankupEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;

    private final Player player;
    private final String currentRank, nextRank;

    private double balance, rankupCost;

    public PreRankupEvent(Player player, String currentRank, String nextRank, double balance, double rankupCost) {
        this.player = player;
        this.currentRank = currentRank;
        this.nextRank = nextRank;
        this.balance = balance;
        this.rankupCost = rankupCost;
    }

    public Player getPlayer() {
        return player;
    }

    public String getCurrentRank() {
        return currentRank;
    }

    public String getNextRank() {
        return nextRank;
    }

    public double getBalance() {
        return balance;
    }

    public double getRankupCost() {
        return rankupCost;
    }

    public void setRankupCost(double cost) {
        rankupCost = cost;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }
}
