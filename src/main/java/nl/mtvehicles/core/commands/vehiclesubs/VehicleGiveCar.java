package nl.mtvehicles.core.commands.vehiclesubs;

import nl.mtvehicles.core.infrastructure.enums.Message;
import nl.mtvehicles.core.infrastructure.models.MTVSubCommand;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import nl.mtvehicles.core.infrastructure.vehicle.VehicleUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * <b>/vehicle givecar %player% %uuid%</b> - add a vehicle to a player.
 * @deprecated Since 2.5.7, use {@link VehicleGive} instead.
 */
@Deprecated
public class VehicleGiveCar extends MTVSubCommand {
    public VehicleGiveCar() {
        this.setPlayerCommand(false);
    }

    @Override
    public boolean execute() {
        if (!checkPermission("mtvehicles.givecar")) return true;

        if (arguments.length != 3) {
            sendMessage(Message.USE_GIVE_CAR);
            return true;
        }

        Player argPlayer = Bukkit.getPlayer(arguments[1]);

        String carUuid = arguments[2].replace("-", "");

        if (argPlayer == null) {
            sendMessage(Message.PLAYER_NOT_FOUND);
            return true;
        }

        ItemStack car = VehicleUtils.createAndGetItemByUUID(argPlayer, carUuid);

        if (car == null){
            sender.sendMessage(ConfigModule.messagesConfig.getMessage(Message.GIVE_CAR_NOT_FOUND));
            return true;
        }
        argPlayer.getInventory().addItem(car);
        sender.sendMessage(ConfigModule.messagesConfig.getMessage(Message.GIVE_CAR_SUCCESS).replace("%p%", argPlayer.getName()));

        return true;
    }
}
