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
        sendMessage("&e----- &6Vehicle Information &e-----");
        sendMessage("&8&l-&r &eType: &6" + vehicle.getVehicleType());
        sendMessage("&8&l-&r &eName: &6" + vehicle.getName());
        sendMessage("&8&l-&r &eKenteken: &6" + ken);
        if (p.hasPermission("mtvehicles.admin")) {
            sendMessage("&8&l-&r &eUUID: &6" + Vehicle.getCarUuid(ken));
        }
        sendMessage("&8&l-&r &eSnelheid: &6" + formatter.format(vehicle.getMaxSpeed()*20).toString().replace(",", ".") + " blocks/sec");
        sendMessage("&8&l-&r &eAcceleratie: &6" + formatter.format(vehicle.getAccelerationSpeed()/0.2*100).toString().replace(",", ".") + " blocks/sec^2");
        sendMessage("&8&l-&r &eEigenaar: &6" + vehicle.getOwnerName());

        if (vehicle.getRiders().size() == 0) {
            sendMessage("&8&l-&r &eBustuurdes: &6Geen");
        } else {
            sendMessage(String.format(
                    "&8&l-&r &eBustuurdes (%s): &6%s",
                    vehicle.getRiders().size(),
                    vehicle.getRiders().stream()
                            .map(UUID::fromString)
                            .map(Bukkit::getOfflinePlayer)
                            .map(OfflinePlayer::getName)
                            .collect(Collectors.joining(", ")))
            );
        }

        if (vehicle.getMembers().size() == 0) {
            sendMessage("&8&l-&r &ePassagiers: &6Geen");
        } else {
            sendMessage(String.format(
                    "&8&l-&r &ePassagiers (%s): &6%s",
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

