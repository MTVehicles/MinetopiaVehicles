package nl.mtvehicles.core.commands.vehiclesubs;

import nl.mtvehicles.core.infrastructure.enums.Message;
import nl.mtvehicles.core.infrastructure.helpers.MenuUtils;
import nl.mtvehicles.core.infrastructure.models.MTVehicleSubCommand;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

public class VehicleRestore extends MTVehicleSubCommand {
    public VehicleRestore() {
        this.setPlayerCommand(true);
    }

    @Override
    public boolean execute() {
        if (!checkPermission("mtvehicles.restore")) return true;

        sendMessage(ConfigModule.messagesConfig.getMessage(Message.MENU_OPEN));

        if (arguments.length != 2) {
            MenuUtils.restoreCMD(player, 1, null);
            MenuUtils.restoreUUID.put("uuid", null);
            return true;
        }
        OfflinePlayer of = Bukkit.getPlayer(arguments[1]);

        if (of == null || !of.hasPlayedBefore()) {
            sendMessage(ConfigModule.messagesConfig.getMessage(Message.PLAYER_NOT_FOUND));
            return true;
        }

        MenuUtils.restoreCMD(player, 1, of.getUniqueId());
        MenuUtils.restoreUUID.put("uuid", of.getUniqueId());
        MenuUtils.restoreId.put("pagina", 1);

        return true;
    }
}
