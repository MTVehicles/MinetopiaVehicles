package nl.mtvehicles.core.listeners;

import de.tr7zw.changeme.nbtapi.NBTItem;
import nl.mtvehicles.core.events.VehiclePlaceEvent;
import nl.mtvehicles.core.infrastructure.enums.Message;
import nl.mtvehicles.core.infrastructure.enums.RegionAction;
import nl.mtvehicles.core.infrastructure.models.MTVListener;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import nl.mtvehicles.core.infrastructure.utils.TextUtils;
import nl.mtvehicles.core.infrastructure.vehicle.Vehicle;
import nl.mtvehicles.core.infrastructure.vehicle.VehicleUtils;
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
                || clickedBlock == null
        ) return;
        if (!(new NBTItem(item)).hasTag("mtvehicles.kenteken")) return;
        String license = VehicleUtils.getLicensePlate(item);
        if (license == null) return;

        VehiclePlaceEvent api = (VehiclePlaceEvent) getAPI();
        if (ConfigModule.vehicleDataConfig.getType(license).isBoat()){ //placing boats (on top of a water body)
            Location spawnLoc = clickedBlock.getLocation();
            for (int i = 0; i < 512; i++) {
                Location locAbove = new Location(spawnLoc.getWorld(), spawnLoc.getX(), spawnLoc.getY() + 1, spawnLoc.getZ());
                if (!locAbove.getBlock().toString().contains("WATER")) break;
                spawnLoc = locAbove;
            }
            api.setLocation(spawnLoc);
        } else {
            api.setLocation(clickedBlock.getLocation());
        }
        api.setLicensePlate(license);
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

        //prevent vehicles from being placed inside walls
        if (!VehicleUtils.isPassable(event.getClickedBlock().getLocation().add(0, 1, 0).getBlock())){
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
