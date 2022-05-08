package nl.mtvehicles.core.listeners.inventory;

import de.tr7zw.changeme.nbtapi.NBTItem;
import nl.mtvehicles.core.Main;
import nl.mtvehicles.core.commands.vehiclesubs.VehicleEdit;
import nl.mtvehicles.core.commands.vehiclesubs.VehicleMenu;
import nl.mtvehicles.core.events.inventory.InventoryClickEvent;
import nl.mtvehicles.core.infrastructure.dataconfig.MessagesConfig;
import nl.mtvehicles.core.infrastructure.dataconfig.VehicleDataConfig;
import nl.mtvehicles.core.infrastructure.enums.InventoryTitle;
import nl.mtvehicles.core.infrastructure.enums.Language;
import nl.mtvehicles.core.infrastructure.enums.Message;
import nl.mtvehicles.core.infrastructure.helpers.ItemUtils;
import nl.mtvehicles.core.infrastructure.helpers.LanguageUtils;
import nl.mtvehicles.core.infrastructure.helpers.MenuUtils;
import nl.mtvehicles.core.infrastructure.helpers.TextUtils;
import nl.mtvehicles.core.infrastructure.models.MTVListener;
import nl.mtvehicles.core.infrastructure.models.Vehicle;
import nl.mtvehicles.core.infrastructure.models.VehicleUtils;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import nl.mtvehicles.core.listeners.VehicleVoucherListener;
import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.permissions.PermissionAttachmentInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static nl.mtvehicles.core.infrastructure.helpers.MenuUtils.getBackItem;
import static nl.mtvehicles.core.infrastructure.helpers.MenuUtils.getCloseItem;

public class InventoryClickListener extends MTVListener {

    public HashMap<UUID, ItemStack> vehicleMenu = new HashMap<>();
    public static HashMap<UUID, Inventory> skinMenu = new HashMap<>();
    public static HashMap<UUID, Integer> intSave = new HashMap<>();

    public static HashMap<UUID, Integer> id = new HashMap<>();
    public static HashMap<UUID, Integer> raw = new HashMap<>();

    private ItemStack clickedItem;
    private int clickedSlot;
    private InventoryTitle title;

    public InventoryClickListener(){
        super(new InventoryClickEvent());
    }

    @EventHandler
    public void onClick(org.bukkit.event.inventory.InventoryClickEvent event) {
        this.event = event;
        if (event.getCurrentItem() == null) return;
        if (!event.getCurrentItem().hasItemMeta()) return;

        String stringTitle = event.getView().getTitle();

        clickedItem = event.getCurrentItem();
        clickedSlot = event.getRawSlot();
        player = (Player) event.getWhoClicked();

        if (InventoryTitle.getByStringTitle(stringTitle) == null) return;
        title = InventoryTitle.getByStringTitle(stringTitle);

        InventoryClickEvent api = (InventoryClickEvent) getAPI();
        api.setClickedSlot(clickedSlot);
        api.setTitle(title);
        callAPI();
        if (isCancelled()) return;

        clickedSlot = api.getClickedSlot();
        title = api.getTitle();

        event.setCancelled(true);

        if (title.equals(InventoryTitle.VEHICLE_MENU)) vehicleMenu();
        else if (title.equals(InventoryTitle.CHOOSE_VEHICLE_MENU)) chooseVehicleMenu();
        else if (title.equals(InventoryTitle.CHOOSE_LANGUAGE_MENU)) chooseLanguageMenu();
        else if (title.equals(InventoryTitle.CONFIRM_VEHICLE_MENU)) confirmVehicleMenu();
        else if (title.equals(InventoryTitle.VEHICLE_RESTORE_MENU)) vehicleRestoreMenu();
        else if (title.equals(InventoryTitle.VEHICLE_EDIT_MENU)) vehicleEditMenu();
        else if (title.equals(InventoryTitle.VEHICLE_SETTINGS_MENU)) vehicleSettingsMenu();
        else if (title.equals(InventoryTitle.VEHICLE_FUEL_MENU)) vehicleFuelMenu();
        else if (title.equals(InventoryTitle.VEHICLE_TRUNK_MENU)) vehicleTrunkMenu();
        else if (title.equals(InventoryTitle.VEHICLE_MEMBERS_MENU)) vehicleMembersMenu();
        else if (title.equals(InventoryTitle.VEHICLE_SPEED_MENU)) vehicleSpeedMenu();
        else if (title.equals(InventoryTitle.JERRYCAN_MENU)) jerryCanMenu();
        else if (title.equals(InventoryTitle.VOUCHER_REDEEM_MENU)) voucherRedeemMenu();
        else event.setCancelled(false);
    }

