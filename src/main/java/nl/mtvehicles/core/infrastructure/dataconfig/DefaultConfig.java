package nl.mtvehicles.core.infrastructure.dataconfig;

import nl.mtvehicles.core.infrastructure.enums.DriveUp;
import nl.mtvehicles.core.infrastructure.enums.RegionAction;
import nl.mtvehicles.core.infrastructure.helpers.TextUtils;
import nl.mtvehicles.core.infrastructure.models.ConfigUtils;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import nl.mtvehicles.core.infrastructure.modules.DependencyModule;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

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
            switch (Objects.requireNonNull(getConfig().getString("driveUp").toLowerCase())){
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

    GasStationConfig gasStations = new GasStationConfig();

    public boolean canUseJerryCan(Location loc){
        if (!gasStations.areGasStationsEnabled()) return true;

        //If a player is in region with 'mtvgasstation=deny', they can't use jerrycans.
        if (DependencyModule.worldGuard.isInRegionWithFlag(loc, "mtv-gasstation", false)) return false;

        if (gasStations.canUseJerryCanOutsideOfGasStation()) return true;

        return gasStations.isInsideGasStation(loc);
    }

    public boolean canUseJerryCan(Player p){
        return canUseJerryCan(p.getLocation());
    }

    public boolean canFillJerryCans(Player p, Location loc){
        if (!gasStations.areGasStationsEnabled()) return false;
        if (!gasStations.isFillJerryCansEnabled()) return false;
        if (!gasStations.isInsideGasStation(loc)) return false;

        if (!gasStations.hasFillJerryCansPermission(p)){
            p.sendMessage(TextUtils.colorize(ConfigModule.messagesConfig.getMessage("noPerms")));
            return false;
        } return true;
    }

    public boolean jerryCanPlaySound(){
        return getConfig().getBoolean("gasStations.fillJerryCans.playSound");
    }

    private class GasStationConfig {

        private boolean areGasStationsEnabled(){
            if (!DependencyModule.isDependencyEnabled("WorldGuard")) return false; //If WorldGuard isn't installed, say it's not enabled.

            return getConfig().getBoolean("gasStations.enabled");
        }

        private boolean canUseJerryCanOutsideOfGasStation(){
            return getConfig().getBoolean("gasStations.canUseJerryCanOutsideOfGasStation");
        }

        private boolean isInsideGasStation(Location loc){
            return DependencyModule.worldGuard.isInRegionWithFlag(loc, "mtv-gasstation", true);
        }

        private boolean isFillJerryCansEnabled(){
            return getConfig().getBoolean("gasStations.fillJerryCans.enabled");
        }

        private boolean hasFillJerryCansPermission(Player p){
            if (!getConfig().getBoolean("gasStations.fillJerryCans.needPermission")) return true; //if there's set that they don't need the permission, just pretend they have it
            return p.hasPermission("mtvehicles.filljerrycans");
        }

    }

    public boolean isFillJerryCansLeverEnabled(){
        return getConfig().getBoolean("gasStations.fillJerryCans.lever");
    }

    public boolean isFillJerryCansTripwireHookEnabled(){
        return getConfig().getBoolean("gasStations.fillJerryCans.tripwireHook");
    }

    public boolean isFillJerryCanPriceEnabled(){
        if (!gasStations.areGasStationsEnabled()) return false;
        if (!gasStations.isFillJerryCansEnabled()) return false;
        if (!DependencyModule.isDependencyEnabled("Vault")) return false; //If Vault isn't installed, say it's not enabled.
        if (!DependencyModule.vault.isEconomySetUp()) return false; //There is no Vault Economy plugin, disable it.

        return getConfig().getBoolean("gasStations.fillJerryCans.price.enabled");
    }

    public double getFillJerryCanPrice(){
        if (getConfig().getDouble("gasStations.fillJerryCans.price.pricePerLitre") <= 0) return 30.0; //Default, if it's not greater than 0
        else return getConfig().getDouble("gasStations.fillJerryCans.price.pricePerLitre");
    }

    //--- Disabled Worlds ---
    public boolean isWorldDisabled(String worldName){
        if (getDisabledWorlds().isEmpty()) return false;
        return getDisabledWorlds().contains(worldName);
    }

    private List<String> getDisabledWorlds(){
        return getConfig().getStringList("disabledWorlds");
    }

    //--- Block Whitelist ---
    public boolean isBlockWhitelistEnabled() {
        return getConfig().getBoolean("blockWhitelist.enabled");
    }

    public List<Material> blockWhiteList() {
        return getConfig().getStringList("blockWhitelist.list").stream().map(Material::getMaterial).collect(Collectors.toList());
    }

    //--- Region Actions ---

    private RegionAction.ListType getRegionActionListType(RegionAction action){
        String configOption = "disabled"; //Default
        switch (action){
            case PLACE:
                configOption = getConfig().getString("regionActions.place");
                break;
            case PICKUP:
                configOption = getConfig().getString("regionActions.pickup");
                break;
            case ENTER:
                configOption = getConfig().getString("regionActions.enter");
                break;
        }
        if (configOption.equalsIgnoreCase("whitelist")) return RegionAction.ListType.WHITELIST;
        else if (configOption.equalsIgnoreCase("blacklist")) return RegionAction.ListType.BLACKLIST;
        else return RegionAction.ListType.DISABLED;
    }

    public boolean canProceedWithAction(RegionAction action, Location loc){
        if (isWorldDisabled(loc.getWorld().getName())) return false;

        if (!DependencyModule.isDependencyEnabled("WorldGuard")) return true;

        boolean returns = true;
        RegionAction.ListType listType = getRegionActionListType(action);
        if (!listType.isEnabled()) return true;

        boolean isWhitelist = listType.isWhitelist();
        boolean isBlacklist = listType.isBlacklist();
        switch (action){
            case PLACE:
                if (isWhitelist)
                    returns = DependencyModule.worldGuard.isInRegionWithFlag(loc, "mtv-place", true);
                else if (isBlacklist)
                    returns = !DependencyModule.worldGuard.isInRegionWithFlag(loc, "mtv-place", false);
                break;
            case PICKUP:
                if (isWhitelist)
                    returns = DependencyModule.worldGuard.isInRegionWithFlag(loc, "mtv-pickup", true);
                else if (isBlacklist)
                    returns = !DependencyModule.worldGuard.isInRegionWithFlag(loc, "mtv-pickup", false);
                break;
            case ENTER:
                if (isWhitelist)
                    returns = DependencyModule.worldGuard.isInRegionWithFlag(loc, "mtv-enter", true);
                else if (isBlacklist)
                    returns = !DependencyModule.worldGuard.isInRegionWithFlag(loc, "mtv-enter", false);
                break;
        }
        return returns;
    }

}
