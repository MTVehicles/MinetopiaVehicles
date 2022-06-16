package nl.mtvehicles.core.infrastructure.helpers;

import nl.mtvehicles.core.infrastructure.dataconfig.DefaultConfig;
import nl.mtvehicles.core.infrastructure.dataconfig.VehicleDataConfig;
import nl.mtvehicles.core.infrastructure.enums.Message;
import nl.mtvehicles.core.infrastructure.enums.RegionAction;
import nl.mtvehicles.core.infrastructure.enums.VehicleType;
import nl.mtvehicles.core.infrastructure.vehicle.Vehicle;
import nl.mtvehicles.core.infrastructure.vehicle.VehicleUtils;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Methods for editing text (and for some reason also deprecated methods for creating vehicles - moved to {@link VehicleUtils} and {@link nl.mtvehicles.core.listeners.VehicleClickListener})
 */
public class TextUtils {
    /**
     * Colorize a String with the ampersand characters.
     * @param text Text
     * @return Colorized text
     */
    public static String colorize(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    /**
     * Get license plate from vehicle armor stand's name
     * @param license Name of the vehicle
     * @return Vehicle's license plate
     * @deprecated Use {@link VehicleUtils#getLicensePlate(Entity)} instead.
     */
    @Deprecated
    public static String licenseReplacer(String license) {
        if (license.split("_").length > 1) {
            return license.split("_")[2];
        }
        return null;
    }

    /**
     * Get a List from multiple Strings
     */
    public static List<String> list(String... strings){
        return Arrays.asList(strings);
    }

    /**
     * Check whether player's inventory is full
     * @param player Player
     * @return True if player's inventory is full
     */
    public static boolean checkInvFull(Player player) {
        return !Arrays.asList(player.getInventory().getStorageContents()).contains(null);
    }
}
