package nl.mtvehicles.core;

import nl.mtvehicles.core.infrastructure.modules.*;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * The plugin's main class
 */
public class Main extends JavaPlugin {
    /**
     * The plugin instance
     */
    public static Main instance;

    /**
     * <b>Version of config.yml - must be equal to the version in SuperSecretSettings.</b><br>
     *
     * We might not change config in every version, why bother creating a new config file on every update then?
     * Change this EVERY TIME you edit config. OTHERWISE, DON'T TOUCH IT.
     *
     * @see nl.mtvehicles.core.infrastructure.dataconfig.SecretSettingsConfig
     */
    public static String configVersion = "2.4.0";
    /**
     * <b>Version of messages_xx.yml files - must be equal to the version in SuperSecretSettings.</b><br>
     *
     * This variable works exactly as 'configVersion'.
     */
    public static String messagesVersion = "2.4.2-dev3";

    @Override
    public void onEnable() {

        instance = this;

        if (!new VersionModule().isSupportedVersion()) return;

        getLogger().info("Plugin has been loaded!");
        if (VersionModule.isPreRelease) getLogger().warning("Be aware: You are using a pre-release. It might not be stable and it's generally not advised to use it on a production server.");
        getLogger().info("--------------------------");
        getLogger().info("Welcome by MTVehicles v" + VersionModule.pluginVersionString + "!");
        getLogger().info("Thanks for using our plugin.");
        getLogger().info("--------------------------");

        disableNBTAPIVersionMessages();

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

    /**
     * Register a listener
     * @see ListenersModule
     */
    public void registerListener(Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, this);
    }

    private void disableNBTAPIVersionMessages(){
        de.tr7zw.changeme.nbtapi.utils.MinecraftVersion.disableUpdateCheck();
    }

    /**
     * Disable the plugin (when a fatal error occurs)
     */
    public static void disablePlugin(){
        logSevere("Disabling the plugin (a critical bug might have occurred)...");
        instance.setEnabled(false);
    }

    /**
     * Log an informative message to the console
     */
    public static void logInfo(String text){
        instance.getLogger().info(text);
    }

    /**
     * Log a warning to the console (yellow)
     */
    public static void logWarning(String text){
        instance.getLogger().warning(text);
    }

    /**
     * Log a message warning about a severe issue to the console (red)
     */
    public static void logSevere(String text){
        instance.getLogger().severe(text);
    }

    /**
     * Run a task using a bukkit scheduler
     */
    public static void schedulerRun(Runnable task){
        Bukkit.getScheduler().runTask(instance, task);
    }
}
