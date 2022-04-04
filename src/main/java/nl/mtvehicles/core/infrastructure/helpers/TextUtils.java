package nl.mtvehicles.core.infrastructure.helpers;

import nl.mtvehicles.core.infrastructure.dataconfig.DefaultConfig;
import nl.mtvehicles.core.infrastructure.dataconfig.VehicleDataConfig;
import nl.mtvehicles.core.infrastructure.enums.Message;
import nl.mtvehicles.core.infrastructure.enums.RegionAction;
import nl.mtvehicles.core.infrastructure.enums.VehicleType;
import nl.mtvehicles.core.infrastructure.models.Vehicle;
import nl.mtvehicles.core.infrastructure.models.VehicleUtils;
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
import java.util.Locale;
import java.util.Map;

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

    public static void createVehicle(String licensePlate, Player p) {
        if (!(VehicleData.autostand2.get(licensePlate) == null)) {
            if (!VehicleData.autostand2.get(licensePlate).isEmpty()) {
                return;
            }
        }
        Vehicle vehicle = VehicleUtils.getByLicensePlate(licensePlate);
        if (vehicle == null) {
            ConfigModule.messagesConfig.sendMessage(p, Message.VEHICLE_NOT_FOUND);
            return;
        }
        if (!vehicle.isOwner(p) && !vehicle.canRide(p) && !p.hasPermission("mtvehicles.ride")) {
            p.sendMessage(TextUtils.colorize(ConfigModule.messagesConfig.getMessage(Message.VEHICLE_NO_RIDER_ENTER).replace("%p%", VehicleUtils.getByLicensePlate(licensePlate).getOwnerName())));
            return;
        }
        for (Entity entity : p.getWorld().getEntities()) {
            if ((boolean) ConfigModule.defaultConfig.get(DefaultConfig.Option.PICKUP_FROM_WATER) && !p.hasPermission("mtvehicles.anwb") && (entity.getLocation().clone().add(0.0, 0.9, 0.0).getBlock().getType().toString().contains("WATER"))) {
                ConfigModule.messagesConfig.sendMessage(p, Message.VEHICLE_IN_WATER);
                return;
            }
            if (entity.getCustomName() != null && entity.getCustomName().contains(licensePlate)) {
                ArmorStand vehicleAs = (ArmorStand) entity;
                if (!entity.isEmpty()) {
                    return;
                }
                VehicleData.fuel.put(licensePlate, vehicle.getFuel());
                VehicleData.fuelUsage.put(licensePlate, (double) ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.FUEL_USAGE));
                VehicleData.type.put(licensePlate, VehicleType.valueOf(ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.VEHICLE_TYPE).toString().toUpperCase(Locale.ROOT)));

                VehicleData.RotationSpeed.put(licensePlate, (int) ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.ROTATION_SPEED));
                VehicleData.MaxSpeed.put(licensePlate, (double) ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.MAX_SPEED));
                VehicleData.AccelerationSpeed.put(licensePlate, (double) ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.ACCELARATION_SPEED));
                VehicleData.BrakingSpeed.put(licensePlate, (double) ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.BRAKING_SPEED));
                VehicleData.MaxSpeedBackwards.put(licensePlate, (double) ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.MAX_SPEED_BACKWARDS));
                VehicleData.FrictionSpeed.put(licensePlate, (double) ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.FRICTION_SPEED));

                Location location = new Location(entity.getWorld(), entity.getLocation().getX(), entity.getLocation().getY(), entity.getLocation().getZ(), entity.getLocation().getYaw(), entity.getLocation().getPitch());

                if (!ConfigModule.defaultConfig.canProceedWithAction(RegionAction.ENTER, location)){
                    ConfigModule.messagesConfig.sendMessage(p, Message.CANNOT_DO_THAT_HERE);
                    return;
                }

                if (vehicleAs.getCustomName().contains("MTVEHICLES_SKIN_" + licensePlate)) {
                    TextUtils.basicStandCreator(licensePlate, "SKIN", location, vehicleAs.getHelmet(), false);
                    TextUtils.basicStandCreator(licensePlate, "MAIN", location, null, true);
                    List<Map<String, Double>> seats = (List<Map<String, Double>>) vehicle.getVehicleData().get("seats");
                    for (int i = 1; i <= seats.size(); i++) {
                        Map<String, Double> seat = seats.get(i - 1);
                        if (i == 1) {
                            TextUtils.mainSeatStandCreator(licensePlate, location, p, seat.get("x"), seat.get("y"), seat.get("z"));
                            BossBarUtils.addBossBar(p, licensePlate);
                            p.sendMessage(TextUtils.colorize(ConfigModule.messagesConfig.getMessage(Message.VEHICLE_ENTER_RIDER).replace("%p%", VehicleUtils.getByLicensePlate(licensePlate).getOwnerName())));
                        }
                        if (i > 1) {
                            VehicleData.seatsize.put(licensePlate, seats.size());
                            VehicleData.seatx.put("MTVEHICLES_SEAT" + (Integer) i + "_" + licensePlate, seat.get("x"));
                            VehicleData.seaty.put("MTVEHICLES_SEAT" + (Integer) i + "_" + licensePlate, seat.get("y"));
                            VehicleData.seatz.put("MTVEHICLES_SEAT" + (Integer) i + "_" + licensePlate, seat.get("z"));
                            Location location2 = new Location(location.getWorld(), location.getX() + Double.valueOf(seat.get("z")), location.getY() + Double.valueOf(seat.get("y")), location.getZ() + Double.valueOf(seat.get("x")));
                            ArmorStand as = location2.getWorld().spawn(location2, ArmorStand.class);
                            as.setCustomName("MTVEHICLES_SEAT" + (Integer) i + "_" + licensePlate);
                            as.setGravity(false);
                            as.setVisible(false);
                            VehicleData.autostand.put("MTVEHICLES_SEAT" + (Integer) i + "_" + licensePlate, as);
                        }
                    }
                    List<Map<String, Double>> wiekens = (List<Map<String, Double>>) vehicle.getVehicleData().get("wiekens");
                    VehicleType vehicleType = ConfigModule.vehicleDataConfig.getType(licensePlate);
                    if (vehicleType.isHelicopter()) {
                        VehicleData.maxheight.put(licensePlate, (int) ConfigModule.defaultConfig.get(DefaultConfig.Option.MAX_FLYING_HEIGHT));
                        for (int i = 1; i <= wiekens.size(); i++) {
                            Map<?, ?> seat = wiekens.get(i - 1);
                            if (i == 1) {
                                Location location2 = new Location(location.getWorld(), location.getX() + (Double) seat.get("z"), (Double) location.getY() + (Double) seat.get("y"), location.getZ() + (Double) seat.get("x"));
                                VehicleData.wiekenx.put("MTVEHICLES_WIEKENS_" + licensePlate, (Double) seat.get("x"));
                                VehicleData.wiekeny.put("MTVEHICLES_WIEKENS_" + licensePlate, (Double) seat.get("y"));
                                VehicleData.wiekenz.put("MTVEHICLES_WIEKENS_" + licensePlate, (Double) seat.get("z"));
                                ArmorStand as = location2.getWorld().spawn(location2, ArmorStand.class);
                                as.setCustomName("MTVEHICLES_WIEKENS_" + licensePlate);
                                as.setGravity(false);
                                as.setVisible(false);
                                VehicleData.autostand.put("MTVEHICLES_WIEKENS_" + licensePlate, as);
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
        if (VehicleUtils.getByLicensePlate(ken) == null) {
            for (World world : Bukkit.getServer().getWorlds()) {
                for (Entity entity : world.getEntities()) {
                    if (entity.getCustomName() != null && entity.getCustomName().contains(ken)) {
                        ArmorStand test = (ArmorStand) entity;
                        if (test.getCustomName().contains("MTVEHICLES_SKIN_" + ken)) {
                            if (!checkInvFull(p)) {
                                p.getInventory().addItem(test.getHelmet());
                            } else {
                                ConfigModule.messagesConfig.sendMessage(p, Message.INVENTORY_FULL);
                                return;
                            }
                        }
                        test.remove();
                    }
                }
            }
            ConfigModule.messagesConfig.sendMessage(p, Message.VEHICLE_NOT_FOUND);
            return;
        }
        if (VehicleUtils.getByLicensePlate(ken).isOwner(p) && !((boolean) ConfigModule.defaultConfig.get(DefaultConfig.Option.CAR_PICKUP)) || p.hasPermission("mtvehicles.oppakken")) {
            for (World world : Bukkit.getServer().getWorlds()) {
                for (Entity entity : world.getEntities()) {
                    if ((boolean) ConfigModule.defaultConfig.get(DefaultConfig.Option.PICKUP_FROM_WATER) && !p.hasPermission("mtvehicles.anwb") && entity.getLocation().clone().add(0.0, 0.9, 0.0).getBlock().getType().toString().contains("WATER")) {
                        ConfigModule.messagesConfig.sendMessage(p, Message.VEHICLE_IN_WATER);
                        return;
                    }
                    if (entity.getCustomName() != null && entity.getCustomName().contains(ken)) {
                        ArmorStand test = (ArmorStand) entity;
                        if (test.getCustomName().contains("MTVEHICLES_SKIN_" + ken)) {
                            if (!checkInvFull(p)) {
                                p.getInventory().addItem(test.getHelmet());
                                p.sendMessage(TextUtils.colorize(ConfigModule.messagesConfig.getMessage(Message.VEHICLE_PICKUP).replace("%p%", VehicleUtils.getByLicensePlate(ken).getOwnerName())));
                            } else {
                                ConfigModule.messagesConfig.sendMessage(p, Message.INVENTORY_FULL);
                                return;
                            }
                        }
                        test.remove();
                    }
                }
            }
        } else {
            if ((boolean) ConfigModule.defaultConfig.get(DefaultConfig.Option.CAR_PICKUP)) {
                p.sendMessage(TextUtils.colorize("&cVoertuigen oppakken staat uitgeschakeld"));
                return;
            }
            p.sendMessage(TextUtils.colorize(ConfigModule.messagesConfig.getMessage(Message.VEHICLE_NO_OWNER_PICKUP).replace("%p%", VehicleUtils.getByLicensePlate(ken).getOwnerName())));
            return;
        }
    }

    public static boolean checkInvFull(Player p) {
        return !Arrays.asList(p.getInventory().getStorageContents()).contains(null);
    }
}
