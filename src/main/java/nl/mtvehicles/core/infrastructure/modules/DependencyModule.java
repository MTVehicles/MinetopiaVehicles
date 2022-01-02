package nl.mtvehicles.core.infrastructure.modules;

import lombok.Getter;
import lombok.Setter;
import nl.mtvehicles.core.infrastructure.dependencies.VaultUtils;
import nl.mtvehicles.core.infrastructure.dependencies.WorldGuardUtils;
import org.bukkit.Bukkit;

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
        if (Bukkit.getServer().getPluginManager().getPlugin("WorldGuard") != null) {
            loadedDependencies.add("WorldGuard");
            worldGuard = new WorldGuardUtils();
        }
        if (Bukkit.getServer().getPluginManager().getPlugin("Vault") != null) {
            loadedDependencies.add("Vault");
            vault = new VaultUtils();
        }
    }

    public static boolean isDependencyEnabled(String name){
        return loadedDependencies.contains(name);
    }

    public static void disableDependency(String name){
        if (isDependencyEnabled(name)) loadedDependencies.remove(name);
    }
}
