package nl.mtvehicles.core.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class LeaveEvent implements Listener {
    @EventHandler(priority = EventPriority.HIGH)
    public void onLeaveEventPlayer(PlayerQuitEvent e) {
        final Player p = e.getPlayer();
        if (p.isInsideVehicle())
            p.leaveVehicle();
    }
}
