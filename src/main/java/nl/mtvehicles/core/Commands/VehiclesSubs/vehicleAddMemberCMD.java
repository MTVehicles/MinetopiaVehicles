package nl.mtvehicles.core.Commands.VehiclesSubs;

import nl.mtvehicles.core.Infrastructure.Helpers.NBTUtils;
import nl.mtvehicles.core.Infrastructure.Helpers.TextUtils;
import nl.mtvehicles.core.Infrastructure.Models.MTVehicleSubCommand;
import nl.mtvehicles.core.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class vehicleAddMemberCMD extends MTVehicleSubCommand {
    @Override
    public boolean execute(CommandSender sender, Command cmd, String s, String[] args) {
        if (!isPlayer) return false;

        Player player = (Player) sender;
        ItemStack item = player.getInventory().getItemInMainHand();
        if (!item.hasItemMeta() || !NBTUtils.contains(item, "mtvehicles.kenteken")) {
            sendMessage(TextUtils.colorize(Main.messagesConfig.getMessage("noVehicleInHand")));
            return true;
        }

        if (args.length != 2) {
            sendMessage("gebruik /vehicle addmember <speler>");
        }

        Player offlinePlayer = Bukkit.getPlayer(args[1]);
        String licensePlate = NBTUtils.getString(item, "mtvehicles.kenteken");

        if (offlinePlayer == null || !offlinePlayer.hasPlayedBefore()) {
            sendMessage(Main.messagesConfig.getMessage("playerNotFound"));
            return true;
        }

        List<String> members = Main.vehicleDataConfig.getConfig().getStringList("vehicle." + licensePlate + ".members");
        members.add(offlinePlayer.getUniqueId().toString());
        Main.vehicleDataConfig.getConfig().set("vehicle." + licensePlate + ".members", members);
        Main.vehicleDataConfig.save();

        Main.vehicleDataConfig.save();
        player.sendMessage(Main.messagesConfig.getMessage("memberChange"));

        return true;
    }
}