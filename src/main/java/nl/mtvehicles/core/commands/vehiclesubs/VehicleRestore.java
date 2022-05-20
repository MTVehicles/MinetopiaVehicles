package nl.mtvehicles.core.commands.vehiclesubs;

import nl.mtvehicles.core.events.inventory.RestoreMenuOpenEvent;
import nl.mtvehicles.core.infrastructure.enums.Message;
import nl.mtvehicles.core.infrastructure.helpers.MenuUtils;
import nl.mtvehicles.core.infrastructure.models.MTVehicleSubCommand;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

/**
 * <b>/vehicle restore (%player%)</b> - open a GUI menu of all vehicles in database (vehicleData.yml), their owner may be specified.
 */
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
            MenuUtils.restoreUUID.put(player, null);
            return true;
        }
        OfflinePlayer of = Bukkit.getPlayer(arguments[1]);

        if (of == null || !of.hasPlayedBefore()) {
            sendMessage(ConfigModule.messagesConfig.getMessage(Message.PLAYER_NOT_FOUND));
            return true;
        }

        RestoreMenuOpenEvent api = new RestoreMenuOpenEvent(player);
        api.call();
        if (api.isCancelled()) return true;

        MenuUtils.restoreCMD(player, 1, of.getUniqueId());
        MenuUtils.restoreUUID.put(player, of.getUniqueId());
        MenuUtils.restorePage.put(player, 1);

        return true;
    }
}
