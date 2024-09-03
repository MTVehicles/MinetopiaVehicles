package nl.mtvehicles.core.events;

import nl.mtvehicles.core.events.interfaces.HasVehicle;
import nl.mtvehicles.core.events.interfaces.IsCancellable;
import nl.mtvehicles.core.infrastructure.models.MTVEvent;
import nl.mtvehicles.core.infrastructure.vehicle.Vehicle;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

public class VehicleMoveEvent extends MTVEvent implements IsCancellable, Cancellable, HasVehicle {
    final private String licensePlate;
    private final Location moveFrom;
    private final Location moveTo;


    public VehicleMoveEvent(String licensePlate, Location moveFrom, Location moveTo){
        this.licensePlate = licensePlate;
        this.moveFrom = moveFrom;
        this.moveTo = moveTo;
    }

    @Override
    public String getLicensePlate() {
        return licensePlate;
    }

    @Override
    public void setCancelled(boolean cancelled) {

    }

    public Location getMoveFrom() {
        return moveFrom;
    }

    public Location getMoveTo() {
        return moveTo;
    }



}
