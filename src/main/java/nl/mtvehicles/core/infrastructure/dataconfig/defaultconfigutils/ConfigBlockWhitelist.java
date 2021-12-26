package nl.mtvehicles.core.infrastructure.dataconfig.defaultconfigutils;

import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;
import java.util.stream.Collectors;

public class ConfigBlockWhitelist {
    private FileConfiguration config = ConfigModule.defaultConfig.getConfig();

    public boolean isBlockWhitelistEnabled() {
        return config.getBoolean("blockWhitelist.enabled");
    }

    public List<Material> blockWhiteList() {
        return config.getStringList("blockWhitelist.list").stream().map(Material::getMaterial).collect(Collectors.toList());
    }
}
