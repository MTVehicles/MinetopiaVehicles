package nl.mtvehicles.core.infrastructure.models;

import de.tr7zw.changeme.nbtapi.NBTItem;
import nl.mtvehicles.core.infrastructure.helpers.ItemUtils;
import nl.mtvehicles.core.infrastructure.helpers.TextUtils;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import org.bukkit.Bukkit;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

// "Static" class
public final class VehicleUtils {

    private VehicleUtils(){}

    public static String getLicensePlate(ItemStack item){
        NBTItem nbt = new NBTItem(item);
        return nbt.getString("mtvehicles.kenteken");
    }

    public static ItemStack getItemByUUID(Player p, String uuid) {
        List<Map<?, ?>> vehicles = ConfigModule.vehiclesConfig.getConfig().getMapList("voertuigen");
        List<Map<?, ?>> matchedVehicles = new ArrayList<>();
        for (Map<?, ?> configVehicle : vehicles) {
            List<Map<?, ?>> skins = (List<Map<?, ?>>) configVehicle.get("cars");
            for (Map<?, ?> skin : skins) {
                if (skin.get("uuid") != null) {
                    if (skin.get("uuid").equals(uuid)) {
                        String nbtVal;
                        if (skin.get("nbtValue") == null) {
                            nbtVal = "null";
                        } else {
                            nbtVal = skin.get("nbtValue").toString();
                        }
                        ItemStack is = ItemUtils.carItem3((Integer) skin.get("itemDamage"), ((String) skin.get("name")), (String) skin.get("SkinItem"), "mtcustom", nbtVal);
                        NBTItem nbt = new NBTItem(is);
                        String kenteken = nbt.getString("mtvehicles.kenteken");
                        matchedVehicles.add(configVehicle);
                        Vehicle vehicle = new Vehicle();
                        List<String> members = ConfigModule.vehicleDataConfig.getConfig().getStringList("voertuig." + kenteken + ".members");
                        List<String> riders = ConfigModule.vehicleDataConfig.getConfig().getStringList("voertuig." + kenteken + ".riders");
                        List<String> kof = ConfigModule.vehicleDataConfig.getConfig().getStringList("voertuig." + kenteken + ".kofferbakData");
                        vehicle.setLicensePlate(kenteken);
                        vehicle.setName((String) skin.get("name"));
                        vehicle.setVehicleType((String) configVehicle.get("vehicleType"));
                        vehicle.setSkinDamage((Integer) skin.get("itemDamage"));
                        vehicle.setSkinItem((String) skin.get("SkinItem"));
                        vehicle.setGlow(false);
                        vehicle.setBenzineEnabled((Boolean) configVehicle.get("benzineEnabled"));
                        vehicle.setFuel(100);
                        vehicle.setHornEnabled((Boolean) configVehicle.get("hornEnabled"));
                        vehicle.setHealth((double) configVehicle.get("maxHealth"));
                        vehicle.setTrunk((Boolean) configVehicle.get("kofferbakEnabled"));
                        vehicle.setTrunkRows(1);
                        vehicle.setFuelUsage(0.01);
                        vehicle.setTrunkData(kof);
                        vehicle.setAccelerationSpeed((Double) configVehicle.get("acceleratieSpeed"));
                        vehicle.setMaxSpeed((Double) configVehicle.get("maxSpeed"));
                        vehicle.setBrakingSpeed((Double) configVehicle.get("brakingSpeed"));
                        vehicle.setFrictionSpeed((Double) configVehicle.get("aftrekkenSpeed"));
                        vehicle.setRotateSpeed((Integer) configVehicle.get("rotateSpeed"));
                        vehicle.setMaxSpeedBackwards((Double) configVehicle.get("maxSpeedBackwards"));
                        vehicle.setOwner(p.getUniqueId().toString());
                        vehicle.setRiders(riders);
                        vehicle.setMembers(members);
                        vehicle.setNbtValue(((String) skin.get("nbtValue")));
                        vehicle.save();
                        return is;
                    }
                }
            }
        }
        return null;
    }

    public static boolean getHornByDamage(int damage){
        List<Map<?, ?>> vehicles = ConfigModule.vehiclesConfig.getConfig().getMapList("voertuigen");
        for (Map<?, ?> configVehicle : vehicles) {
            List<Map<?, ?>> skins = (List<Map<?, ?>>) configVehicle.get("cars");
            for (Map<?, ?> skin : skins) {
                if (skin.get("itemDamage") != null) {
                    if (skin.get("itemDamage").equals(damage)) {
                        return (boolean) configVehicle.get("hornEnabled");
                    }
                }
            }
        }
        return false;
    }

