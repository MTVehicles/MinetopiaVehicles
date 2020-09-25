package nl.mtvehicles.core.Commands.VehiclesSubs;

import nl.mtvehicles.core.Infrastructure.Helpers.NBTUtils;
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
import org.bukkit.inventory.ItemStack;

public class vehicleEditCmd extends MTVehicleSubCommand {
    @Override
    public boolean execute(CommandSender sender, Command cmd, String s, String[] args) {
        if (sender instanceof Player) {
            if (!checkPermission("mtvehicles.edit")){ sendMessage(TextUtils.colorize(Main.messagesConfig.getMessage("noPerms"))); return true;}
            Player p = (Player) sender;
            final ItemStack item = p.getInventory().getItemInMainHand();
            if (item == null||(!item.hasItemMeta() || !(NBTUtils.contains(item, "mtvehicles.kenteken")))) {
                sendMessage(TextUtils.colorize(Main.messagesConfig.getMessage("noVehicleInHand")));
            }else {
                Main.configList.forEach(ConfigUtils::reload);
                String ken = NBTUtils.getString(item, "mtvehicles.kenteken");

                sendMessage(Main.messagesConfig.getMessage("menuOpen"));
                editMenu(p, item);
            }
        }
        return true;
    }

    public static void editMenu(Player p, ItemStack item){
        String ken = NBTUtils.getString(item, "mtvehicles.kenteken");
        Inventory inv = Bukkit.createInventory(null, 27, "Vehicle Edit");
        //inv.addItem(Vehicles.carItem2(Main.vehicleDataConfig.getConfig().getInt("vehicle." + ken + ".skinDamage"), Main.vehicleDataConfig.getConfig().getString("vehicle." + ken + ".name"), Main.vehicleDataConfig.getConfig().getString("vehicle." + ken + ".skinItem"), ken));
        inv.setItem(10, VehiclesUtils.mItem2(Main.vehicleDataConfig.getConfig().getString("vehicle." + ken + ".skinItem"), 1 , (short)Main.vehicleDataConfig.getConfig().getInt("vehicle." + ken + ".skinDamage"), "&6Vehicle Settings", ""));
        inv.setItem(11, VehiclesUtils.mItem2("DIAMOND_HOE", 1 , (short)58, "&6Benzine Settings", ""));
        inv.setItem(12, VehiclesUtils.mItem("CHEST", 1 , (short)0, "&6Kofferbak Settings", ""));
        inv.setItem(13, VehiclesUtils.mItem("PAPER", 1 , (short)0, "&6Member Settings", ""));
        inv.setItem(14, VehiclesUtils.woolItem("STAINED_GLASS_PANE", "LIME_STAINED_GLASS", 1, (short) 5, "&6Speed Settings", ""));
        inv.setItem(16, VehiclesUtils.mItem("BARRIER", 1 , (short)0, "&4Delete Vehicle", "&7LETOP! Je kunt het item niet meer terug krijgen!"));
        p.openInventory(inv);

    }




}
