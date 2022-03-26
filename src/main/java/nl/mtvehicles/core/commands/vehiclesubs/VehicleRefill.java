package nl.mtvehicles.core.commands.vehiclesubs;

import nl.mtvehicles.core.infrastructure.enums.Message;
import nl.mtvehicles.core.infrastructure.models.MTVehicleSubCommand;
import nl.mtvehicles.core.infrastructure.models.Vehicle;
import nl.mtvehicles.core.infrastructure.models.VehicleUtils;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;

public class VehicleRefill extends MTVehicleSubCommand {
    public VehicleRefill() {
        this.setPlayerCommand(true);
    }

    @Override
    public boolean execute(CommandSender sender, Command cmd, String s, String[] args) {
        if (!checkPermission("mtvehicles.refill")) return true;

        final ItemStack item = player.getInventory().getItemInMainHand();

        if (!isHoldingVehicle()) return true;

        final String licensePlate = VehicleUtils.getLicensePlate(item);
        Vehicle vehicle = VehicleUtils.getByLicensePlate(licensePlate);
        vehicle.setFuel(100.0);
        vehicle.save();

        sendMessage(ConfigModule.messagesConfig.getMessage(Message.REFILL_SUCCESSFUL));
        return true;
    }
}