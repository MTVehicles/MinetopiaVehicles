package nl.mtvehicles.core.Commands.VehiclesSubs;

import nl.mtvehicles.core.Infrastructure.Helpers.ItemFactory;
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

public class Menu extends MTVehicleSubCommand {
    @Override
    public boolean execute(CommandSender sender, Command cmd, String s, String[] args) {
        if (sender instanceof Player) {


            if (!checkPermission("mtvehicles.menu")) return true;



            Player p = (Player) sender;

            sendMessage(Main.messagesConfig.getMessage("reloadSuccesvol"));

            Inventory inv = Bukkit.createInventory(null, 18, "Vehicle Menu");
            inv.addItem(carItem((short)1002, "dik"));

            p.openInventory(inv);

        }

        return true;

    }

    public ItemStack carItem(short id, String name){
        ItemStack car = (new ItemFactory(Material.DIAMOND_HOE)).setDurability((short)id).unbreakable().toItemStack();

        return car;
    }
}
