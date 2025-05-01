package nl.mtvehicles.core.events.interfaces;

/**
 * Interface for all events with vehicles where you may edit license plate within API
 * @see HasVehicle
 */
public interface CanEditLicensePlate extends HasVehicle {

    /**
     * Set the license plate of the vehicle used in the event
     * @param licensePlate New license plate
     * @warn This method should be used cautiously, it may cause severe damage if not used correctly.
     */
    public void setLicensePlate(String licensePlate);

}
