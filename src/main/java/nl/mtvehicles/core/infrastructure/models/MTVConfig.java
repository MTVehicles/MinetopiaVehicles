package nl.mtvehicles.core.infrastructure.models;

import nl.mtvehicles.core.Main;
import nl.mtvehicles.core.infrastructure.enums.ConfigType;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.logging.Level;

/**
 * Abstract class for plugin's configuration files
 */
public abstract class MTVConfig {
    /**
     * Type of the configuration file
     */
    final protected ConfigType configType;
    /**
     * Configuration file
     */
    protected FileConfiguration config;
    private File configFile = null;
    private String fileName;

    /**
     * Basic setter
     * @param configType Type of the config
     */
    public MTVConfig(ConfigType configType){
        this.configType = configType;
        if (!configType.isMessages()) this.fileName = configType.getFileName();
    }

    /**
     * Reload configuration file (e.g. if you've just edited it in a text editor)
     */
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


    /**
     * Get the file configuration
     * @deprecated New alternative methods have been created in the 'nl.mtvehicles.core.infrastructure.dataconfig' package. Otherwise, {@link #getConfiguration()} should be used instead.
     *
     * @return Config as FileConfiguration
     * @see #getConfiguration()
     */
    @Deprecated
    public FileConfiguration getConfig() {
        if (config == null) {
            reload();
        }
        return config;
    }

    /**
     * Get the file configuration (new method, protected - should be only used in config classes)
     *
     * @return Config as FileConfiguration
     */
    protected FileConfiguration getConfiguration() {
        if (config == null) {
            reload();
        }
        return config;
    }

    /**
     * Save the newly assigned values to the configuration file
     * @return True if saving was successful
     */
    public boolean save() {
        if (config == null || configFile == null) {
            return false;
        }
        try {
            getConfiguration().save(configFile);
        } catch (IOException ex) {
            Main.instance.getLogger().log(Level.SEVERE, "Could not save config to " + configFile, ex);
        }
        this.reload();
        return true;
    }

    /**
     * Save the default configuration file
     */
    public void saveDefaultConfig() {
        if (configFile == null) {
            configFile = new File(Main.instance.getDataFolder(), fileName);
        }
        if (!configFile.exists()) {
            Main.instance.saveResource(fileName, false);
        }
    }

    /**
     * Set the name of the configuration file (e.g. 'messages/messages_en.yml')
     * @param fileName Name of the configuration file
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Set the configuration file
     * @param configFile Configuration file
     */
    public void setConfigFile(File configFile){
        this.configFile = configFile;
    }
}