    private void vehicleMenu(){
        id.put(player.getUniqueId(), 1);
        raw.put(player.getUniqueId(), clickedSlot);
        MenuUtils.getvehicleCMD(player, id.get(player.getUniqueId()), raw.get(player.getUniqueId()));
    }

    private void chooseVehicleMenu(){
        if (clickedItem.equals(getCloseItem())) {
            player.closeInventory();
            return;
        }
        if (clickedItem.equals(getBackItem())) {
            player.openInventory(VehicleMenu.beginMenu.get(player.getUniqueId()));
            return;
        }

        if (clickedItem.equals(ItemUtils.getMenuItem(ItemUtils.getStainedGlassPane(), 1, "&c", "&c"))) return;

        if (clickedSlot == 53) { //Next page
            MenuUtils.getvehicleCMD(player, id.get(player.getUniqueId()) + 1, raw.get(player.getUniqueId()));
            id.put(player.getUniqueId(), id.get(player.getUniqueId()) + 1);
            return;
        }
        if (clickedSlot == 45) { //Previous page
            if (id.get(player.getUniqueId()) > 1) {
                MenuUtils.getvehicleCMD(player, id.get(player.getUniqueId()) - 1, raw.get(player.getUniqueId()));
                id.put(player.getUniqueId(), id.get(player.getUniqueId()) - 1);
            }
            return;
        }

        vehicleMenu.put(player.getUniqueId(), clickedItem);
        Inventory inv = Bukkit.createInventory(null, 27, InventoryTitle.CONFIRM_VEHICLE_MENU.getStringTitle());
        MessagesConfig msg = ConfigModule.messagesConfig;
        inv.setItem(11, ItemUtils.getMenuItem(
                "RED_WOOL",
                "WOOL",
                (short) 14,
                1,
                "&c" + msg.getMessage(Message.CANCEL),
                "&7" + msg.getMessage(Message.CANCEL_ACTION)
        ));
        inv.setItem(15, ItemUtils.getMenuItem(
                "LIME_WOOL",
                "WOOL",
                (short) 5,
                1,
                "&a"  + msg.getMessage(Message.CONFIRM),
                "&7" + msg.getMessage(Message.CONFIRM_ACTION), "&7" + msg.getMessage(Message.CONFIRM_VEHICLE_GIVE)
        ));
        player.openInventory(inv);
    }

    private void chooseLanguageMenu(){
        if (clickedSlot == 0) LanguageUtils.changeLanguage(player, Language.EN);
        else if (clickedSlot == 1) LanguageUtils.changeLanguage(player, Language.NL);
        else if (clickedSlot == 2) LanguageUtils.changeLanguage(player, Language.ES);
        else if (clickedSlot == 3) LanguageUtils.changeLanguage(player, Language.CS);
        else if (clickedSlot == 4) LanguageUtils.changeLanguage(player, Language.DE);
        else if (clickedSlot == 5) LanguageUtils.changeLanguage(player, Language.CN);
        else if (clickedSlot == 6) LanguageUtils.changeLanguage(player, Language.TR);
        else if (clickedSlot == 8) {
            LanguageUtils.languageCheck.put(player.getUniqueId(), false);
            player.sendMessage("§6You may find more information here: §e§nhttps://wiki.mtvehicles.eu/translating.html");
        }
        player.closeInventory();
    }

