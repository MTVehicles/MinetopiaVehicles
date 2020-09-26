package nl.mtvehicles.core;

import com.comphenix.protocol.ProtocolLibrary;
import nl.mtvehicles.core.Commands.vehicleSubCommandManager;
import nl.mtvehicles.core.Events.*;
import nl.mtvehicles.core.Infrastructure.DataConfig.DefaultConfig;
import nl.mtvehicles.core.Infrastructure.DataConfig.MessagesConfig;
import nl.mtvehicles.core.Infrastructure.DataConfig.VehicleDataConfig;
import nl.mtvehicles.core.Infrastructure.DataConfig.VehiclesConfig;
import nl.mtvehicles.core.Infrastructure.Models.ConfigUtils;
import nl.mtvehicles.core.Infrastructure.Models.MTVehicleSubCommand;
import nl.mtvehicles.core.Movement.VehicleMovement1_12;
import nl.mtvehicles.core.Movement.VehicleMovement1_13;
import nl.mtvehicles.core.Movement.VehicleMovement1_15;
import org.bukkit.Bukkit;
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

        getLogger().info("De plugin is opgestart!");
        Bukkit.getPluginCommand("minetopiavehicles").setExecutor(new vehicleSubCommandManager());
        Bukkit.getPluginManager().registerEvents(new MenuClickEvent(), this);
        Bukkit.getPluginManager().registerEvents(new VehiclePlaceEvent(), this);
        Bukkit.getPluginManager().registerEvents(new VehicleClickEvent(), this);
        Bukkit.getPluginManager().registerEvents(new VehicleLeaveEvent(), this);
        Bukkit.getPluginManager().registerEvents(new ChatEvent(), this);
        Bukkit.getPluginManager().registerEvents(new VehicleEntityEvent(), this);

        if (version.equals("v1_12_R1")) {
            ProtocolLibrary.getProtocolManager().addPacketListener(new VehicleMovement1_12());
            getLogger().info("Loaded vehicle movement for version: "+version);
        }
        if (version.equals("v1_13_R2")) {
            ProtocolLibrary.getProtocolManager().addPacketListener(new VehicleMovement1_13());
            getLogger().info("Loaded vehicle movement for version: "+version);
        }
        if (version.equals("v1_15_R1")) {
            ProtocolLibrary.getProtocolManager().addPacketListener(new VehicleMovement1_15());
            getLogger().info("Loaded vehicle movement for version: "+version);
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

}
