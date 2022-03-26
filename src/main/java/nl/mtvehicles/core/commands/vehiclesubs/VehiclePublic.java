package nl.mtvehicles.core.commands.vehiclesubs;

import nl.mtvehicles.core.infrastructure.enums.Message;
import nl.mtvehicles.core.infrastructure.models.MTVehicleSubCommand;
import nl.mtvehicles.core.infrastructure.models.Vehicle;
import nl.mtvehicles.core.infrastructure.models.VehicleUtils;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;

public class VehiclePublic extends MTVehicleSubCommand {
    public VehiclePublic() {
        this.setPlayerCommand(true);
    }

    @Override
    public boolean execute(CommandSender sender, Command cmd, String s, String[] args) {
        ItemStack item = player.getInventory().getItemInMainHand();

        if (!isHoldingVehicle()) return true;

        String licensePlate = VehicleUtils.getLicensePlate(item);

        Vehicle vehicle = VehicleUtils.getByLicensePlate(licensePlate);

        assert vehicle != null;
        vehicle.setOpen(true);
        vehicle.save();

        sendMessage(ConfigModule.messagesConfig.getMessage(Message.ACTION_SUCCESSFUL));

        return true;
    }
}