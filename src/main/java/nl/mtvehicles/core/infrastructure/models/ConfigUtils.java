package nl.mtvehicles.core.infrastructure.models;

import nl.mtvehicles.core.infrastructure.interfaces.ConfigInterface;
import nl.mtvehicles.core.Main;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.logging.Level;

public class ConfigUtils implements ConfigInterface {
    private FileConfiguration customConfig;
    private File customConfigFile = null;
    private String fileName;

    public void reload() {
        if (customConfigFile == null) {
            setCustomConfigFile(new File(Main.instance.getDataFolder(), fileName));
        }
        if (!customConfigFile.exists())
            this.saveDefaultConfig();

        customConfig = YamlConfiguration.loadConfiguration(customConfigFile);

        Reader defConfigStream;
        defConfigStream = new InputStreamReader(Objects.requireNonNull(Main.instance.getResource(fileName)), StandardCharsets.UTF_8);
        YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
        customConfig.setDefaults(defConfig);
    }


    public FileConfiguration getConfig() {
        if (customConfig == null) {
            reload();
        }
        return customConfig;
    }

    public boolean save() {
        if (customConfig == null || customConfigFile == null) {
            return false;
        }
        try {
            getConfig().save(customConfigFile);
        } catch (IOException ex) {
            Main.instance.getLogger().log(Level.SEVERE, "Could not save config to " + customConfigFile, ex);
        }
        this.reload();
        return true;
    }

    public void saveDefaultConfig() {
        if (customConfigFile == null) {
            customConfigFile = new File(Main.instance.getDataFolder(), fileName);
        }
        if (!customConfigFile.exists()) {
            Main.instance.saveResource(fileName, false);
        }
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setCustomConfigFile(File customConfigFile){
        this.customConfigFile = customConfigFile;
    }
}
