package nl.mtvehicles.core.commands.vehiclesubs;

import nl.mtvehicles.core.infrastructure.enums.Message;
import nl.mtvehicles.core.infrastructure.models.MTVehicleSubCommand;
import nl.mtvehicles.core.infrastructure.models.Vehicle;
import nl.mtvehicles.core.infrastructure.models.VehicleUtils;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class VehicleRemoveMember extends MTVehicleSubCommand {
    public VehicleRemoveMember() {
        this.setPlayerCommand(true);
    }

    @Override
    public boolean execute(CommandSender sender, Command cmd, String s, String[] args) {
        ItemStack item = player.getInventory().getItemInMainHand();

        if (!isHoldingVehicle()) return true;

        if (args.length != 2) {
            player.sendMessage(ConfigModule.messagesConfig.getMessage(Message.USE_REMOVE_MEMBER));
            return true;
        }

        String ken = VehicleUtils.getLicensePlate(item);
        Player of = Bukkit.getPlayer(args[1]);

        Vehicle vehicle = VehicleUtils.getByLicensePlate(ken);

        if (of == null || !of.hasPlayedBefore()) {
            player.sendMessage(ConfigModule.messagesConfig.getMessage(Message.PLAYER_NOT_FOUND));
            return true;
        }

        assert vehicle != null;
        List<String> members = vehicle.getMembers();
        members.remove(of.getUniqueId().toString());
        vehicle.setMembers(members);
        vehicle.save();

        player.sendMessage(ConfigModule.messagesConfig.getMessage(Message.MEMBER_CHANGE));

        return true;
    }
}
