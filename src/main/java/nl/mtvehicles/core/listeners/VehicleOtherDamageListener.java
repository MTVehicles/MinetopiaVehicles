package nl.mtvehicles.core.listeners;

import nl.mtvehicles.core.events.VehicleDamageEvent;
import nl.mtvehicles.core.infrastructure.models.MTVListener;
import nl.mtvehicles.core.infrastructure.vehicle.VehicleUtils;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;

/**
 * On vehicle damage - handles events such as explosions / arrows, which damage the vehicle but are not caused by a player.
 * Damaging the vehicle by a player (e.g. by hitting it with a sword) is handled in {@link nl.mtvehicles.core.listeners.VehicleEntityListener}.
 *
 * @since 2.5.7
 */
public class VehicleOtherDamageListener extends MTVListener {

    private EntityDamageEvent.DamageCause damageCause;

    public VehicleOtherDamageListener(){
        super(new VehicleDamageEvent());
    }

    @EventHandler
    public void onVehicleDamage(EntityDamageEvent event) {
        this.event = event;
        final Entity victim = event.getEntity();

        if (!VehicleUtils.isVehicle(victim)) return;

        String license = VehicleUtils.getLicensePlate(victim);
        if (license == null) return;

        this.damageCause = event.getCause();
        if (damageCause != EntityDamageEvent.DamageCause.BLOCK_EXPLOSION
                && damageCause != EntityDamageEvent.DamageCause.ENTITY_EXPLOSION
                && damageCause != EntityDamageEvent.DamageCause.FIRE
                && damageCause != EntityDamageEvent.DamageCause.LAVA
                && damageCause != EntityDamageEvent.DamageCause.LIGHTNING
                && damageCause != EntityDamageEvent.DamageCause.PROJECTILE
        )
            return;

        VehicleDamageEvent api = (VehicleDamageEvent) getAPI();
        api.setDamage(event.getDamage());
        api.setDamageCause(damageCause);
        api.setLicensePlate(license);
        callAPI();
        if (isCancelled()) return;

        VehicleEntityListener.damage(api.getLicensePlate(), api.getDamage());
    }
}
