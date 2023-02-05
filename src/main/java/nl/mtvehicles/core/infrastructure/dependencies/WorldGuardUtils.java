package nl.mtvehicles.core.infrastructure.dependencies;

import nl.mtvehicles.core.infrastructure.enums.SoftDependency;
import nl.mtvehicles.core.infrastructure.enums.WGFlag;
import nl.mtvehicles.core.infrastructure.modules.DependencyModule;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.codemc.worldguardwrapper.WorldGuardWrapper;
import org.codemc.worldguardwrapper.flag.IWrappedFlag;
import org.codemc.worldguardwrapper.flag.WrappedState;
import org.codemc.worldguardwrapper.region.IWrappedRegion;

import java.util.*;

/**
 * Methods for WorldGuard soft-dependency.<br>
 * @warning <b>Do not initialise this class directly. Use {@link DependencyModule#worldGuard} instead.</b>
 */
public class WorldGuardUtils {
    //This must only be called if DependencyModule made sure that WorldGuard is installed.

    /**
     * WorldGuardWrapper instance (supports both WG v6 and v7 API)
     */
    public static WorldGuardWrapper instance = WorldGuardWrapper.getInstance();
    /**
     * HashMap which contains custom flags
     * @see WGFlag
     */
    public static HashMap<WGFlag, IWrappedFlag<WrappedState>> flags = new HashMap<>();

    /**
     * Default constructor which registers flags - <b>do not use this.</b><br>
     * Use {@link DependencyModule#worldGuard} instead.
     */
    public WorldGuardUtils(){
        registerFlags();
    }

    /**
     * Check whether the given location is inside a gas station
     */
    public boolean isInsideGasStation(Player player, Location loc){
        return isInRegionWithFlag(player, loc, WGFlag.GAS_STATION, WrappedState.ALLOW);
    }

    /**
     * Check whether a location is in a region with a (custom) flag of a specified state.
     * @param player Player (may change the flag depending on who the player is)
     * @param loc Location which may be inside a region
     * @param customFlag Custom WorldGuard flag (see {@link WGFlag})
     * @param flagState State of the flag (ALLOW/DENY)
     *
     * @return True if location is in at least 1 region with the flag with the specified state.
     */
    public boolean isInRegionWithFlag(Player player, Location loc, WGFlag customFlag, WrappedState flagState){
        Set<IWrappedRegion> regions = instance.getRegions(loc);
        if (regions.size() == 0) return false;

        IWrappedFlag<WrappedState> flag = flags.get(customFlag);
        boolean returns = false;
        for (IWrappedRegion region : regions) {
            Optional<WrappedState> regionFlagState = region.getFlag(flag);
            if (regionFlagState.isPresent()){
                WrappedState state = instance.queryFlag(player, loc, flag).orElse(null);
                if (flagState.equals(state)) returns = true;
            }
        }
        return returns;
    }

    /**
     * Check whether a location is in a region with a (custom) flag of a specified state.
     * @param player (may change the flag depending on who the player is)
     * @param loc Location which may be inside a region
     * @param customFlag Custom WorldGuard flag (see {@link WGFlag})
     * @param flagState State of the flag - specified by a boolean (true = ALLOW / false = DENY)
     *
     * @return True if location is in at least 1 region with the flag with the specified state.
     */
    public boolean isInRegionWithFlag(Player player, Location loc, WGFlag customFlag, boolean flagState){
        WrappedState state = (flagState) ? WrappedState.ALLOW : WrappedState.DENY;
        return isInRegionWithFlag(player, loc, customFlag, state);
    }

    private void registerFlags(){
        try {
            for (WGFlag flag: WGFlag.getFlagList()) { //Registering flags
                flags.put(flag, instance.registerFlag(flag.getKey(), WrappedState.class).orElse(null));
            }
        } catch (IllegalStateException e){ //"New flags cannot be registered at this time"
            for (WGFlag flag: WGFlag.getFlagList()) { //Getting already registered flags, if possible
                flags.put(flag, instance.getFlag(flag.getKey(), WrappedState.class).orElse(null));
                if (flags.get(flag) == null) {
                    Bukkit.getLogger().severe("[MTVehicles] Custom WorldGuard flags could not be registered for MTVehicles. Disabling soft-dependency... (If you've just reloaded the plugin with PlugMan, try restarting the server.)");
                    DependencyModule.disableDependency(SoftDependency.WORLD_GUARD);
                    return;
                }
            }
        }
    }
}
