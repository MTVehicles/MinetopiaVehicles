package nl.mtvehicles.core.listeners;

import nl.mtvehicles.core.infrastructure.models.MTVListener;
import org.bukkit.ChatColor;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.SignChangeEvent;

public class SignChangeListener extends MTVListener {
    @EventHandler
    public void onSignChange(SignChangeEvent event) {
        if (!event.getBlock().getType().toString().contains("WALL_SIGN")) {
            return;
        }
        Sign sign = (Sign) event.getBlock().getState();
        if(!sign.getLine(0).equalsIgnoreCase("[Refuel]")){
            return;
        }
        sign.setLine(0, ChatColor.GREEN + sign.getLine(0).toString());
        sign.setLine(0, ChatColor.RED + sign.getLine(2).toString());
        sign.update();
    }
}
