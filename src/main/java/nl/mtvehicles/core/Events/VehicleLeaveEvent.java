package nl.mtvehicles.core.Events;

import nl.mtvehicles.core.Infrastructure.Helpers.BossBarUtils;
import nl.mtvehicles.core.Infrastructure.Helpers.VehicleData;
import nl.mtvehicles.core.Infrastructure.Models.Vehicle;
import org.bukkit.Material;
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
//            Main.vehicleDataConfig.getConfig().set("vehicle." + ken + ".benzine", VehicleData.benzine.get(ken));
//            Main.vehicleDataConfig.save();
            ArmorStand as = VehicleData.autostand.get("MTVEHICLES_MAIN_" + ken);
            ArmorStand as2 = VehicleData.autostand.get("MTVEHICLES_SKIN_" + ken);
            as.setGravity(true);
            as2.setGravity(true);
            List<Map<String, Integer>> seats = (List<Map<String, Integer>>) vehicle.getVehicleData().get("seats");
            for (int i = 2; i <= seats.size(); i++) {
                VehicleData.autostand.get("MTVEHICLES_SEAT" + i + "_" + ken).remove();
            }
        }
    }
}