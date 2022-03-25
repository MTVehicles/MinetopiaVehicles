package nl.mtvehicles.core.events;

import nl.mtvehicles.core.Main;
import nl.mtvehicles.core.infrastructure.annotations.ToDo;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@ToDo(comment = "Should be commented and better prepared for API usage.")
public class VehiclePlaceEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private Boolean cancelled = false;
    private Location location;
    private Player player;

    public Main getPlugin() {
        return Main.instance;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public static void call() {
        Bukkit.getPluginManager().callEvent(new VehiclePlaceEvent());
    }

    public Boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(Boolean cancelled) {
        this.cancelled = cancelled;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}