package nl.mtvehicles.core.Infrastructure.Models;

import nl.mtvehicles.core.Infrastructure.Helpers.ItemUtils;
import nl.mtvehicles.core.Infrastructure.Helpers.NBTUtils;
import nl.mtvehicles.core.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class Vehicle {
    private String licensePlate;
    private String name;
    private String vehicleType;
    private int skinDamage;
    private String skinItem;
    private boolean isGlow;
    private boolean benzineEnabled;
    private double benzine;
    private double benzineVerbruik;
    private boolean kofferbak;
    private int kofferbakRows;
    private List<String> kofferbakData;
    private double acceleratieSpeed;
    private double maxSpeed;
    private double brakingSpeed;
    private double aftrekkenSpeed;
    private int rotateSpeed;
    private double maxSpeedBackwards;
    private String owner;
    private String nbtValue;
    private List<String> riders;
    private List<String> members;
    private Map<?, ?> vehicleData;

    public static HashMap<String, MTVehicleSubCommand> subcommands = new HashMap<>();

    public void save2() {

        Map<String, Object> map = new HashMap<>();
        map.put("isGlow", this.isGlow());
        Main.vehicleDataConfig.getConfig().set(this.getLicensePlate(), map);
        Main.vehicleDataConfig.save();

    }

    public void save() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", this.getName());
        map.put("vehicleType", this.getVehicleType());
        map.put("skinDamage", this.getSkinDamage());
        map.put("skinItem", this.getSkinItem());
        map.put("isGlow", this.isGlow());
        map.put("benzineEnabled", this.isFuelEnabled());
        map.put("benzine", this.getFuel());
        map.put("benzineVerbruik", this.getFuelUsage());
        map.put("kofferbak", this.isTrunkEnabled());
        map.put("kofferbakRows", this.getTrunkRows());
        map.put("kofferbakData", this.getTrunkData());
        map.put("acceleratieSpeed", this.getAccelerationSpeed());
        map.put("maxSpeed", this.getMaxSpeed());
        map.put("brakingSpeed", this.getBrakingSpeed());
        map.put("aftrekkenSpeed", this.getFrictionSpeed());
        map.put("rotateSpeed", this.getRotateSpeed());
        map.put("maxSpeedBackwards", this.getMaxSpeedBackwards());
        map.put("owner", this.getOwner());
        map.put("nbtValue", this.getNbtValue());
        map.put("riders", this.getRiders());
        map.put("members", this.getMembers());
        Main.vehicleDataConfig.getConfig().set(String.format("vehicle.%s", this.getLicensePlate()), map);
        Main.vehicleDataConfig.save();
    }

    public String getOwnerName() {
        return Bukkit.getOfflinePlayer(UUID.fromString(this.getOwner())).getName();
    }


    public static void getVoucher(int damage, Player p) {
        List<Map<?, ?>> vehicles = Main.vehiclesConfig.getConfig().getMapList("voertuigen");
        List<Map<?, ?>> matchedVehicles = new ArrayList<>();
        for (Map<?, ?> configVehicle : vehicles) {
            List<Map<?, ?>> skins = (List<Map<?, ?>>) configVehicle.get("cars");
            for (Map<?, ?> skin : skins) {
                if (skin.get("itemDamage").equals(damage)) {
                    if (skin.get("itemDamage") == null) {
                        return;
                    }
                    ItemStack is = ItemUtils.carItem2((Integer) skin.get("itemDamage"), ((String) skin.get("name")), (String) skin.get("SkinItem"));
                    String kenteken = NBTUtils.getString((is), "mtvehicles.kenteken");
                    matchedVehicles.add(configVehicle);

                    //ItemUtils.createVoucher(skin.get("itemDamage"), skin.get("SkinItem"), skin.get("name"), p);
//                    Vehicle vehicle = new Vehicle();
//                    List<String> members = Main.vehicleDataConfig.getConfig().getStringList("voertuig." + kenteken + ".members");
//                    List<String> riders = Main.vehicleDataConfig.getConfig().getStringList("voertuig." + kenteken + ".riders");
//                    List<String> kof = Main.vehicleDataConfig.getConfig().getStringList("voertuig." + kenteken + ".kofferbakData");
//                    vehicle.setLicensePlate(kenteken);
//                    vehicle.setName((String) skin.get("name"));
//                    vehicle.setVehicleType((String) configVehicle.get("vehicleType"));
//                    vehicle.setSkinDamage((Integer) skin.get("itemDamage"));
//                    vehicle.setSkinItem((String) skin.get("SkinItem"));
//                    vehicle.setGlow(false);
//                    vehicle.setBenzineEnabled((Boolean) configVehicle.get("benzineEnabled"));
//                    vehicle.setBenzine(100);
//                    vehicle.setKofferbak((Boolean) configVehicle.get("kofferbakEnabled"));
//                    vehicle.setKofferbakRows(1);
//                    vehicle.setBenzineVerbruik(0.01);
//                    vehicle.setKofferbakData(kof);
//                    vehicle.setAcceleratieSpeed((Double) configVehicle.get("acceleratieSpeed"));
//                    vehicle.setMaxSpeed((Double) configVehicle.get("maxSpeed"));
//                    vehicle.setBrakingSpeed((Double) configVehicle.get("brakingSpeed"));
//                    vehicle.setAftrekkenSpeed((Double) configVehicle.get("aftrekkenSpeed"));
//                    vehicle.setRotateSpeed((Integer) configVehicle.get("rotateSpeed"));
//                    vehicle.setMaxSpeedBackwards((Double) configVehicle.get("maxSpeedBackwards"));
//                    vehicle.setOwner(p.getUniqueId().toString());
//                    vehicle.setRiders(riders);
//                    vehicle.setMembers(members);
//                    vehicle.save();
                    //p.getInventory().addItem(is);
                }
            }
        }
    }


    public static void getByDamage(int damage, Player p) {
        List<Map<?, ?>> vehicles = Main.vehiclesConfig.getConfig().getMapList("voertuigen");
        List<Map<?, ?>> matchedVehicles = new ArrayList<>();
        for (Map<?, ?> configVehicle : vehicles) {
            List<Map<?, ?>> skins = (List<Map<?, ?>>) configVehicle.get("cars");
            for (Map<?, ?> skin : skins) {
                if (skin.get("itemDamage").equals(damage)) {
                    if (skin.get("itemDamage") == null) {
                        return;
                    }
                    ItemStack is = ItemUtils.carItem2((Integer) skin.get("itemDamage"), ((String) skin.get("name")), (String) skin.get("SkinItem"));
                    String kenteken = NBTUtils.getString((is), "mtvehicles.kenteken");
                    matchedVehicles.add(configVehicle);
                    Vehicle vehicle = new Vehicle();
                    List<String> members = Main.vehicleDataConfig.getConfig().getStringList("voertuig." + kenteken + ".members");
                    List<String> riders = Main.vehicleDataConfig.getConfig().getStringList("voertuig." + kenteken + ".riders");
                    List<String> kof = Main.vehicleDataConfig.getConfig().getStringList("voertuig." + kenteken + ".kofferbakData");
                    vehicle.setLicensePlate(kenteken);
                    vehicle.setName((String) skin.get("name"));
                    vehicle.setVehicleType((String) configVehicle.get("vehicleType"));
                    vehicle.setSkinDamage((Integer) skin.get("itemDamage"));
                    vehicle.setSkinItem((String) skin.get("SkinItem"));
                    vehicle.setGlow(false);
                    vehicle.setBenzineEnabled((Boolean) configVehicle.get("benzineEnabled"));
                    vehicle.setBenzine(100);
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
                    vehicle.save();
                    p.getInventory().addItem(is);
                }
            }
        }
    }

    public static Vehicle getByPlate(String plate) {
        if (!existsByPlate(plate)) return null;

        Map<?, ?> vehicleData = Main.vehicleDataConfig.getConfig()
                .getConfigurationSection(String.format("vehicle.%s", plate)).getValues(true);
        List<Map<?, ?>> vehicles = Main.vehiclesConfig.getConfig().getMapList("voertuigen");
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

        if (matchedVehicles == null) return null;
        if (matchedVehicles.size() == 0) return null;
        if (matchedVehicles.size() > 1) return null;
        Vehicle vehicle = new Vehicle();
        vehicle.setVehicleData(matchedVehicles.get(0));
        vehicle.setLicensePlate(plate);
        vehicle.setName((String) vehicleData.get("name"));
        vehicle.setVehicleType((String) vehicleData.get("vehicleType"));
        vehicle.setSkinDamage((Integer) vehicleData.get("skinDamage"));
        vehicle.setSkinItem((String) vehicleData.get("skinItem"));
        vehicle.setGlow((Boolean) vehicleData.get("isGlow"));
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

    public boolean isFuelEnabled() {
        return benzineEnabled;
    }

    public double getFuel() {
        return benzine;
    }

    public boolean isTrunkEnabled() {
        return kofferbak;
    }

    public int getTrunkRows() {
        return kofferbakRows;
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

    public void setTrunk(boolean trunk) {
        this.kofferbak = trunk;
    }

    public void setTrunkRows(int trunkRows) {
        this.kofferbakRows = trunkRows;
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

    public void setFrictionSpeed(double aftrekkenSpeed) {
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

    public void setNbtValue(String nbt) {
        this.nbtValue = nbt;
    }

    public void setRiders(List<String> riders) {
        this.riders = riders;
    }

    public void setMembers(List<String> members) {
        this.members = members;
    }

    public void setFuelUsage(double benzineVerbruik) {
        this.benzineVerbruik = benzineVerbruik;
    }

    public Map<?, ?> getVehicleData() {
        return vehicleData;
    }

    public void setVehicleData(Map<?, ?> vehicleData) {
        this.vehicleData = vehicleData;
    }

    public boolean canRide(Player p) {
        return Main.vehicleDataConfig.getConfig().getStringList("vehicle." + this.licensePlate + ".riders").contains(p.getUniqueId().toString());
    }

    public static boolean canRide(Player p, String ken) {
        return Main.vehicleDataConfig.getConfig().getStringList("vehicle." + ken + ".riders").contains(p.getUniqueId().toString());
    }

    public boolean canSit(Player p) {
        return Main.vehicleDataConfig.getConfig().getStringList("vehicle." + this.licensePlate + ".members").contains(p.getUniqueId().toString());
    }

    public static boolean canSit(Player p, String ken) {
        return Main.vehicleDataConfig.getConfig().getStringList("vehicle." + ken + ".members").contains(p.getUniqueId().toString());
    }

    public static UUID getOwner(String plate) {
        if (Main.vehicleDataConfig.getConfig().getString("vehicle." + plate + ".owner") == null) {
            return null;
        }
        return UUID.fromString(Main.vehicleDataConfig.getConfig().getString("vehicle." + plate + ".owner"));
    }

    public static String getRidersAsString(String plate) {
        StringBuilder sb = new StringBuilder();
        for (String s : Main.vehicleDataConfig.getConfig().getStringList("vehicle." + plate + ".riders")) {
            if (!UUID.fromString(s).equals(getOwner(plate))) {
                sb.append(Bukkit.getOfflinePlayer(UUID.fromString(s)).getName()).append(", ");
            }
        }
        if (sb.toString().isEmpty()) {
            sb.append("Niemand");
        }
        return sb.toString();
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }
}
