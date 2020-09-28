package nl.mtvehicles.core.Commands.VehiclesSubs;

import nl.mtvehicles.core.Infrastructure.Models.MTVehicleSubCommand;
import nl.mtvehicles.core.Infrastructure.Models.Vehicle;
import nl.mtvehicles.core.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class vehicleHelpCmd extends MTVehicleSubCommand {
    @Override
    public boolean execute(CommandSender sender, Command cmd, String s, String[] args) {
        if (!isPlayer) {
            sendMessage(Main.messagesConfig.getMessage("notForConsole"));
            return false;
        }

        sendMessage("");
        sendMessage("&aMT-Vehicles &2is made by: &aGamerJoep_&2. Do you want know more? Go to &ahttps://mtvehicles.nl");
        sendMessage("");
        sendMessage("&2/vehicle &ainfo &f- &2"+Main.messagesConfig.getMessage("helpInfo"));
        sendMessage("&2/vehicle &ahelp &f- &2"+Main.messagesConfig.getMessage("helpHelp"));
        sendMessage("&2/vehicle &aaddrider &f- &2"+Main.messagesConfig.getMessage("helpAddRider"));
        sendMessage("&2/vehicle &aaddmember &f- &2"+Main.messagesConfig.getMessage("helpAddMember"));
        sendMessage("&2/vehicle &aremoverider &f- &2"+Main.messagesConfig.getMessage("helpRemoveRider"));
        sendMessage("&2/vehicle &aremovemember &f- &2"+Main.messagesConfig.getMessage("helpRemoveMember"));
        sendMessage("&2/vehicle &aadmin &f- &2"+Main.messagesConfig.getMessage("helpAdmin"));
        return true;
    }
}
