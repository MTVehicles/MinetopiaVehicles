package nl.mtvehicles.core.listeners;

import nl.mtvehicles.core.events.VehicleClickEvent;
import nl.mtvehicles.core.infrastructure.dataconfig.VehicleDataConfig;
import nl.mtvehicles.core.infrastructure.enums.Message;
import nl.mtvehicles.core.infrastructure.enums.RegionAction;
import nl.mtvehicles.core.infrastructure.helpers.TextUtils;
import nl.mtvehicles.core.infrastructure.models.MTVListener;
import nl.mtvehicles.core.infrastructure.models.Vehicle;
import nl.mtvehicles.core.infrastructure.models.VehicleUtils;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

import java.util.HashMap;
import java.util.Map;

public class VehicleClickListener extends MTVListener {
    private Map<String, Long> lastUsage = new HashMap<>();

    public VehicleClickListener(){
        super(new VehicleClickEvent());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent event) {
        this.event = event;
        player = event.getPlayer();
        final Entity entity = event.getRightClicked();
        long lastUsed = 0L;

        if (!VehicleUtils.isVehicle(entity)) return;
        if (entity.getCustomName().startsWith("VEHICLE")) return;

        if (lastUsage.containsKey(player.getName())) lastUsed = (lastUsage.get(player.getName())).longValue();
        if (System.currentTimeMillis() - lastUsed >= 500) lastUsage.put(player.getName(), Long.valueOf(System.currentTimeMillis()));
        else return;

        String license = VehicleUtils.getLicensePlate(entity);

        VehicleClickEvent api = (VehicleClickEvent) getAPI();
        api.setLicensePlate(license);
        callAPI();
        if (isCancelled()) return;

        license = api.getLicensePlate();

        event.setCancelled(true);

        if (player.isSneaking()) {

            if (!ConfigModule.defaultConfig.canProceedWithAction(RegionAction.PICKUP, event.getRightClicked().getLocation())){
                ConfigModule.messagesConfig.sendMessage(player, Message.CANNOT_DO_THAT_HERE);
                return;
            }

            TextUtils.pickupVehicle(license, player);
            return;
        }

        if (entity.getCustomName().contains("MTVEHICLES_SEAT")) {
            Vehicle vehicle = VehicleUtils.getByLicensePlate(license);
            if (vehicle == null) return;

            if ((boolean) ConfigModule.vehicleDataConfig.get(license, VehicleDataConfig.Option.IS_OPEN) || vehicle.isOwner(player) || vehicle.canSit(player) || player.hasPermission("mtvehicles.ride")) {
                if (entity.isEmpty()) {
                    entity.addPassenger(player);
                    player.sendMessage(TextUtils.colorize(ConfigModule.messagesConfig.getMessage(Message.VEHICLE_ENTER_MEMBER).replace("%p%", VehicleUtils.getByLicensePlate(license).getOwnerName())));
                }
            } else {
                player.sendMessage(TextUtils.colorize(ConfigModule.messagesConfig.getMessage(Message.VEHICLE_NO_RIDER_ENTER).replace("%p%", VehicleUtils.getByLicensePlate(license).getOwnerName())));
            }
            return;
        }
        TextUtils.createVehicle(license, player);
    }
}
