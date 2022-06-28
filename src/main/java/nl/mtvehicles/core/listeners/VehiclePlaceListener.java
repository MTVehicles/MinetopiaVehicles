package nl.mtvehicles.core.listeners;

import de.tr7zw.changeme.nbtapi.NBTItem;
import nl.mtvehicles.core.events.VehiclePlaceEvent;
import nl.mtvehicles.core.infrastructure.enums.Message;
import nl.mtvehicles.core.infrastructure.enums.RegionAction;
import nl.mtvehicles.core.infrastructure.utils.TextUtils;
import nl.mtvehicles.core.infrastructure.models.MTVListener;
import nl.mtvehicles.core.infrastructure.vehicle.Vehicle;
import nl.mtvehicles.core.infrastructure.vehicle.VehicleUtils;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

/**
 * On place of a vehicle
 */
public class VehiclePlaceListener extends MTVListener {

    public VehiclePlaceListener(){
        super(new VehiclePlaceEvent());
    }

    @EventHandler
    public void onVehiclePlace(final PlayerInteractEvent event) {
        this.event = event;
        player = event.getPlayer();

        final Action action = event.getAction();
        final ItemStack item = event.getItem();
        final Block clickedBlock = event.getClickedBlock();

        if (!action.equals(Action.RIGHT_CLICK_BLOCK)) return;
        if (item == null) return;
        if (!item.hasItemMeta()
                || !(new NBTItem(item)).hasKey("mtvehicles.kenteken")
                || clickedBlock == null
        ) return;
        String license = VehicleUtils.getLicensePlate(item);
        if (license == null) return;

        VehiclePlaceEvent api = (VehiclePlaceEvent) getAPI();
        api.setLicensePlate(license);
        api.setLocation(clickedBlock.getLocation());
        callAPI();
        if (isCancelled()) return;

        Location loc = api.getLocation();
        license = api.getLicensePlate();
        Vehicle vehicle = VehicleUtils.getVehicle(license);
        if (vehicle == null) return;

        if (event.getHand() != EquipmentSlot.HAND) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(TextUtils.colorize(ConfigModule.messagesConfig.getMessage(Message.WRONG_HAND)));
            return;
        }
        if (!VehicleUtils.existsByLicensePlate(license)) {
            ConfigModule.messagesConfig.sendMessage(player, Message.VEHICLE_NOT_FOUND);
            event.setCancelled(true);
            return;
        }

        event.setCancelled(true);

        if (ConfigModule.defaultConfig.isBlockWhitelistEnabled()
                && !ConfigModule.defaultConfig.blockWhiteList().contains(event.getClickedBlock().getType())) {
            ConfigModule.messagesConfig.sendMessage(player, Message.BLOCK_NOT_IN_WHITELIST);
            return;
        }
        if (!ConfigModule.defaultConfig.canProceedWithAction(RegionAction.PLACE, vehicle.getVehicleType(), loc, player)) {
            ConfigModule.messagesConfig.sendMessage(player, Message.CANNOT_DO_THAT_HERE);
            return;
        }

        if (VehicleUtils.getVehicle(license) == null) {
            ConfigModule.messagesConfig.sendMessage(player, Message.VEHICLE_NOT_FOUND);
            return;
        }

        Location location = new Location(loc.getWorld(), loc.getX(), loc.getY() + 1, loc.getZ());

        VehicleUtils.spawnVehicle(license, location);
        player.getInventory().remove(player.getEquipment().getItemInHand());
        player.sendMessage(TextUtils.colorize(ConfigModule.messagesConfig.getMessage(Message.VEHICLE_PLACE).replace("%p%", VehicleUtils.getVehicle(license).getOwnerName())));
    }
}
