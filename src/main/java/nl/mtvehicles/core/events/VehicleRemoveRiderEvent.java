package nl.mtvehicles.core.events;

import nl.mtvehicles.core.events.interfaces.CanEditLicensePlate;
import nl.mtvehicles.core.events.interfaces.IsCancellable;
import nl.mtvehicles.core.infrastructure.models.MTVEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

/**
 * On vehicle remove rider (/vehicle removerider command). Riders are players who may steer the vehicle.
 */
public class VehicleRemoveRiderEvent extends MTVEvent implements IsCancellable, Cancellable, CanEditLicensePlate {

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
     * Get the player who used /vehicle removerider command
     * @see #getPlayer()
     */
    public Player getRemover(){
        return super.getPlayer();
    }

    /**
     * Get the player who is being removed as a rider
     */
    public Player getRemoved(){
        return removedPlayer;
    }

    /**
     * Set the player who is being removed as a rider
     */
    public void setRemoved(Player player){
        this.removedPlayer = player;
    }

}
