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

public class vehicleRemoveMemberCMD extends MTVehicleSubCommand {
    @Override
    public boolean execute(CommandSender sender, Command cmd, String s, String[] args) {
        if (sender instanceof Player) {

            Player p = (Player) sender;
            ItemStack item = p.getInventory().getItemInMainHand();
            if (item == null || (!item.hasItemMeta() || !(NBTUtils.contains(item, "mtvehicles.kenteken")))) {
                sendMessage(TextUtils.colorize(Main.messagesConfig.getMessage("noVehicleInHand")));
            } else {
                if (args.length > 1) {
                    try {
                        String ken = NBTUtils.getString(item, "mtvehicles.kenteken");
                        Vehicle.getByPlate(ken).setOwner(args[1]);
                        Player of = Bukkit.getPlayer(args[1]);
                        if (!of.hasPlayedBefore()) {
                            p.sendMessage(Main.messagesConfig.getMessage("playerNotFound"));

                        } else {
                            List<String> members = Main.vehicleDataConfig.getConfig().getStringList("vehicle." + ken + ".members");
                            members.remove(of.getUniqueId().toString());
                            Main.vehicleDataConfig.getConfig().set("vehicle." + ken + ".members", members);
                            Main.vehicleDataConfig.save();

                            Main.vehicleDataConfig.save();
                            p.sendMessage(Main.messagesConfig.getMessage("memberChange"));
                        }
                    } catch (NullPointerException x) {
                        p.sendMessage(Main.messagesConfig.getMessage("playerNotFound"));
                    }
                } else {
                    p.sendMessage("gebruik /vehicle addmember <speler>");
                }
            }


        }
        return true;
    }
}
