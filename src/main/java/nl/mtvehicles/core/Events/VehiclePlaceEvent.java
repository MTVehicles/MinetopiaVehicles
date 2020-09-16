package nl.mtvehicles.core.Events;

import nl.mtvehicles.core.Infrastructure.Helpers.NBTUtils;
import nl.mtvehicles.core.Infrastructure.Helpers.TextUtils;
import nl.mtvehicles.core.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class VehiclePlaceEvent implements Listener {
    @EventHandler
    public void onVehiclePlace(final PlayerInteractEvent e) {

        final Player p = e.getPlayer();
        final Action action = e.getAction();
        final ItemStack item = e.getItem();
        if (e.getItem() == null||(!e.getItem().hasItemMeta() || !(NBTUtils.contains(item, "mtvehicles.kenteken")))) {
            return;
        }

        if (e.getHand() != EquipmentSlot.HAND) {
            e.setCancelled(true);
            e.getPlayer().sendMessage(TextUtils.colorize(Main.messagesConfig.getMessage("wrongHand")));
            return;
        }

        if (action.equals(Action.RIGHT_CLICK_BLOCK)) {
            p.sendMessage("Ja");
            e.setCancelled(true);
        }
    }
}
