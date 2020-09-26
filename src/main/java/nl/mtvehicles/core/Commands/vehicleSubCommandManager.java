package nl.mtvehicles.core.Commands;

import nl.mtvehicles.core.Commands.VehiclesSubs.*;
import nl.mtvehicles.core.Infrastructure.Models.MTVehicleCommand;
import nl.mtvehicles.core.Infrastructure.Models.Vehicle;
import nl.mtvehicles.core.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;

import java.util.ArrayList;

public class vehicleSubCommandManager extends MTVehicleCommand {
    public static String name = "minetopiavehicles";

    @Override
    public boolean execute(CommandSender sender, Command cmd, String s, String[] args) {

        Main.subcommands.put("info", new vehicleInfoCmd());
        Main.subcommands.put("help", new vehicleHelpCmd());
        Main.subcommands.put("reload", new vehicleReloadCmd());
        Main.subcommands.put("menu", new vehicleMenuCmd());
        Main.subcommands.put("restore", new vehicleRestoreCmd());
        Main.subcommands.put("edit", new vehicleEditCmd());
        Main.subcommands.put("setowner", new vehicleSetOwnerCMD());
        Main.subcommands.put("addmember", new vehicleAddMemberCMD());
        Main.subcommands.put("addrider", new vehicleAddRiderCMD());
        Main.subcommands.put("removemember", new vehicleRemoveMemberCMD());
        Main.subcommands.put("removerider", new vehicleRemoveRiderCMD());
        Main.subcommands.put("admin", new vehicleAdminCMD());

        PluginCommand pluginCommand = Main.instance.getCommand(name);

        if (pluginCommand != null) {
            pluginCommand.setTabCompleter((commandSender, command, s1, strings) -> new ArrayList<>(Main.subcommands.keySet()));
        }

        if (args.length == 0) {
            Vehicle.subcommands.get("help").onExecute(sender, cmd, s, args);
            return true;
        }

        if (Vehicle.subcommands.get(args[0]) == null) {
            sendMessage(Main.messagesConfig.getMessage("cmdNotExists"));
            return true;
        }

        Vehicle.subcommands.get(args[0]).onExecute(sender, cmd, s, args);
        return true;
    }
}
