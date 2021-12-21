package nl.mtvehicles.core.commands.vehiclesubs;

import nl.mtvehicles.core.infrastructure.helpers.ItemUtils;
import nl.mtvehicles.core.infrastructure.helpers.NBTUtils;
import nl.mtvehicles.core.infrastructure.helpers.TextUtils;
import nl.mtvehicles.core.infrastructure.models.ConfigUtils;
import nl.mtvehicles.core.infrastructure.models.MTVehicleSubCommand;
import nl.mtvehicles.core.Main;
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

        Player p = (Player) sender;

        final ItemStack item = p.getInventory().getItemInMainHand();

        if (!item.hasItemMeta() || !NBTUtils.contains(item, "mtvehicles.kenteken")) {
            sendMessage(TextUtils.colorize(ConfigModule.messagesConfig.getMessage("noVehicleInHand")));
            return true;
        }

        ConfigModule.configList.forEach(ConfigUtils::reload);

        sendMessage(ConfigModule.messagesConfig.getMessage("menuOpen"));
        editMenu(p, item);

        return true;
    }

    public static void editMenu(Player p, ItemStack item) {
        String ken = NBTUtils.getString(item, "mtvehicles.kenteken");
        Inventory inv = Bukkit.createInventory(null, 27, "Vehicle Edit");
        inv.setItem(10, ItemUtils.mItem3(ConfigModule.vehicleDataConfig.getConfig().getString("vehicle." + ken + ".skinItem"), 1, (short) ConfigModule.vehicleDataConfig.getConfig().getInt("vehicle." + ken + ".skinDamage"), "&6Vehicle Settings", "", "mtcustom", ConfigModule.vehicleDataConfig.getConfig().getString("vehicle." + ken + ".nbtValue")));
        inv.setItem(11, ItemUtils.mItem2("DIAMOND_HOE", 1, (short) 58, "&6Benzine Settings", ""));
        inv.setItem(12, ItemUtils.mItem("CHEST", 1, (short) 0, "&6Kofferbak Settings", ""));
        inv.setItem(13, ItemUtils.mItem("PAPER", 1, (short) 0, "&6Member Settings", ""));
        inv.setItem(14, ItemUtils.woolItem("STAINED_GLASS_PANE", "LIME_STAINED_GLASS", 1, (short) 5, "&6Speed Settings", ""));
        inv.setItem(16, ItemUtils.mItem("BARRIER", 1, (short) 0, "&4Delete Vehicle", "&7LETOP! Je kunt het item niet meer terug krijgen!"));
        p.openInventory(inv);
    }
}
