package nl.mtvehicles.core.commands.vehiclesubs;

import nl.mtvehicles.core.infrastructure.enums.Message;
import nl.mtvehicles.core.infrastructure.helpers.VehicleData;
import nl.mtvehicles.core.infrastructure.models.MTVehicleSubCommand;
import nl.mtvehicles.core.infrastructure.models.Vehicle;
import nl.mtvehicles.core.infrastructure.models.VehicleUtils;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import org.bukkit.inventory.ItemStack;

/**
 * <b>/vehicle refill</b> - refill the held vehicle.
 */
public class VehicleRefill extends MTVehicleSubCommand {
    public VehicleRefill() {
        this.setPlayerCommand(true);
    }

    @Override
    public boolean execute() {
        if (!checkPermission("mtvehicles.refill")) return true;

        final ItemStack item = player.getInventory().getItemInMainHand();

        if (!isHoldingVehicle()) return true;

        final String licensePlate = VehicleUtils.getLicensePlate(item);
        Vehicle vehicle = VehicleUtils.getVehicle(licensePlate);
        vehicle.setFuel(100.0);
        vehicle.save();

        if (VehicleData.fallDamage.get(licensePlate) != null) VehicleData.fallDamage.remove(licensePlate);

        sendMessage(Message.REFILL_SUCCESSFUL);
        return true;
    }
}
