package nl.mtvehicles.core.commands;

import nl.mtvehicles.core.commands.vehiclesubs.*;
import nl.mtvehicles.core.infrastructure.models.MTVehicleCommand;
import nl.mtvehicles.core.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class VehicleSubCommandManager extends MTVehicleCommand {
    public static String name = "minetopiavehicles";

    public VehicleSubCommandManager() {
        Main.subcommands.put("info", new VehicleInfo());
        Main.subcommands.put("help", new VehicleHelp());
        Main.subcommands.put("admin", new VehicleHelp());
        Main.subcommands.put("reload", new VehicleReload());
        Main.subcommands.put("menu", new VehicleMenu());
        Main.subcommands.put("restore", new VehicleRestore());
        Main.subcommands.put("edit", new VehicleEdit());
        Main.subcommands.put("fuel", new VehicleFuel());
        Main.subcommands.put("benzine", new VehicleFuel());
        Main.subcommands.put("setowner", new VehicleSetOwner());
        Main.subcommands.put("public", new VehiclePublic());
        Main.subcommands.put("private", new VehiclePrivate());
        Main.subcommands.put("addmember", new VehicleAddMember());
        Main.subcommands.put("addrider", new VehicleAddRider());
        Main.subcommands.put("removemember", new VehicleRemoveMember());
        Main.subcommands.put("removerider", new VehicleRemoveRider());
        //Main.subcommands.put("givecar", new VehicleGiveCar());
        //Main.subcommands.put("givevoucher", new VehicleGiveVoucher());
        Main.subcommands.put("update", new VehicleUpdate());
        Main.subcommands.put("delete", new VehicleDelete());
        Main.subcommands.put("language", new VehicleLanguage());
    }

    @Override
    public boolean execute(CommandSender sender, Command cmd, String s, String[] args) {
        if (args.length == 0) {
            Main.subcommands.get("help").onExecute(sender, cmd, s, args);
            return true;
        }

        if (Main.subcommands.get(args[0].toLowerCase()) == null) {
            sendMessage(Main.messagesConfig.getMessage("cmdNotExists"));
            return true;
        }

        Main.subcommands.get(args[0].toLowerCase()).onExecute(sender, cmd, s, args);
        return true;
    }
}
