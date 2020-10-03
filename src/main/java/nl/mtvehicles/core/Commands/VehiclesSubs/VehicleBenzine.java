package nl.mtvehicles.core.Commands.VehiclesSubs;

import nl.mtvehicles.core.Infrastructure.Helpers.ItemFactory;
import nl.mtvehicles.core.Infrastructure.Helpers.TextUtils;
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

import java.util.ArrayList;
import java.util.List;

public class VehicleBenzine extends MTVehicleSubCommand {
    @Override
    public boolean execute(CommandSender sender, Command cmd, String s, String[] args) {
        if (!isPlayer) {
            sendMessage(Main.messagesConfig.getMessage("notForConsole"));
            return false;
        }


        Player p = (Player) sender;

        if (!checkPermission("mtvehicles.benzine")) return true;

        Inventory inv = Bukkit.createInventory(null, 9, "Benzine menu");


        inv.addItem(benzineItem(25, 25));
        inv.addItem(benzineItem(50, 50));
        inv.addItem(benzineItem(75,75));

        p.openInventory(inv);

        return true;
    }

    public static ItemStack benzineItem(int liter, int literold) {
        ItemStack is = new ItemFactory(Material.getMaterial("DIAMOND_HOE")).setAmount(1).setDurability((short) 58).setNBT("mtvehicles.benzineval", ""+literold).setNBT("mtvehicles.benzinesize", ""+liter).toItemStack();
        ItemMeta im = is.getItemMeta();
        List<String> itemlore = new ArrayList<>();
        itemlore.add(TextUtils.colorize("&8"));
        itemlore.add(TextUtils.colorize("&7Jerrycan &e"+literold+"&7/&e"+liter+" &7liter"));
        im.setLore(itemlore);
        im.setUnbreakable(true);
        im.setDisplayName(TextUtils.colorize("&6Jerrycan "+liter+"L"));
        is.setItemMeta(im);
        return is;
    }
}
