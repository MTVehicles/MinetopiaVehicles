package nl.mtvehicles.core.listeners.inventory;

import de.tr7zw.changeme.nbtapi.NBTItem;
import nl.mtvehicles.core.Main;
import nl.mtvehicles.core.commands.vehiclesubs.VehicleEdit;
import nl.mtvehicles.core.commands.vehiclesubs.VehicleMenu;
import nl.mtvehicles.core.infrastructure.models.VehicleUtils;
import nl.mtvehicles.core.listeners.VehicleEntityListener;
import nl.mtvehicles.core.infrastructure.dataconfig.MessagesConfig;
import nl.mtvehicles.core.infrastructure.helpers.ItemUtils;
import nl.mtvehicles.core.infrastructure.helpers.LanguageUtils;
import nl.mtvehicles.core.infrastructure.helpers.MenuUtils;
import nl.mtvehicles.core.infrastructure.helpers.TextUtils;
import nl.mtvehicles.core.infrastructure.models.Vehicle;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.permissions.PermissionAttachmentInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class InventoryClickListener implements Listener {

    public HashMap<UUID, ItemStack> vehicleMenu = new HashMap<>();
    public static HashMap<UUID, Inventory> skinMenu = new HashMap<>();
    public static HashMap<UUID, Integer> intSave = new HashMap<>();

    public static HashMap<UUID, Integer> id = new HashMap<>();
    public static HashMap<UUID, Integer> raw = new HashMap<>();

    @EventHandler
    public void onClick(org.bukkit.event.inventory.InventoryClickEvent e) {
        if (e.getCurrentItem() == null) return;
        if (!e.getCurrentItem().hasItemMeta()) return;
        Player p = (Player) e.getWhoClicked();
        if (e.getView().getTitle().contains("Vehicle Menu")) {
            e.setCancelled(true);
            id.put(p.getUniqueId(), 1);
            raw.put(p.getUniqueId(), e.getRawSlot());
            MenuUtils.getvehicleCMD(p, id.get(p.getUniqueId()), raw.get(p.getUniqueId()));
            return;
        }
        if (e.getView().getTitle().contains("Choose your vehicle")) {
            if (e.getCurrentItem().equals(ItemUtils.mItem("BARRIER", 1, (short) 0, "&4Sluiten", "&cDruk hier om het menu te sluiten!"))) {
                e.setCancelled(true);
                p.closeInventory();
                return;
            }
            if (e.getCurrentItem().equals(ItemUtils.mItem("WOOD_DOOR", 1, (short) 0, "&6Terug", "&eDruk hier om terug te gaan!"))) {
                p.openInventory(VehicleMenu.beginMenu.get(p.getUniqueId()));
                e.setCancelled(true);
                return;
            }
            if (e.getCurrentItem().equals(ItemUtils.mItem("STAINED_GLASS_PANE", 1, (short) 0, "&c", "&c"))) {
                e.setCancelled(true);
                return;
            }
            if (e.getCurrentItem().equals(ItemUtils.mItem("SPECTRAL_ARROW", 1, (short) 0, "&cVolgende Pagina", "&c"))) {
                e.setCancelled(true);
                MenuUtils.getvehicleCMD(p, id.get(p.getUniqueId()) + 1, raw.get(p.getUniqueId()));
                id.put(p.getUniqueId(), id.get(p.getUniqueId()) + 1);
                return;
            }
            if (e.getCurrentItem().equals(ItemUtils.mItem("SPECTRAL_ARROW", 1, (short) 0, "&cVorige Pagina", "&c"))) {
                e.setCancelled(true);
                if (id.get(p.getUniqueId()) > 1) {
                    MenuUtils.getvehicleCMD(p, id.get(p.getUniqueId()) - 1, raw.get(p.getUniqueId()));
                    id.put(p.getUniqueId(), id.get(p.getUniqueId()) - 1);
                }
                return;
            }
            e.setCancelled(true);
            vehicleMenu.put(p.getUniqueId(), e.getCurrentItem());
            Inventory inv = Bukkit.createInventory(null, 27, "Confirm getting vehicle");
            MessagesConfig msg = ConfigModule.messagesConfig;
            inv.setItem(11, ItemUtils.woolItem("WOOL", "RED_WOOL", 1, (short) 14, "&c" + msg.getMessage("cancel"), String.format("&7%s", msg.getMessage("cancelAction"))));
            inv.setItem(15, ItemUtils.woolItem("WOOL", "LIME_WOOL", 1, (short) 5, "&a"  + msg.getMessage("confirm"), String.format("&7%s@&7%s", msg.getMessage("confirmAction"), msg.getMessage("confirmVehicleMenu"))));
            p.openInventory(inv);
        }
        if (e.getView().getTitle().contains("Choose your language")) {
            e.setCancelled(true);
            if (e.getRawSlot() == 9) //English
                LanguageUtils.changeLanguage(p, "en");
            if (e.getRawSlot() == 11) //Dutch
                LanguageUtils.changeLanguage(p, "nl");
            if (e.getRawSlot() == 13) //Spanish
                LanguageUtils.changeLanguage(p, "es");
            if (e.getRawSlot() == 15) //Czech
                LanguageUtils.changeLanguage(p, "cs");

            if (e.getRawSlot() == 17) return;
            p.closeInventory();
        }
        if (e.getView().getTitle().contains("Confirm getting vehicle")) {
            e.setCancelled(true);
            if (e.getRawSlot() == 11) { //cancel getting vehicle
                p.openInventory(skinMenu.get(p.getUniqueId()));
            }
            if (e.getRawSlot() == 15) { //accepting getting vehicle
                if (!canGetVehicleFromMenu(p)) {
                    p.closeInventory();
                    return;
                }

                List<Map<?, ?>> vehicles = ConfigModule.vehiclesConfig.getConfig().getMapList("voertuigen");
                ConfigModule.messagesConfig.sendMessage(p, "completedvehiclegive");
                p.getInventory().addItem(vehicleMenu.get(p.getUniqueId()));

                NBTItem nbt = new NBTItem(vehicleMenu.get(p.getUniqueId()));
                String kenteken = nbt.getString("mtvehicles.kenteken");
                String naam = nbt.getString("mtvehicles.naam");

                Vehicle vehicle = new Vehicle();
                List<String> members = ConfigModule.vehicleDataConfig.getConfig().getStringList("voertuig." + kenteken + ".members");
                List<String> riders = ConfigModule.vehicleDataConfig.getConfig().getStringList("voertuig." + kenteken + ".riders");
                List<String> kof = ConfigModule.vehicleDataConfig.getConfig().getStringList("voertuig." + kenteken + ".kofferbakData");
                vehicle.setLicensePlate(kenteken);
                vehicle.setName(naam);
                vehicle.setVehicleType((String) vehicles.get(intSave.get(p.getUniqueId())).get("vehicleType"));
                vehicle.setSkinDamage(vehicleMenu.get(p.getUniqueId()).getDurability());
                vehicle.setSkinItem(vehicleMenu.get(p.getUniqueId()).getType().toString());
                vehicle.setGlow(false);
                vehicle.setHornEnabled((Boolean) vehicles.get(intSave.get(p.getUniqueId())).get("hornEnabled"));
                vehicle.setHealth((double) vehicles.get(intSave.get(p.getUniqueId())).get("maxHealth"));
                vehicle.setBenzineEnabled((Boolean) vehicles.get(intSave.get(p.getUniqueId())).get("benzineEnabled"));
                vehicle.setFuel(100);
                vehicle.setTrunk((Boolean) vehicles.get(intSave.get(p.getUniqueId())).get("kofferbakEnabled"));
                vehicle.setTrunkRows(1);
                vehicle.setFuelUsage(0.01);
                vehicle.setTrunkData(kof);
                vehicle.setAccelerationSpeed((Double) vehicles.get(intSave.get(p.getUniqueId())).get("acceleratieSpeed"));
                vehicle.setMaxSpeed((Double) vehicles.get(intSave.get(p.getUniqueId())).get("maxSpeed"));
                vehicle.setBrakingSpeed((Double) vehicles.get(intSave.get(p.getUniqueId())).get("brakingSpeed"));
                vehicle.setFrictionSpeed((Double) vehicles.get(intSave.get(p.getUniqueId())).get("aftrekkenSpeed"));
                vehicle.setRotateSpeed((Integer) vehicles.get(intSave.get(p.getUniqueId())).get("rotateSpeed"));
                vehicle.setMaxSpeedBackwards((Double) vehicles.get(intSave.get(p.getUniqueId())).get("maxSpeedBackwards"));
                vehicle.setOwner(p.getUniqueId().toString());
                vehicle.setNbtValue(nbt.getString("mtcustom"));
                vehicle.setRiders(riders);
                vehicle.setMembers(members);
                vehicle.save();
                p.closeInventory();
            }
        }

        if (e.getView().getTitle().contains("Vehicle Restore")) {
            if (e.getCurrentItem().equals(ItemUtils.mItem("STAINED_GLASS_PANE", 1, (short) 0, "&c", "&c"))) {
                e.setCancelled(true);
                return;
            }
            if (e.getCurrentItem().equals(ItemUtils.mItem("SPECTRAL_ARROW", 1, (short) 0, "&cVolgende Pagina", "&c"))) {
                e.setCancelled(true);
                MenuUtils.restoreCMD(p, Integer.parseInt(e.getView().getTitle().replace("Vehicle Restore ", "")) + 1, MenuUtils.restoreUUID.get("uuid"));
                return;
            }
            if (e.getCurrentItem().equals(ItemUtils.mItem("SPECTRAL_ARROW", 1, (short) 0, "&cVorige Pagina", "&c"))) {
                e.setCancelled(true);
                if (!(Integer.parseInt(e.getView().getTitle().replace("Vehicle Restore ", "")) - 1 < 1)) {
                    MenuUtils.restoreCMD(p, Integer.parseInt(e.getView().getTitle().replace("Vehicle Restore ", "")) - 1, MenuUtils.restoreUUID.get("uuid"));
                }
                return;
            }
            e.setCancelled(true);
            ItemStack car = e.getCurrentItem();
            p.getInventory().addItem(car);
        }
        if (e.getView().getTitle().contains("Vehicle Edit")) {
            e.setCancelled(true);
            if (e.getRawSlot() == 10) {
                MenuUtils.menuEdit(p);
            }
            if (e.getCurrentItem().getItemMeta().getDisplayName().contains("Benzine Settings")) {
                MenuUtils.benzineEdit(p);
            }
            if (e.getCurrentItem().getItemMeta().getDisplayName().contains("Kofferbak Settings")) {
                MenuUtils.kofferbakEdit(p);
            }
            if (e.getCurrentItem().getItemMeta().getDisplayName().contains("Member Settings")) {
                MenuUtils.membersEdit(p);
            }
            if (e.getCurrentItem().getItemMeta().getDisplayName().contains("Speed Settings")) {
                MenuUtils.speedEdit(p);
            }
            if (e.getCurrentItem().getItemMeta().getDisplayName().contains("Delete Vehicle")) {
                NBTItem nbt = new NBTItem(p.getInventory().getItemInMainHand());
                String ken = nbt.getString("mtvehicles.kenteken");
                ConfigModule.vehicleDataConfig.getConfig().set("vehicle." + ken, null);
                ConfigModule.vehicleDataConfig.save();
                p.getInventory().getItemInMainHand().setAmount(0);
                p.closeInventory();
            }
        }

        if (e.getView().getTitle().contains("Vehicle Settings")) {
            e.setCancelled(true);
            NBTItem nbt = new NBTItem(p.getInventory().getItemInMainHand());
            String ken = nbt.getString("mtvehicles.kenteken");
            if (e.getCurrentItem().equals(ItemUtils.mItem("BARRIER", 1, (short) 0, "&4Sluiten", "&cDruk hier om het menu te sluiten!"))) {
                e.setCancelled(true);
                p.closeInventory();
                return;
            }
            if (e.getCurrentItem().equals(ItemUtils.mItem("WOOD_DOOR", 1, (short) 0, "&6Terug", "&eDruk hier om terug te gaan!"))) {
                VehicleEdit.editMenu(p, p.getInventory().getItemInMainHand());
                e.setCancelled(true);
                return;
            }

            if (e.getCurrentItem().equals(ItemUtils.glowItem("BOOK", "&6Glow Aanpassen", "&7Huidige: &e" + ConfigModule.vehicleDataConfig.getConfig().getString("vehicle." + ken + ".isGlow")))) {
                ConfigModule.vehicleDataConfig.getConfig().set("vehicle." + ken + ".isGlow", false);
                ItemMeta im = p.getInventory().getItemInMainHand().getItemMeta();
                im.removeEnchant(Enchantment.ARROW_INFINITE);
                im.removeItemFlags(ItemFlag.HIDE_ENCHANTS);
                p.getInventory().getItemInMainHand().setItemMeta(im);
                ConfigModule.vehicleDataConfig.save();
                MenuUtils.menuEdit(p);
            }

            if (e.getCurrentItem().equals(ItemUtils.mItem("BOOK", 1, (short) 0, "&6Glow Aanpassen", "&7Huidige: &e" + ConfigModule.vehicleDataConfig.getConfig().getString("vehicle." + ken + ".isGlow")))) {
                ConfigModule.vehicleDataConfig.getConfig().set("vehicle." + ken + ".isGlow", true);
                ItemMeta im = p.getInventory().getItemInMainHand().getItemMeta();
                im.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
                im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                p.getInventory().getItemInMainHand().setItemMeta(im);
                ConfigModule.vehicleDataConfig.save();
                MenuUtils.menuEdit(p);
            }

            if (e.getCurrentItem().equals(ItemUtils.mItem("PAPER", 1, (short) 0, "&6Kenteken Aanpassen", "&7Huidige: &e" + ken))) {
                p.closeInventory();
                ConfigModule.messagesConfig.sendMessage(p, "typeLicenseInChat");
                ItemUtils.edit.put(p.getUniqueId() + ".kenteken", true);
            }

            if (e.getCurrentItem().getDurability() == (short) ConfigModule.vehicleDataConfig.getConfig().getInt("vehicle." + ken + ".skinDamage")) {
                p.closeInventory();
                ConfigModule.messagesConfig.sendMessage(p, "typeNameInChat");
                ItemUtils.edit.put(p.getUniqueId() + ".naam", true);
            }
        }
        if (e.getView().getTitle().contains("Vehicle Benzine")) {
            e.setCancelled(true);
            NBTItem nbt = new NBTItem(p.getInventory().getItemInMainHand());
            String ken = nbt.getString("mtvehicles.kenteken");
            if (e.getCurrentItem().equals(ItemUtils.mItem("BARRIER", 1, (short) 0, "&4Sluiten", "&cDruk hier om het menu te sluiten!"))) {
                e.setCancelled(true);
                p.closeInventory();
                return;
            }
            if (e.getCurrentItem().equals(ItemUtils.mItem("WOOD_DOOR", 1, (short) 0, "&6Terug", "&eDruk hier om terug te gaan!"))) {
                VehicleEdit.editMenu(p, p.getInventory().getItemInMainHand());
                e.setCancelled(true);
                return;
            }
            String menuitem = new NBTItem(e.getCurrentItem()).getString("mtvehicles.item");
            if (menuitem.contains("1")) {
                if (ConfigModule.vehicleDataConfig.getConfig().getBoolean("vehicle." + ken + ".benzineEnabled")) {
                    ConfigModule.vehicleDataConfig.getConfig().set("vehicle." + ken + ".benzineEnabled", false);
                    ConfigModule.vehicleDataConfig.save();
                } else {
                    ConfigModule.vehicleDataConfig.getConfig().set("vehicle." + ken + ".benzineEnabled", true);
                    ConfigModule.vehicleDataConfig.save();
                }
                MenuUtils.benzineEdit(p);
                ConfigModule.vehicleDataConfig.save();
                MenuUtils.benzineEdit(p);
            }
            if (menuitem.contains("2")) {
                p.closeInventory();
                ConfigModule.messagesConfig.sendMessage(p, "typeNewBenzineInChat");
                ItemUtils.edit.put(p.getUniqueId() + ".benzine", true);
            }
            if (menuitem.contains("3")) {
                p.closeInventory();
                ConfigModule.messagesConfig.sendMessage(p, "typeNewBenzineInChat");
                ItemUtils.edit.put(p.getUniqueId() + ".benzineverbruik", true);
            }
        }
        if (e.getView().getTitle().contains("Vehicle Kofferbak")) {
            e.setCancelled(true);
            if (e.getCurrentItem().equals(ItemUtils.mItem("BARRIER", 1, (short) 0, "&4Sluiten", "&cDruk hier om het menu te sluiten!"))) {
                e.setCancelled(true);
                p.closeInventory();
                return;
            }
            if (e.getCurrentItem().equals(ItemUtils.mItem("WOOD_DOOR", 1, (short) 0, "&6Terug", "&eDruk hier om terug te gaan!"))) {
                VehicleEdit.editMenu(p, p.getInventory().getItemInMainHand());
                e.setCancelled(true);
                return;
            }
            NBTItem nbt = new NBTItem(p.getInventory().getItemInMainHand());
            String ken = nbt.getString("mtvehicles.kenteken");
            String menuitem = new NBTItem(e.getCurrentItem()).getString("mtvehicles.item");
            if (menuitem.contains("1")) {
                if (ConfigModule.vehicleDataConfig.getConfig().getBoolean("vehicle." + ken + ".kofferbak")) {
                    ConfigModule.vehicleDataConfig.getConfig().set("vehicle." + ken + ".kofferbak", false);
                    ConfigModule.vehicleDataConfig.save();
                } else {
                    ConfigModule.vehicleDataConfig.getConfig().set("vehicle." + ken + ".kofferbak", true);
                    ConfigModule.vehicleDataConfig.save();
                }
                MenuUtils.kofferbakEdit(p);
                ConfigModule.vehicleDataConfig.save();
                MenuUtils.kofferbakEdit(p);
            }
            if (menuitem.contains("2")) {
                p.closeInventory();
                ConfigModule.messagesConfig.sendMessage(p, "typeNewRowsInChat");
                ItemUtils.edit.put(p.getUniqueId() + ".kofferbakRows", true);
            }
            if (menuitem.contains("3")) {
                p.closeInventory();
                VehicleEntityListener.kofferbak(p, ken);
            }
        }
        if (e.getView().getTitle().contains("Vehicle Members")) {
            e.setCancelled(true);
            if (e.getCurrentItem().equals(ItemUtils.mItem("BARRIER", 1, (short) 0, "&4Sluiten", "&cDruk hier om het menu te sluiten!"))) {
                e.setCancelled(true);
                p.closeInventory();
                return;
            }
            if (e.getCurrentItem().equals(ItemUtils.mItem("WOOD_DOOR", 1, (short) 0, "&6Terug", "&eDruk hier om terug te gaan!"))) {
                VehicleEdit.editMenu(p, p.getInventory().getItemInMainHand());
                e.setCancelled(true);
                return;
            }
        }
        if (e.getView().getTitle().contains("Vehicle Speed")) {
            e.setCancelled(true);
            if (e.getCurrentItem().equals(ItemUtils.mItem("BARRIER", 1, (short) 0, "&4Sluiten", "&cDruk hier om het menu te sluiten!"))) {
                e.setCancelled(true);
                p.closeInventory();
                return;
            }
            if (e.getCurrentItem().equals(ItemUtils.mItem("WOOD_DOOR", 1, (short) 0, "&6Terug", "&eDruk hier om terug te gaan!"))) {
                VehicleEdit.editMenu(p, p.getInventory().getItemInMainHand());
                e.setCancelled(true);
                return;
            }
            String menuitem = new NBTItem(e.getCurrentItem()).getString("mtvehicles.item");
            if (menuitem.contains("1")) {
                p.closeInventory();
                ConfigModule.messagesConfig.sendMessage(p, "typeSpeedInChat");
                ItemUtils.edit.put(p.getUniqueId() + ".acceleratieSpeed", true);

            }
            if (menuitem.contains("2")) {
                p.closeInventory();
                ConfigModule.messagesConfig.sendMessage(p, "typeSpeedInChat");
                ItemUtils.edit.put(p.getUniqueId() + ".maxSpeed", true);

            }
            if (menuitem.contains("3")) {
                p.closeInventory();
                ConfigModule.messagesConfig.sendMessage(p, "typeSpeedInChat");
                ItemUtils.edit.put(p.getUniqueId() + ".brakingSpeed", true);
            }
            if (menuitem.contains("4")) {
                p.closeInventory();
                ConfigModule.messagesConfig.sendMessage(p, "typeSpeedInChat");
                ItemUtils.edit.put(p.getUniqueId() + ".aftrekkenSpeed", true);

            }
            if (menuitem.contains("5")) {
                p.closeInventory();
                ConfigModule.messagesConfig.sendMessage(p, "typeSpeedInChat");
                ItemUtils.edit.put(p.getUniqueId() + ".rotateSpeed", true);
            }
            if (menuitem.contains("6")) {
                p.closeInventory();
                ConfigModule.messagesConfig.sendMessage(p, "typeSpeedInChat");
                ItemUtils.edit.put(p.getUniqueId() + ".maxSpeedBackwards", true);
            }
        }
        if (e.getView().getTitle().contains("Benzine menu")) {
            e.setCancelled(true);
            p.getInventory().addItem(e.getCurrentItem());
        }

        if (e.getView().getTitle().contains("Voucher Redeem Menu")) {
            e.setCancelled(true);

            if (e.getRawSlot() == 15) { //Yes
                String carUuid = new NBTItem(p.getInventory().getItemInMainHand()).getString("mtvehicles.item");
                if (VehicleUtils.getItemByUUID(p, carUuid) == null){
                    p.sendMessage(ConfigModule.messagesConfig.getMessage("giveCarNotFound"));
                    p.closeInventory();
                    return;
                }
                p.sendMessage(ConfigModule.messagesConfig.getMessage(TextUtils.colorize("voucherRedeem")));
                p.getInventory().getItemInMainHand().setAmount(p.getInventory().getItemInMainHand().getAmount() - 1);
                p.getInventory().addItem(VehicleUtils.getItemByUUID(p, carUuid));
                p.closeInventory();
            }

            else if (e.getRawSlot() == 11) { //No
                p.closeInventory();
            }
        }
    }
    
    private boolean canGetVehicleFromMenu(Player p){
        final int owned = ConfigModule.vehicleDataConfig.getNumberOfOwnedVehicles(p);
        int limit = -1; //If permission is not specified, players can get as many as they want

        if (p.hasPermission("mtvehicles.limit.*")) return true;

        for (PermissionAttachmentInfo permission: p.getEffectivePermissions()) {
            String permName = permission.getPermission();
            if (permName.contains("mtvehicles.limit.") && permission.getValue()){
                try {
                    limit = Integer.parseInt(permName.replace("mtvehicles.limit.", ""));
                    break;
                } catch (Exception e) {
                    limit = 0;
                    Main.logSevere("An error occurred whilst trying to retrieve player's 'mtvehicles.limit.X' permission. You must have done something wrong when setting it.");
                    break;
                }
            }
        }

        final boolean returns = limit == -1 || limit > owned;
        if (!returns) ConfigModule.messagesConfig.sendMessage(p, "tooManyVehicles");

        return returns;
    }
}
