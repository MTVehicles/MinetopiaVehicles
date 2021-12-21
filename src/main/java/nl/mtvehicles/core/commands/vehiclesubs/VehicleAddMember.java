package nl.mtvehicles.core.commands.vehiclesubs;

import nl.mtvehicles.core.infrastructure.helpers.NBTUtils;
import nl.mtvehicles.core.infrastructure.helpers.TextUtils;
import nl.mtvehicles.core.infrastructure.models.MTVehicleSubCommand;
import nl.mtvehicles.core.infrastructure.models.Vehicle;
import nl.mtvehicles.core.Main;
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
        Player player = (Player) sender;
        ItemStack item = player.getInventory().getItemInMainHand();
        if (!item.hasItemMeta() || !NBTUtils.contains(item, "mtvehicles.kenteken")) {
            sendMessage(TextUtils.colorize(ConfigModule.messagesConfig.getMessage("noVehicleInHand")));
            return true;
        }

        if (args.length != 2) {
            player.sendMessage(ConfigModule.messagesConfig.getMessage("useAddMember"));
            return true;
        }

        Player offlinePlayer = Bukkit.getPlayer(args[1]);
        String licensePlate = NBTUtils.getString(item, "mtvehicles.kenteken");

        if (offlinePlayer == null || !offlinePlayer.hasPlayedBefore()) {
            sendMessage(ConfigModule.messagesConfig.getMessage("playerNotFound"));
            return true;
        }

        Vehicle vehicle = Vehicle.getByPlate(licensePlate);

        assert vehicle != null;
        List<String> members = vehicle.getMembers();
        members.add(offlinePlayer.getUniqueId().toString());
        vehicle.setMembers(members);
        vehicle.save();

        sendMessage(ConfigModule.messagesConfig.getMessage("memberChange"));

        return true;
    }
}