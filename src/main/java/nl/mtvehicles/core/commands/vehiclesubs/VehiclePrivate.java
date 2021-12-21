package nl.mtvehicles.core.commands.vehiclesubs;

import nl.mtvehicles.core.Main;
import nl.mtvehicles.core.infrastructure.helpers.NBTUtils;
import nl.mtvehicles.core.infrastructure.helpers.TextUtils;
import nl.mtvehicles.core.infrastructure.models.MTVehicleSubCommand;
import nl.mtvehicles.core.infrastructure.models.Vehicle;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class VehiclePrivate extends MTVehicleSubCommand {
    public VehiclePrivate() {
        this.setPlayerCommand(true);
    }

    @Override
    public boolean execute(CommandSender sender, Command cmd, String s, String[] args) {
        Player p = (Player) sender;
        ItemStack item = player.getInventory().getItemInMainHand();
        if (!item.hasItemMeta() || !NBTUtils.contains(item, "mtvehicles.kenteken")) {
            sendMessage(TextUtils.colorize(ConfigModule.messagesConfig.getMessage("noVehicleInHand")));
            return true;
        }

        String licensePlate = NBTUtils.getString(item, "mtvehicles.kenteken");

        Vehicle vehicle = Vehicle.getByPlate(licensePlate);

        assert vehicle != null;
        vehicle.setOpen(false);
        vehicle.save();

        sendMessage(ConfigModule.messagesConfig.getMessage("actionSuccessful"));

        return true;
    }
}