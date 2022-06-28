package nl.mtvehicles.core.commands.vehiclesubs;

import nl.mtvehicles.core.infrastructure.enums.Message;
import nl.mtvehicles.core.infrastructure.vehicle.VehicleData;
import nl.mtvehicles.core.infrastructure.models.MTVSubCommand;
import nl.mtvehicles.core.infrastructure.vehicle.Vehicle;
import nl.mtvehicles.core.infrastructure.vehicle.VehicleUtils;
import org.bukkit.inventory.ItemStack;

/**
 * <b>/vehicle refill</b> - refill the held vehicle.
 */
public class VehicleRefill extends MTVSubCommand {
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
