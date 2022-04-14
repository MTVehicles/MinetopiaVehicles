package nl.mtvehicles.core.events;

import nl.mtvehicles.core.events.interfaces.HasLocation;
import nl.mtvehicles.core.events.interfaces.HasVehicle;
import nl.mtvehicles.core.infrastructure.models.MTVEvent;
import nl.mtvehicles.core.infrastructure.models.Vehicle;
import nl.mtvehicles.core.infrastructure.models.VehicleUtils;
import org.bukkit.Location;
import org.bukkit.event.Cancellable;

public class VehiclePlaceEvent extends MTVEvent implements Cancellable, HasLocation, HasVehicle {
    private Location location;
    private String licensePlate;

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getLicensePlate(){
        return licensePlate;
    }

    public Vehicle getVehicle(){
        return VehicleUtils.getByLicensePlate(licensePlate);
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }
}