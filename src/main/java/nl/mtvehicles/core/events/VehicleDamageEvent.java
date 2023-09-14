package nl.mtvehicles.core.events;

import nl.mtvehicles.core.events.interfaces.CanEditLicensePlate;
import nl.mtvehicles.core.events.interfaces.IsCancellable;
import nl.mtvehicles.core.infrastructure.models.MTVEvent;
import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;

/**
 * On vehicle damage
 */
public class VehicleDamageEvent extends MTVEvent implements IsCancellable, Cancellable, CanEditLicensePlate {

    private Entity damager;
    private double damage;
    private String licensePlate;

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
     * Get the entity that has damaged the vehicle
     * @return Damager
     */
    public Entity getDamager() {
        return damager;
    }

    /**
     * Set the entity that has damaged the vehicle
     * @param damager New damager
     */
    public void setDamager(Entity damager) {
        this.damager = damager;
    }

    /**
     * Get the damage dealt to the vehicle
     * @return Damage
     */
    public double getDamage() {
        return damage;
    }

    /**
     * Set new damage dealt to the vehicle
     * @param damage New damage
     */
    public void setDamage(double damage) {
        this.damage = damage;
    }
}
