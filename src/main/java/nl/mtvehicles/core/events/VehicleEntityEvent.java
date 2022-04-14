package nl.mtvehicles.core.events;

import nl.mtvehicles.core.events.interfaces.HasVehicle;
import nl.mtvehicles.core.infrastructure.models.MTVEvent;
import nl.mtvehicles.core.infrastructure.models.Vehicle;
import nl.mtvehicles.core.infrastructure.models.VehicleUtils;
import org.bukkit.event.Cancellable;

public class VehicleEntityEvent extends MTVEvent implements Cancellable, HasVehicle {
    private String licensePlate;

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
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
