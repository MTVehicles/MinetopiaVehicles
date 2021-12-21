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

public class VehicleRemoveMember extends MTVehicleSubCommand {
    public VehicleRemoveMember() {
        this.setPlayerCommand(true);
    }

    @Override
    public boolean execute(CommandSender sender, Command cmd, String s, String[] args) {
        Player p = (Player) sender;

        ItemStack item = p.getInventory().getItemInMainHand();

        if (!item.hasItemMeta() || !NBTUtils.contains(item, "mtvehicles.kenteken")) {
            sendMessage(TextUtils.colorize(ConfigModule.messagesConfig.getMessage("noVehicleInHand")));
            return true;
        }

        if (args.length != 2) {
            p.sendMessage(ConfigModule.messagesConfig.getMessage("useRemoveMember"));
            return true;
        }

        String ken = NBTUtils.getString(item, "mtvehicles.kenteken");
        Player of = Bukkit.getPlayer(args[1]);

        Vehicle vehicle = Vehicle.getByPlate(ken);

        if (of == null || !of.hasPlayedBefore()) {
            p.sendMessage(ConfigModule.messagesConfig.getMessage("playerNotFound"));
            return true;
        }

        assert vehicle != null;
        List<String> members = vehicle.getMembers();
        members.remove(of.getUniqueId().toString());
        vehicle.setMembers(members);
        vehicle.save();

        p.sendMessage(ConfigModule.messagesConfig.getMessage("memberChange"));

        return true;
    }
}
