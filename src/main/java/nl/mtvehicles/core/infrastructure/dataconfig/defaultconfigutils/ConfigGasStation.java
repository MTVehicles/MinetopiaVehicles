package nl.mtvehicles.core.infrastructure.dataconfig.defaultconfigutils;

import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

public class ConfigGasStation {
    private FileConfiguration config = ConfigModule.defaultConfig.getConfig();

    public boolean areGasStationsEnabled(){
        return config.getBoolean("gasStations.enabled");
    }

    public boolean canUseJerryCanOutsideOfGasStation(){
        return config.getBoolean("gasStations.canUseJerryCanOutsideOfGasStation");
    }

    public List<String> gasStationList() {
        return new ArrayList<>(config.getStringList("gasStations.list"));
    }
}
