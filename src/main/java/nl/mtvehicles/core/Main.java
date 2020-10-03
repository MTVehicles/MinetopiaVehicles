package nl.mtvehicles.core;

import com.comphenix.protocol.ProtocolLibrary;
import nl.mtvehicles.core.Commands.VehicleTabCompleterManager;
import nl.mtvehicles.core.Commands.VehicleSubCommandManager;
import nl.mtvehicles.core.Events.*;
import nl.mtvehicles.core.Infrastructure.DataConfig.*;
import nl.mtvehicles.core.Infrastructure.Models.ConfigUtils;
import nl.mtvehicles.core.Infrastructure.Models.MTVehicleSubCommand;
import nl.mtvehicles.core.Inventory.InventoryClickEvent;
import nl.mtvehicles.core.Movement.VehicleMovement1_12;
import nl.mtvehicles.core.Movement.VehicleMovement1_13;
import nl.mtvehicles.core.Movement.VehicleMovement1_15;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
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


//        if (getServer().getPluginManager().getPlugin("ProtocolLib") == null) {
//            System.out.println("We zien dat ProtocolLib nog niet geinstalleerd is heb even geduld");
//            File dest = new File("plugins");
//            URL file;
//            try {
//                download(file = new URL("https://github.com/dmulloy2/ProtocolLib/releases/download/4.5.1/ProtocolLib.jar"), dest);
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//            }
//        }

        instance = this;


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

        int pluginId = 5932;
        Metrics metrics = new Metrics(this, pluginId);


        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            if (player.isInsideVehicle()) {
                player.kickPlayer("Ga niet in een voertuig zitten terwijl de reload bezig is!");
            }
        }

        PluginDescriptionFile pdf = this.getDescription();
        File config = new File(getDataFolder(), "config.yml");
        System.out.println(pdf.getVersion());
        if (getConfig().getDouble("Config-Versie") < 2.0) {
            config.renameTo(new File(getDataFolder(), "config_OUD.yml"));
            saveDefaultConfig();
        }
        configList.add(messagesConfig);
        configList.add(vehicleDataConfig);
        configList.add(vehiclesConfig);
        configList.add(defaultConfig);


        System.out.println(this.getFile());

        configList.forEach(ConfigUtils::reload);

        if (version.equals("v1_12_R1")) {
            ProtocolLibrary.getProtocolManager().addPacketListener(new VehicleMovement1_12());
            getLogger().info("Loaded vehicle movement for version: " + version);
        }
        if (version.equals("v1_13_R2")) {
            ProtocolLibrary.getProtocolManager().addPacketListener(new VehicleMovement1_13());
            getLogger().info("Loaded vehicle movement for version: " + version);
        }
        if (version.equals("v1_15_R1")) {
            ProtocolLibrary.getProtocolManager().addPacketListener(new VehicleMovement1_15());
            getLogger().info("Loaded vehicle movement for version: " + version);
        }




    }

    public void download(URL file, File dest) {
        try {
            InputStream is = file.openStream();
            File finaldest = new File(dest + "/" + file.getFile().replace("/dmulloy2/ProtocolLib/releases/download/4.5.1/", "/"));
            finaldest.getParentFile().mkdirs();
            finaldest.createNewFile();
            System.out.println("Voor de laatste stap moeten we even de server herladen!");

            OutputStream os = new FileOutputStream(finaldest);
            byte data[] = new byte[1024];
            int count;
            while ((count = is.read(data, 0, 1024)) != -1) {
                os.write(data, 0, count);
            }
            os.flush();
            is.close();
            os.close();
            if (getServer().getPluginManager().getPlugin("ProtocolLib") == null) {
                this.getServer().reload();
            }
        } catch (Exception ec) {
            ec.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        //
    }

}
