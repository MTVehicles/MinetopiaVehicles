package nl.mtvehicles.core.Infrastructure.Data.Config;

import nl.mtvehicles.core.Infrastructure.Helpers.Text;
import nl.mtvehicles.core.Infrastructure.Models.Config;

public class DefaultConfig extends Config {
    public DefaultConfig() {
        this.setFileName("config.yml");
    }

    public String getMessage(String key) {
        return Text.colorize((String) this.getConfig().get(key));
    }
}
