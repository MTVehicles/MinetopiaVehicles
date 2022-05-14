package nl.mtvehicles.core.commands.vehiclesubs;

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
 * <b>/vehicle removerider %player%</b> - remove a player who may steer the held vehicle.
 */
public class VehicleRemoveRider extends MTVehicleSubCommand {
    public VehicleRemoveRider() {
        this.setPlayerCommand(true);
    }

    @Override
    public boolean execute() {
        ItemStack item = player.getInventory().getItemInMainHand();

        if (!isHoldingVehicle()) return true;

        if (arguments.length != 2) {
            player.sendMessage(ConfigModule.messagesConfig.getMessage(Message.USE_REMOVE_RIDER));
            return true;
        }

        String ken = VehicleUtils.getLicensePlate(item);
        Player of = Bukkit.getPlayer(arguments[1]);
        Vehicle vehicle = VehicleUtils.getByLicensePlate(ken);

        if (of == null || !of.hasPlayedBefore()) {
            player.sendMessage(ConfigModule.messagesConfig.getMessage(Message.PLAYER_NOT_FOUND));
            return true;
        }

        assert vehicle != null;
        List<String> riders = vehicle.getRiders();
        riders.remove(of.getUniqueId().toString());
        vehicle.setRiders(riders);
        vehicle.save();
        player.sendMessage(ConfigModule.messagesConfig.getMessage(Message.MEMBER_CHANGE));

        return true;
    }
}
