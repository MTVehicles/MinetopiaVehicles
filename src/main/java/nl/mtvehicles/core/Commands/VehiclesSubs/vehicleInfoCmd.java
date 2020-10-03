package nl.mtvehicles.core.Commands.VehiclesSubs;

import nl.mtvehicles.core.Infrastructure.Helpers.NBTUtils;
import nl.mtvehicles.core.Infrastructure.Helpers.TextUtils;
import nl.mtvehicles.core.Infrastructure.Models.ConfigUtils;
import nl.mtvehicles.core.Infrastructure.Models.MTVehicleSubCommand;
import nl.mtvehicles.core.Infrastructure.Models.Vehicle;
import nl.mtvehicles.core.Main;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;
import java.util.stream.Collectors;

public class vehicleInfoCmd extends MTVehicleSubCommand {
    @Override
    public boolean execute(CommandSender sender, Command cmd, String s, String[] args) {
        Player p = (Player) sender;
        ItemStack item = p.getInventory().getItemInMainHand();

        if (item == null || (!item.hasItemMeta() || !(NBTUtils.contains(item, "mtvehicles.kenteken")))) {
            sendMessage(TextUtils.colorize(Main.messagesConfig.getMessage("noVehicleInHand")));
            return true;
        }

        Main.configList.forEach(ConfigUtils::reload);

        String ken = NBTUtils.getString(item, "mtvehicles.kenteken");
        Vehicle vehicle = Vehicle.getByPlate(ken);

        if (vehicle == null) return true;

        sendMessage("&6Kenteken: &c" + ken);
        sendMessage("&6Owner: &c" + vehicle.getOwnerName());

        if (vehicle.getRiders().size() == 0) {
            sendMessage("&6Riders: &cGeen");
        } else {
            sendMessage(String.format(
                    "&6Riders (%s): &c%s",
                    vehicle.getRiders().size(),
                    vehicle.getRiders().stream()
                            .map(UUID::fromString)
                            .map(Bukkit::getOfflinePlayer)
                            .map(OfflinePlayer::getName)
                            .collect(Collectors.joining(", ")))
            );
        }

        if (vehicle.getMembers().size() == 0) {
            sendMessage("&6Members: &cGeen");
        } else {
            sendMessage(String.format(
                    "&6Members (%s): &c%s",
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

