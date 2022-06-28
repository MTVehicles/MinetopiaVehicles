package nl.mtvehicles.core.events;

import nl.mtvehicles.core.events.interfaces.HasVehicle;
import nl.mtvehicles.core.events.interfaces.IsCancellable;
import nl.mtvehicles.core.infrastructure.models.MTVEvent;
import nl.mtvehicles.core.infrastructure.vehicle.Vehicle;
import nl.mtvehicles.core.infrastructure.vehicle.VehicleUtils;
import org.bukkit.entity.Player;

/**
 * On vehicle remove member (/vehicle removemember command). Members are players who may sit in the vehicle.
 */
public class VehicleRemoveMemberEvent extends MTVEvent implements IsCancellable, HasVehicle {
    private String licensePlate;
    private Player removedPlayer;

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
     * Get the player who used /vehicle removemember command
     * @see #getPlayer()
     */
    public Player getRemover(){
        return super.getPlayer();
    }

    /**
     * Get the player who is being removed as a member
     */
    public Player getRemoved(){
        return removedPlayer;
    }

    /**
     * Set the player who is being removed as a member
     */
    public void setRemoved(Player player){
        this.removedPlayer = player;
    }

}
