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

public class vehicleSetOwnerCMD extends MTVehicleSubCommand {
    @Override
    public boolean execute(CommandSender sender, Command cmd, String s, String[] args) {
        if (!(sender instanceof Player)) return false;

        Player p = (Player) sender;
        ItemStack item = p.getInventory().getItemInMainHand();

        if (!checkPermission("mtvehicles.setowner")) return true;

        if (item == null || (!item.hasItemMeta() || !(NBTUtils.contains(item, "mtvehicles.kenteken")))) {
            sendMessage(TextUtils.colorize(Main.messagesConfig.getMessage("noVehicleInHand")));
            return true;
        }

        if (args.length != 2) {
            p.sendMessage(Main.messagesConfig.getMessage("useSetOwner"));
            return true;
        }

        String ken = NBTUtils.getString(item, "mtvehicles.kenteken");
        Vehicle.getByPlate(ken).setOwner(args[1]);
        Player of = Bukkit.getPlayer(args[1]);

        if (of == null || !of.hasPlayedBefore()) {
            p.sendMessage(Main.messagesConfig.getMessage("playerNotFound"));
            return true;
        }

        Main.vehicleDataConfig.getConfig().set("vehicle." + ken + ".owner", of.getUniqueId().toString());
        p.sendMessage(Main.messagesConfig.getMessage("memberChange"));
        Main.vehicleDataConfig.save();

        return true;
    }
}
