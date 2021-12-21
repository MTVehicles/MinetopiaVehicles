package nl.mtvehicles.core;

import nl.mtvehicles.core.infrastructure.models.MTVehicleSubCommand;
import nl.mtvehicles.core.infrastructure.modules.*;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public class Main extends JavaPlugin {
    public static Main instance;
    public static String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];

    @Override
    public void onEnable() {

        instance = this;

        if (!version.equals("v1_12_R1") && !version.equals("v1_13_R2") && !version.equals("v1_15_R1") && !version.equals("v1_16_R3") && !version.contains("v1_17") && !version.contains("v1_18")) {
            getLogger().info("-------------------------------------------------------");
            getLogger().info("Your Server version is not supported by the plugin.");
            getLogger().info("Check the supported versions here https://mtvehicles.nl");
            getLogger().info("-------------------------------------------------------");
            setEnabled(false);
            return;
        }

        PluginDescriptionFile pdf = this.getDescription();
        String versions = pdf.getVersion();

        getLogger().info("Plugin has been loaded!");
        getLogger().info("--------------------------");
        getLogger().info("Welcome by MTVehicles " + versions + " !");
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
