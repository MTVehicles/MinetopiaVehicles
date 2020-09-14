package nl.mtvehicles.core.Commands.VehiclesSubs;

import nl.mtvehicles.core.Infrastructure.Models.MTVehicleSubCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class Info extends MTVehicleSubCommand {
    @Override
    public boolean execute(CommandSender sender, Command cmd, String s, String[] args) {
        sendMessage("&2-=-=-=-=-=-=-=-=-=-=-");
        sendMessage("");
        sendMessage("&a MT-Vehicles &2is made by: &aGamerJoep_ &2and &aJeffrey. &2do you want know more? Go to &ahttps://mtvehicles.nl");
        sendMessage("");
        sendMessage("&2-=-=-=-=-=-=-=-=-=-=-");

        return true;
    }
}
