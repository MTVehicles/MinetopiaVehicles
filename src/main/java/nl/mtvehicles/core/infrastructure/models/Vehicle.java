package nl.mtvehicles.core.infrastructure.models;

import nl.mtvehicles.core.Main;
import nl.mtvehicles.core.infrastructure.annotations.ToDo;
import nl.mtvehicles.core.infrastructure.dataconfig.VehicleDataConfig;
import nl.mtvehicles.core.infrastructure.enums.VehicleType;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * Vehicle with its specifications
 * @warning <b>This class may be moved in v2.5.0. Bear it in mind if you're using it in your addon.</b>
 */
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

    /**
     * @deprecated There is no use for this HashMap. Please, avoid using it in any way as it may get removed soon.
     */
    @Deprecated
    public static HashMap<String, MTVehicleSubCommand> subcommands = new HashMap<>();

    /**
     * Save the vehicle specifications (and possible adjustments) to vehicleData.yml
     */
    public void save() {
        Map<String, Object> map = new HashMap<>();
        map.put(VehicleDataConfig.Option.NAME.getPath(), this.getName());
        map.put(VehicleDataConfig.Option.VEHICLE_TYPE.getPath(), this.getVehicleType().toString());
        map.put(VehicleDataConfig.Option.SKIN_DAMAGE.getPath(), this.getSkinDamage());
        map.put(VehicleDataConfig.Option.SKIN_ITEM.getPath(), this.getSkinItem());
        map.put(VehicleDataConfig.Option.IS_OPEN.getPath(), this.isOpen());
        map.put(VehicleDataConfig.Option.IS_GLOWING.getPath(), this.isGlow());
        map.put(VehicleDataConfig.Option.FUEL_ENABLED.getPath(), this.isFuelEnabled());
        map.put(VehicleDataConfig.Option.FUEL.getPath(), this.getFuel());
        map.put(VehicleDataConfig.Option.FUEL_USAGE.getPath(), this.getFuelUsage());
        map.put(VehicleDataConfig.Option.HORN_ENABLED.getPath(), this.isHornEnabled());
        map.put(VehicleDataConfig.Option.HEALTH.getPath(), this.getHealth());
        map.put(VehicleDataConfig.Option.TRUNK_ENABLED.getPath(), this.isTrunkEnabled());
        map.put(VehicleDataConfig.Option.TRUNK_ROWS.getPath(), this.getTrunkRows());
        map.put(VehicleDataConfig.Option.TRUNK_DATA.getPath(), this.getTrunkData());
        map.put(VehicleDataConfig.Option.ACCELARATION_SPEED.getPath(), this.getAccelerationSpeed());
        map.put(VehicleDataConfig.Option.MAX_SPEED.getPath(), this.getMaxSpeed());
        map.put(VehicleDataConfig.Option.BRAKING_SPEED.getPath(), this.getBrakingSpeed());
        map.put(VehicleDataConfig.Option.FRICTION_SPEED.getPath(), this.getFrictionSpeed());
        map.put(VehicleDataConfig.Option.ROTATION_SPEED.getPath(), this.getRotateSpeed());
        map.put(VehicleDataConfig.Option.MAX_SPEED_BACKWARDS.getPath(), this.getMaxSpeedBackwards());
        map.put(VehicleDataConfig.Option.OWNER.getPath(), this.getOwnerUUIDString());
        map.put(VehicleDataConfig.Option.NBT_VALUE.getPath(), this.getNbtValue());
        map.put(VehicleDataConfig.Option.RIDERS.getPath(), this.getRiders());
        map.put(VehicleDataConfig.Option.MEMBERS.getPath(), this.getMembers());
        ConfigModule.vehicleDataConfig.getConfig().set(String.format("vehicle.%s", this.getLicensePlate()), map);
        ConfigModule.vehicleDataConfig.save();
    }

    /**
     * Delete a vehicle from the database (vehicleData.yml)
     * @throws IllegalStateException If vehicle is already deleted.
     */
    public void delete() throws IllegalStateException {
        ConfigModule.vehicleDataConfig.delete(this.getLicensePlate());
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

    /**
     * @deprecated Use {@link #getOwnerUUID()} instead.
     */
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

    /**
     * @deprecated Use {@link #setOwner(UUID)} instead.
     */
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
        return ConfigModule.vehicleDataConfig.getRiders(this.licensePlate).contains(player.getUniqueId().toString());
    }

    public boolean canSit(Player player) {
        return ConfigModule.vehicleDataConfig.getMembers(this.licensePlate).contains(player.getUniqueId().toString());
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    /**
     * @deprecated Use {@link #setVehicleType(VehicleType)} instead.
     */
    @Deprecated
    @ToDo("Remove usages")
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
