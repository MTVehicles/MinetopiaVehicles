package nl.mtvehicles.core.commands.vehiclesubs;

import nl.mtvehicles.core.infrastructure.enums.Message;
import nl.mtvehicles.core.infrastructure.models.MTVehicleSubCommand;
import nl.mtvehicles.core.infrastructure.models.Vehicle;
import nl.mtvehicles.core.infrastructure.models.VehicleUtils;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import org.bukkit.inventory.ItemStack;

/**
 * <b>/vehicle public</b> - set the vehicle as public (anyone can enter it).
 */
public class VehiclePublic extends MTVehicleSubCommand {
    public VehiclePublic() {
        this.setPlayerCommand(true);
    }

    @Override
    public boolean execute() {
        ItemStack item = player.getInventory().getItemInMainHand();

        if (!isHoldingVehicle()) return true;

        String licensePlate = VehicleUtils.getLicensePlate(item);

        Vehicle vehicle = VehicleUtils.getVehicle(licensePlate);

        assert vehicle != null;
        vehicle.setOpen(true);
        vehicle.save();

        sendMessage(ConfigModule.messagesConfig.getMessage(Message.ACTION_SUCCESSFUL));

        return true;
    }
}
