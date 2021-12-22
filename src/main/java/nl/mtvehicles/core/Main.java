package nl.mtvehicles.core;

import nl.mtvehicles.core.infrastructure.modules.*;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    public static Main instance;
    public static boolean isPreRelease = false;
    public static String pluginVersion;
    public static String serverVersion = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];

    @Override
    public void onEnable() {

        instance = this;

        PluginDescriptionFile pdf = this.getDescription();
        pluginVersion = pdf.getVersion();

        if (!new CheckVersionModule().isSupportedVersion()) {
            this.setEnabled(false);
            return;
        }

        getLogger().info("Plugin has been loaded!");
        getLogger().info("--------------------------");
        getLogger().info("Welcome by MTVehicles v" + pluginVersion + "!");
        getLogger().info("Thanks for using our plugin.");
        getLogger().info("--------------------------");

        new CommandModule();
        new ListenersModule();
        new MetricsModule();
        new LoopModule();
        new ConfigModule();
    }

    public static String fol() {
        return String.valueOf(Main.instance.getFile());
    }

    public void registerListener(Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, this);
    }
}
