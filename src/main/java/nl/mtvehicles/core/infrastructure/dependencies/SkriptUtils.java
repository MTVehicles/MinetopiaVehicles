package nl.mtvehicles.core.infrastructure.dependencies;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAddon;
import nl.mtvehicles.core.Main;
import nl.mtvehicles.core.infrastructure.enums.SoftDependency;
import nl.mtvehicles.core.infrastructure.modules.DependencyModule;
import org.bukkit.Bukkit;

import java.io.IOException;

/**
 * Methods for Skript soft-dependency.<br>
 * @warning <b>Do not initialise this class directly. Use {@link DependencyModule#skript} instead.</b>
 * @since 2.5.1
 */
public class SkriptUtils {

    private static SkriptAddon addonInstance;

    /**
     * Default constructor - <b>do not use this.</b><br>
     * Use {@link DependencyModule#skript} instead.
     */
    public SkriptUtils() {}

    public static void load() {
        try {
            getAddonInstance().loadClasses("nl.mtvehicles.core.infrastructure.dependencies.skript", "effects", "expressions", "events");
        } catch (IOException e){
            Bukkit.getLogger().severe("[MTVehicles] An error occurred whilst loading Skript as a soft-dependency. (Make sure you're using the latest version, or try restarting the server.)");
            DependencyModule.loadedDependencies.remove(SoftDependency.SKRIPT);
        }
    }

    public static SkriptAddon getAddonInstance() {
        if (addonInstance == null) {
            addonInstance = Skript.registerAddon(Main.instance);
        }
        return addonInstance;
    }

}
