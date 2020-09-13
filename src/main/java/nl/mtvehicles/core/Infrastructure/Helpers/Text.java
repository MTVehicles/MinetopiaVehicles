package nl.mtvehicles.core.Infrastructure.Helpers;

import org.bukkit.ChatColor;

public class Text {
    public static String colorize(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }
}
