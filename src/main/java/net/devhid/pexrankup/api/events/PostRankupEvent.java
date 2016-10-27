package net.devhid.pexrankup.api.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public final class PostRankupEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    private final Player player;
    private final String newRank;

    private double newBalance;

    public PostRankupEvent(Player player, String newRank, double newBalance) {
        this.player = player;
        this.newRank = newRank;
        this.newBalance = newBalance;
    }

    public Player getPlayer() {
        return player;
    }

    public String getNewRank() {
        return newRank;
    }

    public double getNewBalance() {
        return newBalance;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
