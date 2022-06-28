package nl.mtvehicles.core.commands.vehiclesubs;

import nl.mtvehicles.core.infrastructure.dataconfig.MessagesConfig;
import nl.mtvehicles.core.infrastructure.dataconfig.VehicleDataConfig;
import nl.mtvehicles.core.infrastructure.enums.InventoryTitle;
import nl.mtvehicles.core.infrastructure.enums.Message;
import nl.mtvehicles.core.infrastructure.utils.ItemUtils;
import nl.mtvehicles.core.infrastructure.models.MTVConfig;
import nl.mtvehicles.core.infrastructure.models.MTVSubCommand;
import nl.mtvehicles.core.infrastructure.vehicle.VehicleUtils;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * <b>/vehicle edit</b> - edit held vehicle's specifications (in a GUI).
 */
public class VehicleEdit extends MTVSubCommand {
    public VehicleEdit() {
        this.setPlayerCommand(true);
    }

    @Override
    public boolean execute() {
        if (!checkPermission("mtvehicles.edit")) return true;

        final ItemStack item = player.getInventory().getItemInMainHand();

        if (!isHoldingVehicle()) return true;

        ConfigModule.configList.forEach(MTVConfig::reload);

        sendMessage(Message.MENU_OPEN);
        editMenu(player, item);

        return true;
    }

    /**
     * Open /vehicle edit GUI menu to a player
     * @param p Player
     * @param item Vehicle item
     */
    public static void editMenu(Player p, ItemStack item) {
        String licensePlate = VehicleUtils.getLicensePlate(item);
        MessagesConfig msg = ConfigModule.messagesConfig;
        Inventory inv = Bukkit.createInventory(null, 27, InventoryTitle.VEHICLE_EDIT_MENU.getStringTitle());
        inv.setItem(10, ItemUtils.getMenuCustomItem(
                ItemUtils.getMaterial(ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.SKIN_ITEM).toString()),
                "mtcustom",
                ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.NBT_VALUE),
                msg.getMessage(Message.VEHICLE_SETTINGS),
                ConfigModule.vehicleDataConfig.getDamage(licensePlate),
                ""
        ));
        inv.setItem(11, ItemUtils.getMenuCustomItem(Material.DIAMOND_HOE, msg.getMessage(Message.FUEL_SETTINGS), 58, ""));
        inv.setItem(12, ItemUtils.getMenuItem(Material.CHEST, 1, msg.getMessage(Message.TRUNK_SETTINGS), ""));
        inv.setItem(13, ItemUtils.getMenuItem(Material.PAPER, 1, msg.getMessage(Message.MEMBER_SETTINGS), ""));
        inv.setItem(14, ItemUtils.getMenuItem("LIME_STAINED_GLASS", "STAINED_GLASS", (short) 5, 1, msg.getMessage(Message.SPEED_SETTINGS), ""));
        inv.setItem(16, ItemUtils.getMenuItem(Material.BARRIER, 1, msg.getMessage(Message.DELETE_VEHICLE), msg.getMessage(Message.DELETE_WARNING_LORE)));
        p.openInventory(inv);
    }
}
