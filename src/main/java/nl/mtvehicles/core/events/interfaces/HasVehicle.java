package nl.mtvehicles.core.events.interfaces;

import nl.mtvehicles.core.infrastructure.models.Vehicle;

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
    public Vehicle getVehicle();

    /**
     * Set the license plate of the vehicle used in the event
     * @param licensePlate New license plate
     */
    public void setLicensePlate(String licensePlate);

}
