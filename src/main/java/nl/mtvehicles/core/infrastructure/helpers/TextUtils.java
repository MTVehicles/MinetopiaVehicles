package nl.mtvehicles.core.infrastructure.helpers;

import nl.mtvehicles.core.infrastructure.models.Vehicle;
import nl.mtvehicles.core.Main;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class TextUtils {
    public static String colorize(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    public static String licenseReplacer(String license) {
        if (license.split("_").length > 1) {
            return license.split("_")[2];
        }
        return null;
    }

    public static void basicStandCreator(String license, String type, Location location, ItemStack item, Boolean gravity) {
        ArmorStand as = location.getWorld().spawn(location, ArmorStand.class);
        as.setCustomName("MTVEHICLES_" + type + "_" + license);
        as.setHelmet(item);
        as.setGravity(gravity);
        as.setVisible(false);
        VehicleData.autostand.put("MTVEHICLES_" + type + "_" + license, as);
    }

    public static void mainSeatStandCreator(String license, Location location, Player p, double x, double y, double z) {
        Location location2 = new Location(location.getWorld(), location.getX() + Double.valueOf(z), location.getY() + Double.valueOf(y), location.getZ() + Double.valueOf(z));
        ArmorStand as = location2.getWorld().spawn(location2, ArmorStand.class);
        as.setCustomName("MTVEHICLES_MAINSEAT_" + license);
        VehicleData.autostand.put("MTVEHICLES_MAINSEAT_" + license, as);
        as.setGravity(false);
        VehicleData.speed.put(license, 0.0);
        VehicleData.speedhigh.put(license, 0.0);
        VehicleData.mainx.put("MTVEHICLES_MAINSEAT_" + license, x);
        VehicleData.mainy.put("MTVEHICLES_MAINSEAT_" + license, y);
        VehicleData.mainz.put("MTVEHICLES_MAINSEAT_" + license, z);
        as.setPassenger(p);
        as.setVisible(false);
        VehicleData.autostand2.put(license, as);
    }

    public static void createVehicle(String ken, Player p) {
        if (!(VehicleData.autostand2.get(ken) == null)) {
            if (!VehicleData.autostand2.get(ken).isEmpty()) {
                return;
            }
        }
        Vehicle vehicle = Vehicle.getByPlate(ken);
        if (vehicle == null) {
            ConfigModule.messagesConfig.sendMessage(p, "vehicleNotFound");
            return;
        }
        if (!vehicle.getOwner().equals(p.getUniqueId().toString()) && !vehicle.canRide(p) && !p.hasPermission("mtvehicles.ride")) {
            p.sendMessage(TextUtils.colorize(ConfigModule.messagesConfig.getMessage("vehicleNoRiderEnter").replace("%p%", Bukkit.getOfflinePlayer(UUID.fromString(Vehicle.getByPlate(ken).getOwner().toString())).getName())));
            return;
        }
        for (Entity entity : p.getWorld().getEntities()) {
            if (ConfigModule.defaultConfig.getConfig().getBoolean("anwb") && !p.hasPermission("mtvehicles.anwb") && (entity.getLocation().clone().add(0.0, 0.9, 0.0).getBlock().getType().toString().contains("WATER"))) {
                ConfigModule.messagesConfig.sendMessage(p, "vehicleInWater");
                return;
            }
            if (entity.getCustomName() != null && entity.getCustomName().contains(ken)) {
                ArmorStand vehicleAs = (ArmorStand) entity;
                if (!entity.isEmpty()) {
                    return;
                }
                VehicleData.fuel.put(ken, vehicle.getFuel());
                VehicleData.fuelUsage.put(ken, ConfigModule.vehicleDataConfig.getConfig().getDouble("vehicle." + ken + ".benzineVerbruik"));
                VehicleData.type.put(ken, ConfigModule.vehicleDataConfig.getConfig().getString("vehicle." + ken + ".vehicleType"));

                VehicleData.RotationSpeed.put(ken, ConfigModule.vehicleDataConfig.getConfig().getInt("vehicle."+ken+".rotateSpeed"));
                VehicleData.MaxSpeed.put(ken, ConfigModule.vehicleDataConfig.getConfig().getDouble("vehicle."+ken+".maxSpeed"));
                VehicleData.AccelerationSpeed.put(ken, ConfigModule.vehicleDataConfig.getConfig().getDouble("vehicle."+ken+".acceleratieSpeed"));
                VehicleData.BrakingSpeed.put(ken, ConfigModule.vehicleDataConfig.getConfig().getDouble("vehicle."+ken+".brakingSpeed"));
                VehicleData.MaxSpeedBackwards.put(ken, ConfigModule.vehicleDataConfig.getConfig().getDouble("vehicle."+ken+".maxSpeedBackwards"));
                VehicleData.FrictionSpeed.put(ken, ConfigModule.vehicleDataConfig.getConfig().getDouble("vehicle."+ken+".aftrekkenSpeed"));

                Location location = new Location(entity.getWorld(), entity.getLocation().getX(), entity.getLocation().getY(), entity.getLocation().getZ(), entity.getLocation().getYaw(), entity.getLocation().getPitch());
                if (vehicleAs.getCustomName().contains("MTVEHICLES_SKIN_" + ken)) {
                    TextUtils.basicStandCreator(ken, "SKIN", location, vehicleAs.getHelmet(), false);
                    TextUtils.basicStandCreator(ken, "MAIN", location, null, true);
                    List<Map<String, Double>> seats = (List<Map<String, Double>>) vehicle.getVehicleData().get("seats");
                    for (int i = 1; i <= seats.size(); i++) {
                        Map<String, Double> seat = seats.get(i - 1);
                        if (i == 1) {
                            TextUtils.mainSeatStandCreator(ken, location, p, seat.get("x"), seat.get("y"), seat.get("z"));
                            BossBarUtils.addBossBar(p, ken);
                            p.sendMessage(TextUtils.colorize(ConfigModule.messagesConfig.getMessage("vehicleEnterRider").replace("%p%", Bukkit.getOfflinePlayer(UUID.fromString(Vehicle.getByPlate(ken).getOwner().toString())).getName())));
                        }
                        if (i > 1) {
                            VehicleData.seatsize.put(ken, seats.size());
                            VehicleData.seatx.put("MTVEHICLES_SEAT" + (Integer) i + "_" + ken, seat.get("x"));
                            VehicleData.seaty.put("MTVEHICLES_SEAT" + (Integer) i + "_" + ken, seat.get("y"));
                            VehicleData.seatz.put("MTVEHICLES_SEAT" + (Integer) i + "_" + ken, seat.get("z"));
                            Location location2 = new Location(location.getWorld(), location.getX() + Double.valueOf(seat.get("z")), location.getY() + Double.valueOf(seat.get("y")), location.getZ() + Double.valueOf(seat.get("x")));
                            ArmorStand as = location2.getWorld().spawn(location2, ArmorStand.class);
                            as.setCustomName("MTVEHICLES_SEAT" + (Integer) i + "_" + ken);
                            as.setGravity(false);
                            as.setVisible(false);
                            VehicleData.autostand.put("MTVEHICLES_SEAT" + (Integer) i + "_" + ken, as);
                        }
                    }
                    List<Map<String, Double>> wiekens = (List<Map<String, Double>>) vehicle.getVehicleData().get("wiekens");
                    String vehicleType = ConfigModule.vehicleDataConfig.getConfig().getString("vehicle." + ken + ".vehicleType");
                    if (vehicleType.contains("HELICOPTER")) {
                        VehicleData.maxheight.put(ken, ConfigModule.defaultConfig.getConfig().getInt("helicopterMaxHeight"));
                        if (vehicleType == null) return;
                        for (int i = 1; i <= wiekens.size(); i++) {
                            Map<?, ?> seat = wiekens.get(i - 1);
                            if (i == 1) {
                                Location location2 = new Location(location.getWorld(), location.getX() + (Double) seat.get("z"), (Double) location.getY() + (Double) seat.get("y"), location.getZ() + (Double) seat.get("x"));
                                VehicleData.wiekenx.put("MTVEHICLES_WIEKENS_" + ken, (Double) seat.get("x"));
                                VehicleData.wiekeny.put("MTVEHICLES_WIEKENS_" + ken, (Double) seat.get("y"));
                                VehicleData.wiekenz.put("MTVEHICLES_WIEKENS_" + ken, (Double) seat.get("z"));
                                ArmorStand as = location2.getWorld().spawn(location2, ArmorStand.class);
                                as.setCustomName("MTVEHICLES_WIEKENS_" + ken);
                                as.setGravity(false);
                                as.setVisible(false);
                                VehicleData.autostand.put("MTVEHICLES_WIEKENS_" + ken, as);
                                as.setHelmet((ItemStack) seat.get("item"));
                            }
                        }
                    }
                }
                vehicleAs.remove();
            }
        }
    }

    public static void pickupVehicle(String ken, Player p) {
        if (Vehicle.getByPlate(ken) == null) {
            for (World world : Bukkit.getServer().getWorlds()) {
                for (Entity entity : world.getEntities()) {
                    if (entity.getCustomName() != null && entity.getCustomName().contains(ken)) {
                        ArmorStand test = (ArmorStand) entity;
                        if (test.getCustomName().contains("MTVEHICLES_SKIN_" + ken)) {
                            if (checkInvFull(p) == false) {
                                p.getInventory().addItem(test.getHelmet());
                            } else {
                                ConfigModule.messagesConfig.sendMessage(p, "inventoryFull");
                                return;
                            }
                        }
                        test.remove();
                    }
                }
            }
            ConfigModule.messagesConfig.sendMessage(p, "vehicleNotFound");
            return;
        }
        if (Vehicle.getByPlate(ken).getOwner().equals(p.getUniqueId().toString()) && ConfigModule.defaultConfig.getConfig().getBoolean("carPickup") == false || p.hasPermission("mtvehicles.oppakken")) {
            for (World world : Bukkit.getServer().getWorlds()) {
                for (Entity entity : world.getEntities()) {
                    if (ConfigModule.defaultConfig.getConfig().getBoolean("anwb") && !p.hasPermission("mtvehicles.anwb") && entity.getLocation().clone().add(0.0, 0.9, 0.0).getBlock().getType().toString().contains("WATER")) {
                        ConfigModule.messagesConfig.sendMessage(p, "vehicleInWater");
                        return;
                    }
                    if (entity.getCustomName() != null && entity.getCustomName().contains(ken)) {
                        ArmorStand test = (ArmorStand) entity;
                        if (test.getCustomName().contains("MTVEHICLES_SKIN_" + ken)) {
                            if (checkInvFull(p) == false) {
                                p.getInventory().addItem(test.getHelmet());
                                p.sendMessage(TextUtils.colorize(ConfigModule.messagesConfig.getMessage("vehiclePickup").replace("%p%", Bukkit.getOfflinePlayer(UUID.fromString(Vehicle.getByPlate(ken).getOwner().toString())).getName())));
                            } else {
                                ConfigModule.messagesConfig.sendMessage(p, "inventoryFull");
                                return;
                            }
                        }
                        test.remove();
                    }
                }
            }
        } else {
            if (ConfigModule.defaultConfig.getConfig().getBoolean("carPickup") == true) {
                p.sendMessage(TextUtils.colorize("&cVoertuigen oppakken staat uitgeschakeld"));
                return;
            }
            p.sendMessage(TextUtils.colorize(ConfigModule.messagesConfig.getMessage("vehicleNoOwnerPickup").replace("%p%", Bukkit.getOfflinePlayer(UUID.fromString(Vehicle.getByPlate(ken).getOwner().toString())).getName())));
            return;
        }
    }

    public static boolean checkInvFull(Player p) {
        return !Arrays.asList(p.getInventory().getStorageContents()).contains(null);
    }
}
