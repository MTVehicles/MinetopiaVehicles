package nl.mtvehicles.core.Events;

import nl.mtvehicles.core.Infrastructure.Helpers.BossbarUtils;
import nl.mtvehicles.core.Infrastructure.Models.Vehicle;
import nl.mtvehicles.core.Main;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.spigotmc.event.entity.EntityDismountEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VehicleLeaveEvent implements Listener {
    public static HashMap<String, ArmorStand> autostand = new HashMap<>();
    public static HashMap<String, ArmorStand> autostand2 = new HashMap<>();

    @EventHandler
    public void onVehiclePlace(EntityDismountEvent e) {
        if (e.getDismounted().getCustomName() == null) return;
        if (e.getDismounted().getCustomName().contains("MTVEHICLES_MAINSEAT_")) {
            String ken = e.getDismounted().getCustomName().replace("MTVEHICLES_MAINSEAT_", "");
            if (VehicleLeaveEvent.autostand.get("MTVEHICLES_MAIN_" + ken) == null) {
                return;
            }
            if (Main.vehicleDataConfig.getConfig().getString("vehicle." + ken + ".vehicleType").contains("HELICOPTER")) {
                ArmorStand as4 = VehicleLeaveEvent.autostand.get("MTVEHICLES_WIEKENS_" + ken);
                if (!(as4 == null)) {
                    //as4.getHelmet().setType(VehiclesUtils.mItem("AIR", 1, (short) 1, " ", " "));
                }
                if (!as4.getLocation().getBlock().getType().equals(Material.AIR)) {
                    as4.setGravity(false);
                } else {
                    as4.setGravity(true);
                }
            }
            BossbarUtils.removeBossbar((Player) e.getEntity(), ken);
            Main.vehicleDataConfig.getConfig().set("vehicle." + ken + ".benzine", VehicleClickEvent.benzine.get(ken));
            Main.vehicleDataConfig.save();
            ArmorStand as = VehicleLeaveEvent.autostand.get("MTVEHICLES_MAIN_" + ken);
            ArmorStand as2 = VehicleLeaveEvent.autostand.get("MTVEHICLES_SKIN_" + ken);
            as.setGravity(true);
            as2.setGravity(true);
            Vehicle vehicle = Vehicle.getByPlate(ken);
            List<Map<String, Integer>> seats = (List<Map<String, Integer>>) vehicle.getVehicleData().get("seats");
            for (int i = 2; i <= seats.size(); i++) {
                autostand.get("MTVEHICLES_SEAT" + i + "_" + ken).remove();
            }
        }
    }
}