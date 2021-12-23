package nl.mtvehicles.core.commands.vehiclesubs;

import nl.mtvehicles.core.infrastructure.models.MTVehicleSubCommand;
import nl.mtvehicles.core.Main;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class VehicleHelp extends MTVehicleSubCommand {
    public VehicleHelp() {
        this.setPlayerCommand(true);
    }

    @Override
    public boolean execute(CommandSender sender, Command cmd, String s, String[] args) {
        sendMessage(String.format("&2&lMinetopiaVehicles Commands: (%s)", Main.instance.getDescription().getVersion()));
        sendMessage("");
        sendMessage(String.format("&2/vehicle &ainfo &f- &2%s", ConfigModule.messagesConfig.getMessage("helpInfo")));
        sendMessage(String.format("&2/vehicle &apublic &f- &2%s", ConfigModule.messagesConfig.getMessage("helpPublic")));
        sendMessage(String.format("&2/vehicle &aprivate &f- &2%s", ConfigModule.messagesConfig.getMessage("helpPrivate")));
        sendMessage(String.format("&2/vehicle &aaddrider &f- &2%s", ConfigModule.messagesConfig.getMessage("helpAddRider")));
        sendMessage(String.format("&2/vehicle &aaddmember &f- &2%s", ConfigModule.messagesConfig.getMessage("helpAddMember")));
        sendMessage(String.format("&2/vehicle &aremoverider &f- &2%s", ConfigModule.messagesConfig.getMessage("helpRemoveRider")));
        sendMessage(String.format("&2/vehicle &aremovemember &f- &2%s", ConfigModule.messagesConfig.getMessage("helpRemoveMember")));
        if (sender.hasPermission("mtvehicles.admin")) {
            sendMessage("");
            sendMessage(String.format("&2/vehicle &alanguage &f- &2%s", ConfigModule.messagesConfig.getMessage("adminLanguage")));
            sendMessage(String.format("&2/vehicle &aversion &f- &2%s", ConfigModule.messagesConfig.getMessage("adminVersion")));
            sendMessage(String.format("&2/vehicle &aedit &f- &2%s", ConfigModule.messagesConfig.getMessage("adminEdit")));
            sendMessage(String.format("&2/vehicle &amenu &f- &2%s", ConfigModule.messagesConfig.getMessage("adminMenu")));
            sendMessage(String.format("&2/vehicle &afuel &f- &2%s", ConfigModule.messagesConfig.getMessage("adminBenzine")));
            sendMessage(String.format("&2/vehicle &arestore &f- &2%s", ConfigModule.messagesConfig.getMessage("adminRestore")));
            sendMessage(String.format("&2/vehicle &areload &f- &2%s", ConfigModule.messagesConfig.getMessage("adminReload")));
            sendMessage(String.format("&2/vehicle &agivevoucher &f- &2%s", ConfigModule.messagesConfig.getMessage("adminGivevoucher")));
            sendMessage(String.format("&2/vehicle &agivecar &f- &2%s", ConfigModule.messagesConfig.getMessage("adminGivecar")));
            sendMessage(String.format("&2/vehicle &asetowner &f- &2%s", ConfigModule.messagesConfig.getMessage("adminSetowner")));
            sendMessage(String.format("&2/vehicle &aupdate &f- &2%s", ConfigModule.messagesConfig.getMessage("adminUpdate")));
            sendMessage(String.format("&2/vehicle &adelete &f- &2%s", ConfigModule.messagesConfig.getMessage("adminDelete")));
        }
        sendMessage("");
        sendMessage("&7&oDownload it free on mtvehicles.nl by GamerJoep_.");
        return true;
    }
}
