package nl.mtvehicles.core.commands.vehiclesubs;

import nl.mtvehicles.core.events.inventory.RestoreMenuOpenEvent;
import nl.mtvehicles.core.infrastructure.enums.Message;
import nl.mtvehicles.core.infrastructure.utils.MenuUtils;
import nl.mtvehicles.core.infrastructure.models.MTVSubCommand;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * <b>/vehicle restore (%player%)</b> - open a GUI menu of all vehicles in database (vehicleData.yml), their owner may be specified.
 */
public class VehicleRestore extends MTVSubCommand {
    public VehicleRestore() {
        this.setPlayerCommand(true);
    }

    @Override
    public boolean execute() {
        if (!checkPermission("mtvehicles.restore")) return true;

        UUID ownerUUID = null;

        if (arguments.length == 2) {
            // Also accept offline players (the menu is meant to restore vehicles of players who are not online).
            Player online = Bukkit.getPlayerExact(arguments[1]);
            OfflinePlayer argPlayer = online != null ? online : Bukkit.getOfflinePlayer(arguments[1]);
            if (!argPlayer.isOnline() && !argPlayer.hasPlayedBefore()) {
                sendMessage(Message.OFFLINE_PLAYER_NOT_FOUND);
                return true;
            }
            ownerUUID = argPlayer.getUniqueId();
        }

        RestoreMenuOpenEvent api = new RestoreMenuOpenEvent(player);
        api.call();
        if (api.isCancelled()) return true;

        sendMessage(Message.MENU_OPEN);

        // Set before opening the menu, so that the click listener always finds the values.
        MenuUtils.restoreUUID.put(player, ownerUUID);
        MenuUtils.restorePage.put(player, 1);
        MenuUtils.restoreCMD(player, 1, ownerUUID);

        return true;
    }
}
