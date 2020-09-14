package nl.mtvehicles.core.Commands;

import nl.mtvehicles.core.Commands.VehiclesSubs.*;
import nl.mtvehicles.core.Infrastructure.Helpers.ItemFactory;
import nl.mtvehicles.core.Infrastructure.Models.MTVehicleCommand;
import nl.mtvehicles.core.Infrastructure.Models.MTVehicleSubCommand;
import nl.mtvehicles.core.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.HashMap;

public class Vehicles extends MTVehicleCommand {
    @Override
    public boolean execute(CommandSender sender, Command cmd, String s, String[] args) {
        HashMap<String, MTVehicleSubCommand> subcommands = new HashMap<>();

        subcommands.put("info", new Info());
        subcommands.put("help", new Help());
        subcommands.put("reload", new Reload());

        if (args.length == 0) {
            subcommands.get("help").onExecute(sender, cmd, s, args);

            return true;
        }
        if (subcommands.get(args[0]) == null){
            sendMessage(Main.messagesConfig.getMessage("cmdNotExists"));
        } else {
            subcommands.get(args[0]).onExecute(sender, cmd, s, args);
        }

        return true;
    }
}
