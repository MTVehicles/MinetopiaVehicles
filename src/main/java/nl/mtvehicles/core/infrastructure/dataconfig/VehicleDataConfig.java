package nl.mtvehicles.core.infrastructure.dataconfig;

import nl.mtvehicles.core.Main;
import nl.mtvehicles.core.infrastructure.enums.ConfigType;
import nl.mtvehicles.core.infrastructure.enums.VehicleType;
import nl.mtvehicles.core.infrastructure.models.Config;
import nl.mtvehicles.core.infrastructure.models.VehicleUtils;
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
     * @param value New value of the option (should be the same type, otherwise warning is logged)
     */
    public void set(String licensePlate, Option dataOption, Object value){
        if (!value.getClass().isInstance(dataOption.getValueType())) Main.logWarning("Setting a vehicle data option of an incorrect type. This may lead to further issues...");
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


    public int getDamage(String licensePlate){
        return (int) get(licensePlate, Option.SKIN_DAMAGE);
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
        NAME("name", String.class),
        VEHICLE_TYPE("vehicleType", String.class),
        SKIN_ITEM("skinItem", String.class),
        SKIN_DAMAGE("skinDamage", int.class),
        OWNER("owner", String.class),
        RIDERS("riders", List.class),
        MEMBERS("members", List.class),
        /**
         * Can be found as 'benzineEnabled' in vehicleData.yml
         */
        FUEL_ENABLED("benzineEnabled", boolean.class),
        /**
         * Can be found as 'fuel' in vehicleData.yml
         */
        FUEL("benzine", double.class),
        /**
         * Can be found as 'benzineVerbruik' in vehicleData.yml
         */
        FUEL_USAGE("benzineVerbruik", double.class),
        BRAKING_SPEED("brakingSpeed", double.class),
        /**
         * Can be found as 'aftrekkenSpeed' in vehicleData.yml
         */
        FRICTION_SPEED("aftrekkenSpeed", double.class),
        ACCELARATION_SPEED("acceleratieSpeed", double.class),
        MAX_SPEED("maxSpeed", double.class),
        MAX_SPEED_BACKWARDS("maxSpeedBackwards", double.class),
        ROTATE_SPEED("rotateSpeed", int.class),
        /**
         * Can be found as 'kofferbak' in vehicleData.yml
         */
        TRUNK_ENABLED("kofferbak", boolean.class),
        /**
         * Can be found as 'kofferbakRows' in vehicleData.yml
         */
        TRUNK_ROWS("kofferbakRows", int.class),
        /**
         * Can be found as 'kofferbakData' in vehicleData.yml
         */
        TRUNK_DATA("kofferbakData", List.class),
        IS_OPEN("isOpen", boolean.class),
        IS_GLOWING("isGlow", boolean.class),
        HORN_ENABLED("hornEnabled", boolean.class),
        HEALTH("health", double.class);

        final private String path;
        final private Class type;

        private Option(String path, Class type){
            this.path = path;
            this.type = type;
        }

        /**
         * Get string path of option
         * @return Path of option
         */
        public String getPath() {
            return path;
        }

        /**
         * Get (default) type of option
         * @return Default type of option (as class)
         */
        public Class getValueType() {
            return this.type;
        }
    }
}
