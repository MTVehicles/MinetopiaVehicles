package nl.mtvehicles.core.commands.vehiclesubs;

import nl.mtvehicles.core.events.VehicleAddMemberEvent;
import nl.mtvehicles.core.infrastructure.enums.Message;
import nl.mtvehicles.core.infrastructure.models.MTVSubCommand;
import nl.mtvehicles.core.infrastructure.vehicle.Vehicle;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * <b>/vehicle addmember %player%</b> - add a player who may sit in the vehicle the player is sitting in (if they are its owner) OR player's held vehicle.
 */
public class VehicleAddMember extends MTVSubCommand {
    public VehicleAddMember() {
        this.setPlayerCommand(true);
    }

    @Override
    public boolean execute() {

        Vehicle vehicle = getVehicle();
        if (vehicle == null) return true;

        if (arguments.length != 2) {
            sendMessage(Message.USE_ADD_MEMBER);
            return true;
        }

        Player argPlayer = Bukkit.getPlayer(arguments[1]);

        VehicleAddMemberEvent api = new VehicleAddMemberEvent();
        api.setPlayer(player);
        api.setAdded(argPlayer);
        api.setLicensePlate(vehicle.getLicensePlate());
        api.call();

        if (api.isCancelled()) return true;
        vehicle = api.getVehicle();
        argPlayer = api.getAdded();

        if (vehicle == null){
            sendMessage(Message.VEHICLE_NOT_FOUND);
            return true;
        }

        if (argPlayer == null) {
            sendMessage(Message.PLAYER_NOT_FOUND);
            return true;
        }

        List<String> members = vehicle.getMembers();
        String playerUUID = argPlayer.getUniqueId().toString();

        if (members.contains(playerUUID)){
            sendMessage(Message.ALREADY_MEMBER);
            return true;
        }

        members.add(playerUUID);
        vehicle.setMembers(members);
        vehicle.save();

        sendMessage(Message.MEMBER_CHANGE);

        return true;
    }
}
