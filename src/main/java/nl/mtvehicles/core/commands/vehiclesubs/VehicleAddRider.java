package nl.mtvehicles.core.commands.vehiclesubs;

import de.tr7zw.changeme.nbtapi.NBTItem;
import nl.mtvehicles.core.infrastructure.enums.Message;
import nl.mtvehicles.core.infrastructure.models.MTVehicleSubCommand;
import nl.mtvehicles.core.infrastructure.models.Vehicle;
import nl.mtvehicles.core.infrastructure.models.VehicleUtils;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * <b>/vehicle addrider %player%</b> - add a player who may steer the vehicle the player is sitting in (if they are its owner) OR player's held vehicle.
 */
public class VehicleAddRider extends MTVehicleSubCommand {
    public VehicleAddRider() {
        this.setPlayerCommand(true);
    }

    @Override
    public boolean execute() {

        Vehicle vehicle = getVehicle();
        if (vehicle == null) return true;

        if (arguments.length != 2) {
            sendMessage(Message.USE_ADD_RIDER);
            return true;
        }

        Player argPlayer = Bukkit.getPlayer(arguments[1]);

        if (argPlayer == null) {
            sendMessage(Message.PLAYER_NOT_FOUND);
            return true;
        }

        List<String> riders = vehicle.getRiders();
        String playerUUID = argPlayer.getUniqueId().toString();

        if (riders.contains(playerUUID)){
            sendMessage(Message.ALREADY_RIDER);
            return true;
        }

        riders.add(argPlayer.getUniqueId().toString());
        vehicle.setRiders(riders);
        vehicle.save();

        sendMessage(Message.MEMBER_CHANGE);

        return true;
    }
}
