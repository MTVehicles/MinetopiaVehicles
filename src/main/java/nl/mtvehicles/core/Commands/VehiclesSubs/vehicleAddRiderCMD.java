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

public class vehicleAddRiderCMD extends MTVehicleSubCommand {
    @Override
    public boolean execute(CommandSender sender, Command cmd, String s, String[] args) {
        if (!isPlayer) return false;

        Player p = (Player) sender;
        ItemStack item = p.getInventory().getItemInMainHand();
        if (!item.hasItemMeta() || !NBTUtils.contains(item, "mtvehicles.kenteken")) {
            sendMessage(TextUtils.colorize(Main.messagesConfig.getMessage("noVehicleInHand")));
            return true;
        }

        if (args.length != 2) {
            p.sendMessage("gebruik /vehicle addriders <speler>");
        }

        try {
            String ken = NBTUtils.getString(item, "mtvehicles.kenteken");
            Vehicle.getByPlate(ken).setOwner(args[1]);
            Player of = Bukkit.getPlayer(args[1]);
            if (!of.hasPlayedBefore()) {
                p.sendMessage(Main.messagesConfig.getMessage("playerNotFound"));

            } else {
                List<String> riders = Main.vehicleDataConfig.getConfig().getStringList("vehicle." + ken + ".riders");
                riders.add(of.getUniqueId().toString());
                Main.vehicleDataConfig.getConfig().set("vehicle." + ken + ".riders", riders);
                Main.vehicleDataConfig.save();

                Main.vehicleDataConfig.save();
                p.sendMessage(Main.messagesConfig.getMessage("memberChange"));
            }
        } catch (NullPointerException x) {
            p.sendMessage(Main.messagesConfig.getMessage("playerNotFound"));
        }

        return true;
    }
}
