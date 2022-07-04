package nl.mtvehicles.core.commands.vehiclesubs;

import nl.mtvehicles.core.infrastructure.enums.Message;
import nl.mtvehicles.core.infrastructure.models.MTVSubCommand;
import nl.mtvehicles.core.infrastructure.vehicle.VehicleUtils;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import org.bukkit.inventory.ItemStack;

/**
 * <b>/vehicle repair</b> - repair the held vehicle.
 */
public class VehicleRepair extends MTVSubCommand {
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
        sendMessage(Message.REPAIR_SUCCESSFUL);
        return true;
    }
}
