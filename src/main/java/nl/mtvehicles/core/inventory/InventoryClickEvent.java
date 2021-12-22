package nl.mtvehicles.core.inventory;

import nl.mtvehicles.core.commands.vehiclesubs.VehicleEdit;
import nl.mtvehicles.core.commands.vehiclesubs.VehicleMenu;
import nl.mtvehicles.core.events.JoinEvent;
import nl.mtvehicles.core.events.VehicleEntityEvent;
import nl.mtvehicles.core.infrastructure.dataconfig.MessagesConfig;
import nl.mtvehicles.core.infrastructure.helpers.ItemUtils;
import nl.mtvehicles.core.infrastructure.helpers.MenuUtils;
import nl.mtvehicles.core.infrastructure.helpers.NBTUtils;
import nl.mtvehicles.core.infrastructure.helpers.TextUtils;
import nl.mtvehicles.core.infrastructure.models.Vehicle;
import nl.mtvehicles.core.Main;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import sun.security.krb5.Config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class InventoryClickEvent implements Listener {

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
            inv.setItem(11, ItemUtils.woolItem("WOOL", "RED_WOOL", 1, (short) 14, "&4Annuleren", "&7Druk hier om het te annuleren."));
            inv.setItem(15, ItemUtils.woolItem("WOOL", "LIME_WOOL", 1, (short) 5, "&aCreate Vehicle", "&7Druk hier als je het voertuigen wilt aanmaken en op je naam wilt zetten"));
            p.openInventory(inv);
        }
        if (e.getView().getTitle().contains("Choose your language")) {
            e.setCancelled(true);
            if (e.getRawSlot() == 10) { //English
                JoinEvent.languageCheck.put(p.getUniqueId(), false);
                if (ConfigModule.messagesConfig.setLanguageFile("en")){
                    p.sendMessage(ConfigModule.messagesConfig.getMessage("languageHasChanged"));
                    ConfigModule.defaultConfig.getConfig().set("messagesLanguage", "en");
                    ConfigModule.defaultConfig.save();
                } else {
                    p.sendMessage(ChatColor.RED + "An error occurred whilst trying to set a new language.");
                }
                p.closeInventory();
            }
            if (e.getRawSlot() == 12) { //Dutch
                JoinEvent.languageCheck.put(p.getUniqueId(), false);
                if (ConfigModule.messagesConfig.setLanguageFile("nl")){
                    p.sendMessage(ConfigModule.messagesConfig.getMessage("languageHasChanged"));
                    ConfigModule.defaultConfig.getConfig().set("messagesLanguage", "nl");
                    ConfigModule.defaultConfig.save();
                } else {
                    p.sendMessage(ChatColor.RED + "An error occurred whilst trying to set a new language.");
                }
                p.closeInventory();
            }
            if (e.getRawSlot() == 14) { //Spanish
                JoinEvent.languageCheck.put(p.getUniqueId(), false);
                if (ConfigModule.messagesConfig.setLanguageFile("es")){
                    p.sendMessage(ConfigModule.messagesConfig.getMessage("languageHasChanged"));
                    ConfigModule.defaultConfig.getConfig().set("messagesLanguage", "es");
                    ConfigModule.defaultConfig.save();
                } else {
                    p.sendMessage(ChatColor.RED + "An error occurred whilst trying to set a new language.");
                }
                p.closeInventory();
            }
            if (e.getRawSlot() == 16) { //Czech
                JoinEvent.languageCheck.put(p.getUniqueId(), false);
                if (ConfigModule.messagesConfig.setLanguageFile("cs")){
                    p.sendMessage(ConfigModule.messagesConfig.getMessage("languageHasChanged"));
                    ConfigModule.defaultConfig.getConfig().set("messagesLanguage", "cs");
                    ConfigModule.defaultConfig.save();
                } else {
                    p.sendMessage(ChatColor.RED + "An error occurred whilst trying to set a new language.");
                }
                p.closeInventory();
            }
        }
        if (e.getView().getTitle().contains("Confirm getting vehicle")) {
            e.setCancelled(true);
            if (e.getRawSlot() == 11) { //cancel getting vehicle
                p.openInventory(skinMenu.get(p.getUniqueId()));
            }
            if (e.getRawSlot() == 15) { //accepting getting vehicle
                List<Map<?, ?>> vehicles = ConfigModule.vehiclesConfig.getConfig().getMapList("voertuigen");
                ConfigModule.messagesConfig.sendMessage(p, "completedvehiclegive");
                p.getInventory().addItem(vehicleMenu.get(p.getUniqueId()));
                String kenteken = NBTUtils.getString(vehicleMenu.get(p.getUniqueId()), "mtvehicles.kenteken");
                String naam = NBTUtils.getString(vehicleMenu.get(p.getUniqueId()), "mtvehicles.naam");
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
                vehicle.setBenzineEnabled((Boolean) vehicles.get(intSave.get(p.getUniqueId())).get("benzineEnabled"));
                vehicle.setBenzine(100);
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
                vehicle.setNbtValue(NBTUtils.getString(vehicleMenu.get(p.getUniqueId()), "mtcustom"));
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
                String ken = NBTUtils.getString(p.getInventory().getItemInMainHand(), "mtvehicles.kenteken");
                ConfigModule.vehicleDataConfig.getConfig().set("vehicle." + ken, null);
                ConfigModule.vehicleDataConfig.save();
                p.getInventory().getItemInMainHand().setAmount(0);
                p.closeInventory();
            }
        }

        if (e.getView().getTitle().contains("Vehicle Settings")) {
            e.setCancelled(true);
            String ken = NBTUtils.getString(p.getInventory().getItemInMainHand(), "mtvehicles.kenteken");
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
            String ken = NBTUtils.getString(p.getInventory().getItemInMainHand(), "mtvehicles.kenteken");
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
            String menuitem = NBTUtils.getString(e.getCurrentItem(), "mtvehicles.item");
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
            String ken = NBTUtils.getString(p.getInventory().getItemInMainHand(), "mtvehicles.kenteken");
            String menuitem = NBTUtils.getString(e.getCurrentItem(), "mtvehicles.item");
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
                VehicleEntityEvent.kofferbak(p, ken);
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
            String menuitem = NBTUtils.getString(e.getCurrentItem(), "mtvehicles.item");
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
            if (e.getCurrentItem().getItemMeta().getDisplayName().contains("Ja")) {
                String carUuid = NBTUtils.getString(p.getInventory().getItemInMainHand(), "mtvehicles.item");
                if (Vehicle.getByDamage(p, carUuid) == null){
                    p.sendMessage(ConfigModule.messagesConfig.getMessage("giveCarNotFound"));
                    p.closeInventory();
                    return;
                }
                p.sendMessage(ConfigModule.messagesConfig.getMessage(TextUtils.colorize("voucherRedeem")));
                p.getInventory().getItemInMainHand().setAmount(p.getInventory().getItemInMainHand().getAmount() - 1);
                p.getInventory().addItem(Vehicle.getByDamage(p, carUuid));
                p.closeInventory();
            }
            if (e.getCurrentItem().getItemMeta().getDisplayName().contains("Nee")) {
                p.closeInventory();
            }
        }
    }
}
