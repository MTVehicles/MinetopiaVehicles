package nl.mtvehicles.core.listeners;

import nl.mtvehicles.core.events.VehicleEnterEvent;
import nl.mtvehicles.core.events.VehiclePickUpEvent;
import nl.mtvehicles.core.infrastructure.dataconfig.DefaultConfig;
import nl.mtvehicles.core.infrastructure.enums.Message;
import nl.mtvehicles.core.infrastructure.enums.RegionAction;
import nl.mtvehicles.core.infrastructure.utils.TextUtils;
import nl.mtvehicles.core.infrastructure.models.MTVListener;
import nl.mtvehicles.core.infrastructure.vehicle.Vehicle;
import nl.mtvehicles.core.infrastructure.vehicle.VehicleUtils;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

import java.util.HashMap;
import java.util.Map;

/**
 * On vehicle right click - entering and picking up
 */

public class VehicleClickListener extends MTVListener {
    private final Map<String, Long> lastUsage = new HashMap<>();

    private Entity entity;
    private String license;

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent event) {
        this.event = event;
        player = event.getPlayer();
        entity = event.getRightClicked();

        // Check if the clicked entity is a vehicle
        if (!VehicleUtils.isVehicle(entity)) return;
        event.setCancelled(true);

        if (entity.getCustomName().startsWith("VEHICLE")) return;

        final String playerName = player.getName();
        final long currentTime = System.currentTimeMillis();
        final long lastUsed = lastUsage.getOrDefault(playerName, 0L);

        if (currentTime - lastUsed < 500) return;
        lastUsage.put(playerName, currentTime);

        license = VehicleUtils.getLicensePlate(entity);

        if (player.isSneaking()) {
            pickup();
        } else {
            enter();
        }
    }

    private void pickup() {
        setAPI(new VehiclePickUpEvent());
        VehiclePickUpEvent api = (VehiclePickUpEvent) getAPI();
        api.setLicensePlate(license);
        callAPI();

        if (isCancelled()) return;

        license = api.getLicensePlate();
        final Vehicle vehicle = VehicleUtils.getVehicle(license);
        if (vehicle == null) return;

        final boolean disablePickupFromWater = (boolean) ConfigModule.defaultConfig.get(DefaultConfig.Option.DISABLE_PICKUP_FROM_WATER);
        if (!player.hasPermission("mtvehicles.anwb") && disablePickupFromWater) {
            if (entity.getLocation().clone().add(0, 1, 0).getBlock().isLiquid()) {
                ConfigModule.messagesConfig.sendMessage(player, Message.VEHICLE_IN_WATER);
                return;
            }
        }

        if (!ConfigModule.defaultConfig.canProceedWithAction(RegionAction.PICKUP, vehicle.getVehicleType(), entity.getLocation(), player)) {
            ConfigModule.messagesConfig.sendMessage(player, Message.CANNOT_DO_THAT_HERE);
            return;
        }

        VehicleUtils.pickupVehicle(license, player);
    }

    private void enter() {
        setAPI(new VehicleEnterEvent(license));
        callAPI();

        if (isCancelled()) return;

        final Vehicle vehicle = VehicleUtils.getVehicle(license);
        if (vehicle == null) return;

        final String customName = entity.getCustomName();
        if (customName.contains("MTVEHICLES_SEAT")) {

            if (!vehicle.isPublic() && !vehicle.isOwner(player) && !vehicle.canSit(player) && !player.hasPermission("mtvehicles.ride")) {
                player.sendMessage(TextUtils.colorize(ConfigModule.messagesConfig.getMessage(Message.VEHICLE_NO_RIDER_ENTER).replace("%p%", vehicle.getOwnerName())));
                return;
            }
            if (entity.isEmpty()) {
                entity.addPassenger(player);
                player.sendMessage(TextUtils.colorize(ConfigModule.messagesConfig.getMessage(Message.VEHICLE_ENTER_MEMBER).replace("%p%", vehicle.getOwnerName())));
            }

            return;
        }

        VehicleUtils.enterVehicle(license, player);
    }
}
