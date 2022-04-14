package nl.mtvehicles.core.events;

import nl.mtvehicles.core.Main;
import nl.mtvehicles.core.infrastructure.annotations.ToDo;
import nl.mtvehicles.core.infrastructure.models.MTVEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@ToDo(comment = "Should be commented and better prepared for API usage.")
public class VehiclePlaceEvent extends MTVEvent {
    private Location location;
    private Player player;

    public Main getPlugin() {
        return Main.instance;
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