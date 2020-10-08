package nl.mtvehicles.core;

import com.comphenix.protocol.ProtocolLibrary;
import nl.mtvehicles.core.Commands.VehicleSubCommandManager;
import nl.mtvehicles.core.Commands.VehicleTabCompleterManager;
import nl.mtvehicles.core.Events.*;
import nl.mtvehicles.core.Infrastructure.DataConfig.*;
import nl.mtvehicles.core.Infrastructure.Models.ConfigUtils;
import nl.mtvehicles.core.Infrastructure.Models.MTVehicleSubCommand;
import nl.mtvehicles.core.Inventory.InventoryClickEvent;
import nl.mtvehicles.core.Movement.*;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.ArrayList;
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


        if (!version.equals("v1_12_R1") && !version.equals("v1_13_R2") && !version.equals("v1_15_R1") && !version.equals("v1_16_R2")) {
            getLogger().info("-------------------------------------------------------");
            getLogger().info("We supporten alleen 1.12, 1.13, 1.15, en 1.16.");
            getLogger().info("als je denkt dat dit fout is contacteer ons https://mtvehicles.nl");
            getLogger().info("-------------------------------------------------------");
            return;
        }

        getLogger().info("De plugin is opgestart!");
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

        new Metrics(this, 5932);

        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            if (player.isInsideVehicle()) {
                player.kickPlayer("Ga niet in een voertuig zitten terwijl de reload bezig is!");
            }
        }

//        PluginDescriptionFile pdf = this.getDescription();
//        File config = new File(getDataFolder(), "config.yml");
//        if (getConfig().getDouble("Config-Versie") < 2.0) {
//            config.renameTo(new File(getDataFolder(), "config_OUD.yml"));
//            saveDefaultConfig();
//        }

        System.out.println(version);

        if (version.equals("v1_12_R1")) {
            ProtocolLibrary.getProtocolManager().addPacketListener(new VehicleMovement1_12());
            getLogger().info("Loaded vehicle movement for version: " + version);
        }
        if (version.equals("v1_13_R2")) {
            ProtocolLibrary.getProtocolManager().addPacketListener(new VehicleMovement1_13());
            getLogger().info("Loaded vehicle movement for version: " + version);
        }
        if (version.equals("v1_14_R1")) {
            ProtocolLibrary.getProtocolManager().addPacketListener(new VehicleMovement1_14());
            getLogger().info("Loaded vehicle movement for version: " + version);
        }
        if (version.equals("v1_15_R1")) {
            ProtocolLibrary.getProtocolManager().addPacketListener(new VehicleMovement1_15());
            getLogger().info("Loaded vehicle movement for version: " + version);
        }
        if (version.equals("v1_16_R2")) {
            ProtocolLibrary.getProtocolManager().addPacketListener(new VehicleMovement1_16());
            getLogger().info("Loaded vehicle movement for version: " + version);
        }


        configList.add(messagesConfig);
        configList.add(vehicleDataConfig);
        configList.add(vehiclesConfig);
        configList.add(defaultConfig);
        configList.forEach(ConfigUtils::reload);

    }

    @Override
    public void onDisable() {
        //
    }

    public static String fol(){
        return String.valueOf(Main.instance.getFile());
    }

}
