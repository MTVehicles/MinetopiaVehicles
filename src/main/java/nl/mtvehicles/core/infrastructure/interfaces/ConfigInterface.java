package nl.mtvehicles.core.infrastructure.interfaces;

import org.bukkit.configuration.file.FileConfiguration;

public interface ConfigInterface {
    void reload();

    boolean save();

    FileConfiguration getConfig();

    void saveDefaultConfig();
}
