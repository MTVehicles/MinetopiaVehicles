package nl.mtvehicles.core.infrastructure.dependencies;

import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import nl.mtvehicles.core.Main;
import nl.mtvehicles.core.infrastructure.annotations.ToDo;
import nl.mtvehicles.core.infrastructure.dataconfig.DefaultConfig;
import nl.mtvehicles.core.infrastructure.dataconfig.VehicleDataConfig;
import nl.mtvehicles.core.infrastructure.enums.VehicleType;
import nl.mtvehicles.core.infrastructure.helpers.VehicleData;
import nl.mtvehicles.core.infrastructure.models.VehicleUtils;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import nl.mtvehicles.core.infrastructure.modules.DependencyModule;
import nl.mtvehicles.core.infrastructure.modules.VersionModule;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;

import static nl.mtvehicles.core.infrastructure.models.VehicleUtils.isInsideVehicle;

/**
 * Methods for PlaceholderAPI soft-dependency.<br>
 * <b>Do not initialise this class directly. Use {@link DependencyModule#placeholderAPI} instead.</b>
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
    @ToDo("Add more placeholders.")
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
            String licensePlate =  VehicleUtils.getLicensePlate(p.getPlayer().getVehicle());
            return ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.NAME).toString();
        }

        if (parameter.equalsIgnoreCase("vehicle_type")){
            if (!p.isOnline()) return "";
            if (!isInsideVehicle(p.getPlayer())) return "";
            String licensePlate =  VehicleUtils.getLicensePlate(p.getPlayer().getVehicle());
            return VehicleType.valueOf(ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.VEHICLE_TYPE).toString()).getName();
        }

        if (parameter.equalsIgnoreCase("vehicle_fuel")){
            if (!p.isOnline()) return "";
            if (!isInsideVehicle(p.getPlayer())) return "";
            String licensePlate = VehicleUtils.getLicensePlate(p.getPlayer().getVehicle());
            if (!(boolean) ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.FUEL_ENABLED)) return "";
            DecimalFormat df = new DecimalFormat("#.##");
            return df.format(VehicleData.fuel.get(licensePlate)) + "%";
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
