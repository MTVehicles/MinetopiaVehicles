package nl.mtvehicles.core.events;

import lombok.Getter;
import lombok.Setter;
import nl.mtvehicles.core.events.interfaces.CanEditLicensePlate;
import nl.mtvehicles.core.events.interfaces.IsCancellable;
import nl.mtvehicles.core.infrastructure.models.MTVEvent;
import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.entity.EntityDamageEvent;

import javax.annotation.Nullable;

/**
 * On vehicle damage
 */
public class VehicleDamageEvent extends MTVEvent implements IsCancellable, Cancellable, CanEditLicensePlate {

    private @Nullable Entity damager;
    /**
     * Amount of the damage dealt to the vehicle
     */
    @Setter
    @Getter
    private double damage;
    private String licensePlate;

    @Setter
    @Getter
    /**
     * The cause of the damage dealt to the vehicle
     * @since 2.5.7
     */
    private EntityDamageEvent.DamageCause damageCause;

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
     * @return Damager (null if not caused by an entity)
     */
    public @Nullable Entity getDamager() {
        return damager;
    }

    /**
     * Set the entity that has damaged the vehicle
     * @param damager New damager
     */
    public void setDamager(@Nullable Entity damager) {
        this.damager = damager;
    }

}