    private void confirmVehicleMenu(){
        if (clickedSlot == 11) { //cancel getting vehicle
            player.openInventory(skinMenu.get(player.getUniqueId()));
        } else if (clickedSlot == 15) { //accepting getting vehicle
            if (!canGetVehicleFromMenu()) {
                player.closeInventory();
                return;
            }

            List<Map<?, ?>> vehicles = ConfigModule.vehiclesConfig.getVehicles();
            ConfigModule.messagesConfig.sendMessage(player, Message.COMPLETED_VEHICLE_GIVE);
            player.getInventory().addItem(vehicleMenu.get(player.getUniqueId()));

            NBTItem nbt = new NBTItem(vehicleMenu.get(player.getUniqueId()));
            String licensePlate = nbt.getString("mtvehicles.kenteken");
            String vehicleName = nbt.getString("mtvehicles.naam");

            Vehicle vehicle = new Vehicle();
            List<String> members = ConfigModule.vehicleDataConfig.getMembers(licensePlate);
            List<String> riders = ConfigModule.vehicleDataConfig.getRiders(licensePlate);
            List<String> trunkData = ConfigModule.vehicleDataConfig.getTrunkData(licensePlate);

            vehicle.setLicensePlate(licensePlate);
            vehicle.setName(vehicleName);
            vehicle.setVehicleType((String) vehicles.get(intSave.get(player.getUniqueId())).get("vehicleType"));
            vehicle.setSkinDamage(vehicleMenu.get(player.getUniqueId()).getDurability());
            vehicle.setSkinItem(vehicleMenu.get(player.getUniqueId()).getType().toString());
            vehicle.setGlow(false);
            vehicle.setHornEnabled((Boolean) vehicles.get(intSave.get(player.getUniqueId())).get("hornEnabled"));
            vehicle.setHealth((double) vehicles.get(intSave.get(player.getUniqueId())).get("maxHealth"));
            vehicle.setBenzineEnabled((Boolean) vehicles.get(intSave.get(player.getUniqueId())).get("benzineEnabled"));
            vehicle.setFuel(100);
            vehicle.setTrunk((Boolean) vehicles.get(intSave.get(player.getUniqueId())).get("kofferbakEnabled"));
            vehicle.setTrunkRows(1);
            vehicle.setFuelUsage(0.01);
            vehicle.setTrunkData(trunkData);
            vehicle.setAccelerationSpeed((Double) vehicles.get(intSave.get(player.getUniqueId())).get("acceleratieSpeed"));
            vehicle.setMaxSpeed((Double) vehicles.get(intSave.get(player.getUniqueId())).get("maxSpeed"));
            vehicle.setBrakingSpeed((Double) vehicles.get(intSave.get(player.getUniqueId())).get("brakingSpeed"));
            vehicle.setFrictionSpeed((Double) vehicles.get(intSave.get(player.getUniqueId())).get("aftrekkenSpeed"));
            vehicle.setRotateSpeed((Integer) vehicles.get(intSave.get(player.getUniqueId())).get("rotateSpeed"));
            vehicle.setMaxSpeedBackwards((Double) vehicles.get(intSave.get(player.getUniqueId())).get("maxSpeedBackwards"));
            vehicle.setOwner(player.getUniqueId());
            vehicle.setNbtValue(nbt.getString("mtcustom"));
            vehicle.setRiders(riders);
            vehicle.setMembers(members);
            vehicle.save();

            player.closeInventory();
        }
    }

    private void vehicleRestoreMenu(){
        if (clickedItem.equals(ItemUtils.getMenuItem(ItemUtils.getStainedGlassPane(), 1, "&c", "&c"))) return;

        if (clickedSlot == 53) { //Next page
            MenuUtils.restoreCMD(player, MenuUtils.restoreId.get("pagina") + 1, MenuUtils.restoreUUID.get("uuid"));
            return;
        }

        if (clickedSlot == 45) { //Previous page
            if (MenuUtils.restoreId.get("pagina") - 1 >= 1)
                MenuUtils.restoreCMD(player, MenuUtils.restoreId.get("pagina") - 1, MenuUtils.restoreUUID.get("uuid"));
            return;
        }
        player.getInventory().addItem(clickedItem);
    }

    private void vehicleEditMenu(){
        switch (clickedSlot) {
            case 10:
                MenuUtils.menuEdit(player);
                break;
            case 11:
                MenuUtils.benzineEdit(player);
                break;
            case 12:
                MenuUtils.trunkEdit(player);
                break;
            case 13:
                MenuUtils.membersEdit(player);
                break;
            case 14:
                MenuUtils.speedEdit(player);
                break;
            case 16: //Delete
                try {
                    NBTItem nbt = new NBTItem(player.getInventory().getItemInMainHand());
                    String licensePlate = nbt.getString("mtvehicles.kenteken");
                    VehicleUtils.getByLicensePlate(licensePlate).delete();
                    player.sendMessage(TextUtils.colorize(ConfigModule.messagesConfig.getMessage(Message.VEHICLE_DELETED)));
                } catch (Exception e){
                    player.sendMessage(TextUtils.colorize(ConfigModule.messagesConfig.getMessage(Message.VEHICLE_ALREADY_DELETED)));
                }
                player.getInventory().getItemInMainHand().setAmount(0);
                player.closeInventory();
                break;
        }
    }

