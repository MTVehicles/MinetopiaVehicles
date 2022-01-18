package nl.mtvehicles.core.infrastructure.modules;

import lombok.Getter;
import lombok.Setter;
import nl.mtvehicles.core.Main;
import nl.mtvehicles.core.infrastructure.dataconfig.*;
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

    final private String configVersion = Main.configVersion;
    final private String messagesVersion = Main.messagesVersion;

    public static List<ConfigUtils> configList = new ArrayList<>();
    public static MessagesConfig messagesConfig = new MessagesConfig();
    public static VehicleDataConfig vehicleDataConfig = new VehicleDataConfig();
    public static VehiclesConfig vehiclesConfig = new VehiclesConfig();
    public static SecretSettingsConfig secretSettings = new SecretSettingsConfig();
    public static DefaultConfig defaultConfig = new DefaultConfig();

    public ConfigModule() {
        File defaultconfig = new File(Main.instance.getDataFolder(), "config.yml");
        File vehicleconfig = new File(Main.instance.getDataFolder(), "vehicles.yml");
        SimpleDateFormat formatter = new SimpleDateFormat("dd_MM_yyyy-HH_mm_ss");
        Date date = new Date();
        if (!secretSettings.getConfigVersion().equals(configVersion)) {
            defaultconfig.renameTo(new File(Main.instance.getDataFolder(), "configOld_" + formatter.format(date) + ".yml"));
            vehicleconfig.renameTo(new File(Main.instance.getDataFolder(), "vehiclesOld_" + formatter.format(date) + ".yml"));
            Main.instance.saveDefaultConfig();
        }

        if (!secretSettings.getMessagesVersion().equals(messagesVersion)) {
            messagesConfig.saveNewLanguageFiles(formatter.format(date));
            Main.instance.saveDefaultConfig();
        }

        configList.add(messagesConfig);
        configList.add(vehicleDataConfig);
        configList.add(vehiclesConfig);
        configList.add(secretSettings);
        configList.add(defaultConfig);
        configList.forEach(ConfigUtils::reload);
    }
}