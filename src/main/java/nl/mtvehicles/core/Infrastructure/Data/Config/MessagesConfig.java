package nl.mtvehicles.core.Infrastructure.Data.Config;

import nl.mtvehicles.core.Infrastructure.Helpers.Text;
import nl.mtvehicles.core.Infrastructure.Models.Config;
import org.bukkit.command.CommandSender;

import java.util.List;

public class MessagesConfig extends Config {
    public MessagesConfig() {
        this.setFileName("messages.yml");
    }

    public String getMessage(String key) {
        return Text.colorize((String) this.getConfig().get(key));
    }

    public void sendMessage(CommandSender sender, String key) {
        Object object = this.getConfig().get(key);
        if (object instanceof List) {
            for (String s : this.getConfig().getStringList(key)) {
                sender.sendMessage(Text.colorize(s));
            }
        }
        sender.sendMessage(Text.colorize(String.valueOf(object)));
    }
}
