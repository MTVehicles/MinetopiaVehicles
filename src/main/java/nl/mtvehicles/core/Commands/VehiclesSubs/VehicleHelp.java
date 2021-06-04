package nl.mtvehicles.core.Commands.VehiclesSubs;

import nl.mtvehicles.core.Infrastructure.Models.MTVehicleSubCommand;
import nl.mtvehicles.core.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginDescriptionFile;

public class VehicleHelp extends MTVehicleSubCommand {
    public VehicleHelp() {
        this.setPlayerCommand(true);
    }

    @Override
    public boolean execute(CommandSender sender, Command cmd, String s, String[] args) {
        sendMessage(String.format("&2&lMinetopiaVehicles Commands: (%s)", Main.instance.getDescription().getVersion()));
        sendMessage("");
        sendMessage(String.format("&2/vehicle &ainfo &f- &2%s", Main.messagesConfig.getMessage("helpInfo")));
        //sendMessage("&2/vehicle &ahelp &f- &2"+Main.messagesConfig.getMessage("helpHelp"));
        sendMessage(String.format("&2/vehicle &aaddrider &f- &2%s", Main.messagesConfig.getMessage("helpAddRider")));
        sendMessage(String.format("&2/vehicle &aaddmember &f- &2%s", Main.messagesConfig.getMessage("helpAddMember")));
        sendMessage(String.format("&2/vehicle &aremoverider &f- &2%s", Main.messagesConfig.getMessage("helpRemoveRider")));
        sendMessage(String.format("&2/vehicle &aremovemember &f- &2%s", Main.messagesConfig.getMessage("helpRemoveMember")));
        if (sender.hasPermission("mtvehicles.admin")) {
            sendMessage("");
            sendMessage(String.format("&2/vehicle &aedit &f- &2%s", Main.messagesConfig.getMessage("adminEdit")));
            sendMessage(String.format("&2/vehicle &amenu &f- &2%s", Main.messagesConfig.getMessage("adminMenu")));
            sendMessage(String.format("&2/vehicle &afuel &f- &2%s", Main.messagesConfig.getMessage("adminBenzine")));
            sendMessage(String.format("&2/vehicle &arestore &f- &2%s", Main.messagesConfig.getMessage("adminRestore")));
            sendMessage(String.format("&2/vehicle &areload &f- &2%s", Main.messagesConfig.getMessage("adminReload")));
            sendMessage(String.format("&2/vehicle &agivevoucher &f- &2%s", Main.messagesConfig.getMessage("adminGivevoucher")));
            sendMessage(String.format("&2/vehicle &agivecar &f- &2%s", Main.messagesConfig.getMessage("adminGivecar")));
            sendMessage(String.format("&2/vehicle &asetowner &f- &2%s", Main.messagesConfig.getMessage("adminSetowner")));
            sendMessage(String.format("&2/vehicle &aupdate &f- &2%s", Main.messagesConfig.getMessage("adminUpdate")));
            sendMessage(String.format("&2/vehicle &adelete &f- &2%s", Main.messagesConfig.getMessage("adminDelete")));
        }
        sendMessage("");
        sendMessage("&7&oDownload it free on mtvehicles.nl by GamerJoep_.");
        return true;
    }
}
