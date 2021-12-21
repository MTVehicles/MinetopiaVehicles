package nl.mtvehicles.core.infrastructure.helpers;

import nl.mtvehicles.core.Main;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class BossBarUtils {
    public static BossBar Benzine;
    public static HashMap<String, BossBar> Fuelbar = new HashMap<>();

    public static void setBossBarValue(double counter, String ken) {
        if (ConfigModule.defaultConfig.getConfig().getBoolean("benzine") && ConfigModule.vehicleDataConfig.getConfig().getBoolean("vehicle." + ken + ".benzineEnabled")) {
            Fuelbar.get(ken).setProgress(counter);
            Fuelbar.get(ken).setTitle(Math.round(counter * 100.0D) + "% " + TextUtils.colorize(ConfigModule.messagesConfig.getMessage("bossbarFuel")));

            Double fuel = VehicleData.fuel.get(ken);

            if (fuel < 30) {
                Fuelbar.get(ken).setColor(BarColor.RED);
                return;
            }
            if (fuel < 60) {
                Fuelbar.get(ken).setColor(BarColor.YELLOW);
                return;
            }
            if (fuel < 100) {
                Fuelbar.get(ken).setColor(BarColor.GREEN);
            }
        }
    }

    public static void removeBossBar(Player player, String ken) {
        if (ConfigModule.defaultConfig.getConfig().getBoolean("benzine") && ConfigModule.vehicleDataConfig.getConfig().getBoolean("vehicle." + ken + ".benzineEnabled")) {
            Fuelbar.get(ken).removePlayer(player);
        }
    }

    public static void addBossBar(Player player, String ken) {
        if (ConfigModule.defaultConfig.getConfig().getBoolean("benzine") && ConfigModule.vehicleDataConfig.getConfig().getBoolean("vehicle." + ken + ".benzineEnabled")) {
            double fuel = ConfigModule.vehicleDataConfig.getConfig().getDouble(String.format("vehicle.%s.benzine", player.getVehicle().getCustomName().replace("MTVEHICLES_MAINSEAT_", "")));
            String fuelString = String.valueOf(fuel);
            BossBar bar = Bukkit.createBossBar(Math.round(Double.parseDouble(fuelString)) + "% " + TextUtils.colorize(ConfigModule.messagesConfig.getMessage("bossbarFuel")), BarColor.GREEN, BarStyle.SOLID);
            Fuelbar.put(ken, bar);
            if (fuel < 30) {
                Fuelbar.get(ken).setColor(BarColor.RED);
            }
            if (fuel < 60) {
                Fuelbar.get(ken).setColor(BarColor.YELLOW);
            }
            if (fuel < 100) {
                Fuelbar.get(ken).setColor(BarColor.GREEN);
            }
            Fuelbar.get(ken).addPlayer(player);
        }
    }
}
