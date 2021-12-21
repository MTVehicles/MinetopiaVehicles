package nl.mtvehicles.core.commands;

import nl.mtvehicles.core.commands.vehiclesubs.*;
import nl.mtvehicles.core.infrastructure.models.MTVehicleCommand;
import nl.mtvehicles.core.Main;
import nl.mtvehicles.core.infrastructure.modules.CommandModule;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class VehicleSubCommandManager extends MTVehicleCommand {
    public static String name = "minetopiavehicles";

    public VehicleSubCommandManager() {
        CommandModule.subcommands.put("info", new VehicleInfo());
        CommandModule.subcommands.put("help", new VehicleHelp());
        CommandModule.subcommands.put("admin", new VehicleHelp());
        CommandModule.subcommands.put("reload", new VehicleReload());
        CommandModule.subcommands.put("menu", new VehicleMenu());
        CommandModule.subcommands.put("restore", new VehicleRestore());
        CommandModule.subcommands.put("edit", new VehicleEdit());
        CommandModule.subcommands.put("fuel", new VehicleFuel());
        CommandModule.subcommands.put("benzine", new VehicleFuel());
        CommandModule.subcommands.put("setowner", new VehicleSetOwner());
        CommandModule.subcommands.put("public", new VehiclePublic());
        CommandModule.subcommands.put("private", new VehiclePrivate());
        CommandModule.subcommands.put("addmember", new VehicleAddMember());
        CommandModule.subcommands.put("addrider", new VehicleAddRider());
        CommandModule.subcommands.put("removemember", new VehicleRemoveMember());
        CommandModule.subcommands.put("removerider", new VehicleRemoveRider());
        CommandModule.subcommands.put("givecar", new VehicleGiveCar());
        CommandModule.subcommands.put("givevoucher", new VehicleGiveVoucher());
        CommandModule.subcommands.put("update", new VehicleUpdate());
        CommandModule.subcommands.put("delete", new VehicleDelete());
        CommandModule.subcommands.put("language", new VehicleLanguage());
        CommandModule.subcommands.put("version", new VehicleVersion());
    }

    @Override
    public boolean execute(CommandSender sender, Command cmd, String s, String[] args) {
        if (args.length == 0) {
            CommandModule.subcommands.get("help").onExecute(sender, cmd, s, args);
            return true;
        }

        if (CommandModule.subcommands.get(args[0].toLowerCase()) == null) {
            sendMessage(ConfigModule.messagesConfig.getMessage("cmdNotExists"));
            return true;
        }

        CommandModule.subcommands.get(args[0].toLowerCase()).onExecute(sender, cmd, s, args);
        return true;
    }
}
