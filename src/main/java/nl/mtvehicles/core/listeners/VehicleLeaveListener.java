package nl.mtvehicles.core.listeners;

import nl.mtvehicles.core.infrastructure.helpers.BossBarUtils;
import nl.mtvehicles.core.infrastructure.helpers.VehicleData;
import nl.mtvehicles.core.infrastructure.models.Vehicle;
import nl.mtvehicles.core.infrastructure.models.VehicleUtils;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.spigotmc.event.entity.EntityDismountEvent;

import java.util.List;
import java.util.Map;

public class VehicleLeaveListener implements Listener {

    @EventHandler
    public void onVehicleLeave(EntityDismountEvent e) {
        final Entity entity = e.getDismounted();
        if (!(e.getEntity() instanceof Player)) return;
        final Player player = (Player) e.getEntity();

        if (e.isCancelled()) return;

        if (!VehicleUtils.isVehicle(entity)) return;

        if (entity.getCustomName().contains("MTVEHICLES_MAINSEAT_")) {

            final String license = VehicleUtils.getLicensePlate(entity);
            if (VehicleData.autostand.get("MTVEHICLES_MAIN_" + license) == null) return;

            Vehicle vehicle = VehicleUtils.getByLicensePlate(license);
            if (vehicle.getVehicleType().isHelicopter()) {
                ArmorStand as4 = VehicleData.autostand.get("MTVEHICLES_WIEKENS_" + license);
                as4.setGravity(as4.getLocation().getBlock().getType().equals(Material.AIR));
            }
            BossBarUtils.removeBossBar(player, license);
            ArmorStand as = VehicleData.autostand.get("MTVEHICLES_MAIN_" + license);
            ArmorStand as2 = VehicleData.autostand.get("MTVEHICLES_SKIN_" + license);
            as.setGravity(true);
            as2.setGravity(true);
            List<Map<String, Integer>> seats = (List<Map<String, Integer>>) vehicle.getVehicleData().get("seats");
            for (int i = 2; i <= seats.size(); i++) {
                if (VehicleData.autostand.get("MTVEHICLES_SEAT" + i + "_" + license) != null)
                    VehicleData.autostand.get("MTVEHICLES_SEAT" + i + "_" + license).remove();
            }
            VehicleData.type.remove(license+"b");

            if (ConfigModule.defaultConfig.getConfig().getBoolean("benzine") && ConfigModule.vehicleDataConfig.getConfig().getBoolean("vehicle." + license + ".benzineEnabled")) {
                Double fuel = VehicleData.fuel.get(license);
                ConfigModule.vehicleDataConfig.getConfig().set(String.format("vehicle.%s.benzine", license), fuel);
                ConfigModule.vehicleDataConfig.save();
            }
        }
    }
}