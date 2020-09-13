package nl.mtvehicles.core;

import nl.mtvehicles.core.Infrastructure.Data.Config.*;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    public static Main instance;

    public static MessagesConfig messagesConfig = new MessagesConfig();
    public static VehicleDataConfig vehicleDataConfig = new VehicleDataConfig();
    public static VehiclesConfig vehiclesConfig = new VehiclesConfig();

    @Override
    public void onEnable() {
        //Hallo
    }

    @Override
    public void onDisable() {
        instance = this;
    }
}
