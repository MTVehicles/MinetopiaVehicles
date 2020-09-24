package nl.mtvehicles.core.Commands.VehiclesSubs;

import nl.mtvehicles.core.Infrastructure.Helpers.NBTUtils;
import nl.mtvehicles.core.Infrastructure.Helpers.TextUtils;
import nl.mtvehicles.core.Infrastructure.Models.Config;
import nl.mtvehicles.core.Infrastructure.Models.MTVehicleSubCommand;
import nl.mtvehicles.core.Infrastructure.Models.Vehicle;
import nl.mtvehicles.core.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class InfoCmd extends MTVehicleSubCommand {
    @Override
    public boolean execute(CommandSender sender, Command cmd, String s, String[] args) {
        Player p = (Player) sender;
        ItemStack item = p.getInventory().getItemInMainHand();
        if (item == null || (!item.hasItemMeta() || !(NBTUtils.contains(item, "mtvehicles.kenteken")))) {
            sendMessage(TextUtils.colorize(Main.messagesConfig.getMessage("noVehicleInHand")));
        } else {
            Main.configList.forEach(Config::reload);
            String ken = NBTUtils.getString(item, "mtvehicles.kenteken");
            sendMessage("&6Kenteken: &c" + ken);
            sendMessage("&6Owner: &c" + Bukkit.getOfflinePlayer(UUID.fromString(Vehicle.getByPlate(ken).getOwner().toString())).getName());
            List<?> list = Main.vehicleDataConfig.getConfig().getList("vehicle." + ken + ".riders");
            if (Main.vehicleDataConfig.getConfig().getList("vehicle." + ken + ".riders") == null) {
                sendMessage("&6Riders: &cGeen");
            } else {
                sendMessage("&6Riders: &c");
                for (Object object : list) {
                    String string1 = list.toString().replace("[", "");
                    String string2 = string1.replace("]", "");
                    sendMessage("&6- &c" + Bukkit.getOfflinePlayer(UUID.fromString(string2)).getName());
                }
            }
            List<?> list2 = Main.vehicleDataConfig.getConfig().getList("vehicle." + ken + ".members");
            if (Main.vehicleDataConfig.getConfig().getList("vehicle." + ken + ".members") == null) {
                sendMessage("&6Members: &cGeen");
            } else {
                sendMessage("&6Members: &c");
                for (Object object : list2) {
                    String string1 = list2.toString().replace("[", "");
                    String string2 = string1.replace("]", "");
                    sendMessage("&6- &c" + Bukkit.getOfflinePlayer(UUID.fromString(string2)).getName());
                }
            }
        }

        return true;
    }
}

