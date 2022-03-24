package nl.mtvehicles.core.infrastructure.models;

import de.tr7zw.changeme.nbtapi.NBTItem;
import nl.mtvehicles.core.Main;
import nl.mtvehicles.core.infrastructure.enums.VehicleType;
import nl.mtvehicles.core.infrastructure.helpers.ItemUtils;
import nl.mtvehicles.core.infrastructure.helpers.TextUtils;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class Vehicle {
    private String licensePlate;
    private String name;
    private VehicleType vehicleType;
    private int skinDamage;
    private String skinItem;
    private boolean isGlow;
    private boolean benzineEnabled;
    private double benzine;
    private double fuelUsage;
    private boolean hornEnabled;
    private double health;
    private boolean trunkEnabled;
    private int trunkRows;
    private List<String> kofferbakData;
    private double acceleratieSpeed;
    private double maxSpeed;
    private double brakingSpeed;
    private double frictionSpeed;
    private int rotateSpeed;
    private double maxSpeedBackwards;
    private boolean isOpen;
    private UUID owner;
    private String nbtValue;
    private List<String> riders;
    private List<String> members;
    private Map<?, ?> vehicleData;

    public static HashMap<String, MTVehicleSubCommand> subcommands = new HashMap<>();

    public void save() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", this.getName());
        map.put("vehicleType", this.getVehicleType().toString());
        map.put("skinDamage", this.getSkinDamage());
        map.put("skinItem", this.getSkinItem());
        map.put("isOpen", this.isOpen());
        map.put("isGlow", this.isGlow());
        map.put("benzineEnabled", this.isFuelEnabled());
        map.put("benzine", this.getFuel());
        map.put("benzineVerbruik", this.getFuelUsage());
        map.put("hornEnabled", this.isHornEnabled());
        map.put("health", this.getHealth());
        map.put("kofferbak", this.isTrunkEnabled());
        map.put("kofferbakRows", this.getTrunkRows());
        map.put("kofferbakData", this.getTrunkData());
        map.put("acceleratieSpeed", this.getAccelerationSpeed());
        map.put("maxSpeed", this.getMaxSpeed());
        map.put("brakingSpeed", this.getBrakingSpeed());
        map.put("aftrekkenSpeed", this.getFrictionSpeed());
        map.put("rotateSpeed", this.getRotateSpeed());
        map.put("maxSpeedBackwards", this.getMaxSpeedBackwards());
        map.put("owner", this.getOwnerUUIDString());
        map.put("nbtValue", this.getNbtValue());
        map.put("riders", this.getRiders());
        map.put("members", this.getMembers());
        ConfigModule.vehicleDataConfig.getConfig().set(String.format("vehicle.%s", this.getLicensePlate()), map);
        ConfigModule.vehicleDataConfig.save();
    }

    public void delete() throws IllegalStateException {
        FileConfiguration dataConfig = ConfigModule.vehicleDataConfig.getConfig();
        final String path = "vehicle." + this.getLicensePlate();
        if (!dataConfig.isSet(path)) throw new IllegalStateException("An error occurred while trying to delete a vehicle. Vehicle is already deleted.");
        else dataConfig.set(path, null);
    }

    public static String getLicensePlate(ItemStack item){
        NBTItem nbt = new NBTItem(item);
        return nbt.getString("mtvehicles.kenteken");
    }

    public static ItemStack getByUUID(Player p, String uuid) {
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
                        vehicle.setBenzine(100);
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

    public static ItemStack getCar(String carUuid) {
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

    public static String getLicense(Entity entity){
        return TextUtils.licenseReplacer(Objects.requireNonNull(entity.getCustomName()));
    }

    public static String getCarUuid(String plate) {
        if (!existsByPlate(plate)) return null;

        Map<?, ?> vehicleData = ConfigModule.vehicleDataConfig.getConfig()
                .getConfigurationSection(String.format("vehicle.%s", plate)).getValues(true);
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

    public static Vehicle getByPlate(String plate) {
        if (!existsByPlate(plate)) return null;

        Map<?, ?> vehicleData = ConfigModule.vehicleDataConfig.getConfig()
                .getConfigurationSection(String.format("vehicle.%s", plate)).getValues(true);
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
        vehicle.setLicensePlate(plate);
        vehicle.setOpen(false);
        vehicle.setName((String) vehicleData.get("name"));
        vehicle.setVehicleType((String) vehicleData.get("vehicleType"));
        vehicle.setSkinDamage((Integer) vehicleData.get("skinDamage"));
        vehicle.setSkinItem((String) vehicleData.get("skinItem"));
        vehicle.setGlow((Boolean) vehicleData.get("isGlow"));
        vehicle.setHornEnabled(ConfigModule.vehicleDataConfig.isHornSet(plate) ? (boolean) vehicleData.get("hornEnabled") : ConfigModule.vehicleDataConfig.isHornEnabled(plate));
        vehicle.setHealth(ConfigModule.vehicleDataConfig.isHealthSet(plate) ? (double) vehicleData.get("health") : ConfigModule.vehicleDataConfig.getHealth(plate));
        vehicle.setBenzineEnabled((Boolean) vehicleData.get("benzineEnabled"));
        vehicle.setBenzine((Double) vehicleData.get("benzine"));
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

    public static boolean existsByPlate(String plate) {
        return ConfigModule.vehicleDataConfig.getConfig().get(String.format("vehicle.%s", plate)) != null;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public String getName() {
        return name;
    }

    public int getSkinDamage() {
        return skinDamage;
    }

    public String getSkinItem() {
        return skinItem;
    }

    public boolean isGlow() {
        return isGlow;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public boolean isFuelEnabled() {
        return benzineEnabled;
    }

    public boolean isHornEnabled() {
        return hornEnabled;
    }

    public double getHealth(){
        return health;
    }

    public double getFuel() {
        return benzine;
    }

    public boolean isTrunkEnabled() {
        return trunkEnabled;
    }

    public int getTrunkRows() {
        return trunkRows;
    }

    public double getAccelerationSpeed() {
        return acceleratieSpeed;
    }

    public double getMaxSpeed() {
        return maxSpeed;
    }

    public double getBrakingSpeed() {
        return brakingSpeed;
    }

    public double getFrictionSpeed() {
        return frictionSpeed;
    }

    public int getRotateSpeed() {
        return rotateSpeed;
    }

    public double getMaxSpeedBackwards() {
        return maxSpeedBackwards;
    }

    @Deprecated
    public String getOwnerUUIDString() {
        return owner.toString();
    }

    public UUID getOwnerUUID() {
        return owner;
    }

    public String getOwnerName() {
        return Bukkit.getOfflinePlayer(this.getOwnerUUID()).getName();
    }

    public boolean isOwner(OfflinePlayer player){
        return this.owner.equals(player.getUniqueId());
    }

    public String getNbtValue() {
        return nbtValue;
    }

    public List<String> getRiders() {
        return riders;
    }

    public List<String> getMembers() {
        return members;
    }

    public double getFuelUsage() {
        return fuelUsage;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSkinDamage(int skinDamage) {
        this.skinDamage = skinDamage;
    }

    public void setSkinItem(String skinItem) {
        this.skinItem = skinItem;
    }

    public void setGlow(boolean glow) {
        this.isGlow = glow;
    }

    public void setOpen(boolean open) {
        this.isOpen = open;
    }

    public void setBenzineEnabled(boolean benzineEnabled) {
        this.benzineEnabled = benzineEnabled;
    }

    public void setHornEnabled(boolean hornEnabled) {
        this.hornEnabled = hornEnabled;
    }

    public void setHealth(double health) {
        this.health = health;
    }

    public void setBenzine(double benzine) {
        this.benzine = benzine;
    }

    public void setTrunk(boolean trunk) {
        this.trunkEnabled = trunk;
    }

    public void setTrunkRows(int trunkRows) {
        this.trunkRows = trunkRows;
    }

    public List<String> getTrunkData() {
        return kofferbakData;
    }

    public void setTrunkData(List<String> trunkData) {
        this.kofferbakData = trunkData;
    }

    public void setAccelerationSpeed(double accelerationSpeed) {
        this.acceleratieSpeed = accelerationSpeed;
    }

    public void setMaxSpeed(double maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public void setBrakingSpeed(double brakingSpeed) {
        this.brakingSpeed = brakingSpeed;
    }

    public void setFrictionSpeed(double frictionSpeed) {
        this.frictionSpeed = frictionSpeed;
    }

    public void setRotateSpeed(int rotateSpeed) {
        this.rotateSpeed = rotateSpeed;
    }

    public void setMaxSpeedBackwards(double maxSpeedBackwards) {
        this.maxSpeedBackwards = maxSpeedBackwards;
    }

    @Deprecated
    public void setOwner(String ownerUUID) {
        try {
            this.owner = UUID.fromString(ownerUUID);
        } catch (IllegalArgumentException e){
            Main.logSevere("An error occurred while setting a vehicle's owner. This may lead to further issues...");
        }
    }

    public void setOwner(UUID owner){
        this.owner = owner;
    }

    public void setNbtValue(String nbt) {
        this.nbtValue = nbt;
    }

    public void setRiders(List<String> riders) {
        this.riders = riders;
    }

    public void setMembers(List<String> members) {
        this.members = members;
    }

    public void setFuelUsage(double fuelUsage) {
        this.fuelUsage = fuelUsage;
    }

    public Map<?, ?> getVehicleData() {
        return vehicleData;
    }

    public void setVehicleData(Map<?, ?> vehicleData) {
        this.vehicleData = vehicleData;
    }

    public boolean canRide(Player player) {
        return ConfigModule.vehicleDataConfig.getConfig().getStringList("vehicle." + this.licensePlate + ".riders").contains(player.getUniqueId().toString());
    }

    public static boolean canRide(Player player, String licensePlate) {
        return ConfigModule.vehicleDataConfig.getConfig().getStringList("vehicle." + licensePlate + ".riders").contains(player.getUniqueId().toString());
    }

    public boolean canSit(Player player) {
        return ConfigModule.vehicleDataConfig.getConfig().getStringList("vehicle." + this.licensePlate + ".members").contains(player.getUniqueId().toString());
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

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    @Deprecated
    public void setVehicleType(String vehicleType) {
        try {
            this.vehicleType = VehicleType.valueOf(vehicleType.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e){
            Main.logSevere("An error occurred while setting a vehicle's type. Using default (CAR)...");
            this.vehicleType = VehicleType.CAR;
        }
    }

    public void setVehicleType(VehicleType vehicleType){
        this.vehicleType = vehicleType;
    }
}
