package nl.mtvehicles.core.commands.vehiclesubs;

import nl.mtvehicles.core.infrastructure.dataconfig.DefaultConfig;
import nl.mtvehicles.core.infrastructure.enums.Message;
import nl.mtvehicles.core.infrastructure.models.MTVSubCommand;
import nl.mtvehicles.core.infrastructure.vehicle.Vehicle;
import nl.mtvehicles.core.infrastructure.vehicle.VehicleUtils;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

/**
 * <b>/vehicle setowner %player%</b> - set held vehicle's owner.
 */
public class VehicleSetOwner extends MTVSubCommand {
    public VehicleSetOwner() {
        this.setPlayerCommand(true);
    }

    @Override
    public boolean execute() {
        ItemStack item = player.getInventory().getItemInMainHand();

        boolean playerSetOwner = (boolean) ConfigModule.defaultConfig.get(DefaultConfig.Option.PUT_ONESELF_AS_OWNER);

        if (!playerSetOwner && !checkPermission("mtvehicles.setowner")) {
            return true;
        }

        if (!isHoldingVehicle()) return true;

        if (arguments.length != 2) {
            sendMessage(Message.USE_SET_OWNER);
            return true;
        }

        String licensePlate = VehicleUtils.getLicensePlate(item);

        if (!VehicleUtils.existsByLicensePlate(licensePlate)) {
            sendMessage(Message.VEHICLE_NOT_FOUND);
            return true;
        }

        Player argPlayer = Bukkit.getPlayer(arguments[1]);
        if (argPlayer == null) {
            sendMessage(Message.PLAYER_NOT_FOUND);
            return true;
        }

        Vehicle vehicle = VehicleUtils.getVehicle(licensePlate);
        assert vehicle != null;

        if ((playerSetOwner || !player.hasPermission("mtvehicles.setowner")) && !vehicle.isOwner(player)) {
            sendMessage(Message.NOT_YOUR_CAR);
            return true;
        }

        vehicle.setRiders(new ArrayList<>());
        vehicle.setMembers(new ArrayList<>());
        vehicle.setOwner(argPlayer.getUniqueId());
        vehicle.save();

        sendMessage(Message.MEMBER_CHANGE);

        return true;
    }
}
