package nl.mtvehicles.core.Events;

import nl.mtvehicles.core.Infrastructure.Helpers.BossbarUtils;
import nl.mtvehicles.core.Infrastructure.Helpers.TextUtils;
import nl.mtvehicles.core.Infrastructure.Models.ConfigUtils;
import nl.mtvehicles.core.Infrastructure.Models.Vehicle;
import nl.mtvehicles.core.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
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
import java.util.UUID;

public class VehicleClickEvent implements Listener {
    public static HashMap<String, Double> speed = new HashMap<>();
    public static HashMap<String, Double> speedhigh = new HashMap<>();

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerInteractAtEntity(final PlayerInteractAtEntityEvent event) {
        final Entity a = event.getRightClicked();
        final Player p = event.getPlayer();
        if (a.getCustomName() == null) {
            return;
        }
        if (p.isSneaking()) {
            if (event.getRightClicked().getCustomName().contains("MTVEHICLES_MAINSEAT_")) {
                getShitOppakVehicles(event.getRightClicked().getCustomName().replace("MTVEHICLES_MAINSEAT_", ""), p);
                event.setCancelled(true);
            }
            if (event.getRightClicked().getCustomName().contains("MTVEHICLES_MAIN_")) {
                getShitOppakVehicles(event.getRightClicked().getCustomName().replace("MTVEHICLES_MAIN_", ""), p);
                event.setCancelled(true);
            }
            if (event.getRightClicked().getCustomName().contains("MTVEHICLES_SKIN_")) {
                getShitOppakVehicles(event.getRightClicked().getCustomName().replace("MTVEHICLES_SKIN_", ""), p);
                event.setCancelled(true);
            }
            if (event.getRightClicked().getCustomName().contains("MTVEHICLES_WIEKENS_")) {
                event.setCancelled(true);
            }
            return;
        }
            Main.configList.forEach(ConfigUtils::reload);

        if (event.getRightClicked().getCustomName().contains("MTVEHICLES_MAINSEAT_")) {
            getShitVehicles(event.getRightClicked().getCustomName().replace("MTVEHICLES_MAINSEAT_", ""), p);
            event.setCancelled(true);
        }
        if (event.getRightClicked().getCustomName().contains("MTVEHICLES_MAIN_")) {
            getShitVehicles(event.getRightClicked().getCustomName().replace("MTVEHICLES_MAIN_", ""), p);
            event.setCancelled(true);
        }
        if (event.getRightClicked().getCustomName().contains("MTVEHICLES_SKIN_")) {
            getShitVehicles(event.getRightClicked().getCustomName().replace("MTVEHICLES_SKIN_", ""), p);
            event.setCancelled(true);
        }
        if (event.getRightClicked().getCustomName().contains("MTVEHICLES_SEAT")) {
            event.setCancelled(true);
            String ken = event.getRightClicked().getCustomName().substring(17);
            if (Vehicle.getByPlate(ken).getOwner().equals(p.getUniqueId().toString()) || Vehicle.canSit(p, ken) == true || p.hasPermission("mtvehicles.ride")) {
                if (event.getRightClicked().isEmpty()) {
                    event.getRightClicked().setPassenger(p);
                    p.sendMessage(TextUtils.colorize(Main.messagesConfig.getMessage("vehicleEnterMember").replace("%p%", Bukkit.getOfflinePlayer(UUID.fromString(Vehicle.getByPlate(ken).getOwner().toString())).getName())));
                }
            } else {
                p.sendMessage(TextUtils.colorize(Main.messagesConfig.getMessage("vehicleNoRiderEnter").replace("%p%", Bukkit.getOfflinePlayer(UUID.fromString(Vehicle.getByPlate(ken).getOwner().toString())).getName())));
            }
        }
        if (event.getRightClicked().getCustomName().contains("MTVEHICLES_WIEKENS_")) {
            event.setCancelled(true);
        }
    }

