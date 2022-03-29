package nl.mtvehicles.core.infrastructure.dataconfig;

import nl.mtvehicles.core.Main;
import nl.mtvehicles.core.infrastructure.enums.ConfigType;
import nl.mtvehicles.core.infrastructure.enums.VehicleType;
import nl.mtvehicles.core.infrastructure.models.Config;
import nl.mtvehicles.core.infrastructure.models.VehicleUtils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Locale;
import java.util.Map;

public class VehicleDataConfig extends Config {
    public VehicleDataConfig() {
        super(ConfigType.VEHICLE_DATA);
    }

    /**
     * Get a data option of a vehicle from vehicleData
     *
     * @param licensePlate Vehicle's license plate
     * @param dataOption Data option of a vehicle
     *
     * @return Value of the option (as Object)
     */
    public Object get(String licensePlate, Option dataOption){
        return this.getConfiguration().get(String.format("vehicle.%s.%s", licensePlate, dataOption.getPath()));
    }

    /**
     * Set a data option of a vehicle to vehicleData
     *
     * @param licensePlate Vehicle's license plate
     * @param dataOption Data option of a vehicle
     * @param value New value of the option (should be the same type!)
     */
    public void set(String licensePlate, Option dataOption, Object value){
        this.getConfiguration().set(String.format("vehicle.%s.%s", licensePlate, dataOption.getPath()), value);
        save();
    }

    /**
     * Delete a vehicle from vehicleData
     *
     * @param licensePlate Vehicle's license plate
     */
    public void delete(String licensePlate){
        getConfiguration().set("vehicle." + licensePlate, null);
        save();
    }


    /**
     * Whether the vehicleData.yml file is empty
     */
    public boolean isEmpty(){
        return getConfiguration().getConfigurationSection("vehicle") == null;
    }

    public ConfigurationSection getVehicles(){
        return getConfiguration().getConfigurationSection("vehicle");
    }


    public int getDamage(String licensePlate){
        return (int) get(licensePlate, Option.SKIN_DAMAGE);
    }

    public List<String> getMembers(String licensePlate){
        return (List<String>) get(licensePlate, Option.MEMBERS);
    }

    public List<String> getRiders(String licensePlate){
        return (List<String>) get(licensePlate, Option.RIDERS);
    }

    public List<String> getTrunkData(String licensePlate){
        return (List<String>) get(licensePlate, Option.TRUNK_DATA);
    }

    public VehicleType getType(String licensePlate){
        try {
            return VehicleType.valueOf(get(licensePlate, Option.VEHICLE_TYPE).toString().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e){
            Main.logSevere("An error occurred while setting a vehicle's type. Using default (CAR)...");
            return VehicleType.CAR;
        }
    }


    public boolean isHornEnabled(String license){
        final String path = "vehicle." + license + ".hornEnabled";
        if (!isHornSet(license)) setInitialHorn(license);
        return getConfiguration().getBoolean(path);
    }

    public boolean isHornSet(String license){
        final String path = "vehicle." + license + ".hornEnabled";
        return getConfiguration().isSet(path);
    }

    public void setInitialHorn(String license){
        final String path = "vehicle." + license + ".hornEnabled";
        boolean state = VehicleUtils.getHornByDamage(getDamage(license));
        getConfiguration().set(path, state);
        save();
    }


    public double getHealth(String license){
        final String path = "vehicle." + license + ".health";
        if (!isHealthSet(license)) setInitialHealth(license);
        return getConfiguration().getDouble(path);
    }

    public boolean isHealthSet(String license){
        final String path = "vehicle." + license + ".health";
        return getConfiguration().isSet(path);
    }

    public void setInitialHealth(String license){
        final String path = "vehicle." + license + ".health";
        final int damage = getConfiguration().getInt("vehicle." + license + ".skinDamage");
        double state = VehicleUtils.getMaxHealthByDamage(damage);
        getConfiguration().set(path, state);
        save();
    }

    public void damageVehicle(String license, double damage){
        final String path = "vehicle." + license + ".health";
        double h = getHealth(license) - damage;
        final double health = (h > 0) ? h : 0.0;
        getConfiguration().set(path, health);
        save();
    }

    public void setHealth(String license, double health){
        final String path = "vehicle." + license + ".health";
        getConfiguration().set(path, health);
        save();
    }

    public int getNumberOfOwnedVehicles(Player p){
        final String playerUUID = p.getUniqueId().toString();
        int owned;
        Map<String, Object> vehicleData = getConfiguration().getValues(true);
        owned = (int) vehicleData.keySet().stream().filter(key -> key.contains(".owner") && String.valueOf(vehicleData.get(key)).equals(playerUUID)).count();
        return owned;
    }

    /**
     * Options available in vehicle data file
     */
    public enum Option {
        NAME("name"),
        VEHICLE_TYPE("vehicleType"),
        SKIN_ITEM("skinItem"),
        SKIN_DAMAGE("skinDamage"),
        OWNER("owner"),
        RIDERS("riders"),
        MEMBERS("members"),
        /**
         * Can be found as 'benzineEnabled' in vehicleData.yml
         */
        FUEL_ENABLED("benzineEnabled"),
        /**
         * Can be found as 'fuel' in vehicleData.yml
         */
        FUEL("benzine"),
        /**
         * Can be found as 'benzineVerbruik' in vehicleData.yml
         */
        FUEL_USAGE("benzineVerbruik"),
        BRAKING_SPEED("brakingSpeed"),
        /**
         * Can be found as 'aftrekkenSpeed' in vehicleData.yml
         */
        FRICTION_SPEED("aftrekkenSpeed"),
        ACCELARATION_SPEED("acceleratieSpeed"),
        MAX_SPEED("maxSpeed"),
        MAX_SPEED_BACKWARDS("maxSpeedBackwards"),
        ROTATE_SPEED("rotateSpeed"),
        /**
         * Can be found as 'kofferbak' in vehicleData.yml
         */
        TRUNK_ENABLED("kofferbak"),
        /**
         * Can be found as 'kofferbakRows' in vehicleData.yml
         */
        TRUNK_ROWS("kofferbakRows"),
        /**
         * Can be found as 'kofferbakData' in vehicleData.yml
         */
        TRUNK_DATA("kofferbakData"),
        IS_OPEN("isOpen"),
        IS_GLOWING("isGlow"),
        HORN_ENABLED("hornEnabled"),
        HEALTH("health"),
        NBT_VALUE("nbtValue");

        final private String path;

        private Option(String path){
            this.path = path;
        }

        /**
         * Get string path of option
         * @return Path of option
         */
        public String getPath() {
            return path;
        }
    }
}
