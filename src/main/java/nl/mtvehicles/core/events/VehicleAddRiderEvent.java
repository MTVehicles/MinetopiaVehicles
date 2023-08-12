package nl.mtvehicles.core.events;

import nl.mtvehicles.core.events.interfaces.HasVehicle;
import nl.mtvehicles.core.events.interfaces.IsCancellable;
import nl.mtvehicles.core.infrastructure.models.MTVEvent;
import nl.mtvehicles.core.infrastructure.vehicle.Vehicle;
import nl.mtvehicles.core.infrastructure.vehicle.VehicleUtils;
import org.bukkit.entity.Player;

/**
 * On vehicle add rider (/vehicle addrider command). Riders are players who may steer the vehicle.
 */
public class VehicleAddRiderEvent extends MTVEvent implements IsCancellable, HasVehicle {
    private String licensePlate;
    private Player addedPlayer;

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

    /**
     * Get the player who used /vehicle addrider command
     * @see #getPlayer()
     */
    public Player getAdder(){
        return super.getPlayer();
    }

    /**
     * Get the player who is being added as a rider
     */
    public Player getAdded(){
        return addedPlayer;
    }

    /**
     * Set the player who is being added as a rider
     */
    public void setAdded(Player player){
        this.addedPlayer = player;
    }

}
