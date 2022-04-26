package nl.mtvehicles.core.commands.vehiclesubs;

import nl.mtvehicles.core.infrastructure.dataconfig.DefaultConfig;
import nl.mtvehicles.core.infrastructure.enums.Message;
import nl.mtvehicles.core.infrastructure.helpers.PluginUpdater;
import nl.mtvehicles.core.infrastructure.models.MTVehicleSubCommand;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class VehicleUpdate extends MTVehicleSubCommand {
    @Override
    public boolean execute(CommandSender sender, Command cmd, String s, String[] args) {
        if (!checkPermission("mtvehicles.update")) return true;

        if (!(boolean) ConfigModule.defaultConfig.get(DefaultConfig.Option.AUTO_UPDATE)) {
            sendMessage(ConfigModule.messagesConfig.getMessage(Message.UPDATE_DISABLED));
            return false;
        }

        PluginUpdater.updatePlugin(commandSender);
        return true;
    }
}
