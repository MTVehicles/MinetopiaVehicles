package nl.mtvehicles.core.infrastructure.dataconfig;

import nl.mtvehicles.core.infrastructure.enums.RegionWhitelistAction;
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

    //--- Gas stations ---
    public boolean areGasStationsEnabled(){
        return getConfig().getBoolean("gasStations.enabled");
    }

    public boolean canUseJerryCanOutsideOfGasStation(){
        return getConfig().getBoolean("gasStations.canUseJerryCanOutsideOfGasStation");
    }

    public List<String> gasStationList() {
        return new ArrayList<>(getConfig().getStringList("gasStations.list"));
    }


    //--- Block Whitelist ---
    public boolean isBlockWhitelistEnabled() {
        return getConfig().getBoolean("blockWhitelist.enabled");
    }

    public List<Material> blockWhiteList() {
        return getConfig().getStringList("blockWhitelist.list").stream().map(Material::getMaterial).collect(Collectors.toList());
    }

    //--- Region Whitelist ---
    public boolean isRegionWhitelistEnabled(RegionWhitelistAction action){
        boolean returns = false;
        switch (action){
            case PLACE:
                returns = getConfig().getBoolean("regionWhitelist.place.enable");
                break;
            case PICKUP:
                returns = getConfig().getBoolean("regionWhitelist.pickup.enable");
                break;
            case ENTER:
                returns = getConfig().getBoolean("regionWhitelist.enter.enable");
                break;
        }
        return returns;
    }

    public List<String> regionWhitelist(RegionWhitelistAction action){
        List<String> returns;
        switch (action){
            case PLACE:
                returns = new ArrayList<>(getConfig().getStringList("regionWhitelist.place.list"));
                break;
            case PICKUP:
                returns = new ArrayList<>(getConfig().getStringList("regionWhitelist.pickup.list"));
                break;
            case ENTER:
                returns = new ArrayList<>(getConfig().getStringList("regionWhitelist.enter.list"));
                break;
            default:
                returns = new ArrayList<>();
                break;
        }
        return returns;
    }

}