    private void vehicleSettingsMenu(){
        if (clickedItem.equals(getCloseItem())) {
            player.closeInventory();
            return;
        }
        if (clickedItem.equals(getBackItem())) {
            VehicleEdit.editMenu(player, player.getInventory().getItemInMainHand());
            return;
        }

        NBTItem nbt = new NBTItem(player.getInventory().getItemInMainHand());
        String licensePlate = nbt.getString("mtvehicles.kenteken");

        boolean isGlowing = (boolean) ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.IS_GLOWING);

        if (clickedSlot == 16){
            ItemMeta itemMeta = player.getInventory().getItemInMainHand().getItemMeta();
            if (isGlowing) {
                itemMeta.removeEnchant(Enchantment.ARROW_INFINITE);
                itemMeta.removeItemFlags(ItemFlag.HIDE_ENCHANTS);
                ConfigModule.vehicleDataConfig.set(licensePlate, VehicleDataConfig.Option.IS_GLOWING, false);
            } else {
                itemMeta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
                itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                ConfigModule.vehicleDataConfig.set(licensePlate, VehicleDataConfig.Option.IS_GLOWING, true);
            }
            player.getInventory().getItemInMainHand().setItemMeta(itemMeta);
            ConfigModule.vehicleDataConfig.save();
            MenuUtils.menuEdit(player);
        }

        if (clickedSlot == 13) {
            player.closeInventory();
            ConfigModule.messagesConfig.sendMessage(player, Message.TYPE_LICENSE_IN_CHAT);
            ItemUtils.edit.put(player.getUniqueId() + ".kenteken", true);
        }

