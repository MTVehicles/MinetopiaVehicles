package nl.mtvehicles.core.events.interfaces;

import nl.mtvehicles.core.infrastructure.vehicle.Vehicle;
import nl.mtvehicles.core.infrastructure.vehicle.VehicleUtils;

/**
 * Interface for all events with vehicles
 */
public interface HasVehicle {

    /**
     * Get license plate of the vehicle used in the event
     * @return Vehicle's license plate
     */
    public String getLicensePlate();

    /**
     * Get vehicle used in the event
     * @return Vehicle
     */
    default public Vehicle getVehicle(){
        return VehicleUtils.getVehicle(getLicensePlate());
    }

    /**
     * Set the license plate of the vehicle used in the event
     * @param licensePlate New license plate
     * @warning This method should be used cautiously, it may cause severe damage if not used correctly.
     */
    public void setLicensePlate(String licensePlate);

}
