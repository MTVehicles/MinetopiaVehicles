package nl.mtvehicles.core;

import nl.mtvehicles.core.commands.VehicleSubCommandManager;
import nl.mtvehicles.core.commands.VehicleTabCompleterManager;
import nl.mtvehicles.core.events.*;
import nl.mtvehicles.core.infrastructure.dataconfig.*;
import nl.mtvehicles.core.infrastructure.helpers.TextUtils;
import nl.mtvehicles.core.infrastructure.models.ConfigUtils;
import nl.mtvehicles.core.infrastructure.models.MTVehicleSubCommand;
import nl.mtvehicles.core.inventory.InventoryClickEvent;
import nl.mtvehicles.core.inventory.InventoryCloseEvent;
import nl.mtvehicles.core.movement.MovementManager;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
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

        if (!version.equals("v1_12_R1") && !version.equals("v1_13_R2") && !version.equals("v1_15_R1") && !version.contains("v1_16") && !version.contains("v1_17") && !version.contains("v1_18")) {
            getLogger().info("-------------------------------------------------------");
            getLogger().info("Your Server version is not supported by the plugin");
            getLogger().info("check the supported versions here https://mtvehicles.nl");
            getLogger().info("-------------------------------------------------------");
            return;
        }

        PluginDescriptionFile pdf = this.getDescription();
        String versions = pdf.getVersion();

        getLogger().info("Plugin is started!");
        System.out.println("--------------------------");
        System.out.println("Welcome by MTVehicles " + versions + " !");
        System.out.println("Thanks for using our plugin.");
        System.out.println("--------------------------");

        PluginCommand pluginCommand = Main.instance.getCommand("minetopiavehicles");

        if (pluginCommand != null) {
            pluginCommand.setExecutor(new VehicleSubCommandManager());
            pluginCommand.setTabCompleter(new VehicleTabCompleterManager());
        }

        Bukkit.getPluginManager().registerEvents(new InventoryClickEvent(), this);
        Bukkit.getPluginManager().registerEvents(new VehiclePlaceEvent(), this);
        Bukkit.getPluginManager().registerEvents(new VehicleClickEvent(), this);
        Bukkit.getPluginManager().registerEvents(new VehicleLeaveEvent(), this);
        Bukkit.getPluginManager().registerEvents(new ChatEvent(), this);
        Bukkit.getPluginManager().registerEvents(new VehicleEntityEvent(), this);
        Bukkit.getPluginManager().registerEvents(new JoinEvent(), this);
        Bukkit.getPluginManager().registerEvents(new LeaveEvent(), this);
        Bukkit.getPluginManager().registerEvents(new VehicleVoucherEvent(), this);
        Bukkit.getPluginManager().registerEvents(new InventoryCloseEvent(), this);

        Metrics metrics = new Metrics(this, 5932);
        metrics.addCustomChart(new Metrics.SimplePie("used_language", () -> {
            return defaultConfig.getConfig().getString("messagesLanguage");
        }));

        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            if (p.isInsideVehicle()) {
                p.kickPlayer(TextUtils.colorize(Main.messagesConfig.getMessage("reloadInVehicle")));
            }
            MovementManager.MovementSelector(p);
        }

        File defaultconfig = new File(getDataFolder(), "config.yml");
        File vehicleconfig = new File(getDataFolder(), "vehicles.yml");
        SimpleDateFormat formatter = new SimpleDateFormat("dd_MM_yyyy-HH_mm_ss");
        Date date = new Date();
        if (!getConfig().get("Config-Versie").equals(versions)) {
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

    public static String fol() {
        return String.valueOf(Main.instance.getFile());
    }
}
