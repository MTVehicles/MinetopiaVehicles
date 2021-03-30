package nl.mtvehicles.core.Events;

import nl.mtvehicles.core.Infrastructure.Helpers.BossbarUtils;
import nl.mtvehicles.core.Infrastructure.Helpers.TextUtils;
import nl.mtvehicles.core.Infrastructure.Helpers.VehicleData;
import nl.mtvehicles.core.Infrastructure.Models.ConfigUtils;
import nl.mtvehicles.core.Infrastructure.Models.Vehicle;
import nl.mtvehicles.core.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class VehicleClickEvent implements Listener {
    private Map<String, Long> lastUsage = new HashMap<String, Long>();

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent e) {
        Entity a = e.getRightClicked();
        Player p = e.getPlayer();
        long lastUsed = 0L;
        if (a.getCustomName() == null) {
            return;
        }
        if (!a.getCustomName().contains("MTVEHICLES")) {
            return;
        }
        e.setCancelled(true);
        if (lastUsage.containsKey(p.getName())) {
            lastUsed = ((Long) lastUsage.get(p.getName())).longValue();
        }
        if (System.currentTimeMillis() - lastUsed >= 500) {
            lastUsage.put(p.getName(), Long.valueOf(System.currentTimeMillis()));
        } else {
            return;
        }
        String license = TextUtils.licenseReplacer(a.getCustomName());
        if (p.isSneaking()) {
            TextUtils.pickupVehicle(license, p);
            e.setCancelled(true);
            return;
        }
        //Main.configList.forEach(ConfigUtils::reload);
        if (a.getCustomName().contains("MTVEHICLES_SEAT")) {
            e.setCancelled(true);
            Vehicle vehicle = Vehicle.getByPlate(license);
            if (vehicle == null) {
                return;
            }
            if (vehicle.getOwner().equals(p.getUniqueId().toString()) || vehicle.canSit(p) || p.hasPermission("mtvehicles.ride")) {
                if (a.isEmpty()) {
                    a.setPassenger(p);
                    p.sendMessage(TextUtils.colorize(Main.messagesConfig.getMessage("vehicleEnterMember").replace("%p%", Bukkit.getOfflinePlayer(UUID.fromString(Vehicle.getByPlate(license).getOwner())).getName())));
                }
            } else {
                p.sendMessage(TextUtils.colorize(Main.messagesConfig.getMessage("vehicleNoRiderEnter").replace("%p%", Bukkit.getOfflinePlayer(UUID.fromString(Vehicle.getByPlate(license).getOwner())).getName())));
            }
            return;
        }
        TextUtils.createVehicle(license, p);
        e.setCancelled(true);

        return;
    }

}