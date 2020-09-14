package nl.mtvehicles.core.Commands.VehiclesSubs;

import nl.mtvehicles.core.Infrastructure.Models.MTVehicleSubCommand;
import nl.mtvehicles.core.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Help extends MTVehicleSubCommand {
    @Override
    public boolean execute(CommandSender sender, Command cmd, String s, String[] args) {
        if (!(sender instanceof Player)) {
            sendMessage(Main.messagesConfig.getMessage("notForConsole"));

            return false;
        }

        if (!sender.hasPermission("mtvehicles.admin")) {
            sendMessage(Main.messagesConfig.getMessage("helpMessageSpeler"));

            return true;
        }
        sendMessage(Main.messagesConfig.getMessage("helpMessageAdmin"));

        return true;
    }
}