    public void getShitVehicles(final String ken, final Player p) {
        if (!(VehicleLeaveEvent.autostand2.get(ken) == null)) {
            if (!VehicleLeaveEvent.autostand2.get(ken).isEmpty()) {
                return;
            }
        }
        if (Vehicle.getByPlate(ken) == null) {
            p.sendMessage(TextUtils.colorize(Main.messagesConfig.getMessage("vehicleNotFound")));
            return;
        }
        if (Vehicle.getByPlate(ken).getOwner().equals(p.getUniqueId().toString()) || Vehicle.canRide(p, ken) == true || p.hasPermission("mtvehicles.ride")) {
            for (final World world : Bukkit.getServer().getWorlds()) {
                for (final Entity entity : world.getEntities()) {
                    if (Main.defaultConfig.getConfig().getBoolean("anwb") && !p.hasPermission("mtvehicles.anwb") && (entity.getLocation().clone().add(0.0, 0.9, 0.0).getBlock().getType() == Material.WATER || entity.getLocation().clone().add(0.0, 0.9, 0.0).getBlock().getType() == Material.LEGACY_STATIONARY_WATER)) {
                        p.sendMessage(TextUtils.colorize(Main.messagesConfig.getMessage("vehicleInWater")));
                        return;
                    }
                    if (entity.getCustomName() != null && entity.getCustomName().contains(ken)) {
                        final ArmorStand test = (ArmorStand) entity;
                        if (!entity.isEmpty()) {
                            return;
                        }
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
                                    Location location2 = new Location(location.getWorld(), location.getX() + Double.valueOf(seat.get("z")), location.getY() + Double.valueOf(seat.get("y")), location.getZ() + Double.valueOf(seat.get("z")));
                                    ArmorStand as3 = location2.getWorld().spawn(location2, ArmorStand.class);
                                    as3.setCustomName("MTVEHICLES_MAINSEAT_" + ken);
                                    VehicleLeaveEvent.autostand.put("MTVEHICLES_MAINSEAT_" + ken, as3);
                                    as3.setGravity(false);
                                    speed.put(ken, 0.0);
                                    speedhigh.put(ken, 0.0);
                                    as3.setPassenger(p);
                                    as3.setVisible(false);
                                    VehicleLeaveEvent.autostand2.put(ken, as3);
                                    BossbarUtils.addBossbar(p, ken);
                                    p.sendMessage(TextUtils.colorize(Main.messagesConfig.getMessage("vehicleEnterRider").replace("%p%", Bukkit.getOfflinePlayer(UUID.fromString(Vehicle.getByPlate(ken).getOwner().toString())).getName())));
                                }
                                if (i > 1) {
                                    Location location2 = new Location(location.getWorld(), location.getX() + Double.valueOf(seat.get("z")), location.getY() + Double.valueOf(seat.get("y")), location.getZ() + Double.valueOf(seat.get("x")));
                                    ArmorStand as3 = location2.getWorld().spawn(location2, ArmorStand.class);
                                    as3.setCustomName("MTVEHICLES_SEAT" + (int) i + "_" + ken);

                                    as3.setGravity(false);
                                    as3.setVisible(false);
                                    VehicleLeaveEvent.autostand.put("MTVEHICLES_SEAT" + (int) i + "_" + ken, as3);
                                }
                            }
                            List<Map<String, Double>> wiekens = (List<Map<String, Double>>) vehicle.getVehicleData().get("wiekens");
                            String vehicleType = Main.vehicleDataConfig.getConfig().getString("vehicle." + ken + ".vehicleType");
                            if (vehicleType == null) return;
                            if (vehicleType.contains("HELICOPTER")) {
                                for (int i = 1; i <= wiekens.size(); i++) {
                                    Map<?, ?> seat = wiekens.get(i - 1);
                                    if (i == 1) {
                                        Location location2 = new Location(location.getWorld(), location.getX() + (double) seat.get("z"), (double) location.getY() + (double) seat.get("y"), location.getZ() + (double) seat.get("x"));
                                        ArmorStand as3 = location2.getWorld().spawn(location2, ArmorStand.class);
                                        as3.setCustomName("MTVEHICLES_WIEKENS_" + ken);
                                        as3.setGravity(false);
                                        as3.setVisible(false);
                                        VehicleLeaveEvent.autostand.put("MTVEHICLES_WIEKENS_" + ken, as3);
                                        as3.setHelmet((ItemStack) seat.get("item"));
                                    }
                                }
                            }
                        }
                        test.remove();
                    }
                }
            }
        } else {
            p.sendMessage(TextUtils.colorize(Main.messagesConfig.getMessage("vehicleNoRiderEnter").replace("%p%", Bukkit.getOfflinePlayer(UUID.fromString(Vehicle.getByPlate(ken).getOwner().toString())).getName())));
        }
    }

    public void getShitOppakVehicles(final String ken, final Player p) {
        if (Vehicle.getByPlate(ken) == null) {
            p.sendMessage(TextUtils.colorize(Main.messagesConfig.getMessage("vehicleNotFound")));
            return;
        }
        if (Vehicle.getByPlate(ken).getOwner().equals(p.getUniqueId().toString()) || p.hasPermission("mtvehicles.oppakken")) {
            for (final World world : Bukkit.getServer().getWorlds()) {
                for (final Entity entity : world.getEntities()) {
                    if (Main.defaultConfig.getConfig().getBoolean("anwb") && !p.hasPermission("mtvehicles.anwb") && (entity.getLocation().clone().add(0.0, 0.9, 0.0).getBlock().getType() == Material.WATER || entity.getLocation().clone().add(0.0, 0.9, 0.0).getBlock().getType() == Material.LEGACY_STATIONARY_WATER)) {
                        p.sendMessage(TextUtils.colorize(Main.messagesConfig.getMessage("vehicleInWater")));
                        return;
                    }
                    if (entity.getCustomName() != null && entity.getCustomName().contains(ken)) {
                        final ArmorStand test = (ArmorStand) entity;
                        if (test.getCustomName().contains("MTVEHICLES_SKIN_" + ken)) {
                            p.getInventory().addItem(test.getHelmet());
                            p.sendMessage(TextUtils.colorize(Main.messagesConfig.getMessage("vehiclePickup").replace("%p%", Bukkit.getOfflinePlayer(UUID.fromString(Vehicle.getByPlate(ken).getOwner().toString())).getName())));
                        }
                        test.remove();
                    }
                }
            }
        } else {
            p.sendMessage(TextUtils.colorize(Main.messagesConfig.getMessage("vehicleNoOwnerPickup").replace("%p%", Bukkit.getOfflinePlayer(UUID.fromString(Vehicle.getByPlate(ken).getOwner().toString())).getName())));
            return;
        }
    }
}
