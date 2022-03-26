package nl.mtvehicles.core.commands.vehiclesubs;

import nl.mtvehicles.core.infrastructure.dataconfig.MessagesConfig;
import nl.mtvehicles.core.infrastructure.enums.Message;
import nl.mtvehicles.core.infrastructure.helpers.ItemUtils;
import nl.mtvehicles.core.infrastructure.models.Config;
import nl.mtvehicles.core.infrastructure.models.MTVehicleSubCommand;
import nl.mtvehicles.core.infrastructure.models.VehicleUtils;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class VehicleEdit extends MTVehicleSubCommand {
    public VehicleEdit() {
        this.setPlayerCommand(true);
    }

    @Override
    public boolean execute(CommandSender sender, Command cmd, String s, String[] args) {
        if (!checkPermission("mtvehicles.edit")) return true;

        final ItemStack item = player.getInventory().getItemInMainHand();

        if (!isHoldingVehicle()) return true;

        ConfigModule.configList.forEach(Config::reload);

        sendMessage(ConfigModule.messagesConfig.getMessage(Message.MENU_OPEN));
        editMenu(player, item);

        return true;
    }

    public static void editMenu(Player p, ItemStack item) {
        String licensePlate = VehicleUtils.getLicensePlate(item);
        MessagesConfig msg = ConfigModule.messagesConfig;
        Inventory inv = Bukkit.createInventory(null, 27, "Vehicle Edit");
        inv.setItem(10, ItemUtils.mItem3(ConfigModule.vehicleDataConfig.getConfig().getString("vehicle." + licensePlate + ".skinItem"), 1, (short) ConfigModule.vehicleDataConfig.getConfig().getInt("vehicle." + licensePlate + ".skinDamage"), msg.getMessage(Message.VEHICLE_SETTINGS), "", "mtcustom", ConfigModule.vehicleDataConfig.getConfig().getString("vehicle." + licensePlate + ".nbtValue")));
        inv.setItem(11, ItemUtils.mItem2("DIAMOND_HOE", 1, (short) 58, msg.getMessage(Message.FUEL_SETTINGS), ""));
        inv.setItem(12, ItemUtils.mItem("CHEST", 1, (short) 0, msg.getMessage(Message.TRUNK_SETTINGS), ""));
        inv.setItem(13, ItemUtils.mItem("PAPER", 1, (short) 0, msg.getMessage(Message.MEMBER_SETTINGS), ""));
        inv.setItem(14, ItemUtils.woolItem("STAINED_GLASS_PANE", "LIME_STAINED_GLASS", 1, (short) 5, msg.getMessage(Message.SPEED_SETTINGS), ""));
        inv.setItem(16, ItemUtils.mItem("BARRIER", 1, (short) 0, msg.getMessage(Message.DELETE_VEHICLE), msg.getMessage(Message.DELETE_WARNING_LORE)));
        p.openInventory(inv);
    }
}
