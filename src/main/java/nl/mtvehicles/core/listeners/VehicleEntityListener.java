package nl.mtvehicles.core.listeners;

import de.tr7zw.changeme.nbtapi.NBTItem;
import nl.mtvehicles.core.commands.vehiclesubs.VehicleFuel;
import nl.mtvehicles.core.infrastructure.dataconfig.DefaultConfig;
import nl.mtvehicles.core.infrastructure.dataconfig.VehicleDataConfig;
import nl.mtvehicles.core.infrastructure.enums.Message;
import nl.mtvehicles.core.infrastructure.helpers.BossBarUtils;
import nl.mtvehicles.core.infrastructure.helpers.TextUtils;
import nl.mtvehicles.core.infrastructure.helpers.VehicleData;
import nl.mtvehicles.core.infrastructure.models.Config;
import nl.mtvehicles.core.infrastructure.models.VehicleUtils;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;

public class VehicleEntityListener implements Listener {
    public static HashMap<String, Double> speed = new HashMap<>();

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerInteractAtEntity(EntityDamageByEntityEvent e) {
        final Entity eventEntity = e.getEntity();
        final Entity damager = e.getDamager();

        if (e.isCancelled()) return;

        if (!VehicleUtils.isVehicle(eventEntity)) return;

        if (damager instanceof Player) {
            final Player p = (Player) damager;
            final String license = VehicleUtils.getLicensePlate(eventEntity);

            if (p.isSneaking() && !p.isInsideVehicle()) {
                kofferbak(p, license);
                e.setCancelled(true);
                return;
            }

            if (!p.isInsideVehicle()) return;

            ItemStack item = p.getInventory().getItemInMainHand();

            if (!item.hasItemMeta() || !new NBTItem(item).hasKey("mtvehicles.benzineval")){
                checkDamage(e);
                return;
            }

            NBTItem nbt = new NBTItem(item);

            final double fuel = VehicleData.fuel.get(license);
            final String benval = nbt.getString("mtvehicles.benzineval");
            final String bensize = nbt.getString("mtvehicles.benzinesize");

            if (!ConfigModule.defaultConfig.canUseJerryCan(p)){
                ConfigModule.messagesConfig.sendMessage(p, Message.NOT_IN_A_GAS_STATION);
                return;
            }

            if (Integer.parseInt(benval) < 1) {
                ConfigModule.messagesConfig.sendMessage(p, Message.NO_FUEL);
                return;
            }

            if (fuel > 99) {
                ConfigModule.messagesConfig.sendMessage(p, Message.VEHICLE_FULL);
                return;
            }

            if (fuel + 5 > 100) {
                int rest = (int) (100 - fuel);
                p.setItemInHand(VehicleFuel.benzineItem(Integer.parseInt(bensize), Integer.parseInt(benval) - rest));
                VehicleData.fuel.put(license, VehicleData.fuel.get(license) + rest);
                BossBarUtils.setBossBarValue(fuel / 100.0D, license);
                return;
            }

            if (!(Integer.parseInt(benval) < 5)) {
                VehicleData.fuel.put(license, VehicleData.fuel.get(license) + 5);
                BossBarUtils.setBossBarValue(fuel / 100.0D, license);
                p.setItemInHand(VehicleFuel.benzineItem(Integer.parseInt(bensize), Integer.parseInt(benval) - 5));
            } else {
                VehicleData.fuel.put(license, Double.valueOf(VehicleData.fuel.get(license) + benval));
                BossBarUtils.setBossBarValue(fuel / 100.0D, license);
                p.setItemInHand(VehicleFuel.benzineItem(Integer.parseInt(bensize), 0));
            }
        } else {
            checkDamage(e);
        }
    }

    public static void checkDamage(EntityDamageByEntityEvent e){
        final double damage = e.getDamage();
        final String license = VehicleUtils.getLicensePlate(e.getEntity());

        if (!(boolean) ConfigModule.defaultConfig.get(DefaultConfig.Option.DAMAGE_ENABLED)) return;
        if (VehicleUtils.getByLicensePlate(license) == null) return;

        double damageMultiplier = (double) ConfigModule.defaultConfig.get(DefaultConfig.Option.DAMAGE_MULTIPLIER);
        if (damageMultiplier < 0.1 || damageMultiplier > 5) damageMultiplier = 0.5; //Must be between 0.1 and 5. Default: 0.5
        ConfigModule.vehicleDataConfig.damageVehicle(license, damage * damageMultiplier);
    }

    public static void kofferbak(Player p, String license) {
        if ((boolean) ConfigModule.defaultConfig.get(DefaultConfig.Option.TRUNK_ENABLED)) {
            if (VehicleUtils.getByLicensePlate(license) == null) {
                ConfigModule.messagesConfig.sendMessage(p, Message.VEHICLE_NOT_FOUND);
                return;
            }

            if (VehicleUtils.getByLicensePlate(license).isOwner(p) || p.hasPermission("mtvehicles.kofferbak")) {
                ConfigModule.configList.forEach(Config::reload);
                if ((boolean) ConfigModule.vehicleDataConfig.get(license, VehicleDataConfig.Option.TRUNK_ENABLED)) {

                    if (ConfigModule.vehicleDataConfig.get(license, VehicleDataConfig.Option.TRUNK_DATA) == null) return;

                    Inventory inv = Bukkit.createInventory(null, (int) ConfigModule.vehicleDataConfig.get(license, VehicleDataConfig.Option.TRUNK_ROWS) * 9, "Kofferbak Vehicle: " + license);
                    List<ItemStack> chestContentsFromConfig = (List<ItemStack>) ConfigModule.vehicleDataConfig.get(license, VehicleDataConfig.Option.TRUNK_DATA);

                    for (ItemStack item : chestContentsFromConfig) {
                        if (item != null) inv.addItem(item);
                    }

                    p.openInventory(inv);
                }
            } else {
                p.sendMessage(TextUtils.colorize(ConfigModule.messagesConfig.getMessage(Message.VEHICLE_NO_RIDER_TRUNK).replace("%p%", VehicleUtils.getByLicensePlate(license).getOwnerName())));
            }
        }
    }
}