        if (clickedSlot == 10) {
            player.closeInventory();
            ConfigModule.messagesConfig.sendMessage(player, Message.TYPE_NAME_IN_CHAT);
            ItemUtils.edit.put(player.getUniqueId() + ".naam", true);
        }
    }

    private void vehicleFuelMenu(){
        if (clickedItem.equals(getCloseItem())) {
            player.closeInventory();
            return;
        }
        if (clickedItem.equals(getBackItem())) {
            VehicleEdit.editMenu(player, player.getInventory().getItemInMainHand());
            return;
        }

        NBTItem nbt = new NBTItem(player.getInventory().getItemInMainHand());
        String licensePlate = nbt.getString("mtvehicles.kenteken");
        String menuItem = new NBTItem(clickedItem).getString("mtvehicles.item");

        if (menuItem.contains("1")) {
            if ((boolean) ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.FUEL_ENABLED))
                ConfigModule.vehicleDataConfig.set(licensePlate, VehicleDataConfig.Option.FUEL_ENABLED, false);
            else
                ConfigModule.vehicleDataConfig.set(licensePlate, VehicleDataConfig.Option.FUEL_ENABLED, true);

            ConfigModule.vehicleDataConfig.save();
            MenuUtils.benzineEdit(player);
        }
        if (menuItem.contains("2")) {
            player.closeInventory();
            ConfigModule.messagesConfig.sendMessage(player, Message.TYPE_NEW_BENZINE_IN_CHAT);
            ItemUtils.edit.put(player.getUniqueId() + ".benzine", true);
        }
        if (menuItem.contains("3")) {
            player.closeInventory();
            ConfigModule.messagesConfig.sendMessage(player, Message.TYPE_NEW_BENZINE_IN_CHAT);
            ItemUtils.edit.put(player.getUniqueId() + ".benzineverbruik", true);
        }
    }

    private void vehicleTrunkMenu(){
        if (clickedItem.equals(getCloseItem())) {
            player.closeInventory();
            return;
        }
        if (clickedItem.equals(getBackItem())) {
            VehicleEdit.editMenu(player, player.getInventory().getItemInMainHand());
            return;
        }

        NBTItem nbt = new NBTItem(player.getInventory().getItemInMainHand());
        String licensePlate = nbt.getString("mtvehicles.kenteken");
        String menuItem = new NBTItem(clickedItem).getString("mtvehicles.item");

        if (menuItem.contains("1")) {
            if ((boolean) ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.TRUNK_ENABLED))
                ConfigModule.vehicleDataConfig.set(licensePlate, VehicleDataConfig.Option.TRUNK_ENABLED, false);
            else
                ConfigModule.vehicleDataConfig.set(licensePlate, VehicleDataConfig.Option.TRUNK_ENABLED, true);

            ConfigModule.vehicleDataConfig.save();
            MenuUtils.trunkEdit(player);
        }
        if (menuItem.contains("2")) {
            player.closeInventory();
            ConfigModule.messagesConfig.sendMessage(player, Message.TYPE_NEW_ROWS_IN_CHAT);
            ItemUtils.edit.put(player.getUniqueId() + ".kofferbakRows", true);
        }
        if (menuItem.contains("3")) {
            player.closeInventory();
            VehicleUtils.openTrunk(player, licensePlate);
        }
    }

    private void vehicleMembersMenu(){
        if (clickedItem.equals(getCloseItem()))
            player.closeInventory();
        else if (clickedItem.equals(getBackItem()))
            VehicleEdit.editMenu(player, player.getInventory().getItemInMainHand());
    }

    private void vehicleSpeedMenu(){
        if (clickedItem.equals(getCloseItem())) {
            player.closeInventory();
            return;
        }
        if (clickedItem.equals(getBackItem())) {
            VehicleEdit.editMenu(player, player.getInventory().getItemInMainHand());
            return;
        }

        String menuItem = new NBTItem(clickedItem).getString("mtvehicles.item");
        if (menuItem.contains("1")) {
            player.closeInventory();
            ConfigModule.messagesConfig.sendMessage(player, Message.TYPE_SPEED_IN_CHAT);
            ItemUtils.edit.put(player.getUniqueId() + ".acceleratieSpeed", true);
        }
        if (menuItem.contains("2")) {
            player.closeInventory();
            ConfigModule.messagesConfig.sendMessage(player, Message.TYPE_SPEED_IN_CHAT);
            ItemUtils.edit.put(player.getUniqueId() + ".maxSpeed", true);
        }
        if (menuItem.contains("3")) {
            player.closeInventory();
            ConfigModule.messagesConfig.sendMessage(player, Message.TYPE_SPEED_IN_CHAT);
            ItemUtils.edit.put(player.getUniqueId() + ".brakingSpeed", true);
        }
        if (menuItem.contains("4")) {
            player.closeInventory();
            ConfigModule.messagesConfig.sendMessage(player, Message.TYPE_SPEED_IN_CHAT);
            ItemUtils.edit.put(player.getUniqueId() + ".aftrekkenSpeed", true);
        }
        if (menuItem.contains("5")) {
            player.closeInventory();
            ConfigModule.messagesConfig.sendMessage(player, Message.TYPE_SPEED_IN_CHAT);
            ItemUtils.edit.put(player.getUniqueId() + ".rotateSpeed", true);
        }
        if (menuItem.contains("6")) {
            player.closeInventory();
            ConfigModule.messagesConfig.sendMessage(player, Message.TYPE_SPEED_IN_CHAT);
            ItemUtils.edit.put(player.getUniqueId() + ".maxSpeedBackwards", true);
        }
    }

    private void jerryCanMenu(){
        player.getInventory().addItem(clickedItem);
    }

    private void voucherRedeemMenu(){
        if (clickedSlot == 15) { //Yes
            String carUUID = VehicleVoucherListener.voucher.get(player);
            if (VehicleUtils.getItemByUUID(player, carUUID) == null){
                player.sendMessage(ConfigModule.messagesConfig.getMessage(Message.GIVE_CAR_NOT_FOUND));
                player.closeInventory();
                return;
            }
            player.sendMessage(ConfigModule.messagesConfig.getMessage(Message.VOUCHER_REDEEM));
            player.getInventory().getItemInMainHand().setAmount(player.getInventory().getItemInMainHand().getAmount() - 1);
            player.getInventory().addItem(VehicleUtils.getItemByUUID(player, carUUID));
        }

        VehicleVoucherListener.voucher.remove(player);
        player.closeInventory();
    }


    
    private boolean canGetVehicleFromMenu(){
        final int owned = ConfigModule.vehicleDataConfig.getNumberOfOwnedVehicles(player);
        int limit = -1; //If permission is not specified, players can get as many as they want

        if (player.hasPermission("mtvehicles.limit.*")) return true;

        for (PermissionAttachmentInfo permission: player.getEffectivePermissions()) {
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
        if (!returns) ConfigModule.messagesConfig.sendMessage(player, Message.TOO_MANY_VEHICLES);

        return returns;
    }
}
