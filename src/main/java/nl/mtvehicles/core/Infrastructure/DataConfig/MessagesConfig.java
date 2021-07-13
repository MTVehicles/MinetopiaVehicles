package nl.mtvehicles.core.Infrastructure.DataConfig;

import nl.mtvehicles.core.Infrastructure.Helpers.TextUtils;
import nl.mtvehicles.core.Infrastructure.Models.ConfigUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class MessagesConfig extends ConfigUtils {
    public MessagesConfig() {
        this.setFileName("messages.yml");
    }

    public String getMessage(String key) {
        return TextUtils.colorize((String) this.getConfig().get(key));
    }

    public void sendMessage(CommandSender sender, String key) {
        Object object = this.getConfig().get(key);
        if (object instanceof List) {
            for (String s : this.getConfig().getStringList(key)) {
                sender.sendMessage(TextUtils.colorize(s));
            }
        }
        sender.sendMessage(TextUtils.colorize(String.valueOf(object)));
    }

    public void sendMessage(Player player, String key) {
        Object object = this.getConfig().get(key);
        if (object instanceof List) {
            for (String s : this.getConfig().getStringList(key)) {
                player.sendMessage(TextUtils.colorize(s));
            }
        }
        player.sendMessage(TextUtils.colorize(String.valueOf(object)));
    }
}