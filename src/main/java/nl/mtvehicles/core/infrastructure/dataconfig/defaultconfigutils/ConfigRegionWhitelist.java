package nl.mtvehicles.core.infrastructure.dataconfig.defaultconfigutils;

import nl.mtvehicles.core.infrastructure.enums.RegionWhitelistAction;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;


public class ConfigRegionWhitelist {
    private FileConfiguration config = ConfigModule.defaultConfig.getConfig();

    public boolean isRegionWhitelistEnabled(RegionWhitelistAction action){
        boolean returns = false;
        switch (action){
            case PLACE:
                returns = config.getBoolean("regionWhitelist.place.enable");
                break;
            case PICKUP:
                returns = config.getBoolean("regionWhitelist.pickup.enable");
                break;
            case ENTER:
                returns = config.getBoolean("regionWhitelist.enter.enable");
                break;
        }
        return returns;
    }

    public List<String> getRegionWhitelist(RegionWhitelistAction action){
        List<String> returns;
        switch (action){
            case PLACE:
                returns = new ArrayList<>(config.getStringList("regionWhitelist.place.list"));
                break;
            case PICKUP:
                returns = new ArrayList<>(config.getStringList("regionWhitelist.pickup.list"));
                break;
            case ENTER:
                returns = new ArrayList<>(config.getStringList("regionWhitelist.enter.list"));
                break;
            default:
                returns = new ArrayList<>();
                break;
        }
        return returns;
    }
}
