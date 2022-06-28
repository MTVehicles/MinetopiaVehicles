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

/**
 * Module for managing soft-dependencies.
 */
public class DependencyModule {
    private static @Getter
    @Setter
    DependencyModule instance;

    /**
     * List of all enabled soft-dependencies.
     */
    public static List<SoftDependency> loadedDependencies = new ArrayList<>();

    /**
     * WorldGuard's Utils class
     */
    public static WorldGuardUtils worldGuard;
    /**
     * Vault's Utils class
     */
    public static VaultUtils vault;
    /**
     * PlaceholderAPI's Utils class
     */
    public static PlaceholderUtils placeholderAPI;

    public DependencyModule() {
        if (Bukkit.getServer().getPluginManager().getPlugin("WorldGuard") != null) {
            try {
                worldGuard = new WorldGuardUtils();
                loadedDependencies.add(SoftDependency.WORLD_GUARD);
            } catch (NoClassDefFoundError e){
                Bukkit.getLogger().severe("[MTVehicles] An error occurred whilst loading WorldGuard as a soft-dependency. (Make sure you're using the latest version, or try restarting the server.)");
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

    /**
     * Check whether a soft-dependency is installed and enabled.
     * @param dependency Soft-dependency supported by the plugin
     * @return True if the soft-dependency is enabled.
     *
     * @see SoftDependency
     */
    public static boolean isDependencyEnabled(SoftDependency dependency){
        return loadedDependencies.contains(dependency);
    }

    /**
     * Disable a soft-dependency (usually due to a fatal bug or incompatible server version)
     * @param dependency Soft-dependency supported by the plugin
     *
     * @see SoftDependency
     */
    public static void disableDependency(SoftDependency dependency){
        if (isDependencyEnabled(dependency)) loadedDependencies.remove(dependency);
    }
}
