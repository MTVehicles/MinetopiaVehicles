package nl.mtvehicles.core.Infrastructure.Interfaces;

import org.bukkit.configuration.file.FileConfiguration;

public interface ConfigInterface {
    void reload();

    boolean save();

    FileConfiguration getConfig();

    void saveDefaultConfig();
}
