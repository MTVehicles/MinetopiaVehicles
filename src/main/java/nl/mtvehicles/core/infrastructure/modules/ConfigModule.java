package nl.mtvehicles.core.infrastructure.modules;

import lombok.Getter;
import lombok.Setter;
import nl.mtvehicles.core.Main;
import nl.mtvehicles.core.infrastructure.dataconfig.*;
import nl.mtvehicles.core.infrastructure.models.MTVConfig;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Module for managing configuration files
 */
public class ConfigModule {
    private static @Getter
    @Setter
    ConfigModule instance;

    /**
     * List of all configuration files.
     */
    public static List<MTVConfig> configList = new ArrayList<>();

    /**
     * SuperSecretSettings configuration file
     */
    public static SecretSettingsConfig secretSettings = new SecretSettingsConfig();
    /**
     * messages_xx.yml configuration files
     */
    public static MessagesConfig messagesConfig = new MessagesConfig();
    /**
     * VehicleData.yml configuration file
     */
    public static VehicleDataConfig vehicleDataConfig = new VehicleDataConfig();
    /**
     * Vehicles.yml configuration file
     */
    public static VehiclesConfig vehiclesConfig = new VehiclesConfig();
    /**
     * Default configuration file (config.yml)
     */
    public static DefaultConfig defaultConfig = new DefaultConfig();

    public ConfigModule() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd_MM_yyyy-HH_mm_ss");
        Date date = new Date();

        String configVersion = Main.configVersion;
        String messagesVersion = Main.messagesVersion;

        Main.instance.saveResource("credits.txt", true);

        final boolean oldConfigVersion = !secretSettings.getConfigVersion().equals(configVersion) || defaultConfig.hasOldVersionChecking();
        final boolean oldMessagesVersion = !secretSettings.getMessagesVersion().equals(messagesVersion) || defaultConfig.hasOldVersionChecking();

        if (oldConfigVersion) {
            File dc = new File(Main.instance.getDataFolder(), "config.yml");
            File vc = new File(Main.instance.getDataFolder(), "vehicles.yml");
            File sss = new File(Main.instance.getDataFolder(), "supersecretsettings.yml");
            dc.renameTo(new File(Main.instance.getDataFolder(), "configOld_" + formatter.format(date) + ".yml"));
            vc.renameTo(new File(Main.instance.getDataFolder(), "vehiclesOld_" + formatter.format(date) + ".yml"));
            sss.delete();
            Main.instance.saveDefaultConfig();
        }

        if (oldMessagesVersion) {
            File sss = new File(Main.instance.getDataFolder(), "supersecretsettings.yml");
            sss.delete();
            messagesConfig.saveNewLanguageFiles(formatter.format(date));
        }

        configList.add(secretSettings);
        configList.add(messagesConfig);
        configList.add(vehicleDataConfig);
        configList.add(vehiclesConfig);
        configList.add(defaultConfig);
        reloadConfigs();
    }

    /**
     * Reload all configuration files.
     */
    public static void reloadConfigs(){
        configList.forEach(MTVConfig::reload);
        if (!messagesConfig.setLanguageFile(secretSettings.getMessagesLanguage())){
            Main.instance.getLogger().severe("Messages.yml for your desired language could not be found. Disabling the plugin...");
            Main.disablePlugin();
        }
    }
}
