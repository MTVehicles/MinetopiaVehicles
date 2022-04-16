package nl.mtvehicles.core.events;

import nl.mtvehicles.core.events.interfaces.HasVehicle;
import nl.mtvehicles.core.infrastructure.models.MTVEvent;
import nl.mtvehicles.core.infrastructure.models.Vehicle;
import nl.mtvehicles.core.infrastructure.models.VehicleUtils;
import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;

public class VehicleDamageEvent extends MTVEvent implements Cancellable, HasVehicle {
    private Entity damager;
    private String licensePlate;

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public String getLicensePlate(){
        return licensePlate;
    }

    public Vehicle getVehicle(){
        return VehicleUtils.getByLicensePlate(licensePlate);
    }

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
}
