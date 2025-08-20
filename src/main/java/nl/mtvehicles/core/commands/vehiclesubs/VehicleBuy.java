package nl.mtvehicles.core.commands.vehiclesubs;

import nl.mtvehicles.core.commands.VehicleTabCompleterManager;
import nl.mtvehicles.core.infrastructure.enums.Message;
import nl.mtvehicles.core.infrastructure.enums.SoftDependency;
import nl.mtvehicles.core.infrastructure.models.MTVSubCommand;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import nl.mtvehicles.core.infrastructure.modules.DependencyModule;
import nl.mtvehicles.core.infrastructure.utils.ItemUtils;
import nl.mtvehicles.core.infrastructure.vehicle.VehicleUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

/**
 * <b>/vehicle buy %vehicleName% [--voucher:true]</b> - buy yourself a vehicle / a voucher.<br>
 * Permission: mtvehicles.buycar / mtvehicles.buyvoucher
 * @since 2.5.7
 */
public class VehicleBuy extends MTVSubCommand {
    public VehicleBuy() {
        this.setPlayerCommand(true);
    }

    @Override
    public boolean execute() {
        // If they don't have any necessary permission, don't let them run the command in the first place
        // Otherwise, permissions are checked based on the arguments (whether voucher is given)
        if (!sender.hasPermission("mtvehicles.buycar") && !sender.hasPermission("mtvehicles.buyvoucher")) {
            ConfigModule.messagesConfig.sendMessage(sender, Message.NO_PERMISSION);
            return true;
        }

        if (arguments.length != 2 && arguments.length != 3) {
            sendMessage(Message.USE_NEW_VEHICLE_BUY);
            return true;
        }

        if (!DependencyModule.isDependencyEnabled(SoftDependency.VAULT)) {
            sendMessage(Message.ECONOMY_NOT_SET_UP);
            return true;
        }

        if (!DependencyModule.vault.isEconomySetUp()) {
            sendMessage(Message.ECONOMY_NOT_SET_UP);
            return true;
        }

        if (player.getInventory().firstEmpty() == -1) { //this method returns -1 if no empty slots are found
            sender.sendMessage(ConfigModule.messagesConfig.getMessage(Message.NO_INVENTORY_SPACE));
            return true;
        }

        HashMap<String, String> vehicleList = VehicleTabCompleterManager.getVehicleList();
        if (!vehicleList.containsKey(arguments[1])) {
            sender.sendMessage(ConfigModule.messagesConfig.getMessage(Message.GIVE_CAR_NOT_FOUND));
            return true;
        }

        String carUuid = vehicleList.get(arguments[1]);
        final Double price = VehicleUtils.getPrice(carUuid);

        if (price == null){
            sender.sendMessage(ConfigModule.messagesConfig.getMessage(Message.GIVE_CAR_NOT_FOUND));
            return true;
        }

        boolean useVoucher;

        if (arguments.length < 3) useVoucher = false;
        else useVoucher = arguments[2].equals("--voucher:true");

        if (useVoucher) {
            if (!checkPermission("mtvehicles.buyvoucher")) return true;

            if (!DependencyModule.vault.withdrawMoneyPlayer(player, price)) return true;
            // This also sends a confirmation message - whether the purchase was successful or not.

            player.getInventory().addItem(
                    ItemUtils.createVoucher(carUuid)
            );
        } else {
            if (!checkPermission("mtvehicles.buycar")) return true;

            if (!DependencyModule.vault.withdrawMoneyPlayer(player, price)) return true;
            // This also sends a confirmation message - whether the purchase was successful or not.

            player.getInventory().addItem(
                    VehicleUtils.createAndGetItemByUUID(player, carUuid)
            );
        }

        return true;
    }
}