    public static double getMaxHealthByDamage(int damage){
        List<Map<?, ?>> vehicles = ConfigModule.vehiclesConfig.getConfig().getMapList("voertuigen");
        for (Map<?, ?> configVehicle : vehicles) {
            List<Map<?, ?>> skins = (List<Map<?, ?>>) configVehicle.get("cars");
            for (Map<?, ?> skin : skins) {
                if (skin.get("itemDamage") != null) {
                    if (skin.get("itemDamage").equals(damage)) {
                        return (double) configVehicle.get("maxHealth");
                    }
                }
            }
        }
        return 0;
    }

    public static ItemStack getCarItem(String carUuid) {
        List<Map<?, ?>> vehicles = ConfigModule.vehiclesConfig.getConfig().getMapList("voertuigen");
        List<Map<?, ?>> matchedVehicles = new ArrayList<>();
        for (Map<?, ?> configVehicle : vehicles) {
            List<Map<?, ?>> skins = (List<Map<?, ?>>) configVehicle.get("cars");
            for (Map<?, ?> skin : skins) {
                if (skin.get("uuid") != null) {
                    if (skin.get("uuid").equals(carUuid)) {
                        if (skin.get("uuid") != null) {
                            ItemStack is = ItemUtils.carItem2((Integer) skin.get("itemDamage"), ((String) skin.get("name")), (String) skin.get("SkinItem"));
                            matchedVehicles.add(configVehicle);
                            return is;
                        }
                    }
                }
            }
        }
        return null;
    }

    public static boolean isVehicle(Entity entity){
        return entity.getCustomName() != null && entity instanceof ArmorStand && entity.getCustomName().contains("MTVEHICLES");
    }

    public static String getLicensePlate(Entity entity){
        return TextUtils.licenseReplacer(Objects.requireNonNull(entity.getCustomName()));
    }

    public static String getCarUUID(String licensePlate) {
        if (!existsByLicensePlate(licensePlate)) return null;

        Map<?, ?> vehicleData = ConfigModule.vehicleDataConfig.getConfig()
                .getConfigurationSection(String.format("vehicle.%s", licensePlate)).getValues(true);
        List<Map<?, ?>> vehicles = ConfigModule.vehiclesConfig.getConfig().getMapList("voertuigen");
        List<Map<?, ?>> matchedVehicles = new ArrayList<>();
        for (Map<?, ?> configVehicle : vehicles) {
            List<Map<?, ?>> skins = (List<Map<?, ?>>) configVehicle.get("cars");
            for (Map<?, ?> skin : skins) {
                if (skin.get("itemDamage").equals(vehicleData.get("skinDamage"))) {
                    if (skin.get("SkinItem").equals(vehicleData.get("skinItem"))) {
                        if (skin.get("nbtValue") != null) {
                            if (skin.get("nbtValue").equals(vehicleData.get("nbtValue"))) {
                                matchedVehicles.add(configVehicle);
                                return skin.get("uuid").toString();
                            }
                        } else {
                            matchedVehicles.add(configVehicle);
                            return skin.get("uuid").toString();
                        }
                    }
                }
            }
        }
        return null;
    }

