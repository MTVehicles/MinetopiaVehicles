package nl.mtvehicles.core.events;

import nl.mtvehicles.core.events.interfaces.HasJerryCan;
import nl.mtvehicles.core.events.interfaces.HasVehicle;
import nl.mtvehicles.core.events.interfaces.IsCancellable;
import nl.mtvehicles.core.infrastructure.models.MTVEvent;
import nl.mtvehicles.core.infrastructure.vehicle.Vehicle;
import nl.mtvehicles.core.infrastructure.vehicle.VehicleUtils;

/**
 * On vehicle refuel
 */
public class VehicleFuelEvent extends MTVEvent implements IsCancellable, HasVehicle, HasJerryCan {
    final private double vehicleFuel;
    final private int jerryCanFuel;
    final private int jerryCanSize;

    private String licensePlate;

    public VehicleFuelEvent(double vehicleFuel, int jerryCanFuel, int jerryCanSize){
        this.vehicleFuel = vehicleFuel;
        this.jerryCanFuel = jerryCanFuel;
        this.jerryCanSize = jerryCanSize;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public String getLicensePlate(){
        return licensePlate;
    }

    @Override
    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    @Override
    public int getJerryCanFuel() {
        return jerryCanFuel;
    }

    @Override
    public int getJerryCanSize() {
        return jerryCanSize;
    }

    /**
     * Get the current fuel of the vehicle (before the event is executed)
     * @return Fuel of the vehicle
     */
    public double getVehicleFuel() {
        return vehicleFuel;
    }
}
