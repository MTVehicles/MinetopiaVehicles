package nl.mtvehicles.core.commands.vehiclesubs;

import nl.mtvehicles.core.infrastructure.enums.Message;
import nl.mtvehicles.core.infrastructure.helpers.ItemUtils;
import nl.mtvehicles.core.infrastructure.models.MTVehicleSubCommand;
import nl.mtvehicles.core.infrastructure.models.VehicleUtils;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * <b>/vehicle givevoucher %player% %uuid%</b> - add a vehicle voucher to a player.
 */
public class VehicleGiveVoucher extends MTVehicleSubCommand {
    public VehicleGiveVoucher() {
        this.setPlayerCommand(false);
    }

    @Override
    public boolean execute() {
        if (!checkPermission("mtvehicles.givevoucher")) return true;

        if (arguments.length != 3) {
            sendMessage(ConfigModule.messagesConfig.getMessage(Message.USE_GIVE_VOUCHER));
            return true;
        }

        Player playerVoucherGetter = Bukkit.getPlayer(arguments[1]);

        String carUuid = arguments[2];

        if (playerVoucherGetter == null || !playerVoucherGetter.hasPlayedBefore()) {
            sendMessage(ConfigModule.messagesConfig.getMessage(Message.PLAYER_NOT_FOUND));
            return true;
        }

        ItemStack car = VehicleUtils.getItemByUUID(playerVoucherGetter, carUuid);

        if (car == null){
            sender.sendMessage(ConfigModule.messagesConfig.getMessage(Message.GIVE_CAR_NOT_FOUND));
            return true;
        }

        ItemStack voucher = ItemUtils.createVoucher(carUuid);
        playerVoucherGetter.getInventory().addItem(voucher);
        sender.sendMessage(ConfigModule.messagesConfig.getMessage(Message.GIVE_VOUCHER_SUCCESS).replace("%p%", playerVoucherGetter.getName()));
        return true;
    }
}
