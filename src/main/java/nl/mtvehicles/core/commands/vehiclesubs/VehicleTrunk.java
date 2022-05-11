package nl.mtvehicles.core.commands.vehiclesubs;

import nl.mtvehicles.core.infrastructure.models.MTVehicleSubCommand;
import nl.mtvehicles.core.infrastructure.models.VehicleUtils;
import org.bukkit.inventory.ItemStack;

public class VehicleTrunk extends MTVehicleSubCommand {
    public VehicleTrunk() {
        this.setPlayerCommand(true);
    }

    @Override
    public boolean execute() {

        final ItemStack item = player.getInventory().getItemInMainHand();

        if (!isHoldingVehicle()) return true;

        final String licensePlate = VehicleUtils.getLicensePlate(item);
        VehicleUtils.openTrunk(player, licensePlate);
        return true;
    }
}
