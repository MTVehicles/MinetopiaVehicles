package nl.mtvehicles.core.Events;

import nl.mtvehicles.core.Infrastructure.Models.Vehicle;
import nl.mtvehicles.core.Main;
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
        if (e.getDismounted().getCustomName() == null) return;

        if (e.getDismounted().getCustomName().contains("MTVEHICLES_MAINSEAT_")) {
            final String ken = e.getDismounted().getCustomName().replace("MTVEHICLES_MAINSEAT_", "");
            if (VehicleLeaveEvent.autostand.get("MTVEHICLES_MAIN_" + ken) == null) {
                return;

            }

            if (Main.vehicleDataConfig.getConfig().getString("vehicle." + ken + ".vehicleType").contains("HELICOPTER")) {
                ArmorStand as4 = VehicleLeaveEvent.autostand.get("MTVEHICLES_WIEKENS_" + ken);
                if (!(as4 == null)) {
                    //as4.getHelmet().setType(VehiclesUtils.mItem("AIR", 1, (short) 1, " ", " "));
                }
            }

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

//    @EventHandler
//    public void onInventoryClose(InventoryCloseEvent event) {
//        if (event.getView().getTitle().contains("Kofferbak")){
//            String ken = "AB-AB-AB";
//
//            List<ItemStack> chestContentsFromConfig = (List<ItemStack>) Main.vehicleDataConfig.getConfig().getList("vehicle." +ken+ "dik"); //I've also tried just get("players." + key);
//hestRepopulate.addItem(item);
//            }
//
//        }
//    }
}