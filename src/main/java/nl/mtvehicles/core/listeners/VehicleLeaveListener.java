package nl.mtvehicles.core.listeners;

import nl.mtvehicles.core.Main;
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

        // Early return if not a player
        if (!(event.getEntity() instanceof Player)) return;
        player = (Player) event.getEntity();

        // Validate the entity is a vehicle
        if (!VehicleUtils.isVehicle(entity)) return;
        if (entity.getCustomName() == null || !entity.getCustomName().contains("MTVEHICLES_MAINSEAT_")) return;

        String license = VehicleUtils.getLicensePlate(entity);
        if (license == null || VehicleData.autostand.get("MTVEHICLES_MAIN_" + license) == null) {
            return;
        }

        // Create and call the API event
        this.setAPI(new VehicleLeaveEvent(license));
        callAPI();
        if (isCancelled()) return;

        try {
            // Remove bossbar first
            BossBarUtils.removeBossBar(player, license);

            // Turn off the vehicle - this now properly handles per-user database saving
            VehicleUtils.turnOff(license);
        } catch (Exception e) {
            // Log any errors but don't let them crash the server
            Main.logSevere("Error handling vehicle leave for " + license + ": " + e.getMessage());
            if (Main.instance.isEnabled()) { // Only print stacktrace if plugin is still enabled
                e.printStackTrace();
            }
        }
    }
}