package nl.mtvehicles.core.listeners.inventory;

import de.tr7zw.changeme.nbtapi.NBTItem;
import nl.mtvehicles.core.Main;
import nl.mtvehicles.core.commands.vehiclesubs.VehicleEdit;
import nl.mtvehicles.core.commands.vehiclesubs.VehicleMenu;
import nl.mtvehicles.core.infrastructure.annotations.ToDo;
import nl.mtvehicles.core.infrastructure.dataconfig.MessagesConfig;
import nl.mtvehicles.core.infrastructure.dataconfig.VehicleDataConfig;
import nl.mtvehicles.core.infrastructure.enums.Language;
import nl.mtvehicles.core.infrastructure.enums.Message;
import nl.mtvehicles.core.infrastructure.helpers.ItemUtils;
import nl.mtvehicles.core.infrastructure.helpers.LanguageUtils;
import nl.mtvehicles.core.infrastructure.helpers.MenuUtils;
import nl.mtvehicles.core.infrastructure.models.Vehicle;
import nl.mtvehicles.core.infrastructure.models.VehicleUtils;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.permissions.PermissionAttachmentInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static nl.mtvehicles.core.infrastructure.helpers.MenuUtils.backItem;
import static nl.mtvehicles.core.infrastructure.helpers.MenuUtils.closeItem;

public class InventoryClickListener implements Listener {

    public HashMap<UUID, ItemStack> vehicleMenu = new HashMap<>();
    public static HashMap<UUID, Inventory> skinMenu = new HashMap<>();
    public static HashMap<UUID, Integer> intSave = new HashMap<>();

    public static HashMap<UUID, Integer> id = new HashMap<>();
    public static HashMap<UUID, Integer> raw = new HashMap<>();

