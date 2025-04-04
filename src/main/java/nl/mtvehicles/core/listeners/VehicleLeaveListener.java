package nl.mtvehicles.core.listeners;

import nl.mtvehicles.core.events.VehicleLeaveEvent;
import nl.mtvehicles.core.infrastructure.models.MTVListener;
import nl.mtvehicles.core.infrastructure.utils.BossBarUtils;
import nl.mtvehicles.core.infrastructure.vehicle.VehicleData;
import nl.mtvehicles.core.infrastructure.vehicle.VehicleUtils;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.spigotmc.event.entity.EntityDismountEvent;

/**
 * On leave of a vehicle
 */
public class VehicleLeaveListener extends MTVListener {

    @EventHandler
    public void onVehicleLeave(EntityDismountEvent event) {
        this.event = event;
        final Entity entity = event.getDismounted();
        if (!(event.getEntity() instanceof Player)) return;
        player = (Player) event.getEntity();

        if (!VehicleUtils.isVehicle(entity)) return;
        if (!entity.getCustomName().contains("MTVEHICLES_MAINSEAT_")) return;
        String license = VehicleUtils.getLicensePlate(entity);
        if (VehicleData.autostand.get("MTVEHICLES_MAIN_" + license) == null) return;

        this.setAPI(new VehicleLeaveEvent(license));
        callAPI();
        if (isCancelled()) return;

        BossBarUtils.removeBossBar(player, license);
        VehicleUtils.turnOff(license);
    }
}
