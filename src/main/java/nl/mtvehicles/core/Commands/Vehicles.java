package nl.mtvehicles.core.Commands;

import nl.mtvehicles.core.Commands.VehiclesSubs.Help;
import nl.mtvehicles.core.Infrastructure.Models.MTVehicleCommand;
import nl.mtvehicles.core.Infrastructure.Models.MTVehicleSubCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class Vehicles extends MTVehicleCommand {
    @Override
    public boolean execute(CommandSender sender, Command cmd, String s, String[] args) {
        MTVehicleSubCommand subCommand;

        if (args.length == 0) {


            subCommand = new Help();
            subCommand.execute(sender, cmd, s, args);
            return true;
        }

        if (args.length == 1) {


            if (args[0].equalsIgnoreCase("help")) {
                subCommand = new Help();
                subCommand.execute(sender, cmd, s, args);
            }


            return true;
        }


        return false;
    }
}
