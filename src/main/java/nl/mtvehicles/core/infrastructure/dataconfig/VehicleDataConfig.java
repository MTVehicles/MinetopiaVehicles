package nl.mtvehicles.core.infrastructure.dataconfig;

import nl.mtvehicles.core.infrastructure.helpers.ItemUtils;
import nl.mtvehicles.core.infrastructure.models.ConfigUtils;
import nl.mtvehicles.core.infrastructure.models.Vehicle;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class VehicleDataConfig extends ConfigUtils {
    public VehicleDataConfig() {
        this.setFileName("vehicleData.yml");
    }

    public int getDamage(String license){
        return getConfig().getInt("vehicle." + license + ".skinDamage");
    }

    public boolean isHornEnabled(String license){
        final String path = "vehicle." + license + ".hornEnabled";
        if (!isHornSet(license)) setInitialHorn(license);
        return getConfig().getBoolean(path);
    }

    public boolean isHornSet(String license){
        final String path = "vehicle." + license + ".hornEnabled";
        return getConfig().isSet(path);
    }

    public void setInitialHorn(String license){
        final String path = "vehicle." + license + ".hornEnabled";
        boolean state = Vehicle.getHornByDamage(getDamage(license));
        getConfig().set(path, state);
        save();
    }


    public double getHealth(String license){
        final String path = "vehicle." + license + ".health";
        if (!isHealthSet(license)) setInitialHealth(license);
        return getConfig().getDouble(path);
    }

    public boolean isHealthSet(String license){
        final String path = "vehicle." + license + ".health";
        return getConfig().isSet(path);
    }

    public void setInitialHealth(String license){
        final String path = "vehicle." + license + ".health";
        final int damage = getConfig().getInt("vehicle." + license + ".skinDamage");
        double state = Vehicle.getMaxHealthByDamage(damage);
        getConfig().set(path, state);
        save();
    }

    public void damageVehicle(String license, double damage){
        final String path = "vehicle." + license + ".health";
        double h = getHealth(license) - damage;
        final double health = (h > 0) ? h : 0.0;
        getConfig().set(path, health);
        save();
    }

    public void setHealth(String license, double health){
        final String path = "vehicle." + license + ".health";
        getConfig().set(path, health);
        save();
    }

    public int getNumberOfOwnedVehicles(Player p){
        final String playerUUID = p.getUniqueId().toString();
        int owned = 0;

        List<Map<?, ?>> vehicleData = getConfig().getMapList("vehicle");
        for (Map<?, ?> vehicle : vehicleData) {
            if (String.valueOf(vehicle.get("owner")).equals(playerUUID)) owned++;
        }

        return owned;
    }
}
