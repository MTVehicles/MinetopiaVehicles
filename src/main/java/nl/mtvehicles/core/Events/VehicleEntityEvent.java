package nl.mtvehicles.core.Events;

import nl.mtvehicles.core.Commands.VehiclesSubs.VehicleBenzine;
import nl.mtvehicles.core.Infrastructure.Helpers.BossbarUtils;
import nl.mtvehicles.core.Infrastructure.Helpers.NBTUtils;
import nl.mtvehicles.core.Infrastructure.Helpers.TextUtils;
import nl.mtvehicles.core.Infrastructure.Models.ConfigUtils;
import nl.mtvehicles.core.Infrastructure.Models.Vehicle;
import nl.mtvehicles.core.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class VehicleEntityEvent implements Listener {
    public static HashMap<String, Double> speed = new HashMap<>();

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerInteractAtEntity(EntityDamageByEntityEvent event) {
        Entity a = event.getEntity();
        Entity e = (Entity) event.getDamager();
        if (e instanceof Player) {
            Player p = (Player) e;
            if (a.getCustomName() == null) {
                return;
            }
            if (p.isInsideVehicle()) {
                ItemStack item = p.getInventory().getItemInMainHand();
                if ((!item.hasItemMeta() || !(NBTUtils.contains(item, "mtvehicles.benzineval")))) {
                    return;
                }
                String licensePlate = p.getVehicle().getCustomName().replace("MTVEHICLES_MAINSEAT_", "");
                double curb = VehicleClickEvent.benzine.get(licensePlate);
                String benval = NBTUtils.getString(item, "mtvehicles.benzineval");
                String bensize = NBTUtils.getString(item, "mtvehicles.benzinesize");
                if (Integer.parseInt(benval) < 1){
                    p.sendMessage(TextUtils.colorize(Main.messagesConfig.getMessage("noFuel")));
                    return;
                }
                if (curb > 99){
                    p.sendMessage(TextUtils.colorize(Main.messagesConfig.getMessage("vehicleFull")));
                    return;
                }
                if (curb + 5 > 100) {
                    int test = (int) (100 - curb);
                    p.setItemInHand(VehicleBenzine.benzineItem(Integer.parseInt(bensize), Integer.parseInt(benval) - test));
                    VehicleClickEvent.benzine.put(licensePlate, VehicleClickEvent.benzine.get(licensePlate) + test);
                    BossbarUtils.setbossbarvalue(curb / 100.0D, licensePlate);
                    return;
                }
                if (!(Integer.parseInt(benval) < 5)) {
                    VehicleClickEvent.benzine.put(licensePlate, VehicleClickEvent.benzine.get(licensePlate) + 5);
                    BossbarUtils.setbossbarvalue(curb / 100.0D, licensePlate);
                    p.setItemInHand(VehicleBenzine.benzineItem(Integer.parseInt(bensize), Integer.parseInt(benval) - 5));

                } else {
                    VehicleClickEvent.benzine.put(licensePlate, Double.valueOf(VehicleClickEvent.benzine.get(licensePlate) + benval));
                    BossbarUtils.setbossbarvalue(curb / 100.0D, licensePlate);
                    p.setItemInHand(VehicleBenzine.benzineItem(Integer.parseInt(bensize), Integer.parseInt(benval) - Integer.parseInt(benval)));
                }
            }

            if (p.isSneaking()) {
                Main.configList.forEach(ConfigUtils::reload);
                if (a.getCustomName().contains("MTVEHICLES_MAINSEAT_")) {
                    kofferbak(p, a.getCustomName().replace("MTVEHICLES_MAINSEAT_", ""));
                    event.setCancelled(true);
                }
                if (a.getCustomName().contains("MTVEHICLES_MAIN_")) {
                    kofferbak(p, a.getCustomName().replace("MTVEHICLES_MAIN_", ""));
                    event.setCancelled(true);
                }
                if (a.getCustomName().contains("MTVEHICLES_SKIN_")) {
                    kofferbak(p, a.getCustomName().replace("MTVEHICLES_SKIN_", ""));
                    event.setCancelled(true);
                }
                return;
            }
        }
    }

    public static void kofferbak(Player p, String ken) {
        if (Main.defaultConfig.getConfig().getBoolean("kofferbakEnabled") == true) {
            if (Vehicle.getByPlate(ken) == null) {
                p.sendMessage(TextUtils.colorize(Main.messagesConfig.getMessage("vehicleNotFound")));
                return;
            }
            if (Vehicle.getByPlate(ken).getOwner().equals(p.getUniqueId().toString()) || Vehicle.canRide(p, ken) == true || p.hasPermission("mtvehicles.kofferbak")) {
                Main.configList.forEach(ConfigUtils::reload);
                if (Main.vehicleDataConfig.getConfig().getBoolean("vehicle." + ken + ".kofferbak") == true) {
                    if (Main.vehicleDataConfig.getConfig().getList("vehicle." + ken + ".kofferbakData") == null) {
                        return;
                    }
                    Inventory inv = Bukkit.createInventory(null, Main.vehicleDataConfig.getConfig().getInt("vehicle." + ken + ".kofferbakRows") * 9, "Kofferbak Vehicle: " + ken);
                    List<ItemStack> chestContentsFromConfig = (List<ItemStack>) Main.vehicleDataConfig.getConfig().getList("vehicle." + ken + ".kofferbakData");
                    for (ItemStack item : chestContentsFromConfig) {
                        if (item == null) {
                            continue;
                        }
                        inv.addItem(item);
                    }
                    p.openInventory(inv);
                } else {
                }

            } else {
                p.sendMessage(TextUtils.colorize(Main.messagesConfig.getMessage("vehicleNoRiderKofferbak").replace("%p%", Bukkit.getOfflinePlayer(UUID.fromString(Vehicle.getByPlate(ken).getOwner().toString())).getName())));

            }
        }

    }


    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getView().getTitle().contains("Kofferbak Vehicle: ")) {
            String ken = event.getView().getTitle().replace("Kofferbak Vehicle: ", "");
            List<ItemStack> chest = (List<ItemStack>) Main.vehicleDataConfig.getConfig().getList("vehicle." + ken + ".kofferbakData");
            chest.removeAll(chest);
            for (ItemStack item : event.getInventory().getContents()) {
                chest.add(item);
                Main.vehicleDataConfig.getConfig().set("vehicle." + ken + ".kofferbakData", chest);
                Main.vehicleDataConfig.save();
            }
        }
    }
}