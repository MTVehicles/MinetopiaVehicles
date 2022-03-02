package nl.mtvehicles.core.infrastructure.modules;

import lombok.Getter;
import lombok.Setter;
import nl.mtvehicles.core.Main;
import nl.mtvehicles.core.infrastructure.dataconfig.*;
import nl.mtvehicles.core.infrastructure.models.ConfigUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ConfigModule {
    private static @Getter
    @Setter
    ConfigModule instance;

    public static List<ConfigUtils> configList = new ArrayList<>();
    public static SecretSettingsConfig secretSettings = new SecretSettingsConfig();
    public static MessagesConfig messagesConfig = new MessagesConfig();
    public static VehicleDataConfig vehicleDataConfig = new VehicleDataConfig();
    public static VehiclesConfig vehiclesConfig = new VehiclesConfig();
    public static DefaultConfig defaultConfig = new DefaultConfig();

    public ConfigModule() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd_MM_yyyy-HH_mm_ss");
        Date date = new Date();
        String configVersion = Main.configVersion;
        if (!secretSettings.getConfigVersion().equals(configVersion) || defaultConfig.hasOldVersionChecking()) {
            File dc = new File(Main.instance.getDataFolder(), "config.yml");
            File vc = new File(Main.instance.getDataFolder(), "vehicles.yml");
            File sss = new File(Main.instance.getDataFolder(), "supersecretsettings.yml");
            dc.renameTo(new File(Main.instance.getDataFolder(), "configOld_" + formatter.format(date) + ".yml"));
            vc.renameTo(new File(Main.instance.getDataFolder(), "vehiclesOld_" + formatter.format(date) + ".yml"));
            sss.delete();
            Main.instance.saveDefaultConfig();
        }

        String messagesVersion = Main.messagesVersion;
        if (!secretSettings.getMessagesVersion().equals(messagesVersion) || defaultConfig.hasOldVersionChecking()) {
            messagesConfig.saveNewLanguageFiles(formatter.format(date));
        }

        configList.add(secretSettings);
        configList.add(messagesConfig);
        configList.add(vehicleDataConfig);
        configList.add(vehiclesConfig);
        configList.add(defaultConfig);
        reloadConfigs();
    }

    public static void reloadConfigs(){
        configList.forEach(ConfigUtils::reload);
        if (!messagesConfig.setLanguageFile(secretSettings.getMessagesLanguage())){
            Main.instance.getLogger().severe("Messages.yml for your desired language could not be found. Disabling the plugin...");
            Main.disablePlugin();
        }
    }
}