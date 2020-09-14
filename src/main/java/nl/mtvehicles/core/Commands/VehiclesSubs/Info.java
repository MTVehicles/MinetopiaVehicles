package nl.mtvehicles.core.Commands.VehiclesSubs;

import nl.mtvehicles.core.Infrastructure.Models.MTVehicleSubCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class Info extends MTVehicleSubCommand {
    @Override
    public boolean execute(CommandSender sender, Command cmd, String s, String[] args) {
        sendMessage("&2-=-=-=-=-=-=-=-=-=-=-");
        sendMessage("");
        sendMessage("&a MT-Vehicles &2is gemaakt door: &aGamerJoep_ &2en &aJeffrey. &2wil je meer weten? Ga dan naar &ahttps://mtvehicles.nl");
        sendMessage("");
        sendMessage("&2-=-=-=-=-=-=-=-=-=-=-");

        return true;
    }
}
