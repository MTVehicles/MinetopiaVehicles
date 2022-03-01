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
    }
}
