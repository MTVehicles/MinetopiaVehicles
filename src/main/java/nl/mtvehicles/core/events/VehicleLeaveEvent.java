package nl.mtvehicles.core.events;

import nl.mtvehicles.core.events.interfaces.HasVehicle;
import nl.mtvehicles.core.events.interfaces.IsCancellable;
import nl.mtvehicles.core.infrastructure.models.MTVEvent;
import org.bukkit.event.Cancellable;

/**
 * On vehicle leave
 */
public class VehicleLeaveEvent extends MTVEvent implements IsCancellable, Cancellable, HasVehicle {
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
