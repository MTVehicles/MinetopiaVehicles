package nl.mtvehicles.core.Commands.VehiclesSubs;

import nl.mtvehicles.core.Infrastructure.Models.MTVehicleSubCommand;
import nl.mtvehicles.core.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HelpCmd extends MTVehicleSubCommand {
    @Override
    public boolean execute(CommandSender sender, Command cmd, String s, String[] args) {
        if (!(sender instanceof Player)) {
            sendMessage(Main.messagesConfig.getMessage("notForConsole"));

            return false;
        }

        if (!sender.hasPermission("mtvehicles.admin")) {
            sendMessage("&aMT-Vehicles &2is made by: &aGamerJoep_&2. Do you want know more? Go to &ahttps://mtvehicles.nl");
            sendMessage("");
            sendMessage(Main.messagesConfig.getMessage("helpMessagePlayer"));

            return true;
        }
        sendMessage("&aMT-Vehicles &2is made by: &aGamerJoep_&2. Do you want know more? Go to &ahttps://mtvehicles.nl");
        sendMessage("");
        sendMessage(Main.messagesConfig.getMessage("helpMessageAdmin"));

        return true;
    }
}
