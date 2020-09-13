package nl.mtvehicles.core.Infrastructure.Data.Config;

import nl.mtvehicles.core.Infrastructure.Helpers.Text;
import nl.mtvehicles.core.Infrastructure.Models.Config;

public class MessagesConfig extends Config {
    public MessagesConfig() {
        this.setFileName("messages.yml");
    }

    public String getMessage(String key) {
        return Text.colorize((String) this.getConfig().get(key));


    }
}
