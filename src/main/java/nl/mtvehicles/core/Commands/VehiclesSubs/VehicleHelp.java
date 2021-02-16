package nl.mtvehicles.core.Commands.VehiclesSubs;

import nl.mtvehicles.core.Infrastructure.Models.MTVehicleSubCommand;
import nl.mtvehicles.core.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class VehicleHelp extends MTVehicleSubCommand {
    @Override
    public boolean execute(CommandSender sender, Command cmd, String s, String[] args) {
        if (!isPlayer) {
            sendMessage(Main.messagesConfig.getMessage("notForConsole"));
            return false;
        }

        sendMessage("&2&lMinetopiaVehicles Commands:");
        sendMessage("");
        sendMessage("&2/vehicle &ainfo &f- &2"+Main.messagesConfig.getMessage("helpInfo"));
        //sendMessage("&2/vehicle &ahelp &f- &2"+Main.messagesConfig.getMessage("helpHelp"));
        sendMessage("&2/vehicle &aaddrider &f- &2"+Main.messagesConfig.getMessage("helpAddRider"));
        sendMessage("&2/vehicle &aaddmember &f- &2"+Main.messagesConfig.getMessage("helpAddMember"));
        sendMessage("&2/vehicle &aremoverider &f- &2"+Main.messagesConfig.getMessage("helpRemoveRider"));
        sendMessage("&2/vehicle &aremovemember &f- &2"+Main.messagesConfig.getMessage("helpRemoveMember"));
        if (sender.hasPermission("mtvehicles.admin")) {
            sendMessage("");
            sendMessage("&2/vehicle &aedit &f- &2" + Main.messagesConfig.getMessage("adminEdit"));
            sendMessage("&2/vehicle &amenu &f- &2" + Main.messagesConfig.getMessage("adminMenu"));
            sendMessage("&2/vehicle &abenzine &f- &2" + Main.messagesConfig.getMessage("adminBenzine"));
            sendMessage("&2/vehicle &arestore &f- &2" + Main.messagesConfig.getMessage("adminRestore"));
            sendMessage("&2/vehicle &areload &f- &2" + Main.messagesConfig.getMessage("adminReload"));
            sendMessage("&2/vehicle &agivevoucher &f- &2" + Main.messagesConfig.getMessage("adminGivevoucher"));
            sendMessage("&2/vehicle &agivecar &f- &2" + Main.messagesConfig.getMessage("adminGivecar"));
            sendMessage("&2/vehicle &asetowner &f- &2" + Main.messagesConfig.getMessage("adminSetowner"));
            sendMessage("&2/vehicle &aupdate &f- &2" + Main.messagesConfig.getMessage("adminUpdate"));
            sendMessage("&2/vehicle &adelete &f- &2" + Main.messagesConfig.getMessage("adminDelete"));
        }
        sendMessage("");
        sendMessage("&7&omtvehicles.nl by GamerJoep_ -> Joep#0001 free to use.");
        return true;
    }
}
