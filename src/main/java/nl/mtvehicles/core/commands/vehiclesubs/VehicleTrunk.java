package nl.mtvehicles.core.commands.vehiclesubs;

import nl.mtvehicles.core.events.VehicleOpenTrunkEvent;
import nl.mtvehicles.core.infrastructure.models.MTVSubCommand;
import nl.mtvehicles.core.infrastructure.vehicle.Vehicle;
import nl.mtvehicles.core.infrastructure.vehicle.VehicleUtils;

/**
 * <b>/vehicle trunk</b> - open the trunk of the vehicle the player is sitting in (if they are its owner) OR player's held vehicle.
 */
public class VehicleTrunk extends MTVSubCommand {
    public VehicleTrunk() {
        this.setPlayerCommand(true);
    }

    @Override
    public boolean execute() {

        Vehicle vehicle = getVehicle();
        if (vehicle == null) return true;

        VehicleOpenTrunkEvent api = new VehicleOpenTrunkEvent();
        api.setPlayer(player);
        api.setLicensePlate(vehicle.getLicensePlate());
        api.call();
        if (api.isCancelled()) return true;

        VehicleUtils.openTrunk(player, api.getLicensePlate());
        return true;
    }
}
