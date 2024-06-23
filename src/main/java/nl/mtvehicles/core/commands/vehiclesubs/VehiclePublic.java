package nl.mtvehicles.core.commands.vehiclesubs;

import nl.mtvehicles.core.infrastructure.enums.Message;
import nl.mtvehicles.core.infrastructure.models.MTVSubCommand;
import nl.mtvehicles.core.infrastructure.vehicle.Vehicle;

/**
 * <b>/vehicle public</b> - set the vehicle as public (anyone can enter it).
 */
public class VehiclePublic extends MTVSubCommand {
    public VehiclePublic() {
        this.setPlayerCommand(true);
    }

    @Override
    public boolean execute() {

        if(!player.isInsideVehicle()){
            Vehicle vehicle = getVehicle();
            if (vehicle == null) return true;

            vehicle.setPublic(true);
            vehicle.save();

            sendMessage(Message.ACTION_SUCCESSFUL);
            return true;
        }
        return false;
    }
}
