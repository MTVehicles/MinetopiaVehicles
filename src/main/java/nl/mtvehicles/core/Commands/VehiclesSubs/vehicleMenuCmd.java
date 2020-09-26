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

import java.util.*;

public class vehicleMenuCmd extends MTVehicleSubCommand {
    public static HashMap<UUID, Inventory> beginMenu = new HashMap<>();

    @Override
    public boolean execute(CommandSender sender, Command cmd, String s, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }

        if (!checkPermission("mtvehicles.menu")) return true;

        Player p = (Player) sender;
        sendMessage(Main.messagesConfig.getMessage("menuOpen"));
        int menuSize = Main.defaultConfig.getConfig().getInt("vehicleMenuSize") * 9;
        Inventory inv = Bukkit.createInventory(null, menuSize, "Vehicle Menu");

        for (Map<?, ?> vehicle : Main.vehiclesConfig.getConfig().getMapList("voertuigen")) {
            int itemDamage = (int) vehicle.get("itemDamage");
            String name = (String) vehicle.get("name");
            String skinItem = (String) vehicle.get("skinItem");
            ItemStack itemStack = carItem(itemDamage, name, skinItem);
            inv.addItem(itemStack);
        }

        beginMenu.put(p.getUniqueId(), inv);

        p.openInventory(inv);

        return true;
    }

    public ItemStack carItem(int id, String name, String material) {
        ItemStack car = (new ItemFactory(Material.getMaterial(material))).setDurability((short) id).setName(TextUtils.colorize("&6" + name)).toItemStack();
        ItemMeta im = car.getItemMeta();
        List<String> itemLore = new ArrayList<>();
        itemLore.add(TextUtils.colorize("&a"));
        assert im != null;
        im.setLore(itemLore);
        im.setUnbreakable(true);
        car.setItemMeta(im);

        return car;
    }
}
