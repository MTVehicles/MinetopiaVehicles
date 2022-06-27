package nl.mtvehicles.core.infrastructure.dependencies;

import nl.mtvehicles.core.infrastructure.enums.SoftDependency;
import nl.mtvehicles.core.infrastructure.modules.DependencyModule;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.codemc.worldguardwrapper.WorldGuardWrapper;
import org.codemc.worldguardwrapper.flag.IWrappedFlag;
import org.codemc.worldguardwrapper.flag.WrappedState;
import org.codemc.worldguardwrapper.region.IWrappedRegion;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Methods for WorldGuard soft-dependency.<br>
 * <b>Do not initialise this class directly. Use {@link DependencyModule#worldGuard} instead.</b>
 */
public class WorldGuardUtils {
    //This must only be called if DependencyModule made sure that WorldGuard is installed.

    /**
     * WorldGuard instance
     */
    public WorldGuardWrapper instance = WorldGuardWrapper.getInstance();

    /**
     * List of all custom WorldGuard flags used by the plugin
     */
    public final List<String> flagList = Arrays.asList(
            "mtv-gasstation",
            "mtv-place",
            "mtv-enter",
            "mtv-pickup",
            "mtv-use-car",
            "mtv-use-hover",
            "mtv-user-tank",
            "mtv-use-helicopter",
            "mtv-use-airplane"
    );

    /**
     * Default constructor which registers flags - <b>do not use this.</b><br>
     * Use {@link DependencyModule#worldGuard} instead.
     */
    public WorldGuardUtils(){
        registerFlags();
    }

    /**
     * Check whether a location is in a region with a (custom) flag of a specified state.
     * @param loc - Location which may be inside a region
     * @param flagName - Name of the flag
     * @param flagState - State of the flag - specified by WorldGuard State enum (ALLOW/DENY)
     *
     * @return True if location is in at least 1 region with the flag with the specified state.
     */
    public boolean isInRegionWithFlag(Player player, Location loc, String flagName, WrappedState flagState) {
        Set<IWrappedRegion> regions = instance.getRegions(loc);
        Optional<IWrappedFlag<WrappedState>> flag = instance.getFlag(flagName, WrappedState.class);
        if (!flag.isPresent() || regions.size() == 0) return false;

        for (IWrappedRegion region : regions) {
            Optional<WrappedState> regionFlagState = region.getFlag(flag.get());
            // Flag is present in region, check if it has the correct state then return true.
            WrappedState state = flag.map(f -> instance.queryFlag(player, loc, f).orElse(WrappedState.DENY)).orElse(WrappedState.DENY);
            return state == flagState;
        }

        // No region with flag found.
        return false;
    }

    /**
     * Check whether a location is in a region with a (custom) flag of a specified state.
     * @param loc - Location which may be inside a region
     * @param flagName - Name of the flag
     * @param flagState - State of the flag - specified by a boolean (true = ALLOW / false = DENY)
     *
     * @return True if location is in at least 1 region with the flag with the specified state.
     */
    public boolean isInRegionWithFlag(Player player, Location loc, String flagName, boolean flagState) {
        WrappedState state = (flagState) ? WrappedState.ALLOW : WrappedState.DENY;
        return isInRegionWithFlag(player, loc, flagName, state);
    }

    private void registerFlags(){
        for (String flagName : flagList) {
            if (instance.registerFlag(flagName, WrappedState.class).isPresent()) {
                Bukkit.getLogger().info("Registered flag " + flagName);
            } else {
                Bukkit.getLogger().info("Failed to register flag " + flagName);
                Bukkit.getLogger().info("This flag may already be registered.");
                Bukkit.getLogger().info("If this is the case, please disable the other plugin that uses this flag.");
            }
        }
    }

    private void disableDependency(){
        Bukkit.getLogger().severe("[MTVehicles] Custom WorldGuard flags could not be created for MTVehicles. Disabling as a soft-dependcy... (If you've just reloaded the plugin with PlugMan, try restarting the server.)");
        DependencyModule.disableDependency(SoftDependency.WORLD_GUARD);
    }
}
