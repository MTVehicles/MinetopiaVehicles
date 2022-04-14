package nl.mtvehicles.core.infrastructure.models;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public abstract class MTVEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    protected boolean cancelled = false;
    private Player player;

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    /**
     * Call this event (to other plugins)
     */
    public void call() {
        Bukkit.getPluginManager().callEvent(this);
    }

    /**
     * Check whether the event is cancelled.
     * If event isn't cancellable, always returns false
     *
     * @see org.bukkit.event.Cancellable
     */
    public boolean isCancelled() {
        return this.cancelled;
    }

    /**
     * Get player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Set player
     */
    public void setPlayer(Player player) {
        this.player = player;
    }
}
