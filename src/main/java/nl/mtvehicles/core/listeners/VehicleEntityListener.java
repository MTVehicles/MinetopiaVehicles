package nl.mtvehicles.core.listeners;

import de.tr7zw.changeme.nbtapi.NBTItem;
import nl.mtvehicles.core.commands.vehiclesubs.VehicleFuel;
import nl.mtvehicles.core.events.VehicleDamageEvent;
import nl.mtvehicles.core.events.VehicleFuelEvent;
import nl.mtvehicles.core.events.VehicleOpenTrunkEvent;
import nl.mtvehicles.core.infrastructure.dataconfig.DefaultConfig;
import nl.mtvehicles.core.infrastructure.enums.Message;
import nl.mtvehicles.core.infrastructure.models.MTVListener;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import nl.mtvehicles.core.infrastructure.utils.BossBarUtils;
import nl.mtvehicles.core.infrastructure.vehicle.VehicleData;
import nl.mtvehicles.core.infrastructure.vehicle.VehicleUtils;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;
import java.util.HashMap;

import static org.bukkit.event.entity.EntityDamageEvent.DamageCause.ENTITY_ATTACK;

/**
 * On vehicle left click - damaging, opening a trunk, fueling
 */
public class VehicleEntityListener extends MTVListener {

    public static HashMap<String, Double> speed = new HashMap<>();

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerInteractAtEntity(EntityDamageByEntityEvent event) {
        this.event = event;
        final Entity victim = event.getEntity();

        if (!VehicleUtils.isVehicle(victim)) return;

        String license = VehicleUtils.getLicensePlate(victim);

        if (license == null) return; 

        final Entity damager = event.getDamager();

        if (!(damager instanceof Player)) {
            handleVehicleDamage(damager, license);
            return;
        }

        player = (Player) damager;

        if (player.isSneaking() && !player.isInsideVehicle()) {
            handleOpenTrunk(license);
            event.setCancelled(true);
            return;
        }

        ItemStack heldItem = player.getInventory().getItemInMainHand();

        if (!heldItem.hasItemMeta()) {
            handleVehicleDamage(player, license);
            return;
        }

        NBTItem nbt = new NBTItem(heldItem);
        if (!nbt.hasKey("mtvehicles.benzineval")) {
            handleVehicleDamage(player, license);
            return;
        }

        handleFueling(license, player, nbt);
    }

    private void handleVehicleDamage(Entity damager, String license) {
        setupDamageAPI(damager, license);
        callAPI(null);
        if (isCancelled()) return;

        final String newLicense = ((VehicleDamageEvent) getAPI()).getLicensePlate();
        final double damage = ((VehicleDamageEvent) getAPI()).getDamage();
        damage(newLicense, damage);
    }

    private void handleOpenTrunk(String license) {
        this.setAPI(new VehicleOpenTrunkEvent());
        VehicleOpenTrunkEvent api = (VehicleOpenTrunkEvent) getAPI();
        api.setLicensePlate(license);
        callAPI();
        if (isCancelled()) return;

        if (!VehicleUtils.isTrunkInventoryOpen(player, license)) {
            VehicleUtils.openTrunk(player, api.getLicensePlate());
        }
    }

    private void handleFueling(String license, Player player, NBTItem nbt) {
        double vehicleFuel = VehicleData.fuel.getOrDefault(license, 0.0);
        String jerryCanFuelStr = nbt.getString("mtvehicles.benzineval");
        String jerryCanSizeStr = nbt.getString("mtvehicles.benzinesize");

        if (jerryCanFuelStr == null || jerryCanSizeStr == null) return;

        int jerryCanFuel = Integer.parseInt(jerryCanFuelStr);
        int jerryCanSize = Integer.parseInt(jerryCanSizeStr);

        this.setAPI(new VehicleFuelEvent(vehicleFuel, jerryCanFuel, jerryCanSize));
        VehicleFuelEvent api = (VehicleFuelEvent) getAPI();
        api.setLicensePlate(license);
        callAPI();
        if (isCancelled()) return;

        license = api.getLicensePlate();

        if (!ConfigModule.defaultConfig.canUseJerryCan(player)) {
            ConfigModule.messagesConfig.sendMessage(player, Message.NOT_IN_A_GAS_STATION);
            return;
        }

        if (jerryCanFuel < 1) {
            ConfigModule.messagesConfig.sendMessage(player, Message.NO_FUEL);
            return;
        }

        if (vehicleFuel >= 100) {
            ConfigModule.messagesConfig.sendMessage(player, Message.VEHICLE_FULL);
            return;
        }

        if (VehicleData.fallDamage.get(license) != null && vehicleFuel > 2) {
            VehicleData.fallDamage.remove(license);
        }

        int fuelToAdd = Math.min(5, jerryCanFuel);
        vehicleFuel = Math.min(vehicleFuel + fuelToAdd, 100);
        VehicleData.fuel.put(license, vehicleFuel);

        BossBarUtils.setBossBarValue(vehicleFuel / 100.0D, license);
        player.getInventory().setItemInMainHand(VehicleFuel.jerrycanItem(jerryCanSize, jerryCanFuel - fuelToAdd));
    }

    public static void damage(String license, double damage) {
        if (!(boolean) ConfigModule.defaultConfig.get(DefaultConfig.Option.DAMAGE_ENABLED)) return;
        if (VehicleUtils.getVehicle(license) == null) return;

        double damageMultiplier = (double) ConfigModule.defaultConfig.get(DefaultConfig.Option.DAMAGE_MULTIPLIER);
        damageMultiplier = Math.max(0.1, Math.min(damageMultiplier, 5)); //Must be between 0.1 and 5. Default: 0.5
        ConfigModule.vehicleDataConfig.damageVehicle(license, damage * damageMultiplier);
    }

    private void setupDamageAPI(@Nullable Entity damager, String license) {
        this.setAPI(new VehicleDamageEvent());
        VehicleDamageEvent api = (VehicleDamageEvent) getAPI();
        api.setDamager(damager);
        api.setDamageCause(ENTITY_ATTACK);
        api.setLicensePlate(license);
        api.setDamage(((EntityDamageByEntityEvent) event).getDamage());
    }
}
