package nl.mtvehicles.core.listeners;

import nl.mtvehicles.core.events.VehicleLeaveEvent;
import nl.mtvehicles.core.infrastructure.dataconfig.DefaultConfig;
import nl.mtvehicles.core.infrastructure.dataconfig.VehicleDataConfig;
import nl.mtvehicles.core.infrastructure.helpers.BossBarUtils;
import nl.mtvehicles.core.infrastructure.helpers.VehicleData;
import nl.mtvehicles.core.infrastructure.models.MTVListener;
import nl.mtvehicles.core.infrastructure.models.Vehicle;
import nl.mtvehicles.core.infrastructure.models.VehicleUtils;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.spigotmc.event.entity.EntityDismountEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * On leave of a vehicle
 */
public class VehicleLeaveListener extends MTVListener {
    public static HashMap<String, String> VehicleList = new HashMap<String, String>();

    public VehicleLeaveListener(){
        super(new VehicleLeaveEvent());
    }

    @EventHandler
    public void onVehicleLeave(EntityDismountEvent event) {
        this.event = event;
        final Entity entity = event.getDismounted();
        if (!(event.getEntity() instanceof Player)) return;
        player = (Player) event.getEntity();

        if (!VehicleUtils.isVehicle(entity)) return;
        if (!entity.getCustomName().contains("MTVEHICLES_MAINSEAT_")) return;
        String license = VehicleUtils.getLicensePlate(entity);
        if (VehicleData.autostand.get("MTVEHICLES_MAIN_" + license) == null) return;

        VehicleLeaveEvent api = (VehicleLeaveEvent) getAPI();
        api.setLicensePlate(license);
        callAPI();
        if (isCancelled()) return;

        license = api.getLicensePlate();

        Vehicle vehicle = VehicleUtils.getVehicle(license);
        VehicleList.put(player.getName(), license);

        if (vehicle.getVehicleType().isHelicopter()) {
            ArmorStand blades = VehicleData.autostand.get("MTVEHICLES_WIEKENS_" + license);
            Location locBelow = new Location(blades.getLocation().getWorld(), blades.getLocation().getX(), blades.getLocation().getY() - 0.2, blades.getLocation().getZ(), blades.getLocation().getYaw(), blades.getLocation().getPitch());
            blades.setGravity(locBelow.getBlock().getType().equals(Material.AIR));
        }

        //If a helicopter is 'extremely falling' and player manages to leave it beforehand
        if (vehicle.getVehicleType().isHelicopter() && (boolean) ConfigModule.defaultConfig.get(DefaultConfig.Option.EXTREME_HELICOPTER_FALL) && !entity.isOnGround()){
            VehicleData.fallDamage.put(license, true); //Do not damage when entering afterwards
        }

        BossBarUtils.removeBossBar(player, license);
        ArmorStand standMain = VehicleData.autostand.get("MTVEHICLES_MAIN_" + license);
        ArmorStand standSkin = VehicleData.autostand.get("MTVEHICLES_SKIN_" + license);
        standMain.setGravity(true);
        standSkin.setGravity(true);
        List<Map<String, Integer>> seats = (List<Map<String, Integer>>) vehicle.getVehicleData().get("seats");
        for (int i = 2; i <= seats.size(); i++) {
            if (VehicleData.autostand.get("MTVEHICLES_SEAT" + i + "_" + license) != null)
                VehicleData.autostand.get("MTVEHICLES_SEAT" + i + "_" + license).remove();
        }
        VehicleData.type.remove(license); //.remove(license+"b") used to be here... why? maybe i'm missing something?

        if ((boolean) ConfigModule.defaultConfig.get(DefaultConfig.Option.FUEL_ENABLED) && (boolean) ConfigModule.vehicleDataConfig.get(license, VehicleDataConfig.Option.FUEL_ENABLED)) {
            double fuel = VehicleData.fuel.get(license);
            ConfigModule.vehicleDataConfig.set(license, VehicleDataConfig.Option.FUEL, fuel);
            ConfigModule.vehicleDataConfig.save();
        }
    }
}
