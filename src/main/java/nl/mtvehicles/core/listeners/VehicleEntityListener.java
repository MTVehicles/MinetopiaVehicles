package nl.mtvehicles.core.listeners;

import de.tr7zw.changeme.nbtapi.NBTItem;
import nl.mtvehicles.core.commands.vehiclesubs.VehicleFuel;
import nl.mtvehicles.core.events.VehicleEntityEvent;
import nl.mtvehicles.core.infrastructure.dataconfig.DefaultConfig;
import nl.mtvehicles.core.infrastructure.enums.Message;
import nl.mtvehicles.core.infrastructure.helpers.BossBarUtils;
import nl.mtvehicles.core.infrastructure.helpers.VehicleData;
import nl.mtvehicles.core.infrastructure.models.MTVListener;
import nl.mtvehicles.core.infrastructure.models.VehicleUtils;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class VehicleEntityListener extends MTVListener {

    public VehicleEntityListener(){
        super(new VehicleEntityEvent());
    }

    public static HashMap<String, Double> speed = new HashMap<>();

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerInteractAtEntity(EntityDamageByEntityEvent event) {
        this.event = event;
        final Entity victim = event.getEntity();
        final Entity damager = event.getDamager();

        if (!VehicleUtils.isVehicle(victim)) return;

        if (!(damager instanceof Player)) {
            checkDamage();
            return;
        }

        player = (Player) damager;
        String license = VehicleUtils.getLicensePlate(victim);

        VehicleEntityEvent api = (VehicleEntityEvent) getAPI();
        api.setLicensePlate(license);
        callAPI();
        if (isCancelled()) return;

        license = api.getLicensePlate();

        if (player.isSneaking() && !player.isInsideVehicle()) {
            VehicleUtils.openTrunk(player, license);
            event.setCancelled(true);
            return;
        }

        if (!player.isInsideVehicle()) return;

        ItemStack item = player.getInventory().getItemInMainHand();

        if (!item.hasItemMeta() || !new NBTItem(item).hasKey("mtvehicles.benzineval")){
            checkDamage();
            return;
        }

        NBTItem nbt = new NBTItem(item);

        final double fuel = VehicleData.fuel.get(license);
        final String benval = nbt.getString("mtvehicles.benzineval");
        final String bensize = nbt.getString("mtvehicles.benzinesize");

        if (!ConfigModule.defaultConfig.canUseJerryCan(player)){
            ConfigModule.messagesConfig.sendMessage(player, Message.NOT_IN_A_GAS_STATION);
            return;
        }

        if (Integer.parseInt(benval) < 1) {
            ConfigModule.messagesConfig.sendMessage(player, Message.NO_FUEL);
            return;
        }

        if (fuel > 99) {
            ConfigModule.messagesConfig.sendMessage(player, Message.VEHICLE_FULL);
            return;
        }

        if (VehicleData.fallDamage.get(license) != null && fuel > 2) VehicleData.fallDamage.remove(license);

        if (fuel + 5 > 100) {
            int rest = (int) (100 - fuel);
            player.setItemInHand(VehicleFuel.benzineItem(Integer.parseInt(bensize), Integer.parseInt(benval) - rest));
            VehicleData.fuel.put(license, VehicleData.fuel.get(license) + rest);
            BossBarUtils.setBossBarValue(fuel / 100.0D, license);
            return;
        }

        if (!(Integer.parseInt(benval) < 5)) {
            VehicleData.fuel.put(license, VehicleData.fuel.get(license) + 5);
            BossBarUtils.setBossBarValue(fuel / 100.0D, license);
            player.setItemInHand(VehicleFuel.benzineItem(Integer.parseInt(bensize), Integer.parseInt(benval) - 5));
        } else {
            VehicleData.fuel.put(license, Double.valueOf(VehicleData.fuel.get(license) + benval));
            BossBarUtils.setBossBarValue(fuel / 100.0D, license);
            player.setItemInHand(VehicleFuel.benzineItem(Integer.parseInt(bensize), 0));
        }
    }

    public void checkDamage(){
        final double damage = ((EntityDamageByEntityEvent) event).getDamage();
        final String license = VehicleUtils.getLicensePlate(((EntityDamageByEntityEvent) event).getEntity());

        if (!(boolean) ConfigModule.defaultConfig.get(DefaultConfig.Option.DAMAGE_ENABLED)) return;
        if (VehicleUtils.getByLicensePlate(license) == null) return;

        double damageMultiplier = (double) ConfigModule.defaultConfig.get(DefaultConfig.Option.DAMAGE_MULTIPLIER);
        if (damageMultiplier < 0.1 || damageMultiplier > 5) damageMultiplier = 0.5; //Must be between 0.1 and 5. Default: 0.5
        ConfigModule.vehicleDataConfig.damageVehicle(license, damage * damageMultiplier);
    }
}