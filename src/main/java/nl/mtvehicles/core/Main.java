package nl.mtvehicles.core;

import nl.mtvehicles.core.infrastructure.dataconfig.DefaultConfig;
import nl.mtvehicles.core.infrastructure.dataconfig.VehicleDataConfig;
import nl.mtvehicles.core.infrastructure.enums.SoftDependency;
import nl.mtvehicles.core.infrastructure.modules.*;
import nl.mtvehicles.core.infrastructure.utils.PluginUpdater;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;

public class Main extends JavaPlugin {

    public static Main instance;

    final public static String configVersion = "2.5.5-dev3";
    final public static String messagesVersion = "2.5.6-dev1";

    @Override
    public void onEnable() {
        instance = this;

        if (!new VersionModule().isSupportedVersion()) return;

        // Register shutdown hook first
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            Main pluginInstance = instance;
            if (pluginInstance != null) {
                pluginInstance.getLogger().info("Server shutdown detected - saving vehicle data...");
            } else {
                Bukkit.getLogger().info("[MTVehicles] Server shutdown detected - saving vehicle data...");
            }

            long startTime = System.currentTimeMillis();

            try {
                if (ConfigModule.vehicleDataConfig != null) {
                    ConfigModule.vehicleDataConfig.processSaveQueue();

                    // Save each vehicle to its owner's database
                    for (Map.Entry<String, Map<VehicleDataConfig.Option, Object>> entry :
                            ConfigModule.vehicleDataConfig.getVehicles().entrySet()) {
                        String licensePlate = entry.getKey();
                        String ownerUuidStr = (String) entry.getValue().get(VehicleDataConfig.Option.OWNER);

                        if (ownerUuidStr == null) {
                            Main.logSevere("Vehicle " + licensePlate + " has no owner UUID, skipping save");
                            continue;
                        }

                        try {
                            UUID ownerUuid = UUID.fromString(ownerUuidStr);
                            File dbFile = new File(pluginInstance.getDataFolder(),
                                    "userdata/" + ownerUuid + ".db");

                            // Create new connection for shutdown save
                            try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + dbFile.getAbsolutePath())) {
                                ConfigModule.vehicleDataConfig.saveVehicleToDatabase(licensePlate, conn);
                            }
                        } catch (Exception e) {
                            Main.logSevere("Failed to save vehicle " + licensePlate + " during shutdown: " + e.getMessage());
                        }
                    }
                }
            } catch (Exception e) {
                Main.logSevere("CRITICAL: Failed to save vehicle data on shutdown: " + e.getMessage());
                e.printStackTrace();
            }
        }, "MTVehicles-Shutdown-Hook"));

        logInfo("Plugin has been loaded!");
        if (VersionModule.isPreRelease) logWarning("Be aware: You are using a pre-release. It might not be stable and it's generally not advised to use it on a production server.");
        logInfo("--------------------------");
        logInfo("Welcome by MTVehicles v" + VersionModule.pluginVersionString + "!");
        logInfo("Thanks for using our plugin.");
        logInfo("--------------------------");

        disableNBTAPIVersionMessages();
        loadSkript();

        new CommandModule();
        new ListenersModule();
        new MetricsModule();
        new LoopModule();
        new ConfigModule();

        // Initialize database after all modules are loaded
        initializeDatabase();

        if ((boolean) ConfigModule.defaultConfig.get(DefaultConfig.Option.AUTO_UPDATE)) {
            PluginUpdater.checkNewVersion(getServer().getConsoleSender());
        }
    }

    @Override
    public void onLoad() {
        new DependencyModule();
    }

    @Override
    public void onDisable() {
        // Save all data and clean up resources
        try {
            // Cancel all running tasks first
            Bukkit.getScheduler().cancelTasks(this);

            if (ConfigModule.vehicleDataConfig != null) {
                ConfigModule.vehicleDataConfig.onDisable();
            }

            if (DependencyModule.isDependencyEnabled(SoftDependency.PLACEHOLDER_API)) {
                DependencyModule.placeholderAPI.unregisterOnDisable();
            }

            // Don't set instance to null here - let the shutdown hook complete first
        } catch (Exception e) {
            logSevere("Error during plugin shutdown: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Initialize the SQLite databases
     */
    private void initializeDatabase() {
        try {
            // First ensure all configs are loaded
            ConfigModule.secretSettings.reload();
            ConfigModule.defaultConfig.reload();
            ConfigModule.messagesConfig.reload();

            // Create userdata directory if it doesn't exist
            File userDataDir = new File(getDataFolder(), "userdata");
            if (!userDataDir.exists() && !userDataDir.mkdirs()) {
                throw new Exception("Failed to create userdata directory");
            }

            // Initialize database connection if not already done
            if (ConfigModule.vehicleDataConfig == null) {
                ConfigModule.vehicleDataConfig = new VehicleDataConfig();
            }

            // Then load vehicle data
            if (isEnabled()) {
                ConfigModule.vehicleDataConfig.loadFromDatabase();

                // Verify loaded data
                if (ConfigModule.vehicleDataConfig.isEmpty()) {
                    logInfo("No vehicle data found in database - fresh installation?");
                } else {
                    int vehicleCount = ConfigModule.vehicleDataConfig.getVehicles().size();
                    int userDbCount = userDataDir.listFiles((dir, name) -> name.endsWith(".db")).length;

                    logInfo(String.format("Loaded data for %d vehicles from %d user databases",
                            vehicleCount, userDbCount));

                    // Check for old database format that needs migration
                    File oldDbFile = new File(getDataFolder(), "data/vehicles.db");
                    if (oldDbFile.exists()) {
                        logWarning("Old database format detected. Migration to per-user storage will be performed.");
                    }
                }
            }
        } catch (Exception e) {
            logSevere("Failed to initialize vehicle databases: " + e.getMessage());
            e.printStackTrace();
            disablePlugin();
        }
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

    private void disableNBTAPIVersionMessages() {
        de.tr7zw.changeme.nbtapi.utils.MinecraftVersion.disableUpdateCheck();
    }

    private void loadSkript() {
        if (DependencyModule.isDependencyEnabled(SoftDependency.SKRIPT)) {
            try {
                DependencyModule.skript.load();
            } catch (Exception e) {
                Bukkit.getLogger().severe("MTVehicles could not load Skript addon. (Maybe you've just reloaded the plugin with PlugMan?)");
            }
        }
    }

    /**
     * Disable the plugin (when a fatal error occurs)
     */
    public static void disablePlugin() {
        logSevere("Disabling the plugin (a critical bug might have occurred)...");
        try {
            if (instance != null) {
                // Clean up before disabling
                Bukkit.getScheduler().cancelTasks(instance);

                if (ConfigModule.vehicleDataConfig != null) {
                    ConfigModule.vehicleDataConfig.onDisable();
                }

                instance.setEnabled(false);
            }
        } catch (Exception e) {
            logSevere("Error while disabling plugin: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Log an informative message to the console
     */
    public static void logInfo(String text) {
        if (instance != null) {
            instance.getLogger().info(text);
        } else {
            Bukkit.getLogger().info("[MTVehicles] " + text);
        }
    }

    /**
     * Log a warning to the console (yellow)
     */
    public static void logWarning(String text) {
        if (instance != null) {
            instance.getLogger().warning(text);
        } else {
            Bukkit.getLogger().warning("[MTVehicles] " + text);
        }
    }

    /**
     * Log a message warning about a severe issue to the console (red)
     */
    public static void logSevere(String text) {
        if (instance != null) {
            instance.getLogger().severe(text);
        } else {
            Bukkit.getLogger().severe("[MTVehicles] " + text);
        }
    }

    /**
     * Run a task using a bukkit scheduler
     */
    public static void schedulerRun(Runnable task) {
        if (instance != null && instance.isEnabled()) {
            Bukkit.getScheduler().runTask(instance, task);
        }
    }

    /**
     * Run a task asynchronously using a bukkit scheduler
     */
    public static void schedulerRunAsync(Runnable task) {
        if (instance != null && instance.isEnabled()) {
            Bukkit.getScheduler().runTaskAsynchronously(instance, task);
        }
    }

    /**
     * Get the path to a user's vehicle data file
     * @param userUuid The UUID of the user
     * @return File object pointing to the user's database
     */
    public static File getUserDataFile(UUID userUuid) {
        return new File(instance.getDataFolder(), "userdata/" + userUuid.toString() + ".db");
    }
}