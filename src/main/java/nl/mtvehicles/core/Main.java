package nl.mtvehicles.core;

import nl.mtvehicles.core.commands.VehicleSubCommandManager;
import nl.mtvehicles.core.commands.VehicleTabCompleterManager;
import nl.mtvehicles.core.events.*;
import nl.mtvehicles.core.infrastructure.dataconfig.*;
import nl.mtvehicles.core.infrastructure.helpers.TextUtils;
import nl.mtvehicles.core.infrastructure.models.ConfigUtils;
import nl.mtvehicles.core.infrastructure.models.ListenersModule;
import nl.mtvehicles.core.infrastructure.models.MTVehicleSubCommand;
import nl.mtvehicles.core.inventory.InventoryClickEvent;
import nl.mtvehicles.core.inventory.InventoryCloseEvent;
import nl.mtvehicles.core.movement.MovementManager;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Main extends JavaPlugin {
    public static Main instance;
    public static List<ConfigUtils> configList = new ArrayList<>();
    public static MessagesConfig messagesConfig = new MessagesConfig();
    public static VehicleDataConfig vehicleDataConfig = new VehicleDataConfig();
    public static VehiclesConfig vehiclesConfig = new VehiclesConfig();
    public static DefaultConfig defaultConfig = new DefaultConfig();
    public static String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
    public static HashMap<String, MTVehicleSubCommand> subcommands = new HashMap<>();

    @Override
    public void onEnable() {

        instance = this;

        //Check if the plugin is using the correct version
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

        //Plugin enable message for the console
        getLogger().info("Plugin has been loaded!");
        getLogger().info("--------------------------");
        getLogger().info("Welcome by MTVehicles " + versions + " !");
        getLogger().info("Thanks for using our plugin.");
        getLogger().info("--------------------------");


        //Setting the command for the plugin
        PluginCommand pluginCommand = Main.instance.getCommand("minetopiavehicles");

        if (pluginCommand != null) {
            //Register the /minetopiavehicles command
            pluginCommand.setExecutor(new VehicleSubCommandManager());

            //Register tabcompleter for the command /minetopiavehicles
            pluginCommand.setTabCompleter(new VehicleTabCompleterManager());
        }

        //Register the Listeners
        new ListenersModule();

        //bStats mertrics https://bstats.org/plugin/bukkit/MTVehicles
        Metrics metrics = new Metrics(this, 5932);
        metrics.addCustomChart(new Metrics.SimplePie("used_language", () -> {
            return defaultConfig.getConfig().getString("messagesLanguage");
        }));

        //Looping the online people in the server
        for (Player p : Bukkit.getServer().getOnlinePlayers()) {

            //Checking if someone is in a vehicle while starting
            if (p.isInsideVehicle()) {

                //kick the player if he is in a vehicle when starting the plugin
                p.kickPlayer(TextUtils.colorize(Main.messagesConfig.getMessage("reloadInVehicle")));
            }

            //Select correct packing for player inputs
            MovementManager.MovementSelector(p);
        }

        //Getting the default config
        File defaultconfig = new File(getDataFolder(), "config.yml");

        //Getting the vehicles.yml
        File vehicleconfig = new File(getDataFolder(), "vehicles.yml");

        //Make a format to rename old configs
        SimpleDateFormat formatter = new SimpleDateFormat("dd_MM_yyyy-HH_mm_ss");
        Date date = new Date();

        //Compare the config version with the plugin version
        if (!getConfig().get("Config-Versie").equals(versions)) {
            //Renaming the config files
            defaultconfig.renameTo(new File(getDataFolder(), "configOld_" + formatter.format(date) + ".yml"));
            vehicleconfig.renameTo(new File(getDataFolder(), "vehiclesOld_" + formatter.format(date) + ".yml"));
            saveDefaultConfig();
        }

        configList.add(messagesConfig);
        configList.add(vehicleDataConfig);
        configList.add(vehiclesConfig);
        configList.add(defaultConfig);
        configList.forEach(ConfigUtils::reload);
    }

    public void registerListener(Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, this);
    }

    public static String fol() {
        return String.valueOf(Main.instance.getFile());
    }
}
