package nl.mtvehicles.core.Commands.VehiclesSubs;

import nl.mtvehicles.core.Infrastructure.Helpers.NBTUtils;
import nl.mtvehicles.core.Infrastructure.Helpers.TextUtils;
import nl.mtvehicles.core.Infrastructure.Models.ConfigUtils;
import nl.mtvehicles.core.Infrastructure.Models.MTVehicleSubCommand;
import nl.mtvehicles.core.Infrastructure.Models.Vehicle;
import nl.mtvehicles.core.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class VehicleInfo extends MTVehicleSubCommand {
    @Override
    public boolean execute(CommandSender sender, Command cmd, String s, String[] args) {
        Player p = (Player) sender;
        ItemStack item = p.getInventory().getItemInMainHand();

        if (item == null || (!item.hasItemMeta() || !(NBTUtils.contains(item, "mtvehicles.kenteken")))) {
            sendMessage(TextUtils.colorize(Main.messagesConfig.getMessage("noVehicleInHand")));
            return true;
        }

        Main.configList.forEach(ConfigUtils::reload);

        String ken = NBTUtils.getString(item, "mtvehicles.kenteken");
        Vehicle vehicle = Vehicle.getByPlate(ken);

        if (vehicle == null) return true;




        sendMessage("&6Kenteken: &c" + ken);
        sendMessage("&6Owner: &c" + Bukkit.getOfflinePlayer(UUID.fromString(vehicle.getOwner())).getName());

        if (Main.vehicleDataConfig.getConfig().getStringList(String.format("vehicle.%s.riders", ken)).size() == 0) {
            sendMessage("&6Riders: &cGeen");
        } else {
            sendMessage("&6Riders: &c");
            for (String subj : Main.vehicleDataConfig.getConfig().getStringList(String.format("vehicle.%s.riders", ken))) {
                sendMessage("&6- &c" + Bukkit.getOfflinePlayer(UUID.fromString(subj)).getName());
            }
        }

        if (Main.vehicleDataConfig.getConfig().getStringList(String.format("vehicle.%s.members", ken)).size() == 0) {
            sendMessage("&6Members: &cGeen");
        } else {

            sendMessage("&6Members: &c");
            for (String subj : Main.vehicleDataConfig.getConfig().getStringList(String.format("vehicle.%s.members", ken))) {
                sendMessage("&6- &c" + Bukkit.getOfflinePlayer(UUID.fromString(subj)).getName());
            }
        }

        return true;
    }
}

