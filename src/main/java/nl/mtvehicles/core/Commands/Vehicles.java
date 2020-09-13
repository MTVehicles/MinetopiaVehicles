package nl.mtvehicles.core.Commands;

import nl.mtvehicles.core.Commands.VehiclesSubs.Help;
import nl.mtvehicles.core.Infrastructure.Models.MTVehicleCommand;
import nl.mtvehicles.core.Infrastructure.Models.MTVehicleSubCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.HashMap;

public class Vehicles extends MTVehicleCommand {
    @Override
    public boolean execute(CommandSender sender, Command cmd, String s, String[] args) {
        HashMap<String, MTVehicleSubCommand> subcommands = new HashMap<>();

        subcommands.put("help", new Help());

        if (args.length == 0) {
            subcommands.get("help").onExecute(sender, cmd, s, args);
            return true;
        }


        if (!subcommands.containsKey(args[1])) {
            sendMessage("&cDit command bestaat niet");
        }

        subcommands.get(args[1]).onExecute(sender, cmd, s, args);

        return true;
    }
}
