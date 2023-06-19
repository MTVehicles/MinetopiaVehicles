package nl.mtvehicles.core.commands.vehiclesubs;

import nl.mtvehicles.core.infrastructure.enums.Message;
import nl.mtvehicles.core.infrastructure.models.MTVSubCommand;
import nl.mtvehicles.core.infrastructure.vehicle.Vehicle;

/**
 * <b>/vehicle private</b> - set the vehicle as private (only the owner can enter it).
 */
public class VehiclePrivate extends MTVSubCommand {
    public VehiclePrivate() {
        this.setPlayerCommand(true);
    }

    @Override
    public boolean execute() {

        Vehicle vehicle = getVehicle();
        if (vehicle == null) return true;

        vehicle.setOpen(false);
        vehicle.save();

        sendMessage(Message.ACTION_SUCCESSFUL);
        return true;
    }
}
