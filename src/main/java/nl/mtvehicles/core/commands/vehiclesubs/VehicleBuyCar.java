package nl.mtvehicles.core.commands.vehiclesubs;

import nl.mtvehicles.core.infrastructure.enums.Message;
import nl.mtvehicles.core.infrastructure.enums.SoftDependency;
import nl.mtvehicles.core.infrastructure.models.MTVSubCommand;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import nl.mtvehicles.core.infrastructure.modules.DependencyModule;
import nl.mtvehicles.core.infrastructure.vehicle.VehicleUtils;
import org.bukkit.inventory.ItemStack;

/**
 * <b>/vehicle buycar %uuid%</b> - buy a vehicle.
 * @deprecated Since 2.5.7, use {@link VehicleBuy} instead.
 */
@Deprecated
public class VehicleBuyCar extends MTVSubCommand {
    public VehicleBuyCar() {
        this.setPlayerCommand(true);
    }

    @Override
    public boolean execute() {

        if(!player.hasPermission("mtvehicles.buycar")){
            sendMessage(Message.NO_PERMISSION);
            return true;
        }

        if (arguments.length != 2) {
            sendMessage(Message.USE_BUY_CAR);
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
            ItemStack car = VehicleUtils.createAndGetItemByUUID(player, carUuid);
            player.getInventory().addItem(car);
        }

        return true;
    }
}
