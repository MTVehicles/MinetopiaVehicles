package nl.mtvehicles.core.commands.vehiclesubs;

import nl.mtvehicles.core.infrastructure.enums.Message;
import nl.mtvehicles.core.infrastructure.models.Config;
import nl.mtvehicles.core.infrastructure.models.MTVehicleSubCommand;
import nl.mtvehicles.core.infrastructure.models.Vehicle;
import nl.mtvehicles.core.infrastructure.models.VehicleUtils;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * <b>/vehicle info</b> - get information about the held vehicle
 */
public class VehicleInfo extends MTVehicleSubCommand {
    public VehicleInfo() {
        this.setPlayerCommand(true);
    }

    @Override
    public boolean execute() {

        Vehicle vehicle = getVehicle();
        if (vehicle == null) return true;

        String licensePlate = vehicle.getLicensePlate();

        NumberFormat formatter = new DecimalFormat("#0.000");
        sendMessage(Message.VEHICLE_INFO_INFORMATION);
        sendMessage(ConfigModule.messagesConfig.getMessage(Message.VEHICLE_INFO_TYPE) + vehicle.getVehicleType().getName());
        sendMessage(ConfigModule.messagesConfig.getMessage(Message.VEHICLE_INFO_NAME) + vehicle.getName());
        sendMessage(ConfigModule.messagesConfig.getMessage(Message.VEHICLE_INFO_LICENSE) + licensePlate);
        if (player.hasPermission("mtvehicles.admin")) {
            sendMessage(ConfigModule.messagesConfig.getMessage(Message.VEHICLE_INFO_UUID) + VehicleUtils.getCarUUID(licensePlate));
        }
        sendMessage(ConfigModule.messagesConfig.getMessage(Message.VEHICLE_INFO_SPEED) + formatter.format(vehicle.getMaxSpeed()*20).toString().replace(",", ".") + " blocks/sec");
        sendMessage(ConfigModule.messagesConfig.getMessage(Message.VEHICLE_INFO_ACCELERATION) + formatter.format(vehicle.getAccelerationSpeed()/0.2*100).toString().replace(",", ".") + " blocks/sec^2");
        sendMessage(ConfigModule.messagesConfig.getMessage(Message.VEHICLE_INFO_OWNER) + vehicle.getOwnerName());

        if (vehicle.getRiders().size() == 0) {
            sendMessage(Message.VEHICLE_INFO_RIDERS_NONE);
        } else {
            sendMessage(String.format(
                    ConfigModule.messagesConfig.getMessage(Message.VEHICLE_INFO_RIDERS),
                    vehicle.getRiders().size(),
                    vehicle.getRiders().stream()
                            .map(UUID::fromString)
                            .map(Bukkit::getOfflinePlayer)
                            .map(OfflinePlayer::getName)
                            .collect(Collectors.joining(", ")))
            );
        }

        if (vehicle.getMembers().size() == 0) {
            sendMessage(Message.VEHICLE_INFO_MEMBERS_NONE);
        } else {
            sendMessage(String.format(
                    ConfigModule.messagesConfig.getMessage(Message.VEHICLE_INFO_MEMBERS),
                    vehicle.getMembers().size(),
                    vehicle.getMembers().stream()
                            .map(UUID::fromString)
                            .map(Bukkit::getOfflinePlayer)
                            .map(OfflinePlayer::getName)
                            .collect(Collectors.joining(", ")))
            );
        }

        return true;
    }
}
