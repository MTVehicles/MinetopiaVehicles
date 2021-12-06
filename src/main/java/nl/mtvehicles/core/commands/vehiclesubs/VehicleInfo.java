package nl.mtvehicles.core.commands.vehiclesubs;

import nl.mtvehicles.core.infrastructure.helpers.NBTUtils;
import nl.mtvehicles.core.infrastructure.helpers.TextUtils;
import nl.mtvehicles.core.infrastructure.models.ConfigUtils;
import nl.mtvehicles.core.infrastructure.models.MTVehicleSubCommand;
import nl.mtvehicles.core.infrastructure.models.Vehicle;
import nl.mtvehicles.core.Main;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.UUID;
import java.util.stream.Collectors;

public class VehicleInfo extends MTVehicleSubCommand {
    public VehicleInfo() {
        this.setPlayerCommand(true);
    }

    @Override
    public boolean execute(CommandSender sender, Command cmd, String s, String[] args) {
        Player p = (Player) sender;

        ItemStack item = p.getInventory().getItemInMainHand();

        if (!item.hasItemMeta() || !(NBTUtils.contains(item, "mtvehicles.kenteken"))) {
            sendMessage(TextUtils.colorize(Main.messagesConfig.getMessage("noVehicleInHand")));
            return true;
        }

        Main.configList.forEach(ConfigUtils::reload);

        String ken = NBTUtils.getString(item, "mtvehicles.kenteken");
        Vehicle vehicle = Vehicle.getByPlate(ken);

        if (vehicle == null) return true;

        NumberFormat formatter = new DecimalFormat("#0.000");
        sendMessage(Main.messagesConfig.getMessage("vehicleInfoInformation"));
        sendMessage(Main.messagesConfig.getMessage("vehicleInfoType") + vehicle.getVehicleType());
        sendMessage(Main.messagesConfig.getMessage("vehicleInfoName") + vehicle.getName());
        sendMessage(Main.messagesConfig.getMessage("vehicleInfoLicense") + ken);
        if (p.hasPermission("mtvehicles.admin")) {
            sendMessage(Main.messagesConfig.getMessage("vehicleInfoUUID") + Vehicle.getCarUuid(ken));
        }
        sendMessage(Main.messagesConfig.getMessage("vehicleInfoSpeed") + formatter.format(vehicle.getMaxSpeed()*20).toString().replace(",", ".") + " blocks/sec");
        sendMessage(Main.messagesConfig.getMessage("vehicleInfoAcceleration") + formatter.format(vehicle.getAccelerationSpeed()/0.2*100).toString().replace(",", ".") + " blocks/sec^2");
        sendMessage(Main.messagesConfig.getMessage("vehicleInfoOwner") + vehicle.getOwnerName());

        if (vehicle.getRiders().size() == 0) {
            sendMessage(Main.messagesConfig.getMessage("vehicleInfoRiders"));
        } else {
            sendMessage(String.format(
                    Main.messagesConfig.getMessage("vehicleInfoRiders2"),
                    vehicle.getRiders().size(),
                    vehicle.getRiders().stream()
                            .map(UUID::fromString)
                            .map(Bukkit::getOfflinePlayer)
                            .map(OfflinePlayer::getName)
                            .collect(Collectors.joining(", ")))
            );
        }

        if (vehicle.getMembers().size() == 0) {
            sendMessage(Main.messagesConfig.getMessage("vehicleInfoMembers"));
        } else {
            sendMessage(String.format(
                    Main.messagesConfig.getMessage("vehicleInfoMembers2"),
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

