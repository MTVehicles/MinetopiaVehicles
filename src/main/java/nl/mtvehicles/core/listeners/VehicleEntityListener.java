package nl.mtvehicles.core.listeners;

import de.tr7zw.changeme.nbtapi.NBTItem;
import nl.mtvehicles.core.commands.vehiclesubs.VehicleFuel;
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

            double curb = VehicleData.fuel.get(license);
            String benval = nbt.getString("mtvehicles.benzineval");
            String bensize = nbt.getString("mtvehicles.benzinesize");

            if (!ConfigModule.defaultConfig.canUseJerryCan(p)){
                ConfigModule.messagesConfig.sendMessage(p, "notInAGasStation");
                return;
            }

            if (Integer.parseInt(benval) < 1) {
                ConfigModule.messagesConfig.sendMessage(p, "noFuel");
                return;
            }

            if (curb > 99) {
                ConfigModule.messagesConfig.sendMessage(p, "vehicleFull");
                return;
            }

            if (curb + 5 > 100) {
                int test = (int) (100 - curb);
                p.setItemInHand(VehicleFuel.benzineItem(Integer.parseInt(bensize), Integer.parseInt(benval) - test));
                VehicleData.fuel.put(license, VehicleData.fuel.get(license) + test);
                BossBarUtils.setBossBarValue(curb / 100.0D, license);
                return;
            }

            if (!(Integer.parseInt(benval) < 5)) {
                VehicleData.fuel.put(license, VehicleData.fuel.get(license) + 5);
                BossBarUtils.setBossBarValue(curb / 100.0D, license);
                p.setItemInHand(VehicleFuel.benzineItem(Integer.parseInt(bensize), Integer.parseInt(benval) - 5));
            } else {
                VehicleData.fuel.put(license, Double.valueOf(VehicleData.fuel.get(license) + benval));
                BossBarUtils.setBossBarValue(curb / 100.0D, license);
                p.setItemInHand(VehicleFuel.benzineItem(Integer.parseInt(bensize), 0));
            }
        } else {
            checkDamage(e);
        }
    }

    public static void checkDamage(EntityDamageByEntityEvent e){
        final double damage = e.getDamage();
        final String license = VehicleUtils.getLicensePlate(e.getEntity());

        if (!ConfigModule.defaultConfig.getConfig().getBoolean("damageEnabled")) return;
        if (VehicleUtils.getByLicensePlate(license) == null) return;

        double damageMultiplier = ConfigModule.defaultConfig.getConfig().getDouble("damageMultiplier");
        if (damageMultiplier < 0.1 || damageMultiplier > 5) damageMultiplier = 0.5; //Must be between 0.1 and 5. Default: 0.5
        ConfigModule.vehicleDataConfig.damageVehicle(license, damage * damageMultiplier);
    }

    public static void kofferbak(Player p, String license) {
        if (ConfigModule.defaultConfig.getConfig().getBoolean("kofferbakEnabled")) {
            if (VehicleUtils.getByLicensePlate(license) == null) {
                ConfigModule.messagesConfig.sendMessage(p, "vehicleNotFound");
                return;
            }

            if (VehicleUtils.getByLicensePlate(license).isOwner(p) || p.hasPermission("mtvehicles.kofferbak")) {
                ConfigModule.configList.forEach(Config::reload);
                if (ConfigModule.vehicleDataConfig.getConfig().getBoolean("vehicle." + license + ".kofferbak")) {

                    if (ConfigModule.vehicleDataConfig.getConfig().getList("vehicle." + license + ".kofferbakData") == null) return;

                    Inventory inv = Bukkit.createInventory(null, ConfigModule.vehicleDataConfig.getConfig().getInt("vehicle." + license + ".kofferbakRows") * 9, "Kofferbak Vehicle: " + license);
                    List<ItemStack> chestContentsFromConfig = (List<ItemStack>) ConfigModule.vehicleDataConfig.getConfig().getList("vehicle." + license + ".kofferbakData");

                    for (ItemStack item : chestContentsFromConfig) {
                        if (item != null) inv.addItem(item);
                    }

                    p.openInventory(inv);
                }
            } else {
                p.sendMessage(TextUtils.colorize(ConfigModule.messagesConfig.getMessage("vehicleNoRiderKofferbak").replace("%p%", VehicleUtils.getByLicensePlate(license).getOwnerName())));
            }
        }
    }
}