package nl.mtvehicles.core.Events;

import nl.mtvehicles.core.Main;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.PluginDescriptionFile;

public class LeaveEvent implements Listener {
    @EventHandler(priority = EventPriority.HIGH)
    public void onLeaveEventPlayer(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        if (p.isInsideVehicle()) {
            p.getVehicle().removePassenger(p);
        }
    }
}
