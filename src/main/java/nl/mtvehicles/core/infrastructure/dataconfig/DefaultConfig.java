package nl.mtvehicles.core.infrastructure.dataconfig;

import nl.mtvehicles.core.infrastructure.helpers.TextUtils;
import nl.mtvehicles.core.infrastructure.models.ConfigUtils;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DefaultConfig extends ConfigUtils {
    public DefaultConfig() {
        this.setFileName("config.yml");
    }

    public String getMessage(String key) {
        return TextUtils.colorize((String) this.getConfig().get(key));
    }

    public boolean isBlockWhitelistEnabled() {
        return getConfig().getBoolean("blockWhitelist.enabled");
    }

    public boolean areGasStationsEnabled(){
        return getConfig().getBoolean("gasStations.enabled");
    }

    public boolean canUseJerryCanOutsideOfGasStation(){
        return getConfig().getBoolean("gasStations.canUseJerryCanOutsideOfGasStation");
    }

    public List<String> gasStationList() {
        return new ArrayList<>(getConfig().getStringList("gasStations.list"));
    }

    public List<Material> blockWhiteList() {
        return getConfig().getStringList("blockWhitelist.list").stream().map(Material::getMaterial).collect(Collectors.toList());
    }

}
