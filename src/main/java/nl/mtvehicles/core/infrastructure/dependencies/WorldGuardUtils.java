package nl.mtvehicles.core.infrastructure.dependencies;

import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import nl.mtvehicles.core.infrastructure.modules.DependencyModule;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;


public class WorldGuardUtils {
    //This is only called if DependencyModule made sure that WorldGuard is installed.

    WorldGuard instance = WorldGuard.getInstance();
    FlagRegistry registry = instance.getFlagRegistry();

    public static StateFlag MTV_GASSTATION_FLAG;
    public static StateFlag MTV_PLACE_FLAG;
    public static StateFlag MTV_PICKUP_FLAG;
    public static StateFlag MTV_ENTER_FLAG;

    public WorldGuardUtils(){
        registerFlags();
    }

    public boolean isInRegionWithFlag(Location loc, String flagName, StateFlag.State flagState){
        ApplicableRegionSet regions = WorldGuard.getInstance().getPlatform().getRegionContainer().get(new BukkitWorld(loc.getWorld())).getApplicableRegions(BlockVector3.at(loc.getX(),loc.getY(),loc.getZ()));
        StateFlag flag = (StateFlag) registry.get(flagName);
        if (flag == null || regions.size() == 0) return false;

        boolean returns = false;
        for (ProtectedRegion region: regions) {
            if (region.getFlag(flag).equals(flagState)) returns = true;
        } //If location is in at least 1 region with the flag
        return returns;
    }

    public boolean isInRegionWithFlag(Location loc, String flagName, boolean flagState){
        StateFlag.State state = (flagState) ? StateFlag.State.ALLOW : StateFlag.State.DENY;
        return isInRegionWithFlag(loc, flagName, state);
    }

    private void registerFlags(){
        try {
            StateFlag mtvGasStationFlag = new StateFlag("mtv-gasstation", true);
            registry.register(mtvGasStationFlag);
            StateFlag mtvPlaceFlag = new StateFlag("mtv-place", true);
            registry.register(mtvPlaceFlag);
            StateFlag mtvEnterFlag = new StateFlag("mtv-enter", true);
            registry.register(mtvEnterFlag);
            StateFlag mtvPickupFlag = new StateFlag("mtv-pickup", true);
            registry.register(mtvPickupFlag);

            //Only set our fields if there was no error
            MTV_GASSTATION_FLAG = mtvGasStationFlag;
            MTV_PLACE_FLAG = mtvPlaceFlag;
            MTV_ENTER_FLAG = mtvEnterFlag;
            MTV_PICKUP_FLAG = mtvPickupFlag;
        } catch (FlagConflictException e) {
            Bukkit.getLogger().info(ChatColor.RED + "[MTVehicles] Custom WorldGuard flags could not be created for MTVehicles. Disabling as a softdepend...");
            DependencyModule.disableDependency("WorldGuard");
        }
    }
}
