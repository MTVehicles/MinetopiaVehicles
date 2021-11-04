package nl.mtvehicles.core.commands.vehiclesubs;

import nl.mtvehicles.core.Main;
import nl.mtvehicles.core.infrastructure.helpers.NBTUtils;
import nl.mtvehicles.core.infrastructure.helpers.TextUtils;
import nl.mtvehicles.core.infrastructure.models.MTVehicleSubCommand;
import nl.mtvehicles.core.infrastructure.models.Vehicle;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class VehiclePublic extends MTVehicleSubCommand {
    public VehiclePublic() {
        this.setPlayerCommand(true);
    }

    @Override
    public boolean execute(CommandSender sender, Command cmd, String s, String[] args) {
        Player p = (Player) sender;
        ItemStack item = player.getInventory().getItemInMainHand();
        if (!item.hasItemMeta() || !NBTUtils.contains(item, "mtvehicles.kenteken")) {
            sendMessage(TextUtils.colorize(Main.messagesConfig.getMessage("noVehicleInHand")));
            return true;
        }

        String licensePlate = NBTUtils.getString(item, "mtvehicles.kenteken");

        Vehicle vehicle = Vehicle.getByPlate(licensePlate);

        assert vehicle != null;
        vehicle.setOpen(true);
        vehicle.save();

        sendMessage(Main.messagesConfig.getMessage("actionSuccessful"));

        return true;
    }
}