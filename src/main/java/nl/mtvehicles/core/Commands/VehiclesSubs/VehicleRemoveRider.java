package nl.mtvehicles.core.Commands.VehiclesSubs;

import nl.mtvehicles.core.Infrastructure.Helpers.NBTUtils;
import nl.mtvehicles.core.Infrastructure.Helpers.TextUtils;
import nl.mtvehicles.core.Infrastructure.Models.MTVehicleSubCommand;
import nl.mtvehicles.core.Infrastructure.Models.Vehicle;
import nl.mtvehicles.core.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class VehicleRemoveRider extends MTVehicleSubCommand {
    public VehicleRemoveRider() {
        this.setPlayerCommand(true);
    }

    @Override
    public boolean execute(CommandSender sender, Command cmd, String s, String[] args) {
        ItemStack item = player.getInventory().getItemInMainHand();

        if (!item.hasItemMeta() || !(NBTUtils.contains(item, "mtvehicles.kenteken"))) {
            sendMessage(TextUtils.colorize(Main.messagesConfig.getMessage("noVehicleInHand")));
            return true;
        }

        if (args.length != 2) {
            player.sendMessage(Main.messagesConfig.getMessage("useRemoveRider"));
            return true;
        }

        String ken = NBTUtils.getString(item, "mtvehicles.kenteken");
        Player of = Bukkit.getPlayer(args[1]);
        Vehicle vehicle = Vehicle.getByPlate(ken);

        if (of == null || !of.hasPlayedBefore()) {
            player.sendMessage(Main.messagesConfig.getMessage("playerNotFound"));
            return true;
        }

        assert vehicle != null;
        List<String> riders = vehicle.getRiders();
        riders.remove(of.getUniqueId().toString());
        vehicle.setRiders(riders);
        vehicle.save();
        player.sendMessage(Main.messagesConfig.getMessage("memberChange"));

        return true;
    }
}
