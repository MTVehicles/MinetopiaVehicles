package nl.mtvehicles.core.infrastructure.utils;

import nl.mtvehicles.core.infrastructure.dataconfig.DefaultConfig;
import nl.mtvehicles.core.infrastructure.dataconfig.VehicleDataConfig;
import nl.mtvehicles.core.infrastructure.enums.Message;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import nl.mtvehicles.core.infrastructure.vehicle.VehicleData;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import java.util.HashMap;

/**
 * Methods for BossBars
 */
public class BossBarUtils {
    /**
     * Fuel bossbar
     */
    public static HashMap<String, BossBar> Fuelbar = new HashMap<>();

    /**
     * Set bossbar's fuel amount
     * @param counter Fuel amount
     * @param licensePlate Vehicle's license plate (this vehicle's fuel is displayed)
     */
    public static void setBossBarValue(double counter, String licensePlate) {
        if ((boolean) ConfigModule.defaultConfig.get(DefaultConfig.Option.FUEL_ENABLED) && (boolean) ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.FUEL_ENABLED)) {
            Fuelbar.get(licensePlate).setProgress(counter);
            Fuelbar.get(licensePlate).setTitle(Math.round(counter * 100.0D) + "% " + TextUtils.colorize(ConfigModule.messagesConfig.getMessage(Message.BOSSBAR_FUEL)));

            Double fuel = VehicleData.fuel.get(licensePlate);

            if (fuel < 30) {
                Fuelbar.get(licensePlate).setColor(BarColor.RED);
                return;
            }
            if (fuel < 60) {
                Fuelbar.get(licensePlate).setColor(BarColor.YELLOW);
                return;
            }
            if (fuel < 100) {
                Fuelbar.get(licensePlate).setColor(BarColor.GREEN);
            }
        }
    }

    /**
     * Remove fuel bossbar from player
     * @param player Player
     * @param licensePlate Vehicle's license plate (this vehicle's fuel is displayed)
     */
    public static void removeBossBar(Player player, String licensePlate) {
        if ((boolean) ConfigModule.defaultConfig.get(DefaultConfig.Option.FUEL_ENABLED) && (boolean) ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.FUEL_ENABLED)) {
            Fuelbar.get(licensePlate).removePlayer(player);
        }
    }

    /**
     * Show fuel bossbar for a player
     * @param player Player
     * @param licensePlate Vehicle's license plate (this vehicle's fuel is displayed)
     */
    public static void addBossBar(Player player, String licensePlate) {
        if ((boolean) ConfigModule.defaultConfig.get(DefaultConfig.Option.FUEL_ENABLED) && (boolean) ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.FUEL_ENABLED)) {
            double fuel = (double) ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.FUEL);
            String fuelString = String.valueOf(fuel);
            BossBar bar = Bukkit.createBossBar(Math.round(Double.parseDouble(fuelString)) + "% " + TextUtils.colorize(ConfigModule.messagesConfig.getMessage(Message.BOSSBAR_FUEL)), BarColor.GREEN, BarStyle.SOLID);
            Fuelbar.put(licensePlate, bar);
            if (fuel < 30) {
                Fuelbar.get(licensePlate).setColor(BarColor.RED);
            }
            if (fuel < 60) {
                Fuelbar.get(licensePlate).setColor(BarColor.YELLOW);
            }
            if (fuel < 100) {
                Fuelbar.get(licensePlate).setColor(BarColor.GREEN);
            }
            Fuelbar.get(licensePlate).addPlayer(player);
        }
    }
}
