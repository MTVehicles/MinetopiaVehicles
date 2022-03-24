package nl.mtvehicles.core.commands.vehiclesubs;

import de.tr7zw.changeme.nbtapi.NBTItem;
import nl.mtvehicles.core.infrastructure.helpers.TextUtils;
import nl.mtvehicles.core.infrastructure.models.MTVehicleSubCommand;
import nl.mtvehicles.core.infrastructure.models.Vehicle;
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

        if (!item.hasItemMeta() || !(new NBTItem(item)).hasKey("mtvehicles.kenteken")) {
            sendMessage(TextUtils.colorize(ConfigModule.messagesConfig.getMessage("noVehicleInHand")));
            return true;
        }

        try {
            String licensePlate = Vehicle.getLicensePlate(item);
            Vehicle.getByPlate(licensePlate).delete();
            sendMessage(TextUtils.colorize(ConfigModule.messagesConfig.getMessage("vehicleDeleted")));
        } catch (Exception e){
            sendMessage(TextUtils.colorize(ConfigModule.messagesConfig.getMessage("vehicleAlreadyDeleted")));
        }

        p.getInventory().getItemInMainHand().setAmount(0);
        return true;
    }
}
