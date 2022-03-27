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
import nl.mtvehicles.core.infrastructure.modules.VersionModule;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;

public class PlaceholderUtils extends PlaceholderExpansion {
    //This is only called if DependencyModule made sure that Vault is installed.
    private final Main plugin = Main.instance;

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
        return VersionModule.pluginVersion;
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    @ToDo(comment = "Add more placeholders.")
    public String onRequest(OfflinePlayer p, String parameter) { //Placeholder 'mtv_%parameter%'

        if (parameter.equalsIgnoreCase("vehicle_licensePlate")){
            if (!p.isOnline()) return "";
            if (!isInsideVehicle(p)) return "";
            return VehicleUtils.getLicensePlate(p.getPlayer().getVehicle());
        }

        if (parameter.equalsIgnoreCase("vehicle_name")){
            if (!p.isOnline()) return "";
            if (!isInsideVehicle(p)) return "";
            String licensePlate =  VehicleUtils.getLicensePlate(p.getPlayer().getVehicle());
            return ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.NAME).toString();
        }

        if (parameter.equalsIgnoreCase("vehicle_type")){
            if (!p.isOnline()) return "";
            if (!isInsideVehicle(p)) return "";
            String licensePlate =  VehicleUtils.getLicensePlate(p.getPlayer().getVehicle());
            return VehicleType.valueOf(ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.VEHICLE_TYPE).toString()).getName();
        }

        if (parameter.equalsIgnoreCase("vehicle_fuel")){
            if (!p.isOnline()) return "";
            if (!isInsideVehicle(p)) return "";
            String licensePlate = VehicleUtils.getLicensePlate(p.getPlayer().getVehicle());
            if (!(boolean) ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.FUEL_ENABLED)) return "";
            DecimalFormat df = new DecimalFormat("#.##");
            return df.format(VehicleData.fuel.get(licensePlate)) + "%";
        }

        if (parameter.equalsIgnoreCase("fuel_pricePerLitre")){
            return ConfigModule.defaultConfig.get(DefaultConfig.Option.GAS_STATIONS_FILL_JERRYCANS_PRICE_PER_LITRE).toString();
        }

        return null;
    }

    public static String parsePlaceholders(Player player, String text){
        return PlaceholderAPI.setPlaceholders(player, text);
    }

    private boolean isInsideVehicle(OfflinePlayer p){
        if (!p.getPlayer().isInsideVehicle()) return false;
        return VehicleUtils.isVehicle(p.getPlayer().getVehicle());
    }
}
