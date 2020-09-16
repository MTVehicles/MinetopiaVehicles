package nl.mtvehicles.core.Infrastructure.Models;

import nl.mtvehicles.core.Main;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Vehicle {
    private String licensePlate;
    private String name;
    private int skinDamage;
    private String skinItem;
    private boolean isGlow;
    private boolean benzineEnabled;
    private double benzine;
    private boolean kofferbak;
    private int kofferbakRows;
    private List<ItemStack> kofferbakData;
    private double acceleratieSpeed;
    private double maxSpeed;
    private double brakingSpeed;
    private double aftrekkenSpeed;
    private int rotateSpeed;
    private double maxSpeedBackwards;
    private UUID owner;
    private List<UUID> riders;
    private List<UUID> members;


    public static void VehicleCreate(String license, int skinDamage, UUID Owner) {



    }

    public static Vehicle getByPlate(String plate) {
        List<Map<?, ?>> vehiclesData = Main.vehicleDataConfig.getConfig().getMapList(plate);

        if (!existsByPlate(plate)) return null;

        Map<?, ?> vehicleData = vehiclesData.get(0);

        Vehicle vehicle = new Vehicle();
        vehicle.setLicensePlate((String) vehicleData.get("licensePlate"));

        return vehicle;
    }

    public static boolean existsByPlate(String plate) {
        List<Map<?, ?>> vehiclesData = Main.vehicleDataConfig.getConfig().getMapList(plate);

        return vehiclesData.size() == 1;
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
    public boolean isBenzineEnabled() {
        return benzineEnabled;
    }
    public double getBenzine() {
        return benzine;
    }
    public boolean isKofferbak() {
        return kofferbak;
    }
    public int getKofferbakRows() {
        return kofferbakRows;
    }
    public double getAcceleratieSpeed() {
        return acceleratieSpeed;
    }
    public double getMaxSpeed() {
        return maxSpeed;
    }
    public double getBrakingSpeed() {
        return brakingSpeed;
    }
    public double getAftrekkenSpeed() {
        return aftrekkenSpeed;
    }
    public int getRotateSpeed() {
        return rotateSpeed;
    }
    public double getMaxSpeedBackwards() {
        return maxSpeedBackwards;
    }
    public UUID getOwner() {
        return owner;
    }
    public List<UUID> getRiders() {
        return riders;
    }
    public List<UUID> getMembers() {
        return members;
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
        isGlow = glow;
    }
    public void setBenzineEnabled(boolean benzineEnabled) {
        this.benzineEnabled = benzineEnabled;
    }
    public void setBenzine(double benzine) {
        this.benzine = benzine;
    }
    public void setKofferbak(boolean kofferbak) {
        this.kofferbak = kofferbak;
    }
    public void setKofferbakRows(int kofferbakRows) {
        this.kofferbakRows = kofferbakRows;
    }
    public List<ItemStack> getKofferbakData() {
        return kofferbakData;
    }
    public void setKofferbakData(List<ItemStack> kofferbakData) {
        this.kofferbakData = kofferbakData;
    }
    public void setAcceleratieSpeed(double acceleratieSpeed) {
        this.acceleratieSpeed = acceleratieSpeed;
    }
    public void setMaxSpeed(double maxSpeed) {
        this.maxSpeed = maxSpeed;
    }
    public void setBrakingSpeed(double brakingSpeed) {
        this.brakingSpeed = brakingSpeed;
    }
    public void setAftrekkenSpeed(double aftrekkenSpeed) {
        this.aftrekkenSpeed = aftrekkenSpeed;
    }
    public void setRotateSpeed(int rotateSpeed) {
        this.rotateSpeed = rotateSpeed;
    }
    public void setMaxSpeedBackwards(double maxSpeedBackwards) {
        this.maxSpeedBackwards = maxSpeedBackwards;
    }
    public void setOwner(UUID owner) {
        this.owner = owner;
    }
    public void setRiders(List<UUID> riders) {
        this.riders = riders;
    }
    public void setMembers(List<UUID> members) {
        this.members = members;
    }


}
