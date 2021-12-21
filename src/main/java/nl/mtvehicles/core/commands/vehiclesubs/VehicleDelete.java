package nl.mtvehicles.core.commands.vehiclesubs;

import nl.mtvehicles.core.infrastructure.helpers.NBTUtils;
import nl.mtvehicles.core.infrastructure.helpers.TextUtils;
import nl.mtvehicles.core.infrastructure.models.MTVehicleSubCommand;
import nl.mtvehicles.core.Main;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class VehicleDelete extends MTVehicleSubCommand {
    public VehicleDelete() {
        this.setPlayerCommand(true);
    }

    @Override
    public boolean execute(CommandSender sender, Command cmd, String s, String[] args) {
        if (!checkPermission("mtvehicles.delete")) return true;

        Player p = (Player) sender;

        ItemStack item = p.getInventory().getItemInMainHand();

        if (!item.hasItemMeta() || !NBTUtils.contains(item, "mtvehicles.kenteken")) {
            sendMessage(TextUtils.colorize(ConfigModule.messagesConfig.getMessage("noVehicleInHand")));
            return true;
        }

        String ken = NBTUtils.getString(item, "mtvehicles.kenteken");
        ConfigModule.vehicleDataConfig.getConfig().set("vehicle." + ken, null);
        ConfigModule.vehicleDataConfig.save();
        p.getInventory().getItemInMainHand().setAmount(0);
        sendMessage(TextUtils.colorize(ConfigModule.messagesConfig.getMessage("vehicleDeleted")));

        return true;
    }
}
