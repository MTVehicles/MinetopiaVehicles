package nl.mtvehicles.core.infrastructure.models;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public abstract class MTVEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled = false;

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    /**
     * Call this event
     */
    public void call() {
        Bukkit.getPluginManager().callEvent(this);
    }

    /**
     * Check whether the event is cancelled
     */
    public boolean isCancelled() {
        return this.cancelled;
    }

    /**
     * Set whether the event is cancelled
     * @param cancelled
     */
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
