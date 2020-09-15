package nl.mtvehicles.core.Infrastructure.Data.Config;

import nl.mtvehicles.core.Infrastructure.Helpers.TextUtils;
import nl.mtvehicles.core.Infrastructure.Models.Config;

public class DefaultConfig extends Config {
    public DefaultConfig() {
        this.setFileName("config.yml");
    }

    public String getMessage(String key) {
        return TextUtils.colorize((String) this.getConfig().get(key));
    }
}