    private ItemStack clickedItem;
    private int clickedSlot;
    private String title;
    private Player p;

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getCurrentItem() == null) return;
        if (!e.getCurrentItem().hasItemMeta()) return;

        clickedItem = e.getCurrentItem();
        clickedSlot = e.getRawSlot();
        title = e.getView().getTitle();
        p = (Player) e.getWhoClicked();

        e.setCancelled(true);

        if (title.contains("Vehicle Menu")) vehicleMenu();
        else if (title.contains("Choose your vehicle")) chooseVehicleMenu();
        else if (title.contains("Choose your language")) chooseLanguageMenu();
        else if (title.contains("Confirm getting vehicle")) confirmVehicleMenu();
        else if (title.contains("Vehicle Restore")) vehicleRestoreMenu();
        else if (title.contains("Vehicle Edit")) vehicleEditMenu();
        else if (title.contains("Vehicle Settings")) vehicleSettingsMenu();
        else if (title.contains("Vehicle Benzine")) vehicleBenzineMenu();
        else if (title.contains("Vehicle Kofferbak")) vehicleTrunkMenu();
        else if (title.contains("Vehicle Members")) vehicleMembersMenu();
        else if (title.contains("Vehicle Speed")) vehicleSpeedMenu();
        else if (title.contains("Benzine menu")) benzineMenu();
        else if (title.contains("Voucher Redeem Menu")) voucherRedeemMenu();
        else e.setCancelled(false);
    }

    private void vehicleMenu(){
        id.put(p.getUniqueId(), 1);
        raw.put(p.getUniqueId(), clickedSlot);
        MenuUtils.getvehicleCMD(p, id.get(p.getUniqueId()), raw.get(p.getUniqueId()));
    }

    @ToDo(comment = "To be translated.")
    private void chooseVehicleMenu(){
        if (clickedItem.equals(closeItem)) {
            p.closeInventory();
            return;
        }
        if (clickedItem.equals(backItem)) {
            p.openInventory(VehicleMenu.beginMenu.get(p.getUniqueId()));
            return;
        }

        if (clickedItem.equals(ItemUtils.mItem("STAINED_GLASS_PANE", 1, (short) 0, "&c", "&c"))) return;

        if (clickedItem.equals(ItemUtils.mItem("SPECTRAL_ARROW", 1, (short) 0, "&cVolgende Pagina", "&c"))) {
            MenuUtils.getvehicleCMD(p, id.get(p.getUniqueId()) + 1, raw.get(p.getUniqueId()));
            id.put(p.getUniqueId(), id.get(p.getUniqueId()) + 1);
            return;
        }
        if (clickedItem.equals(ItemUtils.mItem("SPECTRAL_ARROW", 1, (short) 0, "&cVorige Pagina", "&c"))) {
            if (id.get(p.getUniqueId()) > 1) {
                MenuUtils.getvehicleCMD(p, id.get(p.getUniqueId()) - 1, raw.get(p.getUniqueId()));
                id.put(p.getUniqueId(), id.get(p.getUniqueId()) - 1);
            }
            return;
        }

        vehicleMenu.put(p.getUniqueId(), clickedItem);
        Inventory inv = Bukkit.createInventory(null, 27, "Confirm getting vehicle");
        MessagesConfig msg = ConfigModule.messagesConfig;
        inv.setItem(11, ItemUtils.woolItem("WOOL", "RED_WOOL", 1, (short) 14, "&c" + msg.getMessage(Message.CANCEL), String.format("&7%s", msg.getMessage(Message.CANCEL_ACTION))));
        inv.setItem(15, ItemUtils.woolItem("WOOL", "LIME_WOOL", 1, (short) 5, "&a"  + msg.getMessage(Message.CONFIRM), String.format("&7%s@&7%s", msg.getMessage(Message.CONFIRM_ACTION), msg.getMessage(Message.CONFIRM_VEHICLE_MENU))));
        p.openInventory(inv);
    }

    private void chooseLanguageMenu(){
        if (clickedSlot == 0) LanguageUtils.changeLanguage(p, Language.EN);
        else if (clickedSlot == 1) LanguageUtils.changeLanguage(p, Language.NL);
        else if (clickedSlot == 2) LanguageUtils.changeLanguage(p, Language.ES);
        else if (clickedSlot == 3) LanguageUtils.changeLanguage(p, Language.CS);
        else if (clickedSlot == 4) LanguageUtils.changeLanguage(p, Language.DE);
        else if (clickedSlot == 5) LanguageUtils.changeLanguage(p, Language.CN);
        else if (clickedSlot == 6) LanguageUtils.changeLanguage(p, Language.TR);
        else if (clickedSlot == 8) {
            LanguageUtils.languageCheck.put(p.getUniqueId(), false);
            p.sendMessage("§6You may find more information here: §e§nhttps://github.com/GamerJoep/MinetopiaVehicles/wiki/Translate-the-plugin");
        }
        p.closeInventory();
    }

    private void confirmVehicleMenu(){
        if (clickedSlot == 11) { //cancel getting vehicle
            p.openInventory(skinMenu.get(p.getUniqueId()));
        } else if (clickedSlot == 15) { //accepting getting vehicle
            if (!canGetVehicleFromMenu(p)) {
                p.closeInventory();
                return;
            }

            List<Map<?, ?>> vehicles = ConfigModule.vehiclesConfig.getConfig().getMapList("voertuigen");
            ConfigModule.messagesConfig.sendMessage(p, Message.COMPLETED_VEHICLE_GIVE);
            p.getInventory().addItem(vehicleMenu.get(p.getUniqueId()));

            NBTItem nbt = new NBTItem(vehicleMenu.get(p.getUniqueId()));
            String licensePlate = nbt.getString("mtvehicles.kenteken");
            String vehicleName = nbt.getString("mtvehicles.naam");

            Vehicle vehicle = new Vehicle();
            List<String> members = ConfigModule.vehicleDataConfig.getMembers(licensePlate);
            List<String> riders = ConfigModule.vehicleDataConfig.getRiders(licensePlate);
            List<String> trunkData = ConfigModule.vehicleDataConfig.getTrunkData(licensePlate);

            vehicle.setLicensePlate(licensePlate);
            vehicle.setName(vehicleName);
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
            vehicle.setTrunkData(trunkData);
            vehicle.setAccelerationSpeed((Double) vehicles.get(intSave.get(p.getUniqueId())).get("acceleratieSpeed"));
            vehicle.setMaxSpeed((Double) vehicles.get(intSave.get(p.getUniqueId())).get("maxSpeed"));
            vehicle.setBrakingSpeed((Double) vehicles.get(intSave.get(p.getUniqueId())).get("brakingSpeed"));
            vehicle.setFrictionSpeed((Double) vehicles.get(intSave.get(p.getUniqueId())).get("aftrekkenSpeed"));
            vehicle.setRotateSpeed((Integer) vehicles.get(intSave.get(p.getUniqueId())).get("rotateSpeed"));
            vehicle.setMaxSpeedBackwards((Double) vehicles.get(intSave.get(p.getUniqueId())).get("maxSpeedBackwards"));
            vehicle.setOwner(p.getUniqueId());
            vehicle.setNbtValue(nbt.getString("mtcustom"));
            vehicle.setRiders(riders);
            vehicle.setMembers(members);
            vehicle.save();

            p.closeInventory();
        }
    }

    @ToDo(comment = "To be translated.")
    private void vehicleRestoreMenu(){
        if (clickedItem.equals(ItemUtils.mItem("STAINED_GLASS_PANE", 1, (short) 0, "&c", "&c"))) return;

        if (clickedItem.equals(ItemUtils.mItem("SPECTRAL_ARROW", 1, (short) 0, "&cVolgende Pagina", "&c"))) {
            MenuUtils.restoreCMD(p, Integer.parseInt(title.replace("Vehicle Restore ", "")) + 1, MenuUtils.restoreUUID.get("uuid"));
            return;
        }

        if (clickedItem.equals(ItemUtils.mItem("SPECTRAL_ARROW", 1, (short) 0, "&cVorige Pagina", "&c"))) {
            if (!(Integer.parseInt(title.replace("Vehicle Restore ", "")) - 1 < 1))
                MenuUtils.restoreCMD(p, Integer.parseInt(title.replace("Vehicle Restore ", "")) - 1, MenuUtils.restoreUUID.get("uuid"));
            return;
        }
        p.getInventory().addItem(clickedItem);
    }

    private void vehicleEditMenu(){
        switch (clickedSlot) {
            case 10:
                MenuUtils.menuEdit(p);
                break;
            case 11:
                MenuUtils.benzineEdit(p);
                break;
            case 12:
                MenuUtils.trunkEdit(p);
                break;
            case 13:
                MenuUtils.membersEdit(p);
                break;
            case 14:
                MenuUtils.speedEdit(p);
                break;
            case 16: //Delete
                NBTItem nbt = new NBTItem(p.getInventory().getItemInMainHand());
                String licensePlate = nbt.getString("mtvehicles.kenteken");
                VehicleUtils.getByLicensePlate(licensePlate).delete();
                p.getInventory().getItemInMainHand().setAmount(0);
                p.closeInventory();
                break;
        }
    }

    @ToDo(comment = "To be translated.")
    private void vehicleSettingsMenu(){
        if (clickedItem.equals(closeItem)) {
            p.closeInventory();
            return;
        }
        if (clickedItem.equals(backItem)) {
            VehicleEdit.editMenu(p, p.getInventory().getItemInMainHand());
            return;
        }

        NBTItem nbt = new NBTItem(p.getInventory().getItemInMainHand());
        String licensePlate = nbt.getString("mtvehicles.kenteken");

        if (clickedItem.equals(ItemUtils.glowItem("BOOK", "&6Glow Aanpassen", "&7Huidige: &e" + ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.IS_GLOWING)))) {
            ItemMeta itemMeta = p.getInventory().getItemInMainHand().getItemMeta();
            itemMeta.removeEnchant(Enchantment.ARROW_INFINITE);
            itemMeta.removeItemFlags(ItemFlag.HIDE_ENCHANTS);
            p.getInventory().getItemInMainHand().setItemMeta(itemMeta);

            ConfigModule.vehicleDataConfig.set(licensePlate, VehicleDataConfig.Option.IS_GLOWING, false);
            ConfigModule.vehicleDataConfig.save();
            MenuUtils.menuEdit(p);
        }

        if (clickedItem.equals(ItemUtils.mItem("BOOK", 1, (short) 0, "&6Glow Aanpassen", "&7Huidige: &e" + ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.IS_GLOWING)))) {
            ItemMeta itemMeta = p.getInventory().getItemInMainHand().getItemMeta();
            itemMeta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            p.getInventory().getItemInMainHand().setItemMeta(itemMeta);

            ConfigModule.vehicleDataConfig.set(licensePlate, VehicleDataConfig.Option.IS_GLOWING, true);
            ConfigModule.vehicleDataConfig.save();
            MenuUtils.menuEdit(p);
        }

        if (clickedItem.equals(ItemUtils.mItem("PAPER", 1, (short) 0, "&6Kenteken Aanpassen", "&7Huidige: &e" + licensePlate))) {
            p.closeInventory();
            ConfigModule.messagesConfig.sendMessage(p, Message.TYPE_LICENSE_IN_CHAT);
            ItemUtils.edit.put(p.getUniqueId() + ".kenteken", true);
        }

        if (clickedItem.getDurability() == (short) ConfigModule.vehicleDataConfig.getDamage(licensePlate)) {
            p.closeInventory();
            ConfigModule.messagesConfig.sendMessage(p, Message.TYPE_NAME_IN_CHAT);
            ItemUtils.edit.put(p.getUniqueId() + ".naam", true);
        }
    }

    private void vehicleBenzineMenu(){
        if (clickedItem.equals(closeItem)) {
            p.closeInventory();
            return;
        }
        if (clickedItem.equals(backItem)) {
            VehicleEdit.editMenu(p, p.getInventory().getItemInMainHand());
            return;
        }

        NBTItem nbt = new NBTItem(p.getInventory().getItemInMainHand());
        String licensePlate = nbt.getString("mtvehicles.kenteken");
        String menuItem = new NBTItem(clickedItem).getString("mtvehicles.item");

        if (menuItem.contains("1")) {
            if ((boolean) ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.FUEL_ENABLED))
                ConfigModule.vehicleDataConfig.set(licensePlate, VehicleDataConfig.Option.FUEL_ENABLED, false);
            else
                ConfigModule.vehicleDataConfig.set(licensePlate, VehicleDataConfig.Option.FUEL_ENABLED, true);

            ConfigModule.vehicleDataConfig.save();
            MenuUtils.benzineEdit(p);
        }
        if (menuItem.contains("2")) {
            p.closeInventory();
            ConfigModule.messagesConfig.sendMessage(p, Message.TYPE_NEW_BENZINE_IN_CHAT);
            ItemUtils.edit.put(p.getUniqueId() + ".benzine", true);
        }
        if (menuItem.contains("3")) {
            p.closeInventory();
            ConfigModule.messagesConfig.sendMessage(p, Message.TYPE_NEW_BENZINE_IN_CHAT);
            ItemUtils.edit.put(p.getUniqueId() + ".benzineverbruik", true);
        }
    }

    private void vehicleTrunkMenu(){
        if (clickedItem.equals(closeItem)) {
            p.closeInventory();
            return;
        }
        if (clickedItem.equals(backItem)) {
            VehicleEdit.editMenu(p, p.getInventory().getItemInMainHand());
            return;
        }

        NBTItem nbt = new NBTItem(p.getInventory().getItemInMainHand());
        String licensePlate = nbt.getString("mtvehicles.kenteken");
        String menuItem = new NBTItem(clickedItem).getString("mtvehicles.item");

        if (menuItem.contains("1")) {
            if ((boolean) ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.TRUNK_ENABLED))
                ConfigModule.vehicleDataConfig.set(licensePlate, VehicleDataConfig.Option.TRUNK_ENABLED, false);
            else
                ConfigModule.vehicleDataConfig.set(licensePlate, VehicleDataConfig.Option.TRUNK_ENABLED, true);

            ConfigModule.vehicleDataConfig.save();
            MenuUtils.trunkEdit(p);
        }
        if (menuItem.contains("2")) {
            p.closeInventory();
            ConfigModule.messagesConfig.sendMessage(p, Message.TYPE_NEW_ROWS_IN_CHAT);
            ItemUtils.edit.put(p.getUniqueId() + ".kofferbakRows", true);
        }
        if (menuItem.contains("3")) {
            p.closeInventory();
            VehicleUtils.openTrunk(p, licensePlate);
        }
    }

    private void vehicleMembersMenu(){
        if (clickedItem.equals(closeItem))
            p.closeInventory();
        else if (clickedItem.equals(backItem))
            VehicleEdit.editMenu(p, p.getInventory().getItemInMainHand());
    }

    private void vehicleSpeedMenu(){
        if (clickedItem.equals(closeItem)) {
            p.closeInventory();
            return;
        }
        if (clickedItem.equals(backItem)) {
            VehicleEdit.editMenu(p, p.getInventory().getItemInMainHand());
            return;
        }

        String menuItem = new NBTItem(clickedItem).getString("mtvehicles.item");
        if (menuItem.contains("1")) {
            p.closeInventory();
            ConfigModule.messagesConfig.sendMessage(p, Message.TYPE_SPEED_IN_CHAT);
            ItemUtils.edit.put(p.getUniqueId() + ".acceleratieSpeed", true);
        }
        if (menuItem.contains("2")) {
            p.closeInventory();
            ConfigModule.messagesConfig.sendMessage(p, Message.TYPE_SPEED_IN_CHAT);
            ItemUtils.edit.put(p.getUniqueId() + ".maxSpeed", true);
        }
        if (menuItem.contains("3")) {
            p.closeInventory();
            ConfigModule.messagesConfig.sendMessage(p, Message.TYPE_SPEED_IN_CHAT);
            ItemUtils.edit.put(p.getUniqueId() + ".brakingSpeed", true);
        }
        if (menuItem.contains("4")) {
            p.closeInventory();
            ConfigModule.messagesConfig.sendMessage(p, Message.TYPE_SPEED_IN_CHAT);
            ItemUtils.edit.put(p.getUniqueId() + ".aftrekkenSpeed", true);
        }
        if (menuItem.contains("5")) {
            p.closeInventory();
            ConfigModule.messagesConfig.sendMessage(p, Message.TYPE_SPEED_IN_CHAT);
            ItemUtils.edit.put(p.getUniqueId() + ".rotateSpeed", true);
        }
        if (menuItem.contains("6")) {
            p.closeInventory();
            ConfigModule.messagesConfig.sendMessage(p, Message.TYPE_SPEED_IN_CHAT);
            ItemUtils.edit.put(p.getUniqueId() + ".maxSpeedBackwards", true);
        }
    }

    private void benzineMenu(){
        p.getInventory().addItem(clickedItem);
    }

    private void voucherRedeemMenu(){
        if (clickedSlot == 15) { //Yes
            String carUuid = new NBTItem(p.getInventory().getItemInMainHand()).getString("mtvehicles.item");
            if (VehicleUtils.getItemByUUID(p, carUuid) == null){
                p.sendMessage(ConfigModule.messagesConfig.getMessage(Message.GIVE_CAR_NOT_FOUND));
                p.closeInventory();
                return;
            }
            p.sendMessage(ConfigModule.messagesConfig.getMessage(Message.VOUCHER_REDEEM));
            p.getInventory().getItemInMainHand().setAmount(p.getInventory().getItemInMainHand().getAmount() - 1);
            p.getInventory().addItem(VehicleUtils.getItemByUUID(p, carUuid));
        }

        p.closeInventory();
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
        if (!returns) ConfigModule.messagesConfig.sendMessage(p, Message.TOO_MANY_VEHICLES);

        return returns;
    }
}
