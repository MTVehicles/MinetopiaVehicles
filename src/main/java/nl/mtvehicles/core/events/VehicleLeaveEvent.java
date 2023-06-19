package nl.mtvehicles.core.events;

import nl.mtvehicles.core.events.interfaces.HasVehicle;
import nl.mtvehicles.core.events.interfaces.IsCancellable;
import nl.mtvehicles.core.infrastructure.models.MTVEvent;
import nl.mtvehicles.core.infrastructure.vehicle.Vehicle;
import nl.mtvehicles.core.infrastructure.vehicle.VehicleUtils;

/**
 * On vehicle leave
 */
public class VehicleLeaveEvent extends MTVEvent implements IsCancellable, HasVehicle {
    final private String licensePlate;

    public VehicleLeaveEvent(String licensePlate){
        this.licensePlate = licensePlate;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public String getLicensePlate(){
        return licensePlate;
    }

}
