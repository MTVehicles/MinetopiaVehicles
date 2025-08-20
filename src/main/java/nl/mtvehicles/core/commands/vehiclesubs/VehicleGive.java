package nl.mtvehicles.core.commands.vehiclesubs;

import nl.mtvehicles.core.commands.VehicleTabCompleterManager;
import nl.mtvehicles.core.infrastructure.enums.Message;
import nl.mtvehicles.core.infrastructure.models.MTVSubCommand;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import nl.mtvehicles.core.infrastructure.utils.ItemUtils;
import nl.mtvehicles.core.infrastructure.vehicle.VehicleUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

/**
 * <b>/vehicle give %player% %vehicleName% [--voucher:true]</b> - give a vehicle / a voucher to a player.<br>
 * Permission: mtvehicles.givecar / mtvehicles.givevoucher
 * @since 2.5.7
 */
public class VehicleGive extends MTVSubCommand {
    public VehicleGive() {
        this.setPlayerCommand(false);
    }

    @Override
    public boolean execute() {
        // If they don't have any necessary permission, don't let them run the command in the first place
        // Otherwise, permissions are checked based on the arguments (whether voucher is given)
        if (!sender.hasPermission("mtvehicles.givecar") && !sender.hasPermission("mtvehicles.givevoucher")) {
            ConfigModule.messagesConfig.sendMessage(sender, Message.NO_PERMISSION);
            return true;
        }

        if (arguments.length != 3 && arguments.length != 4) {
            sendMessage(Message.USE_NEW_VEHICLE_GIVE);
            return true;
        }

        Player argPlayer = Bukkit.getPlayer(arguments[1]);
        if (argPlayer == null) {
            sendMessage(Message.PLAYER_NOT_FOUND);
            return true;
        }

        HashMap<String, String> vehicleList = VehicleTabCompleterManager.getVehicleList();
        if (!vehicleList.containsKey(arguments[2])) {
            sender.sendMessage(ConfigModule.messagesConfig.getMessage(Message.GIVE_CAR_NOT_FOUND));
            return true;
        }

        String carUuid = vehicleList.get(arguments[2]);

        ItemStack itemToGive;
        boolean useVoucher;

        if (arguments.length < 4) useVoucher = false;
        else useVoucher = arguments[3].equals("--voucher:true");

        if (useVoucher) {
            if (!checkPermission("mtvehicles.givevoucher")) return true;

            if (VehicleUtils.getItem(carUuid) == null){
                sender.sendMessage(ConfigModule.messagesConfig.getMessage(Message.GIVE_CAR_NOT_FOUND));
                return true;
            }

            itemToGive = ItemUtils.createVoucher(carUuid);
        } else {
            if (!checkPermission("mtvehicles.givecar")) return true;

            ItemStack car = VehicleUtils.createAndGetItemByUUID(argPlayer, carUuid);

            if (car == null){
                sender.sendMessage(ConfigModule.messagesConfig.getMessage(Message.GIVE_CAR_NOT_FOUND));
                return true;
            }

            itemToGive = car;
        }

        HashMap<Integer, ItemStack> failedItems = argPlayer.getInventory().addItem(itemToGive);

        if (!failedItems.isEmpty()) {
            sender.sendMessage(ConfigModule.messagesConfig.getMessage(Message.NO_INVENTORY_SPACE));
            return true;
        }

        if (useVoucher)
            sender.sendMessage(ConfigModule.messagesConfig.getMessage(Message.GIVE_VOUCHER_SUCCESS).replace("%p%", argPlayer.getName()));
        else
            sender.sendMessage(ConfigModule.messagesConfig.getMessage(Message.GIVE_CAR_SUCCESS).replace("%p%", argPlayer.getName()));
        return true;
    }
}
