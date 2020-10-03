package nl.mtvehicles.core.Commands.VehiclesSubs;

import nl.mtvehicles.core.Infrastructure.Helpers.ItemUtils;
import nl.mtvehicles.core.Infrastructure.Models.MTVehicleSubCommand;
import nl.mtvehicles.core.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class VehicleMenu extends MTVehicleSubCommand {
    public static HashMap<UUID, Inventory> beginMenu = new HashMap<>();

    @Override
    public boolean execute(CommandSender sender, Command cmd, String s, String[] args) {
        if (!isPlayer) return false;

        if (!checkPermission("mtvehicles.menu")) return true;
        Player p = (Player) sender;
        sendMessage(Main.messagesConfig.getMessage("menuOpen"));
        int menuSize = Main.defaultConfig.getConfig().getInt("vehicleMenuSize") * 9;
        Inventory inv = Bukkit.createInventory(null, menuSize, "Vehicle Menu");
        for (Map<?, ?> vehicle : Main.vehiclesConfig.getConfig().getMapList("voertuigen")) {
            int itemDamage = (int) vehicle.get("itemDamage");
            String name = (String) vehicle.get("name");
            String skinItem = (String) vehicle.get("skinItem");
            ItemStack itemStack = ItemUtils.carItem(itemDamage, name, skinItem);
            inv.addItem(itemStack);
        }

        beginMenu.put(p.getUniqueId(), inv);

        p.openInventory(inv);

        return true;
    }
}