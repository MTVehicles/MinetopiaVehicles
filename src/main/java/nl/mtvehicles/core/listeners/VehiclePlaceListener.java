package nl.mtvehicles.core.listeners;

import de.tr7zw.changeme.nbtapi.NBTItem;
import nl.mtvehicles.core.events.VehiclePlaceEvent;
import nl.mtvehicles.core.infrastructure.dataconfig.DefaultConfig;
import nl.mtvehicles.core.infrastructure.enums.Message;
import nl.mtvehicles.core.infrastructure.enums.RegionAction;
import nl.mtvehicles.core.infrastructure.helpers.ItemFactory;
import nl.mtvehicles.core.infrastructure.helpers.TextUtils;
import nl.mtvehicles.core.infrastructure.models.Vehicle;
import nl.mtvehicles.core.infrastructure.models.VehicleUtils;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import nl.mtvehicles.core.infrastructure.modules.VersionModule;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class VehiclePlaceListener implements Listener {
    @EventHandler
    public void onVehiclePlace(final PlayerInteractEvent e) {
        final Player p = e.getPlayer();
        final Action action = e.getAction();
        final ItemStack item = e.getItem();

        if (e.isCancelled()) return;
        if (!VersionModule.getServerVersion().isOld()){
            if (((org.bukkit.event.Cancellable) e).isCancelled()) return;
        }

        if (e.getItem() == null) return;

        if (!e.getItem().hasItemMeta()
                || !(new NBTItem(e.getItem())).hasKey("mtvehicles.kenteken")
                || e.getClickedBlock() == null
        ) return;

        if (!action.equals(Action.RIGHT_CLICK_BLOCK)) return;

        if (e.getHand() != EquipmentSlot.HAND) {
            e.setCancelled(true);
            e.getPlayer().sendMessage(TextUtils.colorize(ConfigModule.messagesConfig.getMessage(Message.WRONG_HAND)));
            return;
        }

        String license = VehicleUtils.getLicensePlate(item);
        if (license == null) return;

        if (!VehicleUtils.existsByLicensePlate(license)) {
            ConfigModule.messagesConfig.sendMessage(p, Message.VEHICLE_NOT_FOUND);
            e.setCancelled(true);
            return;
        }


        VehiclePlaceEvent vehiclePlaceEvent = new VehiclePlaceEvent();
        vehiclePlaceEvent.setLocation(e.getClickedBlock().getLocation());
        vehiclePlaceEvent.setPlayer(e.getPlayer());
        // You can set more things take a look at VehiclePlaceEvent
        Bukkit.getPluginManager().callEvent(vehiclePlaceEvent);

        if (vehiclePlaceEvent.isCancelled()) return;


        Location loc = e.getClickedBlock().getLocation();
        e.setCancelled(true);

        if (ConfigModule.defaultConfig.isBlockWhitelistEnabled()
                && !ConfigModule.defaultConfig.blockWhiteList().contains(e.getClickedBlock().getType())) {
            ConfigModule.messagesConfig.sendMessage(p, Message.BLOCK_NOT_IN_WHITELIST);
            return;
        }
        if (!ConfigModule.defaultConfig.canProceedWithAction(RegionAction.PLACE, loc)) {
            ConfigModule.messagesConfig.sendMessage(p, Message.CANNOT_DO_THAT_HERE);
            return;
        }

        if (VehicleUtils.getByLicensePlate(license) == null) {
            ConfigModule.messagesConfig.sendMessage(p, Message.VEHICLE_NOT_FOUND);
            return;
        }

        Location location = new Location(loc.getWorld(), loc.getX(), loc.getY() + 1, loc.getZ());

        VehicleUtils.spawnVehicle(license, location);
        p.getInventory().remove(p.getEquipment().getItemInHand());
        p.sendMessage(TextUtils.colorize(ConfigModule.messagesConfig.getMessage(Message.VEHICLE_PLACE).replace("%p%", VehicleUtils.getByLicensePlate(license).getOwnerName())));
    }
}
