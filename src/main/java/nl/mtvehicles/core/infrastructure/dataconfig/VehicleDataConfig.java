package nl.mtvehicles.core.infrastructure.dataconfig;

import nl.mtvehicles.core.infrastructure.models.ConfigUtils;
import nl.mtvehicles.core.infrastructure.models.Vehicle;

public class VehicleDataConfig extends ConfigUtils {
    public VehicleDataConfig() {
        this.setFileName("vehicleData.yml");
    }

    public boolean isHornEnabled(String license){
        final String path = "vehicle." + license + ".hornEnabled";
        if (!isHornSet(license)) setHorn(license);
        return getConfig().getBoolean(path);
    }

    public boolean isHornSet(String license){
        final String path = "vehicle." + license + ".hornEnabled";
        return getConfig().isSet(path);
    }

    public void setHorn(String license){
        final String path = "vehicle." + license + ".hornEnabled";
        final int damage = getConfig().getInt("vehicle." + license + ".skinDamage");
        boolean state = Vehicle.getHornByDamage(damage);
        getConfig().set(path, state);
        save();
    }


    public double getHealth(String license){
        final String path = "vehicle." + license + ".health";
        if (!isHealthSet(license)) setHealth(license);
        return getConfig().getDouble(path);
    }

    public boolean isHealthSet(String license){
        final String path = "vehicle." + license + ".health";
        return getConfig().isSet(path);
    }

    public void setHealth(String license){
        final String path = "vehicle." + license + ".health";
        final int damage = getConfig().getInt("vehicle." + license + ".skinDamage");
        double state = Vehicle.getHealthByDamage(damage);
        getConfig().set(path, state);
    }

    public void damageVehicle(String license, double damage){
        final String path = "vehicle." + license + ".health";
        double h = getHealth(license) - damage;
        final double health = (h > 0) ? h : 0.0;
        getConfig().set(path, health);
        save();
    }
}
