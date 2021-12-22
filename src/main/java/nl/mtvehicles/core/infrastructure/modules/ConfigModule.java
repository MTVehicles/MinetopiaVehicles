package nl.mtvehicles.core.infrastructure.modules;

import lombok.Getter;
import lombok.Setter;
import nl.mtvehicles.core.Main;
import nl.mtvehicles.core.infrastructure.dataconfig.DefaultConfig;
import nl.mtvehicles.core.infrastructure.dataconfig.MessagesConfig;
import nl.mtvehicles.core.infrastructure.dataconfig.VehicleDataConfig;
import nl.mtvehicles.core.infrastructure.dataconfig.VehiclesConfig;
import nl.mtvehicles.core.infrastructure.models.ConfigUtils;
import org.bukkit.plugin.PluginDescriptionFile;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ConfigModule {
    private static @Getter
    @Setter
    ConfigModule instance;

    final public static String configVersion = Main.configVersion;

    public static List<ConfigUtils> configList = new ArrayList<>();
    public static MessagesConfig messagesConfig = new MessagesConfig();
    public static VehicleDataConfig vehicleDataConfig = new VehicleDataConfig();
    public static VehiclesConfig vehiclesConfig = new VehiclesConfig();
    public static DefaultConfig defaultConfig = new DefaultConfig();

    public ConfigModule() {
        PluginDescriptionFile pdf = Main.instance.getDescription();
        String versions = pdf.getVersion();

        File defaultconfig = new File(Main.instance.getDataFolder(), "config.yml");
        File vehicleconfig = new File(Main.instance.getDataFolder(), "vehicles.yml");
        SimpleDateFormat formatter = new SimpleDateFormat("dd_MM_yyyy-HH_mm_ss");
        Date date = new Date();
        if (!Main.instance.getConfig().get("Config-Versie").equals(configVersion)) {
            defaultconfig.renameTo(new File(Main.instance.getDataFolder(), "configOld_" + formatter.format(date) + ".yml"));
            vehicleconfig.renameTo(new File(Main.instance.getDataFolder(), "vehiclesOld_" + formatter.format(date) + ".yml"));
            messagesConfig.saveNewLanguageFiles(formatter.format(date)); //Messages might have been updated too - changes the files
            Main.instance.saveDefaultConfig();
        }

        configList.add(messagesConfig);
        configList.add(vehicleDataConfig);
        configList.add(vehiclesConfig);
        configList.add(defaultConfig);
        configList.forEach(ConfigUtils::reload);
    }
}