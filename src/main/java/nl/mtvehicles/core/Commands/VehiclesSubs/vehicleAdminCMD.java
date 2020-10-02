package nl.mtvehicles.core.Commands.VehiclesSubs;

import nl.mtvehicles.core.Infrastructure.Models.MTVehicleSubCommand;
import nl.mtvehicles.core.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class vehicleAdminCMD extends MTVehicleSubCommand {
    @Override
    public boolean execute(CommandSender sender, Command cmd, String s, String[] args) {
        if (!isPlayer) {
            sendMessage(Main.messagesConfig.getMessage("notForConsole"));
            return false;
        }

        sendMessage("");
        sendMessage("&aMT-Vehicles &2is made by: &aGamerJoep_&2. Do you want know more? Go to &ahttps://mtvehicles.nl");
        sendMessage("");
        sendMessage("&2/vehicle &aedit &f- &2" + Main.messagesConfig.getMessage("adminEdit"));
        sendMessage("&2/vehicle &amenu &f- &2" + Main.messagesConfig.getMessage("adminMenu"));
        sendMessage("&2/vehicle &abenzine &f- &2" + Main.messagesConfig.getMessage("adminBenzine"));
        sendMessage("&2/vehicle &arestore &f- &2" + Main.messagesConfig.getMessage("adminRestore"));
        sendMessage("&2/vehicle &areload &f- &2" + Main.messagesConfig.getMessage("adminReload"));
        return true;
    }
}
