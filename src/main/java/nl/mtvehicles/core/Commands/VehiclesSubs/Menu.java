package nl.mtvehicles.core.Commands.VehiclesSubs;

import nl.mtvehicles.core.Infrastructure.Helpers.ItemFactory;
import nl.mtvehicles.core.Infrastructure.Helpers.Text;
import nl.mtvehicles.core.Infrastructure.Models.Config;
import nl.mtvehicles.core.Infrastructure.Models.MTVehicleSubCommand;
import nl.mtvehicles.core.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class Menu extends MTVehicleSubCommand {
    @Override
    public boolean execute(CommandSender sender, Command cmd, String s, String[] args) {
        if (sender instanceof Player) {
            if (!checkPermission("mtvehicles.menu")) return true;

            Player p = (Player) sender;
            sendMessage(Main.messagesConfig.getMessage("reloadSuccesvol"));
            Inventory inv = Bukkit.createInventory(null, 18, "Vehicle Menu");

            for (Map<?, ?> vehicle : Main.vehiclesConfig.getConfig().getMapList("voertuigen")) {
                System.out.println(vehicle);
                inv.addItem(carItem((short)vehicle.get("itemDamage"), (String) vehicle.get("name"), (String) vehicle.get("item")));
            }

            p.openInventory(inv);
        }

        return true;
    }

    public ItemStack carItem(short id, String name, String material){
        ItemStack car = (new ItemFactory(Material.getMaterial(material))).setDurability((short)id).unbreakable().setName(Text.colorize(name)).toItemStack();

        return car;
    }
}
