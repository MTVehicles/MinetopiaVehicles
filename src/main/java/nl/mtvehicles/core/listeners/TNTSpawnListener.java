package nl.mtvehicles.core.listeners;

import nl.mtvehicles.core.infrastructure.dataconfig.DefaultConfig;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import nl.mtvehicles.core.infrastructure.vehicle.VehicleData;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TNTSpawnListener implements Listener {


    final int COOLDOWN_TIME = (int) ConfigModule.defaultConfig.get(DefaultConfig.Option.AIRPLANE_COOLDOWN) * 1000;
    private final Map<UUID, Long> cooldownMap = new HashMap<>();

    @EventHandler
    public void onPlayerClick(PlayerInteractEvent event) {

        Player player = event.getPlayer();
        ItemStack mainHandItem = player.getInventory().getItemInMainHand();

        if((boolean) ConfigModule.defaultConfig.get(DefaultConfig.Option.AIRPLANE_TNT)) {
            if (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                if (mainHandItem.getType() == Material.TNT) {
                    if (player.getVehicle() == null) return;
                    final Entity vehicle = player.getVehicle();
                    if (!(vehicle instanceof ArmorStand)) return;
                    if (vehicle.getCustomName() == null) return;
                    if (vehicle.getCustomName().replace("MTVEHICLES_MAINSEAT_", "").isEmpty()) return; //Not sure what this line is supposed to be doing here but I'm keeping it, just in case
                    String license = vehicle.getCustomName().replace("MTVEHICLES_MAINSEAT_", "");
                    if (VehicleData.type.get(license).isAirplane()) {
                        long currentTime = System.currentTimeMillis();
                        long lastTime = cooldownMap.getOrDefault(player.getUniqueId(), 0L);
                        if (currentTime - lastTime >= COOLDOWN_TIME) {
                            spawnFallingTNT(player);
                            cooldownMap.put(player.getUniqueId(), currentTime);
                        }
                    }
                }
            }
        }
    }
    private void spawnFallingTNT(Player player) {
        Location playerLocation = player.getLocation();
        playerLocation.setY(playerLocation.getY() - 1);
        player.getWorld().spawn(playerLocation, TNTPrimed.class);
        ItemStack mainHandItem = player.getInventory().getItemInMainHand();
        mainHandItem.setAmount(mainHandItem.getAmount()  - 1);
    }

}
