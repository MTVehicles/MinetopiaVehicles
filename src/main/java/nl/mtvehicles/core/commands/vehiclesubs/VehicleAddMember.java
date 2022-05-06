package nl.mtvehicles.core.commands.vehiclesubs;

import de.tr7zw.changeme.nbtapi.NBTItem;
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

public class VehicleAddMember extends MTVehicleSubCommand {
    public VehicleAddMember() {
        this.setPlayerCommand(true);
    }

    @Override
    public boolean execute(CommandSender sender, Command cmd, String s, String[] args) {
        if (!isHoldingVehicle()) return true;

        ItemStack item = player.getInventory().getItemInMainHand();
        NBTItem nbt = new NBTItem(item);

        if (args.length != 2) {
            player.sendMessage(ConfigModule.messagesConfig.getMessage(Message.USE_ADD_MEMBER));
            return true;
        }

        Player offlinePlayer = Bukkit.getPlayer(args[1]);
        String licensePlate = nbt.getString("mtvehicles.kenteken");

        if (offlinePlayer == null || !offlinePlayer.hasPlayedBefore()) {
            sendMessage(ConfigModule.messagesConfig.getMessage(Message.PLAYER_NOT_FOUND));
            return true;
        }

        Vehicle vehicle = VehicleUtils.getByLicensePlate(licensePlate);

        assert vehicle != null;
        List<String> members = vehicle.getMembers();
        members.add(offlinePlayer.getUniqueId().toString());
        vehicle.setMembers(members);
        vehicle.save();

        sendMessage(ConfigModule.messagesConfig.getMessage(Message.MEMBER_CHANGE));

        return true;
    }
}
