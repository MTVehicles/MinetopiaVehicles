package nl.mtvehicles.core.commands.vehiclesubs;

import nl.mtvehicles.core.infrastructure.enums.Message;
import nl.mtvehicles.core.infrastructure.utils.LanguageUtils;
import nl.mtvehicles.core.infrastructure.models.MTVSubCommand;

/**
 * <b>/vehicle language</b> - set the plugin's language (in a GUI).
 */
public class VehicleLanguage extends MTVSubCommand {
    public VehicleLanguage() {
        this.setPlayerCommand(true);
    }

    @Override
    public boolean execute() {
        if (sender.hasPermission("mtvehicles.language") || sender.hasPermission("mtvehicles.admin")) LanguageUtils.openLanguageGUI(player);
        else sendMessage(Message.NO_PERMISSION);

        return true;
    }
}
