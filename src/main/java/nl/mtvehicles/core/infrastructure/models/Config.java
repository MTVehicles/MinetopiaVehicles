package nl.mtvehicles.core.infrastructure.models;

import nl.mtvehicles.core.Main;
import nl.mtvehicles.core.infrastructure.enums.ConfigType;
import nl.mtvehicles.core.infrastructure.interfaces.ConfigInterface;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.logging.Level;

public abstract class Config implements ConfigInterface {
    final protected ConfigType configType;
    protected FileConfiguration config;
    private File configFile = null;
    private String fileName;

    public Config(ConfigType configType){
        this.configType = configType;
        if (!configType.isMessages()) this.fileName = configType.getFileName();
    }

    public void reload() {
        if (configFile == null) {
            setConfigFile(new File(Main.instance.getDataFolder(), fileName));
        }
        if (!configFile.exists())
            this.saveDefaultConfig();

        config = YamlConfiguration.loadConfiguration(configFile);

        Reader defConfigStream;
        defConfigStream = new InputStreamReader(Objects.requireNonNull(Main.instance.getResource(fileName)), StandardCharsets.UTF_8);
        YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
        config.setDefaults(defConfig);
    }


    public FileConfiguration getConfig() {
        if (config == null) {
            reload();
        }
        return config;
    }

    public boolean save() {
        if (config == null || configFile == null) {
            return false;
        }
        try {
            getConfig().save(configFile);
        } catch (IOException ex) {
            Main.instance.getLogger().log(Level.SEVERE, "Could not save config to " + configFile, ex);
        }
        this.reload();
        return true;
    }

    public void saveDefaultConfig() {
        if (configFile == null) {
            configFile = new File(Main.instance.getDataFolder(), fileName);
        }
        if (!configFile.exists()) {
            Main.instance.saveResource(fileName, false);
        }
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setConfigFile(File configFile){
        this.configFile = configFile;
    }
}
