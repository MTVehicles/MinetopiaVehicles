package nl.mtvehicles.core.Commands.VehiclesSubs;

import nl.mtvehicles.core.Infrastructure.Helpers.TextUtils;
import nl.mtvehicles.core.Infrastructure.Helpers.VehiclesUtils;
import nl.mtvehicles.core.Infrastructure.Models.ConfigUtils;
import nl.mtvehicles.core.Infrastructure.Models.MTVehicleSubCommand;
import nl.mtvehicles.core.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class vehicleRestoreCmd extends MTVehicleSubCommand {
    @Override
    public boolean execute(CommandSender sender, Command cmd, String s, String[] args) {
        if (sender instanceof Player) {
            if (!checkPermission("mtvehicles.restore")) { sendMessage(TextUtils.colorize(Main.messagesConfig.getMessage("noPerms"))); return true;}

            Player p = (Player) sender;
            sendMessage(Main.messagesConfig.getMessage("menuOpen"));
            Inventory inv = Bukkit.createInventory(null, 54, "Vehicle Restore");
            Main.configList.forEach(ConfigUtils::reload);
            if (Main.vehicleDataConfig.getConfig().getConfigurationSection("vehicle") == null) {

            } else {
                for (String key : Main.vehicleDataConfig.getConfig().getConfigurationSection("vehicle").getKeys(false)) {
                    if (Main.vehicleDataConfig.getConfig().getBoolean("vehicle."+key+".isGlow") == true){
                        inv.addItem(VehiclesUtils.carItem2glow(Main.vehicleDataConfig.getConfig().getInt("vehicle."+key+".skinDamage"), Main.vehicleDataConfig.getConfig().getString("vehicle."+key+".name"), Main.vehicleDataConfig.getConfig().getString("vehicle."+key+".skinItem"), key));
                    } else {
                        inv.addItem(VehiclesUtils.carItem2(Main.vehicleDataConfig.getConfig().getInt("vehicle."+key+".skinDamage"), Main.vehicleDataConfig.getConfig().getString("vehicle."+key+".name"), Main.vehicleDataConfig.getConfig().getString("vehicle."+key+".skinItem"), key));
                    }

                }

//                int abc = Main.vehicleDataConfig.getConfig().getConfigurationSection("vehicle").getKeys(false).size();
//                System.out.println(abc);
                //inv.addItem(Vehicles.carItem2(Main.vehicleDataConfig.getConfig().getInt("vehicle."+key+".skinDamage"), Main.vehicleDataConfig.getConfig().getString("vehicle."+key+".name"), Main.vehicleDataConfig.getConfig().getString("vehicle."+key+".skinItem"), key));
                for (int i = 36; i <= 44; i++) {
                    inv.setItem(i, VehiclesUtils.mItem("STAINED_GLASS_PANE", 1, (short) 0, "&c", "&c"));
                }
                p.openInventory(inv);
            }
        }
        return true;
    }



}
