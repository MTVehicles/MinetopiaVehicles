package nl.mtvehicles.core.commands.vehiclesubs;

import nl.mtvehicles.core.infrastructure.enums.Message;
import nl.mtvehicles.core.infrastructure.models.MTVSubCommand;
import nl.mtvehicles.core.infrastructure.vehicle.VehicleUtils;

/**
 * <b>/vehicle despawn %license-plate%</b> - Purge all vehicles with given license plate IF PLACED, FROM ALL WORLDS.
 * Does NOT remove the vehicle from the dabatase (vehicleData.yml) - use <code>/vehicle delete</code> for that.
 * @see VehicleDelete
 */
public class VehicleDespawn extends MTVSubCommand {

    @Override
    public boolean execute() {

        if (!checkPermission("mtvehicles.despawn")) return true;

        if (arguments.length != 2) {
            sendMessage(Message.USE_VEHICLE_DESPAWN);
            return true;
        }

        String licensePlate = arguments[1];
        int despawnVehicles = VehicleUtils.despawnVehicle(licensePlate);

        if (despawnVehicles == 0) {
            sendMessage(Message.NO_VEHICLE_TO_DESPAWN);
        } else {
            sendMessage(Message.VEHICLE_DESPAWNED);
        }
        return true;
    }
}
