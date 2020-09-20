package nl.mtvehicles.core.Events;

import nl.mtvehicles.core.Infrastructure.Models.Config;
import nl.mtvehicles.core.Infrastructure.Models.Vehicle;
import nl.mtvehicles.core.Main;
import nl.mtvehicles.core.Movement.VehicleMovement;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VehicleClickEvent implements Listener {


    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerInteractAtEntity(final PlayerInteractAtEntityEvent event) {
        final Entity a = event.getRightClicked();
        final Player p = event.getPlayer();
        if (a.getCustomName() == null) {
            return;
        }
        if (p.isSneaking()){
            if (event.getRightClicked().getCustomName().contains("MTVEHICLES_MAINSEAT_")) {
                getShitOppakVehicles(event.getRightClicked().getCustomName().replace("MTVEHICLES_MAINSEAT_", ""), p);
            }
            if (event.getRightClicked().getCustomName().contains("MTVEHICLES_MAIN_")) {
                getShitOppakVehicles(event.getRightClicked().getCustomName().replace("MTVEHICLES_MAIN_", ""), p);
            }
            if (event.getRightClicked().getCustomName().contains("MTVEHICLES_SKIN_")) {
                getShitOppakVehicles(event.getRightClicked().getCustomName().replace("MTVEHICLES_SKIN_", ""), p);
            }
            return;
        }
        Main.configList.forEach(Config::reload);
        if (event.getRightClicked().getCustomName().contains("MTVEHICLES_MAINSEAT_")) {
            getShitVehicles(event.getRightClicked().getCustomName().replace("MTVEHICLES_MAINSEAT_", ""), p);
        }
        if (event.getRightClicked().getCustomName().contains("MTVEHICLES_MAIN_")) {
            getShitVehicles(event.getRightClicked().getCustomName().replace("MTVEHICLES_MAIN_", ""), p);
        }
        if (event.getRightClicked().getCustomName().contains("MTVEHICLES_SKIN_")) {
            getShitVehicles(event.getRightClicked().getCustomName().replace("MTVEHICLES_SKIN_", ""), p);
        }

    }

    public void getShitVehicles(final String ken, final Player p) {
        for (final World world : Bukkit.getServer().getWorlds()) {
            for (final Entity entity : world.getEntities()) {
                if (entity.getCustomName() != null && entity.getCustomName().contains(ken)) {
                    final ArmorStand test = (ArmorStand) entity;
                    if (test.getCustomName().contains("MTVEHICLES_SKIN_" + ken)) {
                        Location loc = test.getLocation();
                        Location location = new Location(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
                        ArmorStand as = location.getWorld().spawn(location, ArmorStand.class);
                        as.setCustomName("MTVEHICLES_SKIN_" + ken);
                        as.setHelmet(test.getHelmet());
                        ArmorStand as2 = location.getWorld().spawn(location, ArmorStand.class);
                        as2.setCustomName("MTVEHICLES_MAIN_" + ken);
                        Vehicle vehicle = Vehicle.getByPlate(ken);
                        List<Map<String, Double>> seats = (List<Map<String, Double>>) vehicle.getVehicleData().get("seats");
                        VehicleLeaveEvent.autostand.put("MTVEHICLES_SKIN_" + ken, as);
                        VehicleLeaveEvent.autostand.put("MTVEHICLES_MAIN_" + ken, as2);
                        as.setGravity(false);
                        as2.setGravity(true);
                        as.setVisible(false);
                        as2.setVisible(false);
                        for (int i = 1; i <= seats.size(); i++) {
                            Map<String, Double> seat = seats.get(i - 1);
                            if (i == 1) {
                                Location location2 = new Location(location.getWorld(), location.getX() + Double.valueOf(seat.get("x")), location.getY() + Double.valueOf(seat.get("y")), location.getZ() + Double.valueOf(seat.get("z")));
                                ArmorStand as3 = location2.getWorld().spawn(location2, ArmorStand.class);
                                as3.setCustomName("MTVEHICLES_MAINSEAT_" + ken);
                                VehicleLeaveEvent.autostand.put("MTVEHICLES_MAINSEAT_" + ken, as3);
                                as3.setGravity(false);
                                VehicleMovement.speed.put(ken, 0.0);
                                as3.setPassenger(p);
                                as3.setVisible(false);
                            }
                            if (i > 1) {
                                Location location2 = new Location(location.getWorld(), location.getX() + Double.valueOf(seat.get("x")), location.getY() + Double.valueOf(seat.get("y")), location.getZ() + Double.valueOf(seat.get("z")));
                                ArmorStand as3 = location2.getWorld().spawn(location2, ArmorStand.class);
                                as3.setCustomName("MTVEHICLES_SEAT" + (int)i + "_" + ken);

                                as3.setGravity(false);
                                as3.setVisible(false);
                                VehicleLeaveEvent.autostand.put("MTVEHICLES_SEAT" + (int)i + "_" + ken, as3);
                            }

                        }
                    }
                    test.remove();
                }
            }
        }
    }

    public void getShitOppakVehicles(final String ken, final Player p) {
        for (final World world : Bukkit.getServer().getWorlds()) {
            for (final Entity entity : world.getEntities()) {
                if (entity.getCustomName() != null && entity.getCustomName().contains(ken)) {
                    final ArmorStand test = (ArmorStand) entity;
                    if (test.getCustomName().contains("MTVEHICLES_SKIN_" + ken)) {
                        p.getInventory().addItem(test.getHelmet());

                    }
                    test.remove();
                }
            }
        }
    }

}
