package nl.mtvehicles.core.infrastructure.dataconfig;

import nl.mtvehicles.core.infrastructure.enums.*;
import nl.mtvehicles.core.infrastructure.helpers.TextUtils;
import nl.mtvehicles.core.infrastructure.models.Config;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import nl.mtvehicles.core.infrastructure.modules.DependencyModule;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Methods for config.yml.<br>
 * <b>Do not initialise this class directly. Use {@link ConfigModule#defaultConfig} instead.</b>
 */
public class DefaultConfig extends Config {
    /**
     * Default constructor - <b>do not use this.</b><br>
     * Use {@link ConfigModule#defaultConfig} instead.
     */
    public DefaultConfig() {
        super(ConfigType.DEFAULT);
    }

    /**
     * Get value of a config option specified by a String.
     * @deprecated This may lead to issues - use {@link #get(Option)} instead.
     */
    @Deprecated
    public String getMessage(String key) {
        return TextUtils.colorize(this.getConfiguration().getString(key));
    }

    /**
     * Get a value of an option from config.yml
     *
     * @param configOption Config.yml option
     * @return Value of the option (as Object)
     */
    public Object get(Option configOption){
        return this.getConfiguration().get(configOption.getPath());
    }

    /**
     * Check whether the last version (before an update) was using an older method of checking the config version.
     * This is present only for compatibility when updating from lower versions.
     *
     * @return True if the "Config-Versie" option was still present in config.yml.
     */
    public boolean hasOldVersionChecking(){
        return this.getConfiguration().get("Config-Versie") != null;
    }

    //--- DriveUp ---

    /**
     * Check which driveUp option is specified in config.yml (BLOCKS/SLABS/BOTH).
     */
    public DriveUp driveUpSlabs(){
        DriveUp returns = DriveUp.BOTH; //default value
        try {
            switch (Objects.requireNonNull(get(Option.DRIVE_UP).toString().toLowerCase())){
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

    private GasStationConfig gasStations = new GasStationConfig();

    /**
     * Check whether a jerrycan may be used on a specified location.
     * @param loc Location
     * @return True if gas stations are enabled, player is outside a region with flag 'mtvgasstation=deny' and inside a gas station if necessary.
     */
    public boolean canUseJerryCan(Location loc){
        if (!gasStations.areGasStationsEnabled()) return true;

        //If a player is in region with 'mtvgasstation=deny', they can't use jerrycans.
        if (DependencyModule.worldGuard.isInRegionWithFlag(loc, "mtv-gasstation", false)) return false;

        if (gasStations.canUseJerryCanOutsideOfGasStation()) return true;

        return gasStations.isInsideGasStation(loc);
    }

    /**
     * Whether a player can use a jerrycan on their location.
     * @param p Player
     * @see #canUseJerryCan(Location)
     */
    public boolean canUseJerryCan(Player p){
        return canUseJerryCan(p.getLocation());
    }

    /**
     * Check whether a jerrycan may be filled by a specified player on a specified location.
     * @return True if jerrycan may be filled.
     */
    public boolean canFillJerryCans(Player p, Location loc){
        if (!gasStations.areGasStationsEnabled()) return false;
        if (!gasStations.isFillJerryCansEnabled()) return false;
        if (!gasStations.isInsideGasStation(loc)) return false;

        if (!gasStations.hasFillJerryCansPermission(p)){
            p.sendMessage(TextUtils.colorize(ConfigModule.messagesConfig.getMessage(Message.NO_PERMISSION)));
            return false;
        } return true;
    }

    /**
     * Check whether jerrycan sounds are turned on in config.yml.
     */
    public boolean jerryCanPlaySound(){
        return (boolean) get(Option.GAS_STATIONS_FILL_JERRYCANS_PLAY_SOUND);
    }

    private class GasStationConfig {

        private boolean areGasStationsEnabled(){
            if (!DependencyModule.isDependencyEnabled(SoftDependency.WORLD_GUARD)) return false; //If WorldGuard isn't installed, say it's not enabled.

            return (boolean) get(Option.GAS_STATIONS_ENABLED);
        }

        private boolean canUseJerryCanOutsideOfGasStation(){
            return (boolean) get(Option.GAS_STATIONS_CAN_USE_JERRYCAN_OUTSIDE_OF_GAS_STATION);
        }

        private boolean isInsideGasStation(Location loc){
            return DependencyModule.worldGuard.isInRegionWithFlag(loc, "mtv-gasstation", true);
        }

        private boolean isFillJerryCansEnabled(){
            return (boolean) get(Option.GAS_STATIONS_FILL_JERRYCANS_ENABLED);
        }

        private boolean hasFillJerryCansPermission(Player p){
            if (!(boolean) get(Option.GAS_STATIONS_FILL_JERRYCANS_NEED_PERMISSION)) return true; //if there's set that they don't need the permission, just pretend they have it
            return p.hasPermission("mtvehicles.filljerrycans");
        }

    }

    /**
     * Check whether jerrycan may be filled by clicking on LEVERS.
     */
    public boolean isFillJerryCansLeverEnabled(){
        return (boolean) get(Option.GAS_STATIONS_FILL_JERRYCANS_LEVER);
    }

    /**
     * Check whether jerrycan may be filled by clicking on TRIPWIRE HOOKS.
     */
    public boolean isFillJerryCansTripwireHookEnabled(){
        return (boolean) get(Option.GAS_STATIONS_FILL_JERRYCANS_TRIPWIRE_HOOK);
    }

    /**
     * If prices for jerrycan filling are enabled.
     */
    public boolean isFillJerryCanPriceEnabled(){
        if (!gasStations.areGasStationsEnabled()) return false;
        if (!gasStations.isFillJerryCansEnabled()) return false;
        if (!DependencyModule.isDependencyEnabled(SoftDependency.VAULT)) return false; //If Vault isn't installed, say it's not enabled.
        if (!DependencyModule.vault.isEconomySetUp()) return false; //There is no Vault Economy plugin, disable it.

        return (boolean) get(Option.GAS_STATIONS_FILL_JERRYCANS_PRICE_ENABLED);
    }

    /**
     * Get the price of fuel (per litre).
     */
    public double getFillJerryCanPrice(){
        if ((double) get(Option.GAS_STATIONS_FILL_JERRYCANS_PRICE_PER_LITRE) <= 0) return 30.0; //Default, if it's not greater than 0
        else return (double) get(Option.GAS_STATIONS_FILL_JERRYCANS_PRICE_PER_LITRE);
    }

    //--- Disabled Worlds ---

    /**
     * Check whether a world is disabled.
     * @param worldName Name of the world
     * @return True if disabled.
     */
    public boolean isWorldDisabled(String worldName){
        if (getDisabledWorlds().isEmpty()) return false;
        return getDisabledWorlds().contains(worldName);
    }

    /**
     * Get the list of all disabled worlds.
     */
    private List<String> getDisabledWorlds(){
        return (List<String>) get(Option.DISABLED_WORLDS);
    }

    //--- Block Whitelist ---

    /**
     * Check whether block whitelist is enabled.
     */
    public boolean isBlockWhitelistEnabled() {
        return (boolean) get(Option.BLOCK_WHITELIST_ENABLED);
    }

    /**
     * Get the list of all whitelisted blocks.
     */
    public List<Material> blockWhiteList() {
        return ((List<String>) get(Option.BLOCK_WHITELIST_LIST)).stream().map(Material::getMaterial).collect(Collectors.toList());
    }

    //--- Region Actions ---

    /**
     * Get ListType (DISABLED/WHITELIST/BLACKLIST) for an action (PLACE/PICKUP/ENTER) from config.yml.
     */
    private RegionAction.ListType getRegionActionListType(RegionAction action){
        String configOption = "disabled"; //Default
        switch (action){
            case PLACE:
                configOption = get(Option.REGION_ACTIONS_PLACE).toString().toLowerCase(Locale.ROOT);
                break;
            case PICKUP:
                configOption = get(Option.REGION_ACTIONS_PICKUP).toString().toLowerCase(Locale.ROOT);
                break;
            case ENTER:
                configOption = get(Option.REGION_ACTIONS_ENTER).toString().toLowerCase(Locale.ROOT);
                break;
        }
        if (configOption.equalsIgnoreCase("whitelist")) return RegionAction.ListType.WHITELIST;
        else if (configOption.equalsIgnoreCase("blacklist")) return RegionAction.ListType.BLACKLIST;
        else return RegionAction.ListType.DISABLED;
    }

    /**
     * Check whether a player can proceed with a specified action on a specified location.
     * @param action Action (PLACE/PICKUP/ENTER)
     * @param vehicleType Type of the vehicle (enum)
     * @param loc Location
     * @return True if nothing prevents the player from proceeding.
     */
    public boolean canProceedWithAction(RegionAction action, VehicleType vehicleType, Location loc){
        if (isWorldDisabled(loc.getWorld().getName())) return false;

        if (!DependencyModule.isDependencyEnabled(SoftDependency.WORLD_GUARD)) return true;

        if (vehicleType.isUsageDisabled(loc)) return false;

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

    //--- Driving ---

    /**
     * Check whether vehicles are steered using where drivers are facing.
     * @return True if this is enabled.
     */
    public boolean usePlayerFacingDriving(){
        return (boolean) get(Option.USE_PLAYER_FACING);
    }

    /**
     * Options available in MTV's default configuration file (config.yml)
     */
    public enum Option {
        AUTO_UPDATE("autoUpdate", true),
        VEHICLE_MENU_SIZE("vehicleMenuSize", 3),
        HELICOPTER_BLADES_ALWAYS_ON("helicopterBladesAlwaysOn", true),
        DISABLE_PICKUP_FROM_WATER("disablePickupFromWater", false),
        TRUNK_ENABLED("trunkEnabled", true),
        PUT_ONESELF_AS_OWNER("putOneselfAsOwner", false),
        MAX_FLYING_HEIGHT("maxFlyingHeight", 150),
        TAKE_OFF_SPEED("takeOffSpeed", 0.4),
        CAR_PICKUP("carPickup", false),
        FUEL_ENABLED("fuelEnabled", true),
        FUEL_MULTIPLIER("fuelMultiplier", 1),
        JERRYCANS("jerrycans", new ArrayList<>(Arrays.asList(25, 50, 75))),
        DAMAGE_ENABLED("damageEnabled", false),
        DAMAGE_MULTIPLIER("damageMultiplier", 0.5),
        EXPLODE_WHEN_DESTROYED("explodeWhenDestroyed", false),
        HORN_COOLDOWN("hornCooldown", 5),
        HORN_TYPE("hornType", "minetopiaclassic.horn1"),
        TANK_TNT("tankTNT", false),
        TANK_COOLDOWN("tankCooldown", 10),
        DRIVE_UP("driveUp", "both"),
        EXTREME_HELICOPTER_FALL("extremeHelicopterFall", false),
        HELICOPTER_FALL_DAMAGE("helicopterFallDamage", 40.0),
        DRIVE_ON_CARPETS("driveOnCarpets", true),
        BLOCK_WHITELIST_ENABLED("blockWhitelist.enabled", false),
        BLOCK_WHITELIST_LIST("blockWhitelist.list", new ArrayList<>().add("GRAY_CONCRETE")),
        DISABLED_WORLDS("disabledWorlds", new ArrayList<>()),
        GAS_STATIONS_ENABLED("gasStations.enabled", false),
        GAS_STATIONS_CAN_USE_JERRYCAN_OUTSIDE_OF_GAS_STATION("gasStations.canUseJerryCanOutsideOfGasStation", true),
        GAS_STATIONS_FILL_JERRYCANS_ENABLED("gasStations.fillJerryCans.enabled", true),
        GAS_STATIONS_FILL_JERRYCANS_NEED_PERMISSION("gasStations.fillJerryCans.needPermission", false),
        GAS_STATIONS_FILL_JERRYCANS_PLAY_SOUND("gasStations.fillJerryCans.playSound", true),
        GAS_STATIONS_FILL_JERRYCANS_LEVER("gasStations.fillJerryCans.lever", true),
        GAS_STATIONS_FILL_JERRYCANS_TRIPWIRE_HOOK("gasStations.fillJerryCans.tripwireHook", false),
        GAS_STATIONS_FILL_JERRYCANS_PRICE_ENABLED("gasStations.fillJerryCans.price.enabled", true),
        GAS_STATIONS_FILL_JERRYCANS_PRICE_PER_LITRE("gasStations.fillJerryCans.price.pricePerLitre", 30.0),
        REGION_ACTIONS_PLACE("regionActions.place", "disabled"),
        REGION_ACTIONS_ENTER("regionActions.enter", "disabled"),
        REGION_ACTIONS_PICKUP("regionActions.pickup", "disabled"),
        USE_PLAYER_FACING("usePlayerFacing", false);

        final private String path;
        final private Object defaultValue;

        private Option(String path, Object defaultValue){
            this.path = path;
            this.defaultValue = defaultValue;
        }

        /**
         * Get default value of option
         * @return Default value of option
         */
        public Object getDefaultValue() {
            return defaultValue;
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
         * @return Default type of option
         */
        public Type getValueType() {
            return this.getDefaultValue().getClass();
        }
    }

}
