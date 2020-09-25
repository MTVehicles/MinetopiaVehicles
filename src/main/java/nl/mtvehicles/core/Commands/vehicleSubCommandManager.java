package nl.mtvehicles.core.Commands;

import nl.mtvehicles.core.Commands.VehiclesSubs.*;
import nl.mtvehicles.core.Infrastructure.Models.MTVehicleCommand;
import nl.mtvehicles.core.Infrastructure.Models.Vehicle;
import nl.mtvehicles.core.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class vehicleSubCommandManager extends MTVehicleCommand {
    @Override

    public boolean execute(CommandSender sender, Command cmd, String s, String[] args) {


        Vehicle.subcommands.put("info", new vehicleInfoCmd()); //vehicle help
        Vehicle.subcommands.put("help", new vehicleHelpCmd()); //vehicle help
        Vehicle.subcommands.put("reload", new vehicleReloadCmd());
        Vehicle.subcommands.put("menu", new vehicleMenuCmd());
        Vehicle.subcommands.put("restore", new vehicleRestoreCmd());
        Vehicle.subcommands.put("edit", new vehicleEditCmd());
        Vehicle.subcommands.put("setowner", new vehicleSetOwnerCMD());
        Vehicle.subcommands.put("addmember", new vehicleAddMemberCMD());
        Vehicle.subcommands.put("addrider", new vehicleAddRiderCMD());
        Vehicle.subcommands.put("removemember", new vehicleRemoveMemberCMD());
        Vehicle.subcommands.put("removerider", new vehicleRemoveRiderCMD());
        Vehicle.subcommands.put("admin", new vehicleAdminCMD());



        if (args.length == 0) {
            Vehicle.subcommands.get("help").onExecute(sender, cmd, s, args);

            return true;
        }
        if (Vehicle.subcommands.get(args[0]) == null){
            sendMessage(Main.messagesConfig.getMessage("cmdNotExists"));
        } else {
            Vehicle.subcommands.get(args[0]).onExecute(sender, cmd, s, args);
        }

        return true;
    }
}
