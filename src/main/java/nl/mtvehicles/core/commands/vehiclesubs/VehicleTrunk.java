package nl.mtvehicles.core.commands.vehiclesubs;

import nl.mtvehicles.core.events.VehicleOpenTrunkEvent;
import nl.mtvehicles.core.infrastructure.models.MTVehicleSubCommand;
import nl.mtvehicles.core.infrastructure.models.VehicleUtils;
import org.bukkit.inventory.ItemStack;

/**
 * <b>/vehicle trunk</b> - open the trunk of the held vehicle.
 */
public class VehicleTrunk extends MTVehicleSubCommand {
    public VehicleTrunk() {
        this.setPlayerCommand(true);
    }

    @Override
    public boolean execute() {

        final ItemStack item = player.getInventory().getItemInMainHand();

        if (!isHoldingVehicle()) return true;

        String licensePlate = VehicleUtils.getLicensePlate(item);

        VehicleOpenTrunkEvent api = new VehicleOpenTrunkEvent();
        api.setPlayer(player);
        api.setLicensePlate(licensePlate);
        api.call();
        if (api.isCancelled()) return true;

        VehicleUtils.openTrunk(player, api.getLicensePlate());
        return true;
    }
}
