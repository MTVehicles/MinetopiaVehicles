package nl.mtvehicles.core;

import nl.mtvehicles.core.Commands.VehiclesSub;
import nl.mtvehicles.core.Events.MenuClickEvent;
import nl.mtvehicles.core.Infrastructure.DataConfig.*;
import nl.mtvehicles.core.Infrastructure.DataConfig.DefaultConfig;
import nl.mtvehicles.core.Infrastructure.DataConfig.MessagesConfig;
import nl.mtvehicles.core.Infrastructure.DataConfig.VehicleDataConfig;
import nl.mtvehicles.core.Infrastructure.DataConfig.VehiclesConfig;
import nl.mtvehicles.core.Infrastructure.Models.Config;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class Main extends JavaPlugin {
    public static Main instance;

    public static List<Config> configList = new ArrayList<>();

    public static MessagesConfig messagesConfig = new MessagesConfig();
    public static VehicleDataConfig vehicleDataConfig = new VehicleDataConfig();
    public static VehiclesConfig vehiclesConfig = new VehiclesConfig();
    public static DefaultConfig defaultConfig = new DefaultConfig();

    @Override
    public void onEnable() {
        instance = this;

        getLogger().info("De plugin is opgestart!");
        Bukkit.getPluginCommand("minetopiavehicles").setExecutor(new VehiclesSub());
        Bukkit.getPluginManager().registerEvents((Listener)new MenuClickEvent(), (Plugin)this);

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
