package nl.mtvehicles.core;

import nl.mtvehicles.core.Commands.Vehicles;
import nl.mtvehicles.core.Infrastructure.Data.Config.*;
import nl.mtvehicles.core.Infrastructure.Models.Config;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class Main extends JavaPlugin {
    public static Main instance;

    public List<Config> configList = new ArrayList<>();

    public static MessagesConfig messagesConfig = new MessagesConfig();
    public static VehicleDataConfig vehicleDataConfig = new VehicleDataConfig();
    public static VehiclesConfig vehiclesConfig = new VehiclesConfig();
    public static DefaultConfig defaultConfig = new DefaultConfig();

    @Override
    public void onEnable() {
        instance = this;

        System.out.println("De plugin is opgestart!");
        Bukkit.getPluginCommand("minetopiavehicles").setExecutor(new Vehicles());

        configList.add(messagesConfig);
        configList.add(vehicleDataConfig);
        configList.add(vehiclesConfig);
        configList.add(defaultConfig);

        configList.forEach(Config::reload);
    }

    @Override
    public void onDisable() {
        //
    }

}
