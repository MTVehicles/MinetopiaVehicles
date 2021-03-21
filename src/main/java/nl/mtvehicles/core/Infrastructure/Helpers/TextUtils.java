package nl.mtvehicles.core.Infrastructure.Helpers;

import nl.mtvehicles.core.Events.VehicleClickEvent;
import nl.mtvehicles.core.Events.VehicleLeaveEvent;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class TextUtils {
    public static String colorize(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    public static String licenseReplacer(String license) {
        if (license.split("_").length > 1) {
            return license.split("_")[2];
        }
        return null;
    }

    public static void basicStandCreator(String license, String type, Location location, ItemStack item, Boolean gravity){
        ArmorStand as = location.getWorld().spawn(location, ArmorStand.class);
        as.setCustomName("MTVEHICLES_"+type+"_" + license);
        as.setHelmet(item);
        as.setGravity(gravity);
        as.setVisible(false);
        VehicleLeaveEvent.autostand.put("MTVEHICLES_"+type+"_" + license, as);
    }
    public static void mainSeatStandCreator(String license, Location location, Player p, double x, double y, double z){
        Location location2 = new Location(location.getWorld(), location.getX() + Double.valueOf(z), location.getY() + Double.valueOf(y), location.getZ() + Double.valueOf(z));
        ArmorStand as = location2.getWorld().spawn(location2, ArmorStand.class);
        as.setCustomName("MTVEHICLES_MAINSEAT_" + license);
        VehicleLeaveEvent.autostand.put("MTVEHICLES_MAINSEAT_" + license, as);
        as.setGravity(false);
        VehicleClickEvent.speed.put(license, 0.0);
        VehicleClickEvent.speedhigh.put(license, 0.0);
        VehicleClickEvent.mainx.put("MTVEHICLES_MAINSEAT_" + license, x);
        VehicleClickEvent.mainy.put("MTVEHICLES_MAINSEAT_" + license, y);
        VehicleClickEvent.mainz.put("MTVEHICLES_MAINSEAT_" + license, z);
        as.setPassenger(p);
        as.setVisible(false);
        VehicleLeaveEvent.autostand2.put(license, as);
    }
}
