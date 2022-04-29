package nl.mtvehicles.core.infrastructure.modules;

import lombok.Getter;
import lombok.Setter;
import nl.mtvehicles.core.infrastructure.dependencies.PlaceholderUtils;
import nl.mtvehicles.core.infrastructure.dependencies.VaultUtils;
import nl.mtvehicles.core.infrastructure.dependencies.WorldGuardUtils;
import nl.mtvehicles.core.infrastructure.enums.SoftDependency;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;

public class DependencyModule {
    private static @Getter
    @Setter
    DependencyModule instance;

    public static List<SoftDependency> loadedDependencies = new ArrayList<>();
    public static WorldGuardUtils worldGuard;
    public static VaultUtils vault;
    public static PlaceholderUtils placeholderAPI;

    public DependencyModule() {
        final String serverVersion = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        if (Bukkit.getServer().getPluginManager().getPlugin("WorldGuard") != null) {
            if (serverVersion.equals("v1_12_R1")) Bukkit.getLogger().warning("[MTVehicles] Cannot load WorldGuard as a soft-dependency in 1.12. (1.12 uses 6.x.x API whereas newer versions use 7.x.x)");
            else {
                try {
                    worldGuard = new WorldGuardUtils();
                    loadedDependencies.add(SoftDependency.WORLD_GUARD);
                } catch (NoClassDefFoundError e){
                    Bukkit.getLogger().severe("[MTVehicles] An error occurred whilst loading WorldGuard as a soft-dependency. (Make sure you're using a version 7.x.x, or try restarting the server.)");
                }
            }
        }
        if (Bukkit.getServer().getPluginManager().getPlugin("Vault") != null) {
            try {
                vault = new VaultUtils();
                loadedDependencies.add(SoftDependency.VAULT);
            } catch (NoClassDefFoundError e){
                Bukkit.getLogger().severe("[MTVehicles] An error occurred whilst loading Vault as a soft-dependency. (Make sure you're using the latest version, or try restarting the server.)");
            }
        }
        if (Bukkit.getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            try {
                placeholderAPI = new PlaceholderUtils();
                placeholderAPI.register();
                loadedDependencies.add(SoftDependency.PLACEHOLDER_API);
            } catch (NoClassDefFoundError e){
                Bukkit.getLogger().severe("[MTVehicles] An error occurred whilst loading PlaceholderAPI as a soft-dependency. (Make sure you're using the latest version, or try restarting the server.)");
            }
        }
    }

    public static boolean isDependencyEnabled(SoftDependency dependency){
        return loadedDependencies.contains(dependency);
    }

    public static void disableDependency(SoftDependency dependency){
        if (isDependencyEnabled(dependency)) loadedDependencies.remove(dependency);
    }
}
