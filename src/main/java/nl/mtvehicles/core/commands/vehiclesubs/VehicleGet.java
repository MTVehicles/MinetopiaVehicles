package nl.mtvehicles.core.commands.vehiclesubs;

import nl.mtvehicles.core.infrastructure.models.MTVehicleSubCommand;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.List;

public class VehicleGet extends MTVehicleSubCommand {
    public VehicleGet() {
        this.setPlayerCommand(true);
    }

    @Override
    public boolean execute(CommandSender sender, Command cmd, String s, String[] args) {
        Player p = (Player) sender;

        if (args.length == 2) {
            Inventory inv = Bukkit.createInventory(null, 54, "Super vette koele auto parts man broer");
            for (Entity entity : p.getWorld().getEntities()) {
                if (entity instanceof ArmorStand) {
                    ArmorStand as = (ArmorStand) entity;
                    if (as.getCustomName() != null) {
                        if (as.getCustomName().contains("VEHICLE")) {
                            if (as.getHelmet() != null) {
                                if (as.getHelmet().hasItemMeta()) {
                                    if (as.getHelmet().getItemMeta().hasDisplayName()) {
                                        if (as.getHelmet().getItemMeta().getDisplayName().toLowerCase().contains(args[1].toLowerCase())) {
                                            Bukkit.broadcastMessage("" + as.getHelmet().getItemMeta().getDisplayName());
                                            inv.addItem(as.getHelmet());
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            Bukkit.broadcastMessage("-- end --");
            p.openInventory(inv);
            return true;
        }

        if (args.length == 1) {

            Location EntityArea = new Location(p.getWorld(), p.getLocation().getX(), p.getLocation().getY(), p.getLocation().getZ());
            List<Entity> nearbyEntities = (List<Entity>) EntityArea.getWorld().getNearbyEntities(EntityArea, 1, 1, 1);
            Inventory inv = Bukkit.createInventory(null, 54, "Super vette koele auto parts man broer");
            for (Entity entity : p.getWorld().getEntities()) {
                if (nearbyEntities.contains(entity)) {
                    if (entity instanceof ArmorStand) {
                        ArmorStand as = (ArmorStand) entity;
                        if (as.getCustomName().contains("VEHICLE")) {
                            if (as.getHelmet() != null) {
                                if (as.getHelmet().hasItemMeta()) {
                                    if (as.getHelmet().getItemMeta().hasDisplayName()) {
                                        Bukkit.broadcastMessage("" + as.getHelmet().getItemMeta().getDisplayName());
                                    }
                                }
                                inv.addItem(as.getHelmet());
                            }
                        }
                    }
                }
            }
            Bukkit.broadcastMessage("-- end --");
            p.openInventory(inv);
            return true;
        }
        return true;
    }
}
