package nl.mtvehicles.core.events;

import nl.mtvehicles.core.infrastructure.enums.RegionAction;
import nl.mtvehicles.core.infrastructure.helpers.TextUtils;
import nl.mtvehicles.core.infrastructure.models.Vehicle;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class VehicleClickEvent implements Listener {
    private Map<String, Long> lastUsage = new HashMap<>();

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent e) {
        final Entity vehicle = e.getRightClicked();
        final Player p = e.getPlayer();
        long lastUsed = 0L;

        if (vehicle.getCustomName() == null) return;

        if (!vehicle.getCustomName().contains("MTVEHICLES")) return;

        e.setCancelled(true);

        if (vehicle.getCustomName().startsWith("VEHICLE")) return;

        if (lastUsage.containsKey(p.getName()))
            lastUsed = ((Long) lastUsage.get(p.getName())).longValue();

        if (System.currentTimeMillis() - lastUsed >= 500) lastUsage.put(p.getName(), Long.valueOf(System.currentTimeMillis()));
        else return;

        String license = TextUtils.licenseReplacer(vehicle.getCustomName());

        if (p.isSneaking()) {

            if (!ConfigModule.defaultConfig.canProceedWithAction(RegionAction.PICKUP, e.getRightClicked().getLocation())){
                ConfigModule.messagesConfig.sendMessage(p, "cannotDoThatHere");
                return;
            }

            TextUtils.pickupVehicle(license, p);
            return;
        }

        if (vehicle.getCustomName().contains("MTVEHICLES_SEAT")) {
            Vehicle vehicle = Vehicle.getByPlate(license);
            if (vehicle == null) return;

            if (ConfigModule.vehicleDataConfig.getConfig().getBoolean("vehicle."+license+".isOpen") || vehicle.getOwner().equals(p.getUniqueId().toString()) || vehicle.canSit(p) || p.hasPermission("mtvehicles.ride")) {
                if (vehicle.isEmpty()) {
                    vehicle.addPassenger(p);
                    p.sendMessage(TextUtils.colorize(ConfigModule.messagesConfig.getMessage("vehicleEnterMember").replace("%p%", Bukkit.getOfflinePlayer(UUID.fromString(Vehicle.getByPlate(license).getOwner())).getName())));
                }
            } else {
                p.sendMessage(TextUtils.colorize(ConfigModule.messagesConfig.getMessage("vehicleNoRiderEnter").replace("%p%", Bukkit.getOfflinePlayer(UUID.fromString(Vehicle.getByPlate(license).getOwner())).getName())));
            }
            return;
        }
        TextUtils.createVehicle(license, p);
    }
}