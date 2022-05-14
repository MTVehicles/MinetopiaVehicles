package nl.mtvehicles.core.commands.vehiclesubs;

import nl.mtvehicles.core.infrastructure.enums.Message;
import nl.mtvehicles.core.infrastructure.models.MTVehicleSubCommand;
import nl.mtvehicles.core.infrastructure.models.VehicleUtils;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import org.bukkit.inventory.ItemStack;

/**
 * <b>/vehicle repair</b> - repair the held vehicle.
 */
public class VehicleRepair extends MTVehicleSubCommand {
    public VehicleRepair() {
        this.setPlayerCommand(true);
    }

    @Override
    public boolean execute() {
        if (!checkPermission("mtvehicles.repair")) return true;

        final ItemStack item = player.getInventory().getItemInMainHand();

        if (!isHoldingVehicle()) return true;

        final String license = VehicleUtils.getLicensePlate(item);
        final int damage = ConfigModule.vehicleDataConfig.getDamage(license);
        final double maxHealth = VehicleUtils.getMaxHealthByDamage(damage);

        ConfigModule.vehicleDataConfig.setHealth(license, maxHealth);
        sendMessage(ConfigModule.messagesConfig.getMessage(Message.REPAIR_SUCCESSFUL));
        return true;
    }
}
