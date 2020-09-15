package nl.mtvehicles.core.Events;

import nl.mtvehicles.core.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.Map;

public class MenuClickEvent implements Listener {
    @EventHandler
    public void onClick(InventoryClickEvent e) {

        if(e.getView().getTitle().contains("Vehicle Menu")){
            e.setCancelled(true);
            Player p = (Player) e.getWhoClicked();
            p.sendMessage("vte");
            for (Map<?, ?> vehicle : Main.vehiclesConfig.getConfig().getMapList("voertuigen")) {
                //System.out.println(vehicle.get("item"));
//                for (Map<?, ?> cars : vehicle.get("cars")) {
//
//                    System.out.println(cars.get("item"));
//
//                }

            }

            //System.out.println(cars.get("item"));
        }
    }
}
