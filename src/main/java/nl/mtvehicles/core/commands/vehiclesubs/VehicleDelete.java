package nl.mtvehicles.core.commands.vehiclesubs;

import nl.mtvehicles.core.infrastructure.enums.Message;
import nl.mtvehicles.core.infrastructure.helpers.TextUtils;
import nl.mtvehicles.core.infrastructure.models.MTVehicleSubCommand;
import nl.mtvehicles.core.infrastructure.models.VehicleUtils;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import org.bukkit.inventory.ItemStack;

public class VehicleDelete extends MTVehicleSubCommand {
    public VehicleDelete() {
        this.setPlayerCommand(true);
    }

    @Override
    public boolean execute() {
        if (!checkPermission("mtvehicles.delete")) return true;

        ItemStack item = player.getInventory().getItemInMainHand();

        if (!isHoldingVehicle()) return true;

        try {
            String licensePlate = VehicleUtils.getLicensePlate(item);
            VehicleUtils.getByLicensePlate(licensePlate).delete();
            sendMessage(TextUtils.colorize(ConfigModule.messagesConfig.getMessage(Message.VEHICLE_DELETED)));
        } catch (Exception e){
            sendMessage(TextUtils.colorize(ConfigModule.messagesConfig.getMessage(Message.VEHICLE_ALREADY_DELETED)));
        }

        player.getInventory().getItemInMainHand().setAmount(0);
        return true;
    }
}
