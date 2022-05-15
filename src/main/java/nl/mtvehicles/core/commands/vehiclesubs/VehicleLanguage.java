package nl.mtvehicles.core.commands.vehiclesubs;

import nl.mtvehicles.core.infrastructure.enums.Message;
import nl.mtvehicles.core.infrastructure.helpers.LanguageUtils;
import nl.mtvehicles.core.infrastructure.models.MTVehicleSubCommand;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;

/**
 * <b>/vehicle language</b> - set the plugin's language (in a GUI).
 */
public class VehicleLanguage extends MTVehicleSubCommand {
    public VehicleLanguage() {
        this.setPlayerCommand(true);
    }

    @Override
    public boolean execute() {
        if (sender.hasPermission("mtvehicles.language") || sender.hasPermission("mtvehicles.admin")) LanguageUtils.openLanguageGUI(player);
        else ConfigModule.messagesConfig.sendMessage(sender, Message.NO_PERMISSION);

        return true;
    }
}
