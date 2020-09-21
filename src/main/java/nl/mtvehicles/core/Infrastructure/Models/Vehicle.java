package nl.mtvehicles.core.Infrastructure.Models;

import nl.mtvehicles.core.Infrastructure.Helpers.NBTUtils;
import nl.mtvehicles.core.Main;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class Vehicle {
    private String licensePlate;
    private String name;
    private int skinDamage;
    private String skinItem;
    private boolean isGlow;
    private boolean benzineEnabled;
    private double benzine;
    private double benzineVerbruik;
    private boolean kofferbak;
    private int kofferbakRows;
    private List<ItemStack> kofferbakData;
    private double acceleratieSpeed;
    private double maxSpeed;
    private double brakingSpeed;
    private double aftrekkenSpeed;
    private int rotateSpeed;
    private double maxSpeedBackwards;
    private String owner;
    private List<String> riders;
    private List<String> members;
    private Map<?, ?> vehicleData;


    public void save2() {

        Map<String, Object> map = new HashMap<>();
        map.put("isGlow", this.isGlow());

        Main.vehicleDataConfig.getConfig().set(this.getLicensePlate(), map);
        Main.vehicleDataConfig.save();

    }

    public void save() {

        Map<String, Object> map = new HashMap<>();
        map.put("name", this.getName());
        map.put("skinDamage", this.getSkinDamage());
        map.put("skinItem", this.getSkinItem());
        map.put("isGlow", this.isGlow());
        map.put("benzineEnabled", this.isBenzineEnabled());
        map.put("benzine", this.getBenzine());
        map.put("benzineVerbruik", this.getBenzineVerbruik());
        map.put("kofferbak", this.isKofferbak());
        map.put("kofferbakRows", this.getKofferbakRows());
        map.put("kofferbakData", this.getKofferbakData());
        map.put("acceleratieSpeed", this.getAcceleratieSpeed());
        map.put("maxSpeed", this.getMaxSpeed());
        map.put("brakingSpeed", this.getBrakingSpeed());
        map.put("aftrekkenSpeed", this.getAftrekkenSpeed());
        map.put("rotateSpeed", this.getRotateSpeed());
        map.put("maxSpeedBackwards", this.getMaxSpeedBackwards());
        map.put("owner", this.getOwner());
        map.put("riders", this.getRiders());
        map.put("members", this.getMembers());

        Main.vehicleDataConfig.getConfig().set(this.getLicensePlate(), map);
        Main.vehicleDataConfig.save();

    }

    public static Vehicle getByPlate(String plate) {
        if (!existsByPlate(plate)) return null;

        ConfigurationSection section = Main.vehicleDataConfig.getConfig().getConfigurationSection(String.format("vehicle.%s", plate));

        Map<?, ?> vehicleData = section.getValues(true);

        List<Map<?, ?>> vehicles = Main.vehiclesConfig.getConfig().getMapList("voertuigen");

        List<Map<?, ?>> matchedVehicles = new ArrayList<>();

        for (Map<?, ?> configVehicle : vehicles) {
            List<Map<?, ?>> skins = (List<Map<?, ?>>) configVehicle.get("cars");
            for (Map<?, ?> skin : skins) {
                if (skin.get("itemDamage").equals(vehicleData.get("skinDamage"))) {
                    matchedVehicles.add(configVehicle);
                }
            }
        }

        if (matchedVehicles == null) return null;

        if (matchedVehicles.size() == 0) return null;

        if (matchedVehicles.size() > 1) return null;

        Vehicle vehicle = new Vehicle();

        vehicle.setVehicleData(matchedVehicles.get(0));

        vehicle.setLicensePlate((String) vehicleData.get("licensePlate"));
        vehicle.setName((String) vehicleData.get("name"));
        vehicle.setSkinDamage((int) vehicleData.get("skinDamage"));
        vehicle.setSkinItem((String) vehicleData.get("skinItem"));
        vehicle.setGlow((boolean) vehicleData.get("isGlow"));
        vehicle.setBenzineEnabled((boolean) vehicleData.get("benzineEnabled"));
        vehicle.setBenzine((double) vehicleData.get("benzine"));
        vehicle.setKofferbak((boolean) vehicleData.get("kofferbak"));
        vehicle.setKofferbakRows((int) vehicleData.get("kofferbakRows"));
        vehicle.setKofferbakData((List<ItemStack>) vehicleData.get("kofferbakData"));
        vehicle.setAcceleratieSpeed((double) vehicleData.get("acceleratieSpeed"));
        vehicle.setMaxSpeed((double) vehicleData.get("maxSpeed"));
        vehicle.setBrakingSpeed((double) vehicleData.get("brakingSpeed"));
        vehicle.setAftrekkenSpeed((double) vehicleData.get("aftrekkenSpeed"));
        vehicle.setRotateSpeed((int) vehicleData.get("rotateSpeed"));
        vehicle.setMaxSpeedBackwards((double) vehicleData.get("maxSpeedBackwards"));
        vehicle.setOwner((String) vehicleData.get("owner"));
        vehicle.setRiders((List<String>) vehicleData.get("riders"));
        vehicle.setMembers((List<String>) vehicleData.get("members"));

        return vehicle;
    }

    public static boolean existsByPlate(String plate) {
        return Main.vehicleDataConfig.getConfig().get(String.format("vehicle.%s", plate)) != null;
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

    public String getOwner() {
        return owner;
    }

    public List<String> getRiders() {
        return riders;
    }

    public List<String> getMembers() {
        return members;
    }

    public double getBenzineVerbruik() {
        return benzineVerbruik;
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

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public void setRiders(List<String> riders) {
        this.riders = riders;
    }

    public void setMembers(List<String> members) {
        this.members = members;
    }

    public void setBenzineVerbruik(double benzineVerbruik) {
        this.benzineVerbruik = benzineVerbruik;
    }


    public Map<?, ?> getVehicleData() {
        return vehicleData;
    }

    public void setVehicleData(Map<?, ?> vehicleData) {
        this.vehicleData = vehicleData;
    }


    public static boolean canRide(Player p, String ken) {
        return Main.vehicleDataConfig.getConfig().getStringList("vehicle." + ken + ".riders").contains(p.getUniqueId().toString());
    }

    public static boolean canSit(Player p, String ken) {
        return Main.vehicleDataConfig.getConfig().getStringList("vehicle." + ken + ".members").contains(p.getUniqueId().toString());
    }

}
