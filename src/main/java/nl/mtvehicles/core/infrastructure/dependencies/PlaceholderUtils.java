package nl.mtvehicles.core.infrastructure.dependencies;

import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import nl.mtvehicles.core.Main;
import nl.mtvehicles.core.infrastructure.annotations.ToDo;
import nl.mtvehicles.core.infrastructure.dataconfig.DefaultConfig;
import nl.mtvehicles.core.infrastructure.dataconfig.VehicleDataConfig;
import nl.mtvehicles.core.infrastructure.enums.VehicleType;
import nl.mtvehicles.core.infrastructure.vehicle.Vehicle;
import nl.mtvehicles.core.infrastructure.vehicle.VehicleUtils;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import nl.mtvehicles.core.infrastructure.modules.DependencyModule;
import nl.mtvehicles.core.infrastructure.modules.VersionModule;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;

import static nl.mtvehicles.core.infrastructure.vehicle.VehicleUtils.isInsideVehicle;

/**
 * Methods for PlaceholderAPI soft-dependency.<br>
 * @warning <b>Do not initialise this class directly. Use {@link DependencyModule#placeholderAPI} instead.</b>
 */
public class PlaceholderUtils extends PlaceholderExpansion {
    //This must only be called if DependencyModule made sure that Vault is installed.
    private final Main plugin = Main.instance;

    /**
     * Default constructor - <b>do not use this.</b><br>
     * Use {@link DependencyModule#placeholderAPI} instead.
     */
    public PlaceholderUtils(){}

    @Override
    public String getAuthor() {
        return "MTVehicles";
    }

    @Override
    public String getIdentifier() {
        return "mtv";
    }

    @Override
    public String getVersion() {
        return VersionModule.pluginVersionString;
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onRequest(OfflinePlayer p, String parameter) { //Placeholder 'mtv_%parameter%'

        //Global placeholders
        if (parameter.equalsIgnoreCase("fuel_pricePerLitre")){
            return ConfigModule.defaultConfig.get(DefaultConfig.Option.GAS_STATIONS_FILL_JERRYCANS_PRICE_PER_LITRE).toString();
        }


        //Per-player placeholders
        if (parameter.equalsIgnoreCase("vehicle_licensePlate")){
            if (!p.isOnline()) return "";
            if (!isInsideVehicle(p.getPlayer())) return "";
            return VehicleUtils.getLicensePlate(p.getPlayer().getVehicle());
        }

        if (parameter.equalsIgnoreCase("vehicle_name")){
            if (!p.isOnline()) return "";
            if (!isInsideVehicle(p.getPlayer())) return "";
            final String licensePlate =  VehicleUtils.getLicensePlate(p.getPlayer().getVehicle());
            return ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.NAME).toString();
        }

        if (parameter.equalsIgnoreCase("vehicle_type")){
            if (!p.isOnline()) return "";
            if (!isInsideVehicle(p.getPlayer())) return "";
            final String licensePlate =  VehicleUtils.getLicensePlate(p.getPlayer().getVehicle());
            return VehicleType.valueOf(ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.VEHICLE_TYPE).toString()).getName();
        }

        if (parameter.equalsIgnoreCase("vehicle_fuel")){
            if (!p.isOnline()) return "";
            if (!isInsideVehicle(p.getPlayer())) return "";
            final String licensePlate = VehicleUtils.getLicensePlate(p.getPlayer().getVehicle());
            final Double fuel = VehicleUtils.getVehicle(licensePlate).getCurrentFuel();
            if (fuel == null) return "";
            DecimalFormat df = new DecimalFormat("#.##");
            return df.format(fuel) + " %";
        }

        if (parameter.equalsIgnoreCase("vehicle_speed")){
            if (!p.isOnline()) return "";
            if (!isInsideVehicle(p.getPlayer())) return "";
            final String licensePlate = VehicleUtils.getLicensePlate(p.getPlayer().getVehicle());
            final Double speed = VehicleUtils.getVehicle(licensePlate).getCurrentSpeed();
            if (speed == null) return "0.0 blocks/sec";
            DecimalFormat df = new DecimalFormat("#.###");
            return df.format(speed) + " blocks/sec";
        }

        if (parameter.equalsIgnoreCase("vehicle_maxspeed")){
            if (!p.isOnline()) return "";
            if (!isInsideVehicle(p.getPlayer())) return "";
            final String licensePlate = VehicleUtils.getLicensePlate(p.getPlayer().getVehicle());
            DecimalFormat df = new DecimalFormat("#.###");
            return df.format(((Double) ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.MAX_SPEED) * 20)) + " blocks/sec";
        }

        if (parameter.equalsIgnoreCase("vehicle_place")){
            if (!p.isOnline()) return "";
            if (!isInsideVehicle(p.getPlayer())) return "";
            final Vehicle.Seat seat = Vehicle.Seat.getSeat(p.getPlayer());
            return (seat == null) ? "" : seat.toString();
        }

        if (parameter.equalsIgnoreCase("vehicle_seats")){
            if (!p.isOnline()) return "";
            if (!isInsideVehicle(p.getPlayer())) return "";
            final String licensePlate = VehicleUtils.getLicensePlate(p.getPlayer().getVehicle());
            final Vehicle vehicle = VehicleUtils.getVehicle(licensePlate);
            return String.valueOf(vehicle.getSeatsAmount());
        }

        if (parameter.equalsIgnoreCase("vehicle_uuid")){
            if (!p.isOnline()) return "";
            if (!isInsideVehicle(p.getPlayer())) return "";
            final String licensePlate = VehicleUtils.getLicensePlate(p.getPlayer().getVehicle());
            return VehicleUtils.getUUID(licensePlate);
        }

        if (parameter.equalsIgnoreCase("vehicle_owner")){
            if (!p.isOnline()) return "";
            if (!isInsideVehicle(p.getPlayer())) return "";
            final String licensePlate = VehicleUtils.getLicensePlate(p.getPlayer().getVehicle());
            return VehicleUtils.getVehicle(licensePlate).getOwnerName();
        }

        return null;
    }

    /**
     * Parse a text with placeholders.
     * @param player Player
     * @param text Text with placeholders
     * @return Text with placeholders replaced with their value
     */
    public static String parsePlaceholders(Player player, String text){
        return PlaceholderAPI.setPlaceholders(player, text);
    }
}
