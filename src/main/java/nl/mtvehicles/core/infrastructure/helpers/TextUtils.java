package nl.mtvehicles.core.infrastructure.helpers;

import nl.mtvehicles.core.infrastructure.dataconfig.DefaultConfig;
import nl.mtvehicles.core.infrastructure.dataconfig.VehicleDataConfig;
import nl.mtvehicles.core.infrastructure.enums.Message;
import nl.mtvehicles.core.infrastructure.enums.RegionAction;
import nl.mtvehicles.core.infrastructure.enums.VehicleType;
import nl.mtvehicles.core.infrastructure.models.Vehicle;
import nl.mtvehicles.core.infrastructure.models.VehicleUtils;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Methods for editing text (and for some reason also deprecated methods for creating vehicles - moved to {@link VehicleUtils} and {@link nl.mtvehicles.core.listeners.VehicleClickListener})
 */
public class TextUtils {
    /**
     * Colorize a String with '&' characters.
     * @param text Text
     * @return Colorized text
     */
    public static String colorize(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    /**
     * Get license plate from vehicle armor stand's name
     * @param license Name of the vehicle
     * @return Vehicle's license plate
     * @deprecated Use {@link VehicleUtils#getLicensePlate(Entity)} instead.
     */
    @Deprecated
    public static String licenseReplacer(String license) {
        if (license.split("_").length > 1) {
            return license.split("_")[2];
        }
        return null;
    }

    /**
     * Get a List from multiple Strings
     */
    public static List<String> list(String... strings){
        return Arrays.asList(strings);
    }

    @Deprecated
    private static void basicStandCreator(String license, String type, Location location, ItemStack item, Boolean gravity) {
        ArmorStand as = location.getWorld().spawn(location, ArmorStand.class);
        as.setCustomName("MTVEHICLES_" + type + "_" + license);
        as.setHelmet(item);
        as.setGravity(gravity);
        as.setVisible(false);
        VehicleData.autostand.put("MTVEHICLES_" + type + "_" + license, as);
    }

    @Deprecated
    private static void mainSeatStandCreator(String license, Location location, Player p, double x, double y, double z) {
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

    /**
     * @deprecated This method somehow worked, no idea how though. Use {@link nl.mtvehicles.core.listeners.VehicleClickListener#placeVehicle(String, Player)} instead.
     */
    @Deprecated
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
            if ((boolean) ConfigModule.defaultConfig.get(DefaultConfig.Option.DISABLE_PICKUP_FROM_WATER) && !p.hasPermission("mtvehicles.anwb") && (entity.getLocation().clone().add(0.0, 0.9, 0.0).getBlock().getType().toString().contains("WATER"))) {
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

                if (!ConfigModule.defaultConfig.canProceedWithAction(RegionAction.ENTER, vehicle.getVehicleType(), location)){
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

    /**
     * Pick up a vehicle and put it to player's inventory
     * @param ken Vehicle's license plate
     * @param p Player
     *
     * @deprecated Moved. Use {@link VehicleUtils#pickupVehicle(String, Player)}.
     */
    @Deprecated
    public static void pickupVehicle(String ken, Player p) {
        VehicleUtils.pickupVehicle(ken, p);
    }

    /**
     * Check whether player's inventory is full
     * @param player Player
     * @return True if player's inventory is full
     */
    public static boolean checkInvFull(Player player) {
        return !Arrays.asList(player.getInventory().getStorageContents()).contains(null);
    }
}
