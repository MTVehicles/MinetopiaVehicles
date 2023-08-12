package nl.mtvehicles.core.events;

import nl.mtvehicles.core.events.interfaces.HasVehicle;
import nl.mtvehicles.core.infrastructure.models.MTVEvent;
import org.bukkit.event.Cancellable;

/**
 * On region enter
 */
public class VehicleRegionEnterEvent extends MTVEvent implements Cancellable, HasVehicle {
    final private String licensePlate;
    final private String regionName;

    public VehicleRegionEnterEvent(String licensePlate, String regionName){
        this.licensePlate = licensePlate;
        this.regionName = regionName;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public String getLicensePlate(){
        return licensePlate;
    }

    public String getRegionName(){
        return regionName;
    }

}
