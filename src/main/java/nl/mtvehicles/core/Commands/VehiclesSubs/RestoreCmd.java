package nl.mtvehicles.core.Commands.VehiclesSubs;

import nl.mtvehicles.core.Infrastructure.Helpers.ItemFactory;
import nl.mtvehicles.core.Infrastructure.Helpers.TextUtils;
import nl.mtvehicles.core.Infrastructure.Helpers.Vehicles;
import nl.mtvehicles.core.Infrastructure.Models.Config;
import nl.mtvehicles.core.Infrastructure.Models.MTVehicleSubCommand;
import nl.mtvehicles.core.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class RestoreCmd extends MTVehicleSubCommand {
    @Override
    public boolean execute(CommandSender sender, Command cmd, String s, String[] args) {
        if (sender instanceof Player) {
            if (!checkPermission("mtvehicles.restore")) return true;

            Player p = (Player) sender;
            sendMessage(Main.messagesConfig.getMessage("menuOpen"));
            Inventory inv = Bukkit.createInventory(null, 54, "Vehicle Restore");
            Main.configList.forEach(Config::reload);
            if (Main.vehicleDataConfig.getConfig().getConfigurationSection("vehicle") == null) {

            } else {
                for (String key : Main.vehicleDataConfig.getConfig().getConfigurationSection("vehicle").getKeys(false)) {
                    inv.addItem(Vehicles.carItem2(Main.vehicleDataConfig.getConfig().getInt("vehicle."+key+".skinDamage"), Main.vehicleDataConfig.getConfig().getString("vehicle."+key+".name"), Main.vehicleDataConfig.getConfig().getString("vehicle."+key+".skinItem"), key));
                }


                p.openInventory(inv);
            }
        }
        return true;
    }



}
