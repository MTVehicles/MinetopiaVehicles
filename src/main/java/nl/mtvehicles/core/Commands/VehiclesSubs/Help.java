package nl.mtvehicles.core.Commands.VehiclesSubs;

import nl.mtvehicles.core.Infrastructure.Helpers.Text;
import nl.mtvehicles.core.Infrastructure.Models.MTVehicleSubCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class Help extends MTVehicleSubCommand {
    @Override
    public boolean execute(CommandSender sender, Command cmd, String s, String[] args) {

        sender.sendMessage(Text.colorize("&1Super vet!!!"));


        return false;
    }
}
