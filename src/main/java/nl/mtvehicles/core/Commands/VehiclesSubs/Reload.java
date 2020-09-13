package nl.mtvehicles.core.Commands.VehiclesSubs;

import nl.mtvehicles.core.Infrastructure.Models.Config;
import nl.mtvehicles.core.Infrastructure.Models.MTVehicleSubCommand;
import nl.mtvehicles.core.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class Reload extends MTVehicleSubCommand {
    @Override
    public boolean execute(CommandSender sender, Command cmd, String s, String[] args) {
        if (sender.hasPermission("mtvehicles.reload"))
            Bukkit.getLogger().info("Reload config files..");
            Main.configList.forEach(Config::reload);
            Bukkit.getLogger().info("Files loaded!");
            sendMessage(Main.messagesConfig.getMessage("reloadSuccesvol"));
            return false;
    }
}