    public static Vehicle getByLicensePlate(String licensePlate) {
        if (!existsByLicensePlate(licensePlate)) return null;

        Map<?, ?> vehicleData = ConfigModule.vehicleDataConfig.getConfig()
                .getConfigurationSection(String.format("vehicle.%s", licensePlate)).getValues(true);
        List<Map<?, ?>> vehicles = ConfigModule.vehiclesConfig.getConfig().getMapList("voertuigen");
        List<Map<?, ?>> matchedVehicles = new ArrayList<>();
        for (Map<?, ?> configVehicle : vehicles) {
            List<Map<?, ?>> skins = (List<Map<?, ?>>) configVehicle.get("cars");
            for (Map<?, ?> skin : skins) {
                if (skin.get("itemDamage").equals(vehicleData.get("skinDamage"))) {
                    if (skin.get("SkinItem").equals(vehicleData.get("skinItem"))) {
                        if (skin.get("nbtValue") != null) {
                            if (skin.get("nbtValue").equals(vehicleData.get("nbtValue"))) {
                                matchedVehicles.add(configVehicle);
                            }
                        } else {
                            matchedVehicles.add(configVehicle);
                        }
                    }
                }
            }
        }
        if (matchedVehicles.size() == 0) return null;
        if (matchedVehicles.size() > 1) return null;
        Vehicle vehicle = new Vehicle();
        vehicle.setVehicleData(matchedVehicles.get(0));
        vehicle.setLicensePlate(licensePlate);
        vehicle.setOpen(false);
        vehicle.setName((String) vehicleData.get("name"));
        vehicle.setVehicleType((String) vehicleData.get("vehicleType"));
        vehicle.setSkinDamage((Integer) vehicleData.get("skinDamage"));
        vehicle.setSkinItem((String) vehicleData.get("skinItem"));
        vehicle.setGlow((Boolean) vehicleData.get("isGlow"));
        vehicle.setHornEnabled(ConfigModule.vehicleDataConfig.isHornSet(licensePlate) ? (boolean) vehicleData.get("hornEnabled") : ConfigModule.vehicleDataConfig.isHornEnabled(licensePlate));
        vehicle.setHealth(ConfigModule.vehicleDataConfig.isHealthSet(licensePlate) ? (double) vehicleData.get("health") : ConfigModule.vehicleDataConfig.getHealth(licensePlate));
        vehicle.setBenzineEnabled((Boolean) vehicleData.get("benzineEnabled"));
        vehicle.setFuel((Double) vehicleData.get("benzine"));
        vehicle.setFuelUsage((Double) vehicleData.get("benzineVerbruik"));
        vehicle.setTrunk((Boolean) vehicleData.get("kofferbak"));
        vehicle.setTrunkRows((Integer) vehicleData.get("kofferbakRows"));
        vehicle.setTrunkData((List<String>) vehicleData.get("kofferbakData"));
        vehicle.setAccelerationSpeed((Double) vehicleData.get("acceleratieSpeed"));
        vehicle.setMaxSpeed((Double) vehicleData.get("maxSpeed"));
        vehicle.setBrakingSpeed((Double) vehicleData.get("brakingSpeed"));
        vehicle.setFrictionSpeed((Double) vehicleData.get("aftrekkenSpeed"));
        vehicle.setRotateSpeed((Integer) vehicleData.get("rotateSpeed"));
        vehicle.setMaxSpeedBackwards((Double) vehicleData.get("maxSpeedBackwards"));
        vehicle.setOwner((String) vehicleData.get("owner"));
        vehicle.setRiders((List<String>) vehicleData.get("riders"));
        vehicle.setMembers((List<String>) vehicleData.get("members"));
        return vehicle;
    }

    public static boolean existsByLicensePlate(String licensePlate) {
        return ConfigModule.vehicleDataConfig.getConfig().get(String.format("vehicle.%s", licensePlate)) != null;
    }

    public static boolean canRide(Player player, String licensePlate) {
        return ConfigModule.vehicleDataConfig.getConfig().getStringList("vehicle." + licensePlate + ".riders").contains(player.getUniqueId().toString());
    }

    public static boolean canSit(Player player, String licensePlate) {
        return ConfigModule.vehicleDataConfig.getConfig().getStringList("vehicle." + licensePlate + ".members").contains(player.getUniqueId().toString());
    }

    public static UUID getOwnerUUID(String licensePlate) {
        if (ConfigModule.vehicleDataConfig.getConfig().getString("vehicle." + licensePlate + ".owner") == null) {
            return null;
        }
        return UUID.fromString(ConfigModule.vehicleDataConfig.getConfig().getString("vehicle." + licensePlate + ".owner"));
    }

    @Deprecated
    public static String getRidersAsString(String licensePlate) {
        StringBuilder sb = new StringBuilder();
        for (String s : ConfigModule.vehicleDataConfig.getConfig().getStringList("vehicle." + licensePlate + ".riders")) {
            if (!UUID.fromString(s).equals(getOwnerUUID(licensePlate))) {
                sb.append(Bukkit.getOfflinePlayer(UUID.fromString(s)).getName()).append(", ");
            }
        }
        if (sb.toString().isEmpty()) {
            sb.append("Niemand");
        }
        return sb.toString();
    }
}
