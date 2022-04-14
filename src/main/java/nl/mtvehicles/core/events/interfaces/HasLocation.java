package nl.mtvehicles.core.events.interfaces;

import org.bukkit.Location;

public interface HasLocation {

    /**
     * Get the location used in the event
     * @return Event location
     */
    public Location getLocation();

    /**
     * Set the location used in the event
     * @param location New event location
     */
    public void setLocation(Location location);

}
