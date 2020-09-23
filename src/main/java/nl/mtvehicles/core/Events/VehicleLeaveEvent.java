package nl.mtvehicles.core.Events;

import nl.mtvehicles.core.Infrastructure.Models.Vehicle;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
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
    public void onVehiclePlace(final EntityDismountEvent e) {
        if (e.getDismounted().getCustomName().contains("MTVEHICLES_MAINSEAT_")) {
            final String ken = e.getDismounted().getCustomName().replace("MTVEHICLES_MAINSEAT_", "");
            if (VehicleLeaveEvent.autostand.get("MTVEHICLES_MAIN_"+ken) == null){
                return;

            }
            ArmorStand as = VehicleLeaveEvent.autostand.get("MTVEHICLES_MAIN_"+ken);
            ArmorStand as2 = VehicleLeaveEvent.autostand.get("MTVEHICLES_SKIN_"+ken);
            as.setGravity(true);
            as2.setGravity(true);
            Vehicle vehicle = Vehicle.getByPlate(ken);
            List<Map<String, Integer>> seats = (List<Map<String, Integer>>) vehicle.getVehicleData().get("seats");
            for (int i = 1; i <= seats.size(); i++) {
                Map<String, Integer> seat = seats.get(i - 1);
                if (i > 1) {
                    autostand.get("MTVEHICLES_SEAT" + i + "_" + ken).remove();

                }
            }
        }
    }
}