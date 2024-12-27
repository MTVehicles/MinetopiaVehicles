package nl.mtvehicles.core.events;

import nl.mtvehicles.core.events.interfaces.HasVehicle;
import nl.mtvehicles.core.events.interfaces.IsCancellable;
import nl.mtvehicles.core.infrastructure.models.MTVEvent;
import org.bukkit.Location;
import org.bukkit.event.Cancellable;

/**
 * On vehicle enter
 */
public class VehicleEnterEvent extends MTVEvent implements IsCancellable, Cancellable, HasVehicle {
    final private String licensePlate;
    final private Location location;

    public VehicleEnterEvent(String licensePlate, Location location){
        this.licensePlate = licensePlate;
        this.location = location;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public String getLicensePlate(){
        return licensePlate;
    }

    /**
     * Get the location of vehicle
     * @return Vehicle location
     */
    public Location getLocation() {
        return location;
    }

}
