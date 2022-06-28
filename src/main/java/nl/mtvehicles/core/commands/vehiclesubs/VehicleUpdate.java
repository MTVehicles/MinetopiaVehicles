package nl.mtvehicles.core.commands.vehiclesubs;

import nl.mtvehicles.core.infrastructure.dataconfig.DefaultConfig;
import nl.mtvehicles.core.infrastructure.enums.Message;
import nl.mtvehicles.core.infrastructure.utils.PluginUpdater;
import nl.mtvehicles.core.infrastructure.models.MTVSubCommand;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;

/**
 * <b>/vehicle update</b> - update the plugin if a newer version is available.
 */
public class VehicleUpdate extends MTVSubCommand {
    @Override
    public boolean execute() {
        if (!checkPermission("mtvehicles.update")) return true;

        if (!(boolean) ConfigModule.defaultConfig.get(DefaultConfig.Option.AUTO_UPDATE)) {
            sendMessage(Message.UPDATE_DISABLED);
            return false;
        }

        PluginUpdater.updatePlugin(sender);
        return true;
    }
}
