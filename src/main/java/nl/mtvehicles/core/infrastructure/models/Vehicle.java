package nl.mtvehicles.core.infrastructure.models;

import nl.mtvehicles.core.Main;
import nl.mtvehicles.core.infrastructure.enums.VehicleType;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.*;

public class Vehicle {
    private String licensePlate;
    private String name;
    private VehicleType vehicleType;
    private int skinDamage;
    private String skinItem;
    private boolean isGlow;
    private boolean benzineEnabled;
    private double fuel;
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
        return fuel;
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

    public void setFuel(double fuel) {
        this.fuel = fuel;
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

    public boolean canSit(Player player) {
        return ConfigModule.vehicleDataConfig.getConfig().getStringList("vehicle." + this.licensePlate + ".members").contains(player.getUniqueId().toString());
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
