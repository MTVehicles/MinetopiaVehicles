package nl.mtvehicles.core.commands.vehiclesubs;

import nl.mtvehicles.core.infrastructure.enums.Message;
import nl.mtvehicles.core.infrastructure.enums.SoftDependency;
import nl.mtvehicles.core.infrastructure.models.MTVSubCommand;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import nl.mtvehicles.core.infrastructure.modules.DependencyModule;
import nl.mtvehicles.core.infrastructure.utils.ItemUtils;
import nl.mtvehicles.core.infrastructure.vehicle.VehicleUtils;
import org.bukkit.inventory.ItemStack;

/**
 * <b>/vehicle buyvoucher %uuid%</b> - buy a vehicle voucher.
 */
public class VehicleBuyVoucher extends MTVSubCommand {
    public VehicleBuyVoucher() {
        this.setPlayerCommand(true);
    }

    @Override
    public boolean execute() {

        if (arguments.length != 2) {
            sendMessage(Message.USE_BUY_VOUCHER);
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

        String carUuid = arguments[1];
        final Double price = VehicleUtils.getPrice(carUuid);

        if (price == null){
            sender.sendMessage(ConfigModule.messagesConfig.getMessage(Message.GIVE_CAR_NOT_FOUND));
            return true;
        }

        if (DependencyModule.vault.withdrawMoneyPlayer(player, price)){
            VehicleUtils.getItemByUUID(player, carUuid); //Necessary for the vehicle to be created
            ItemStack voucher = ItemUtils.createVoucher(carUuid);
            player.getInventory().addItem(voucher);
        }

        return true;
    }
}
