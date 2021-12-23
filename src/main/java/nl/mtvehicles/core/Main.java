package nl.mtvehicles.core;

import nl.mtvehicles.core.infrastructure.modules.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    public static Main instance;
    public static boolean isPreRelease;
    public static String configVersion = "2.2.1"; //We might not change config in every version, why bother creating a new config file then? Change this only when necessary.;
    public static String pluginVersion;
    public static String serverVersion = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];

    @Override
    public void onEnable() {

        instance = this;

        PluginDescriptionFile pdf = this.getDescription();
        pluginVersion = pdf.getVersion();
        isPreRelease = pluginVersion.toLowerCase().contains("pre"); //Pre-releases should thus be named "vX.Y.Z-preU" etc...

        if (!new CheckVersionModule().isSupportedVersion()) {
            this.setEnabled(false);
            return;
        }

        getLogger().info("Plugin has been loaded!");
        if (isPreRelease) getLogger().info(ChatColor.YELLOW + "Be aware: You are using a pre-release. It might not be stable and it's generally not advised to use it on a production server.");
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
