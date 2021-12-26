package nl.mtvehicles.core.infrastructure.dependencies;

import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import nl.mtvehicles.core.infrastructure.modules.DependencyModule;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;


public class WorldGuardUtils {
    //This is only called if DependencyModule made sure that WorldGuard is installed.

    WorldGuard instance;
    RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();

    public WorldGuardUtils(){
        instance = WorldGuard.getInstance();
    }

    public boolean isWithinRegion(Location loc, String regionName){
        RegionManager regions = container.get((com.sk89q.worldedit.world.World) loc.getWorld());
        if (regions == null) return false; //The world has no region support or region data failed to load.
        if (!regions.hasRegion(regionName)) return false; //There is no such region in the world.

        ProtectedRegion region = regions.getRegion(regionName);
        if (region.contains(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ())) return true; //Contains x, y, z -> Is within region
        else return false;
    }

    public boolean isInAtLeastOneRegion(Location loc, List<String> regionNames){
        boolean returns = false;
        for (String region : regionNames){
            if (isWithinRegion(loc, region)) returns = true;
        } //If location is in at least 1 region, variable is set to true
        return returns;
    }
}
