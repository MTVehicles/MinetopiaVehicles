package nl.mtvehicles.core.infrastructure.interfaces;

import org.bukkit.configuration.file.FileConfiguration;

public interface ConfigInterface {

    /**
     * Reload configuration file (e.g. if you've just edited it in a text editor)
     */
    void reload();

    /**
     * Save the newly assigned values to the configuration file
     *
     * @return Whether saving was successful
     */
    boolean save();

    /**
     * Get the file configuration
     *
     * @return Config as FileConfiguration
     */
    FileConfiguration getConfig();

    void saveDefaultConfig();
}
