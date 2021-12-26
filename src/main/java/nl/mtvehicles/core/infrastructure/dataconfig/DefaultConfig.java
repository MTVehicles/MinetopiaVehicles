package nl.mtvehicles.core.infrastructure.dataconfig;

import nl.mtvehicles.core.infrastructure.dataconfig.defaultconfigutils.ConfigBlockWhitelist;
import nl.mtvehicles.core.infrastructure.dataconfig.defaultconfigutils.ConfigGasStation;
import nl.mtvehicles.core.infrastructure.dataconfig.defaultconfigutils.ConfigRegionWhitelist;
import nl.mtvehicles.core.infrastructure.enums.RegionWhitelistAction;
import nl.mtvehicles.core.infrastructure.helpers.TextUtils;
import nl.mtvehicles.core.infrastructure.models.ConfigUtils;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DefaultConfig extends ConfigUtils {
    public static ConfigGasStation gasStation = new ConfigGasStation();
    public static ConfigBlockWhitelist blockWhitelist = new ConfigBlockWhitelist();
    public static ConfigRegionWhitelist regionWhitelist = new ConfigRegionWhitelist();

    public DefaultConfig() {
        this.setFileName("config.yml");
    }

    public String getMessage(String key) {
        return TextUtils.colorize((String) this.getConfig().get(key));
    }

}
