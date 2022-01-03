package nl.mtvehicles.core.infrastructure.modules;

import lombok.Getter;
import lombok.Setter;
import nl.mtvehicles.core.infrastructure.dependencies.VaultUtils;
import nl.mtvehicles.core.infrastructure.dependencies.WorldGuardUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

public class DependencyModule {
    private static @Getter
    @Setter
    DependencyModule instance;

    public static List<String> loadedDependencies = new ArrayList<>();
    public static WorldGuardUtils worldGuard;
    public static VaultUtils vault;

    public DependencyModule() {
        final String serverVersion = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        if (Bukkit.getServer().getPluginManager().getPlugin("WorldGuard") != null) {
            if (serverVersion.equals("v1_12_R1")) Bukkit.getLogger().info(ChatColor.YELLOW + "[MTVehicles] Cannot load WorldGuard as a soft-dependency in 1.12. (1.12 uses 6.x.x API whereas newer versions use 7.x.x)");
            else {
                try {
                    worldGuard = new WorldGuardUtils();
                    loadedDependencies.add("WorldGuard");
                } catch (NoClassDefFoundError e){
                    Bukkit.getLogger().info(ChatColor.RED + "[MTVehicles] An error occurred whilst loading WorldGuard as a soft-dependency. (Make sure you're using a version 7.x.x, or try restarting the server.)");
                }
            }
        }
        if (Bukkit.getServer().getPluginManager().getPlugin("Vault") != null) {
            try {
                vault = new VaultUtils();
                loadedDependencies.add("Vault");
            } catch (NoClassDefFoundError e){
                Bukkit.getLogger().info(ChatColor.RED + "[MTVehicles] An error occurred whilst loading Vault as a soft-dependency. (Make sure you're using the latest version, or try restarting the server.)");
            }
        }
    }

    public static boolean isDependencyEnabled(String name){
        return loadedDependencies.contains(name);
    }

    public static void disableDependency(String name){
        if (isDependencyEnabled(name)) loadedDependencies.remove(name);
    }
}
