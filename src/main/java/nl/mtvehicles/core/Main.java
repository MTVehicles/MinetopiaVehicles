package nl.mtvehicles.core;

import nl.mtvehicles.core.infrastructure.modules.*;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    public static Main instance;

    /**
     * We might not change config in every version, why bother creating a new config file on every update then?
     * Change this EVERY TIME you edit config. OTHERWISE, DON'T TOUCH IT.
     * (The same applies for Main.messagesVersion, it controls message files.)
     *
     * @see nl.mtvehicles.core.infrastructure.dataconfig.SecretSettingsConfig
     */
    public static String configVersion = "2.3.0-dev19";
    public static String messagesVersion = "2.3.0-dev14";

    @Override
    public void onEnable() {

        instance = this;

        if (!new VersionModule().isSupportedVersion()) return;

        getLogger().info("Plugin has been loaded!");
        if (VersionModule.isPreRelease) getLogger().warning("Be aware: You are using a pre-release. It might not be stable and it's generally not advised to use it on a production server.");
        getLogger().info("--------------------------");
        getLogger().info("Welcome by MTVehicles v" + VersionModule.pluginVersion + "!");
        getLogger().info("Thanks for using our plugin.");
        getLogger().info("--------------------------");

        new CommandModule();
        new ListenersModule();
        new MetricsModule();
        new LoopModule();
        new ConfigModule();
    }

    @Override
    public void onLoad(){
        new DependencyModule();
    }

    public static String getFileAsString() {
        return String.valueOf(Main.instance.getFile());
    }

    public void registerListener(Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, this);
    }

    public static void disablePlugin(){
        instance.setEnabled(false);
    }
}
