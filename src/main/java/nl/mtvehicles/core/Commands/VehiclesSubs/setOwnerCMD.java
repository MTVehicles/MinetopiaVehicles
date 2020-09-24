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

public class setOwnerCMD extends MTVehicleSubCommand {
    @Override
    public boolean execute(CommandSender sender, Command cmd, String s, String[] args) {
        if (sender instanceof Player) {

            Player p = (Player) sender;
            ItemStack item = p.getInventory().getItemInMainHand();
            if (!checkPermission("mtvehicles.setowner")) {
                sendMessage(TextUtils.colorize(Main.messagesConfig.getMessage("noPerms")));
                return true;
            }
            if (item == null || (!item.hasItemMeta() || !(NBTUtils.contains(item, "mtvehicles.kenteken")))) {
                sendMessage(TextUtils.colorize(Main.messagesConfig.getMessage("noVehicleInHand")));
            } else {
                try {
                    String ken = NBTUtils.getString(item, "mtvehicles.kenteken");
                    Vehicle.getByPlate(ken).setOwner(args[1]);
                    Player of = Bukkit.getPlayer(args[1]);
                    if (!of.hasPlayedBefore()) {
                        p.sendMessage(Main.messagesConfig.getMessage("playerNotFound"));

                    } else {
                        Main.vehicleDataConfig.getConfig().set("vehicle."+ken+".owner", of.getUniqueId().toString());
                        p.sendMessage(Main.messagesConfig.getMessage("ownerChange"));
                        Main.vehicleDataConfig.save();
                    }
                }
                catch (NullPointerException x) {
                    p.sendMessage(Main.messagesConfig.getMessage("playerNotFound"));
                    }

            }


        }
        return true;
    }
}
