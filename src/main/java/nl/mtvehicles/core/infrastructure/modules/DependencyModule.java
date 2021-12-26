package nl.mtvehicles.core.infrastructure.modules;

import lombok.Getter;
import lombok.Setter;
import nl.mtvehicles.core.Main;
import nl.mtvehicles.core.infrastructure.dependencies.WorldGuardUtils;

import java.util.ArrayList;
import java.util.List;

public class DependencyModule {
    private static @Getter
    @Setter
    DependencyModule instance;

    public static List<String> loadedDependencies = new ArrayList<>();
    public static WorldGuardUtils worldGuard;

    public DependencyModule() {
        if (Main.instance.getServer().getPluginManager().getPlugin("WorldGuard") != null) {
            loadedDependencies.add("WorldGuard");
            worldGuard = new WorldGuardUtils();
        }
    }

    public static boolean isDependencyEnabled(String name){
        if (loadedDependencies.contains(name)) return true;
        else return false;
    }
}
