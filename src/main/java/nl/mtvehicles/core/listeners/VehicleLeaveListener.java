package nl.mtvehicles.core.listeners;

import nl.mtvehicles.core.events.VehicleLeaveEvent;
import nl.mtvehicles.core.infrastructure.dataconfig.DefaultConfig;
import nl.mtvehicles.core.infrastructure.dataconfig.VehicleDataConfig;
import nl.mtvehicles.core.infrastructure.utils.BossBarUtils;
import nl.mtvehicles.core.infrastructure.vehicle.VehicleData;
import nl.mtvehicles.core.infrastructure.models.MTVListener;
import nl.mtvehicles.core.infrastructure.vehicle.Vehicle;
import nl.mtvehicles.core.infrastructure.vehicle.VehicleUtils;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.spigotmc.event.entity.EntityDismountEvent;

import java.util.List;
import java.util.Map;

/**
 * On leave of a vehicle
 */
public class VehicleLeaveListener extends MTVListener {

    public VehicleLeaveListener(){
        super(new VehicleLeaveEvent());
    }

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

        VehicleLeaveEvent api = (VehicleLeaveEvent) getAPI();
        api.setLicensePlate(license);
        callAPI();
        if (isCancelled()) return;

        license = api.getLicensePlate();

        BossBarUtils.removeBossBar(player, license);
        VehicleUtils.turnOff(license);
    }
}
