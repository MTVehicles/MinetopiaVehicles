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
    private Map<String, Long> lastUsage = new HashMap<>();

    private Entity entity;
    private String license;

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent event) {
        this.event = event;
        player = event.getPlayer();
        entity = event.getRightClicked();
        long lastUsed = 0L;

        if (!VehicleUtils.isVehicle(entity)) return;
        event.setCancelled(true); //Prevents skin item from getting grabbed by a player

        if (entity.getCustomName().startsWith("VEHICLE")) return;

        if (lastUsage.containsKey(player.getName())) lastUsed = (lastUsage.get(player.getName())).longValue();
        if (System.currentTimeMillis() - lastUsed >= 500) lastUsage.put(player.getName(), Long.valueOf(System.currentTimeMillis()));
        else return;

        license = VehicleUtils.getLicensePlate(entity);

        if (player.isSneaking()) {
            pickup();
            return;
        }
        enter();
    }

    private void pickup(){
        this.setAPI(new VehiclePickUpEvent());
        VehiclePickUpEvent api = (VehiclePickUpEvent) getAPI();
        api.setLicensePlate(license);
        callAPI();
        if (isCancelled()) return;

        license = api.getLicensePlate();
        Vehicle vehicle = VehicleUtils.getVehicle(license);
        if (vehicle == null) return;

        if (!((boolean) ConfigModule.defaultConfig.get(DefaultConfig.Option.CAR_PICKUP)) && !player.hasPermission("mtvehicles.oppakken")) {
            ConfigModule.messagesConfig.sendMessage(player, Message.CANNOT_DO_THAT_HERE);
            return;
        }

        if (!vehicle.isOwner(player) && !player.hasPermission("mtvehicles.oppakken")){
            ConfigModule.messagesConfig.sendMessage(player, Message.VEHICLE_NO_OWNER_PICKUP, "%p%", vehicle.getOwnerName());
            return;
        }

        if (!player.hasPermission("mtvehicles.anwb") && (boolean) ConfigModule.defaultConfig.get(DefaultConfig.Option.DISABLE_PICKUP_FROM_WATER)){
            if (entity.getLocation().clone().add(0, 1, 0).getBlock().isLiquid()) {
                ConfigModule.messagesConfig.sendMessage(player, Message.VEHICLE_IN_WATER);
                return;
            }
        }

        if (!ConfigModule.defaultConfig.canProceedWithAction(RegionAction.PICKUP, vehicle.getVehicleType(), ((PlayerInteractAtEntityEvent) event).getRightClicked().getLocation(), player)){
            ConfigModule.messagesConfig.sendMessage(player, Message.CANNOT_DO_THAT_HERE);
            return;
        }

        VehicleUtils.pickupVehicle(vehicle, player);
    }

    private void enter(){
        this.setAPI(new VehicleEnterEvent(license));
        callAPI();
        if (isCancelled()) return;

        Vehicle vehicle = VehicleUtils.getVehicle(license);
        if (vehicle == null) return;

        // if clicked armor stand is a passenger seat
        if (entity.getCustomName().contains("MTVEHICLES_SEAT")) {

            if (!vehicle.isPublic() && !vehicle.isOwner(player) && !vehicle.canSit(player) && !player.hasPermission("mtvehicles.ride")) {
                ConfigModule.messagesConfig.sendMessage(player, Message.VEHICLE_NO_RIDER_ENTER, "%p%", vehicle.getOwnerName());
                return;
            }

            if (entity.isEmpty()) {
                entity.addPassenger(player);
                ConfigModule.messagesConfig.sendMessage(player, Message.VEHICLE_ENTER_MEMBER, "%p%", vehicle.getOwnerName());
            }

            return;
        }

        VehicleUtils.enterVehicle(vehicle, player);
    }

}
