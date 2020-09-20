package nl.mtvehicles.core.Events;

import nl.mtvehicles.core.Infrastructure.Helpers.NBTUtils;
import nl.mtvehicles.core.Infrastructure.Helpers.TextUtils;
import nl.mtvehicles.core.Infrastructure.Models.Config;
import nl.mtvehicles.core.Infrastructure.Models.Vehicle;
import nl.mtvehicles.core.Main;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;

public class VehiclePlaceEvent implements Listener {
    @EventHandler
    public void onVehiclePlace(final PlayerInteractEvent e) {

        final Player p = e.getPlayer();
        final Action action = e.getAction();
        final ItemStack item = e.getItem();
        if (e.getItem() == null || (!e.getItem().hasItemMeta() || !(NBTUtils.contains(item, "mtvehicles.kenteken")))) {
            return;
        }

        if (e.getHand() != EquipmentSlot.HAND) {
            e.setCancelled(true);
            e.getPlayer().sendMessage(TextUtils.colorize(Main.messagesConfig.getMessage("wrongHand")));
            return;
        }
        String ken = NBTUtils.getString(item, "mtvehicles.kenteken");
        if (action.equals(Action.RIGHT_CLICK_BLOCK)) {
            p.sendMessage("Ja");
            e.setCancelled(true);
            Main.configList.forEach(Config::reload);

            Location loc = e.getClickedBlock().getLocation();
            Location location = new Location(loc.getWorld(), loc.getX(), loc.getY() + 1, loc.getZ());
            ArmorStand as = location.getWorld().spawn(location, ArmorStand.class);
            as.setVisible(false);

            as.setCustomName("MTVEHICLES_SKIN_" + ken);
            as.setHelmet(item);
            ArmorStand as2 = location.getWorld().spawn(location, ArmorStand.class);
            as2.setVisible(false);
            as2.setCustomName("MTVEHICLES_MAIN_" + ken);
            Vehicle vehicle = Vehicle.getByPlate(ken);
            List<Map<String, Double>> seats = (List<Map<String, Double>>) vehicle.getVehicleData().get("seats");
            p.getInventory().getItemInMainHand().setAmount(0);
            for (int i = 1; i <= seats.size(); i++) {
                Map<String, Double> seat = seats.get(i - 1);
                if (i == 1) {
                    Location location2 = new Location(location.getWorld(), location.getX() + seat.get("x"), location.getY() + seat.get("y"), location.getZ() + seat.get("z"));
                    ArmorStand as3 = location2.getWorld().spawn(location2, ArmorStand.class);
                    as3.setCustomName("MTVEHICLES_MAINSEAT_" + ken);
                    as3.setGravity(false);
                    as3.setVisible(false);
                }

            }
        }

    }
}
