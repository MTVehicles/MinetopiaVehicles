package nl.mtvehicles.core.Commands;

import nl.mtvehicles.core.Commands.VehiclesSubs.*;
import nl.mtvehicles.core.Infrastructure.Models.MTVehicleCommand;
import nl.mtvehicles.core.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class VehicleSubCommandManager extends MTVehicleCommand {
    public static String name = "minetopiavehicles";

    public VehicleSubCommandManager() {
        Main.subcommands.put("info", new VehicleInfo());
        Main.subcommands.put("help", new VehicleHelp());
        Main.subcommands.put("reload", new VehicleReload());
        Main.subcommands.put("menu", new VehicleMenu());
        Main.subcommands.put("restore", new VehicleRestore());
        Main.subcommands.put("edit", new VehicleEdit());
        Main.subcommands.put("benzine", new VehicleBenzine());
        Main.subcommands.put("setowner", new VehicleSetOwner());
        Main.subcommands.put("addmember", new VehicleAddMember());
        Main.subcommands.put("addrider", new VehicleAddRider());
        Main.subcommands.put("removemember", new VehicleRemoveMember());
        Main.subcommands.put("removerider", new VehicleRemoveRider());
        Main.subcommands.put("admin", new VehicleAdmin());
    }

    @Override
    public boolean execute(CommandSender sender, Command cmd, String s, String[] args) {
        if (args.length == 0) {
            Main.subcommands.get("help").onExecute(sender, cmd, s, args);
            return true;
        }

        if (Main.subcommands.get(args[0]) == null) {
            sendMessage(Main.messagesConfig.getMessage("cmdNotExists"));
            return true;
        }


            Main.subcommands.get(args[0]).onExecute(sender, cmd, s, args);




        return true;
    }



}
