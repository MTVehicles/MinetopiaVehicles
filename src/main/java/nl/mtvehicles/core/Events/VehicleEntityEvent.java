package nl.mtvehicles.core.Events;

import nl.mtvehicles.core.Infrastructure.Helpers.ItemFactory;
import nl.mtvehicles.core.Infrastructure.Helpers.TextUtils;
import nl.mtvehicles.core.Infrastructure.Helpers.VehiclesUtils;
import nl.mtvehicles.core.Infrastructure.Models.ConfigUtils;
import nl.mtvehicles.core.Infrastructure.Models.Vehicle;
import nl.mtvehicles.core.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class VehicleEntityEvent implements Listener {
    public static HashMap<String, Double> speed = new HashMap<>();

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerInteractAtEntity(EntityDamageByEntityEvent event) {
        final Entity a = event.getEntity();
        final Player p = (Player) event.getDamager();
        if (a.getCustomName() == null) {
            return;
        }


        if (p.isSneaking()) {
            if (a.getCustomName().contains("MTVEHICLES_MAINSEAT_")) {
                kofferbak(p, a.getCustomName().replace("MTVEHICLES_MAINSEAT_", ""));
                event.setCancelled(true);
            }
            if (a.getCustomName().contains("MTVEHICLES_MAIN_")) {
                kofferbak(p, a.getCustomName().replace("MTVEHICLES_MAIN_", ""));
                event.setCancelled(true);
            }
            if (a.getCustomName().contains("MTVEHICLES_SKIN_")) {
                kofferbak(p, a.getCustomName().replace("MTVEHICLES_SKIN_", ""));
                event.setCancelled(true);
            }
            return;
        }
    }

    public static void kofferbak(Player p, String ken) {
        if (Vehicle.getByPlate(ken) == null) {
            p.sendMessage(TextUtils.colorize(Main.messagesConfig.getMessage("vehicleNotFound")));
            return;
        }
        if (Vehicle.getByPlate(ken).getOwner().equals(p.getUniqueId().toString()) || Vehicle.canRide(p, ken) == true || p.hasPermission("mtvehicles.kofferbak")) {
            Main.configList.forEach(ConfigUtils::reload);
            if (Main.vehicleDataConfig.getConfig().getBoolean("vehicle." + ken + ".kofferbak") == true) {
                if (Main.vehicleDataConfig.getConfig().getList("vehicle." + ken + ".kofferbakData") == null) {
                    return;
                }
                Inventory inv = Bukkit.createInventory(null, Main.vehicleDataConfig.getConfig().getInt("vehicle." + ken + ".kofferbakRows") * 9, "Kofferbak Vehicle: "+ken);
                List<ItemStack> chestContentsFromConfig = (List<ItemStack>) Main.vehicleDataConfig.getConfig().getList("vehicle." + ken + ".kofferbakData");
                for (ItemStack item : chestContentsFromConfig) {
                    if (item == null) {
                        continue;
                    }
                    inv.addItem(item);
                }
                p.openInventory(inv);

            } else {
            }

        } else {
            p.sendMessage(TextUtils.colorize(Main.messagesConfig.getMessage("vehicleNoRiderKofferbak").replace("%p%", Bukkit.getOfflinePlayer(UUID.fromString(Vehicle.getByPlate(ken).getOwner().toString())).getName())));

        }

    }


    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getView().getTitle().contains("Kofferbak Vehicle: ")) {
            String ken = event.getView().getTitle().replace("Kofferbak Vehicle: ", "");
            List<ItemStack> chest = (List<ItemStack>) Main.vehicleDataConfig.getConfig().getList("vehicle." + ken + ".kofferbakData");
            chest.removeAll(chest);
            for (ItemStack item : event.getInventory().getContents()) {
                chest.add(item);
                Main.vehicleDataConfig.getConfig().set("vehicle." + ken + ".kofferbakData", chest);
                Main.vehicleDataConfig.save();
            }
        }
    }
}