package nl.mtvehicles.core.listeners;

import nl.mtvehicles.core.events.VehicleDamageEvent;
import nl.mtvehicles.core.infrastructure.models.MTVListener;
import nl.mtvehicles.core.infrastructure.vehicle.VehicleUtils;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

import java.util.Collection;

/**
 * Listener for vehicle damage caused by entities or environmental effects.
 * @since 2.5.7
 */
public class VehicleOtherDamageListener extends MTVListener {

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

        EntityDamageEvent.DamageCause damageCause = event.getCause();
        if (damageCause != EntityDamageEvent.DamageCause.FIRE
                && damageCause != EntityDamageEvent.DamageCause.LAVA
                && damageCause != EntityDamageEvent.DamageCause.PROJECTILE
        )
            return;

        VehicleDamageEvent api = (VehicleDamageEvent) getAPI();
        api.setDamage(event.getDamage());
        api.setDamageCause(damageCause);
        api.setLicensePlate(license);
        callAPI();
        if (isCancelled()) return;

        VehicleEntityListener.damage(api.getLicensePlate(), Double.valueOf(api.getDamage()));
    }

    @EventHandler
    public void onBlockExplode(BlockExplodeEvent event){
        this.event = event;
        //I'm using World#getNearbyEntities instead of Location#getNearbyEntities because the latter is not supported in earlier versions
        Collection<Entity> nearbyEntities = event.getBlock().getLocation().getWorld().getNearbyEntities(event.getBlock().getLocation(), 5, 5, 5);
        damageNearbyVehicles(nearbyEntities, 40.0, EntityDamageEvent.DamageCause.BLOCK_EXPLOSION);
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        this.event = event;
        Collection<Entity> nearbyEntities = event.getEntity().getNearbyEntities(5, 5, 5);
        if (event.getEntity().getType().equals(EntityType.WITHER)) damageNearbyVehicles(nearbyEntities, 80.0, EntityDamageEvent.DamageCause.ENTITY_EXPLOSION);
        else damageNearbyVehicles(nearbyEntities, 40.0, EntityDamageEvent.DamageCause.ENTITY_EXPLOSION);
    }

    private void damageNearbyVehicles(Collection<Entity> entities, double damage, EntityDamageEvent.DamageCause cause) {
        for (Entity entity : entities) {
            if (VehicleUtils.isVehicle(entity)) {
                String license = VehicleUtils.getLicensePlate(entity);
                if (license == null) continue;

                VehicleDamageEvent api = (VehicleDamageEvent) getAPI();
                api.setDamage(damage);
                api.setDamageCause(cause);
                api.setLicensePlate(license);
                callAPI();
                if (!isCancelled()) VehicleEntityListener.damage(api.getLicensePlate(), Double.valueOf(api.getDamage()));
            }
        }
    }

}
