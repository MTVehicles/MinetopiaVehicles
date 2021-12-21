package nl.mtvehicles.core.events;

import nl.mtvehicles.core.Main;
import nl.mtvehicles.core.infrastructure.helpers.BossBarUtils;
import nl.mtvehicles.core.infrastructure.helpers.TextUtils;
import nl.mtvehicles.core.infrastructure.helpers.VehicleData;
import nl.mtvehicles.core.infrastructure.models.Vehicle;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import org.bukkit.Material;
import org.bukkit.boss.BarColor;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.spigotmc.event.entity.EntityDismountEvent;

import java.util.List;
import java.util.Map;

public class VehicleLeaveEvent implements Listener {

    @EventHandler
    public void onVehicleLeave(EntityDismountEvent e) {
        if (e.getDismounted().getCustomName() == null) return;
        if (e.getDismounted().getCustomName().contains("MTVEHICLES_MAINSEAT_")) {
            String ken = e.getDismounted().getCustomName().replace("MTVEHICLES_MAINSEAT_", "");
            if (VehicleData.autostand.get("MTVEHICLES_MAIN_" + ken) == null) {
                return;
            }
            Vehicle vehicle = Vehicle.getByPlate(ken);
            if (vehicle.getVehicleType().contains("HELICOPTER")) {
                ArmorStand as4 = VehicleData.autostand.get("MTVEHICLES_WIEKENS_" + ken);
                if (!as4.getLocation().getBlock().getType().equals(Material.AIR)) {
                    as4.setGravity(false);
                } else {
                    as4.setGravity(true);
                }
            }
            BossBarUtils.removeBossBar((Player) e.getEntity(), ken);
            ArmorStand as = VehicleData.autostand.get("MTVEHICLES_MAIN_" + ken);
            ArmorStand as2 = VehicleData.autostand.get("MTVEHICLES_SKIN_" + ken);
            as.setGravity(true);
            as2.setGravity(true);
            List<Map<String, Integer>> seats = (List<Map<String, Integer>>) vehicle.getVehicleData().get("seats");
            for (int i = 2; i <= seats.size(); i++) {
                if (VehicleData.autostand.get("MTVEHICLES_SEAT" + i + "_" + ken) != null) {
                    VehicleData.autostand.get("MTVEHICLES_SEAT" + i + "_" + ken).remove();
                }
            }
            VehicleData.type.remove(ken+"b");

            if (ConfigModule.defaultConfig.getConfig().getBoolean("benzine") && ConfigModule.vehicleDataConfig.getConfig().getBoolean("vehicle." + ken + ".benzineEnabled")) {
                Double fuel = VehicleData.fuel.get(ken);
                ConfigModule.vehicleDataConfig.getConfig().set(String.format("vehicle.%s.benzine", ken), fuel);
                ConfigModule.vehicleDataConfig.save();
            }
        }
    }
}