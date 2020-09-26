package nl.mtvehicles.core.Commands.VehiclesSubs;

import nl.mtvehicles.core.Events.MenuClickEvent;
import nl.mtvehicles.core.Infrastructure.Helpers.ItemFactory;
import nl.mtvehicles.core.Infrastructure.Helpers.TextUtils;
import nl.mtvehicles.core.Infrastructure.Models.MTVehicleSubCommand;
import nl.mtvehicles.core.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class vehicleMenuCmd extends MTVehicleSubCommand {
    public static HashMap<UUID, Inventory> beginmenu = new HashMap<>();

    @Override
    public boolean execute(CommandSender sender, Command cmd, String s, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }

        if (!checkPermission("mtvehicles.menu")) return true;

        Player p = (Player) sender;
        sendMessage(Main.messagesConfig.getMessage("menuOpen"));
        Inventory inv = Bukkit.createInventory(null, Main.defaultConfig.getConfig().getInt("vehicleMenuSize") * 9, "Vehicle Menu");
        for (Map<?, ?> vehicle : Main.vehiclesConfig.getConfig().getMapList("voertuigen")) {
            inv.addItem(carItem((int) vehicle.get("itemDamage"), (String) vehicle.get("name"), (String) vehicle.get("skinItem")));
        }
        beginmenu.put(p.getUniqueId(), inv);
        p.openInventory(inv);

        return true;
    }

    public ItemStack carItem(int id, String name, String material) {
        ItemStack car = (new ItemFactory(Material.getMaterial(material))).setDurability((short) id).setName(TextUtils.colorize("&6" + name)).toItemStack();
        ItemMeta im = car.getItemMeta();
        List<String> itemlore = new ArrayList<>();
        itemlore.add(TextUtils.colorize("&a"));
        im.setLore(itemlore);
        im.setUnbreakable(true);
        car.setItemMeta(im);


        return car;
    }
}
