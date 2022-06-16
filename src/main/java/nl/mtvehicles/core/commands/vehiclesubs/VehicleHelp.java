package nl.mtvehicles.core.commands.vehiclesubs;

import nl.mtvehicles.core.Main;
import nl.mtvehicles.core.infrastructure.enums.Message;
import nl.mtvehicles.core.infrastructure.models.MTVSubCommand;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;

/**
 * <b>/vehicle help</b> - list of all MTV commands.
 */
public class VehicleHelp extends MTVSubCommand {
    public VehicleHelp() {
        this.setPlayerCommand(true);
    }

    @Override
    public boolean execute() {
        sendMessage(String.format("&2&lMinetopiaVehicles Commands: (%s)", Main.instance.getDescription().getVersion()));
        sendMessage("");
        sendMessage(String.format("&2/vehicle &ainfo &f- &2%s", desc(Message.HELP_INFO)));
        sendMessage(String.format("&2/vehicle &apublic &f- &2%s", desc(Message.HELP_PUBLIC)));
        sendMessage(String.format("&2/vehicle &aprivate &f- &2%s", desc(Message.HELP_PRIVATE)));
        sendMessage(String.format("&2/vehicle &aaddrider &f- &2%s", desc(Message.HELP_ADD_RIDER)));
        sendMessage(String.format("&2/vehicle &aaddmember &f- &2%s", desc(Message.HELP_ADD_MEMBER)));
        sendMessage(String.format("&2/vehicle &aremoverider &f- &2%s", desc(Message.HELP_REMOVE_RIDER)));
        sendMessage(String.format("&2/vehicle &aremovemember &f- &2%s", desc(Message.HELP_REMOVE_MEMBER)));
        if (sender.hasPermission("mtvehicles.admin")) {
            sendMessage("");
            sendMessage(String.format("&2/vehicle &alanguage &f- &2%s", desc(Message.ADMIN_LANGUAGE)));
            sendMessage(String.format("&2/vehicle &aversion &f- &2%s", desc(Message.ADMIN_VERSION)));
            sendMessage(String.format("&2/vehicle &arefill &f- &2%s", desc(Message.ADMIN_REFILL)));
            sendMessage(String.format("&2/vehicle &arepair &f- &2%s", desc(Message.ADMIN_REPAIR)));
            sendMessage(String.format("&2/vehicle &aedit &f- &2%s", desc(Message.ADMIN_EDIT)));
            sendMessage(String.format("&2/vehicle &amenu &f- &2%s", desc(Message.ADMIN_MENU)));
            sendMessage(String.format("&2/vehicle &afuel &f- &2%s", desc(Message.ADMIN_FUEL)));
            sendMessage(String.format("&2/vehicle &arestore &f- &2%s", desc(Message.ADMIN_RESTORE)));
            sendMessage(String.format("&2/vehicle &areload &f- &2%s", desc(Message.ADMIN_RELOAD)));
            sendMessage(String.format("&2/vehicle &agivevoucher &f- &2%s", desc(Message.ADMIN_GIVEVOUCHER)));
            sendMessage(String.format("&2/vehicle &agivecar &f- &2%s", desc(Message.ADMIN_GIVECAR)));
            sendMessage(String.format("&2/vehicle &asetowner &f- &2%s", desc(Message.ADMIN_SETOWNER)));
            sendMessage(String.format("&2/vehicle &aupdate &f- &2%s", desc(Message.ADMIN_UPDATE)));
            sendMessage(String.format("&2/vehicle &adelete &f- &2%s", desc(Message.ADMIN_DELETE)));
        }
        sendMessage("");
        sendMessage("&7&oDownload it for free at mtvehicles.nl, by GamerJoep_.");
        return true;
    }

    private String desc(Message message){
        return ConfigModule.messagesConfig.getMessage(message);
    }
}
