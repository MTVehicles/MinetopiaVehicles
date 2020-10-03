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

public class VehicleGiveCar extends MTVehicleSubCommand {
    @Override
    public boolean execute(CommandSender sender, Command cmd, String s, String[] args) {
        if (!(sender instanceof Player)) return false;

        Player player = (Player) sender;
        ItemStack item = player.getInventory().getItemInMainHand();

        if (!checkPermission("mtvehicles.givecar")) return true;

        if (item == null || (!item.hasItemMeta() || !(NBTUtils.contains(item, "mtvehicles.kenteken")))) {
            sendMessage(TextUtils.colorize(Main.messagesConfig.getMessage("noVehicleInHand")));
            return true;
        }

        if (args.length != 2) {
            player.sendMessage(Main.messagesConfig.getMessage("useSetOwner"));
            return true;
        }

        String licensePlate = NBTUtils.getString(item, "mtvehicles.kenteken");

        if (!Vehicle.existsByPlate(licensePlate)) {
            player.sendMessage(Main.messagesConfig.getMessage("vehicleNotFound"));
            return true;
        }

        Player of = Bukkit.getPlayer(args[1]);

        if (of == null || !of.hasPlayedBefore()) {
            player.sendMessage(Main.messagesConfig.getMessage("playerNotFound"));
            return true;
        }


        player.sendMessage(Main.messagesConfig.getMessage("memberChange"));
        return true;
    }
}
