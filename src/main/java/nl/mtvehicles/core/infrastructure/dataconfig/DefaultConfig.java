package nl.mtvehicles.core.infrastructure.dataconfig;

import nl.mtvehicles.core.infrastructure.enums.DriveUp;
import nl.mtvehicles.core.infrastructure.enums.RegionWhitelistAction;
import nl.mtvehicles.core.infrastructure.helpers.TextUtils;
import nl.mtvehicles.core.infrastructure.models.ConfigUtils;
import nl.mtvehicles.core.infrastructure.modules.DependencyModule;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class DefaultConfig extends ConfigUtils {
    public DefaultConfig() {
        this.setFileName("config.yml");
    }

    public String getMessage(String key) {
        return TextUtils.colorize((String) this.getConfig().get(key));
    }

    //--- DriveUp ---
    public DriveUp driveUpSlabs(){
        DriveUp returns = DriveUp.BOTH; //default value
        try {
            switch (Objects.requireNonNull(getConfig().getString("driveUp"))){
                case "blocks": case "block":
                    returns = DriveUp.BLOCKS; break;
                case "slabs": case "slab":
                    returns = DriveUp.SLABS; break;
            }
        } catch (NullPointerException e){
            //driveUp was not set up correctly in config and thus it's using the default = both.
        }
        return returns;
    }

    //--- Gas stations ---
    public boolean areGasStationsEnabled(){
        if (!DependencyModule.isDependencyEnabled("WorldGuard")) return false; //If WorldGuard isn't installed, say it's not enabled.

        return getConfig().getBoolean("gasStations.enabled");
    }

    public boolean canUseJerryCanOutsideOfGasStation(){
        return getConfig().getBoolean("gasStations.canUseJerryCanOutsideOfGasStation");
    }

    public boolean isFillJerryCansEnabled(){
        if (!areGasStationsEnabled()) return false;

        return getConfig().getBoolean("gasStations.fillJerryCans.enabled");
    }

    public boolean hasFillJerryCansPermission(Player p){
        if (!getConfig().getBoolean("gasStations.fillJerryCans.needPermission")) return true; //if there's set that they don't need the permission, just pretend they have it

        if (p.hasPermission("mtvehicles.filljerrycans")) return true;
        else return false;
    }

    public boolean isFillJerryCansLeverEnabled(){
        return getConfig().getBoolean("gasStations.fillJerryCans.lever");
    }

    public boolean isFillJerryCansTripwireHookEnabled(){
        return getConfig().getBoolean("gasStations.fillJerryCans.tripwireHook");
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
