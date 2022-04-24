package nl.mtvehicles.core.infrastructure.helpers;

import nl.mtvehicles.core.Main;
import nl.mtvehicles.core.infrastructure.annotations.ToDo;
import nl.mtvehicles.core.infrastructure.dataconfig.MessagesConfig;
import nl.mtvehicles.core.infrastructure.dataconfig.VehicleDataConfig;
import nl.mtvehicles.core.infrastructure.enums.Message;
import nl.mtvehicles.core.infrastructure.models.Vehicle;
import nl.mtvehicles.core.infrastructure.models.VehicleUtils;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import org.apache.commons.lang.RandomStringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

import static nl.mtvehicles.core.infrastructure.modules.VersionModule.getServerVersion;

@ToDo(comment = "Honestly, this code is awful. I hardly understand most of it. Also... why parsing to Strings and then parsing back to materials again?")
public class ItemUtils {
    public static HashMap<String, Boolean> edit = new HashMap<>();

    /**
     * Get material from string (e.g. 'DIAMOND_HOE'). Accepts legacy names.
     * @param string Material as a String
     * @return Material
     */
    public static Material getMaterial(String string){
        try {
            return Material.getMaterial(string);
        } catch (Exception e1){
            try {
                return Material.getMaterial(string, true);
            } catch (Exception e2){
                Main.logSevere("An error occurred while trying to obtain material from string '" + string + "'.");
                e2.printStackTrace();
                return null;
            }
        }
    }

    /**
     * Get item used in vehicle menu. Updated method (used to be #carItem(...)).
     */
    public static ItemStack getMenuVehicle(@NotNull Material material, int durability, String name){
        if (!material.isItem()) return null;
        ItemStack vehicle = (new ItemFactory(material))
                .setName(TextUtils.colorize("&6" + name))
                .setDurability(durability)
                .setUnbreakable(true)
                .setLore("&a")
                .toItemStack();
        return vehicle;
    }

    @Deprecated
    public static ItemStack carItem9(int durability, String name, String material, String model, String nbt) {
        ItemStack car = (new ItemFactory(Material.getMaterial(material))).setDurability(durability).setName(TextUtils.colorize("&6" + name)).toItemStack();
        ItemMeta im = car.getItemMeta();
        List<String> itemlore = new ArrayList<>();
        itemlore.add(TextUtils.colorize("&a"));
        im.setLore(itemlore);
        im.setUnbreakable(true);
        car.setItemMeta(im);
        return car;
    }

    /**
     * Create a new vehicle item (used in "Choose vehicle menu" and #getCarItem(...)). Updated method (used to be #carItem2(...)).
     */
    public static ItemStack getVehicleItem(@NotNull Material material, int durability, String name){
        if (!material.isItem()) return null;
        String licensePlate = generateLicencePlate();
        ItemStack vehicle = (new ItemFactory(material))
                .setDurability(durability)
                .setName(TextUtils.colorize("&6" + name))
                .setNBT("mtvehicles.kenteken", licensePlate)
                .setNBT("mtvehicles.naam", name)
                .setLore("&a", "&a" + licensePlate, "&a")
                .setUnbreakable(true)
                .toItemStack();
        return vehicle;
    }

    /**
     * Create a new vehicle item <b>with a custom NBT</b> (used in "Choose vehicle menu" and #getCarItem(...)). Updated method (used to be #carItem3(...)).
     */
    public static ItemStack getVehicleItem(@NotNull Material material, int durability, String name, String nbtKey, String nbtValue){
        if (!material.isItem()) return null;
        String licensePlate = generateLicencePlate();
        ItemStack vehicle = (new ItemFactory(material))
                .setDurability(durability)
                .setName(TextUtils.colorize("&6" + name))
                .setNBT("mtvehicles.kenteken", licensePlate)
                .setNBT("mtvehicles.naam", name)
                .setNBT(nbtKey, nbtValue)
                .setLore("&a", "&a" + licensePlate, "&a")
                .setUnbreakable(true)
                .toItemStack();
        return vehicle;
    }

    /**
     * Restore a vehicle item with a known license plate (used in /vehicle restore and #spawnVehicle(...)). Updated method (used to be #carItem5(...)).
     */
    public static ItemStack getVehicleItem(@NotNull Material material, int durability, boolean glowing, String name, String licensePlate){
        if (!material.isItem()) return null;
        ItemStack vehicle = (new ItemFactory(material))
                .setDurability(durability)
                .setName(TextUtils.colorize("&6" + name))
                .setGlowing(glowing)
                .setNBT("mtvehicles.kenteken", licensePlate)
                .setNBT("mtvehicles.naam", name)
                .setLore("&a", "&a" + licensePlate, "&a")
                .setUnbreakable(true)
                .toItemStack();
        return vehicle;
    }

    /**
     * Restore a vehicle item with a known license plate and <b>a custom NBT</b> (used in /vehicle restore). Updated method (used to be #carItem4(...)).
     */
    public static ItemStack getVehicleItem(@NotNull Material material, int durability, boolean glowing, String name, String licensePlate, String nbtKey, String nbtValue){
        if (!material.isItem()) return null;
        ItemStack vehicle = (new ItemFactory(material))
                .setDurability(durability)
                .setName(TextUtils.colorize("&6" + name))
                .setNBT("mtvehicles.kenteken", licensePlate)
                .setNBT("mtvehicles.naam", name)
                .setNBT(nbtKey, nbtValue)
                .setLore("&a", "&a" + licensePlate, "&a")
                .setUnbreakable(true)
                .toItemStack();
        return vehicle;
    }

    private static String generateLicencePlate() {
        String plate = String.format("%s-%s-%s", RandomStringUtils.random(2, true, false), RandomStringUtils.random(2, true, false), RandomStringUtils.random(2, true, false));
        return plate.toUpperCase();
    }

    /**
     * Get a menu item by material, amount, name and lore (as List).
     * <b>Used for items whose Material name has been changed between versions.</b>
     *
     * An updated method (used to be #woolItem(...)).
     */
    public static ItemStack getMenuItem(String materialName, String materialLegacyName, short legacyData, int amount, String name, List<String> lores){
        ItemStack item;
        try {
            item = new ItemStack(Material.getMaterial(materialLegacyName.toUpperCase(Locale.ROOT)), amount);
            item.setDurability(legacyData);
        } catch (Exception e){
            item = new ItemStack(getMaterial(materialName.toUpperCase(Locale.ROOT)), amount);
        }
        return (new ItemFactory(item))
                .setName(name)
                .setLore(lores)
                .toItemStack();
    }

    /**
     * <b>Do not use this. Use getMenuItem() instead.</b>
     * @see #getMenuItem(String materialName, String materialLegacyName, short legacyData, int amount, String name, List lore)
     */
    @Deprecated
    @ToDo(comment = "remove all usages")
    public static ItemStack woolItem(String mat1, String mat2, int amount, short durability, String text, String lores) {
        List<String> lore = new ArrayList<>();
        lore.add(lores);
        return getMenuItem(mat2, mat1, durability, amount, text, lore);
    }


    /**
     * Get a menu item by material, amount, name and lore (as List)
     */
    public static ItemStack getMenuItem(@NotNull Material material, int amount, String name, List<String> lores){
        ItemStack item = (new ItemFactory(material, amount))
                .setName(name)
                .setLore(lores)
                .toItemStack();
        return item;
    }

    /**
     * Get a menu item by material, amount, name and lore (as multiple Strings)
     */
    public static ItemStack getMenuItem(@NotNull Material material, int amount, String name, String... lores){
        return getMenuItem(material, amount, name, Arrays.asList(lores));
    }

    /**
     * Get a glowing menu item by material, amount, name and lore (as List).
     * (Used to be #glowItem(). Updated.)
     */
    public static ItemStack getMenuGlowingItem(@NotNull Material material, int amount, String name, List<String> lores){
        ItemStack item = (new ItemFactory(material, amount))
                .setName(name)
                .setGlowing(true)
                .setLore(lores)
                .toItemStack();
        return item;
    }

    /**
     * Get a glowing menu item by material, amount, name and lore (as multiple Strings).
     * (Used to be #glowItem(). Updated.)
     */
    public static ItemStack getMenuGlowingItem(@NotNull Material material, int amount, String name, String... lores){
        return getMenuGlowingItem(material, amount, name, Arrays.asList(lores));
    }

    /**
     * Get a menu item by material, amount, durability, unbreakability (boolean), name and lore (as List)
     */
    public static ItemStack getMenuItem(@NotNull Material material, int amount, int durability, boolean unbreakable, String name, List<String> lores){
        ItemStack item = (new ItemFactory(material, amount))
                .setName(name)
                .setDurability(durability)
                .setUnbreakable(unbreakable)
                .setLore(lores)
                .toItemStack();
        return item;
    }

    /**
     * Get a menu item by material, amount, durability, unbreakability (boolean), name and lore (as multiple Strings)
     */
    public static ItemStack getMenuItem(@NotNull Material material, int amount, int durability, String name, List<String> lores){
        return getMenuItem(material, amount, durability, false, name, lores);
    }

    /**
     * Get a menu item by material, amount, durability, name and lore (as multiple Strings). (Unbreakable is set to false.)
     */
    public static ItemStack getMenuItem(@NotNull Material material, int amount, int durability, String name, String... lores){
        return getMenuItem(material, amount, durability, false, name, Arrays.asList(lores));
    }

    /**
     * <b>Do not use this. Use getMenuItem() instead.</b>
     * @param lores Lore as String - you can use %nl% for a new line
     *
     * @see #getMenuItem(Material material, int amount, int durability, String name, String... lores)
     */
    @Deprecated
    @ToDo(comment = "remove all usages")
    public static ItemStack mItem(String material, int amount, short durability, String text, String lores) {
        List<String> itemLore = Arrays.asList(TextUtils.colorize(lores).split("%nl%"));
        Material m = getMaterial(material);
        return getMenuItem(m, amount, durability, false, text, itemLore);
    }

    /**
     * Get menu item about riders/drivers.
     * An updated method (used to be #mItemRiders(...)).
     */
    public static ItemStack getMenuRidersItem(String licensePlate){
        List<String> lore = new ArrayList<>();
        MessagesConfig msg = ConfigModule.messagesConfig;
        Vehicle vehicle = VehicleUtils.getByLicensePlate(licensePlate);

        if (vehicle == null) return null;

        if (vehicle.getRiders().size() == 0) {
            lore.add(msg.getMessage(Message.VEHICLE_INFO_RIDERS_NONE));
        } else {
            lore.add(String.format(
                        ConfigModule.messagesConfig.getMessage(Message.VEHICLE_INFO_RIDERS),
                        vehicle.getRiders().size(),
                        ""
                    )
            );
            for (String rider : vehicle.getRiders()) {
                lore.add(TextUtils.colorize(("&7- &e" + Bukkit.getOfflinePlayer(UUID.fromString(rider)).getName())));
            }
        }

        return getMenuItem(Material.PAPER, 1, "&6" + msg.getMessage(Message.RIDERS), lore);
    }

    /**
     * Get menu item about members/passengers.
     * An updated method (used to be #mItemMembers(...)).
     */
    public static ItemStack getMenuMembersItem(String licensePlate){
        List<String> lore = new ArrayList<>();
        MessagesConfig msg = ConfigModule.messagesConfig;
        Vehicle vehicle = VehicleUtils.getByLicensePlate(licensePlate);

        if (vehicle == null) return null;

        if (vehicle.getMembers().size() == 0) {
            lore.add(msg.getMessage(Message.VEHICLE_INFO_MEMBERS_NONE));
        } else {
            lore.add(String.format(
                            ConfigModule.messagesConfig.getMessage(Message.VEHICLE_INFO_MEMBERS),
                            vehicle.getMembers().size(),
                            ""
                    )
            );
            for (String member : vehicle.getMembers()) {
                lore.add(TextUtils.colorize(("&7- &e" + Bukkit.getOfflinePlayer(UUID.fromString(member)).getName())));
            }
        }

        return getMenuItem(Material.PAPER, 1, "&6" + msg.getMessage(Message.MEMBERS), lore);
    }

    /**
     * Get a custom menu item which looks like a vehicle.
     * An updated method (used to be #mItem2(...)).
     */
    public static ItemStack getMenuVehicleItem(@NotNull Material material, int durability, String name, List<String> lore){
        if (!material.isItem()) return null;
        ItemStack vehicle = (new ItemFactory(material))
                .setDurability(durability)
                .setName(name)
                .setLore(lore)
                .setUnbreakable(true)
                .toItemStack();
        return vehicle;
    }

    /**
     * Get a custom menu item which looks like a vehicle <b>with a custom NBT</b>.
     * An updated method (used to be #mItem3(...)).
     */
    public static ItemStack getMenuVehicleItem(@NotNull Material material, int durability, String nbtKey, String nbtValue, String name, List<String> lore){
        if (!material.isItem()) return null;
        ItemStack vehicle = (new ItemFactory(material))
                .setDurability(durability)
                .setName(name)
                .setNBT(nbtKey, nbtValue)
                .setLore(lore)
                .setUnbreakable(true)
                .toItemStack();
        return vehicle;
    }

    /**
     * <b>Do not use this. Use getMenuVehicleItem() instead.</b>
     * @see #getMenuVehicleItem(Material, int, String, List)
     */
    @Deprecated
    @ToDo(comment = "remove all usages")
    public static ItemStack mItem2(String material, int amount, short durability, String text, String lores) {
        List<String> lore = new ArrayList<>();
        lore.add(lores);
        return getMenuVehicleItem(getMaterial(material), durability, text, lore);
    }

    /**
     * Create a new voucher. An updated method.
     * @param carUUID UUID of the car (to be found in vehicles.yml)
     */
    public static ItemStack createVoucher(String carUUID) {
        MessagesConfig msg = ConfigModule.messagesConfig;
        ItemStack voucher = (new ItemFactory(Material.PAPER))
                .setName(TextUtils.colorize(VehicleUtils.getCarItem(carUUID).getItemMeta().getDisplayName() + " Voucher"))
                .setLore(
                        TextUtils.colorize("&8&m                                    "),
                        TextUtils.colorize(msg.getMessage(Message.VOUCHER_DESCRIPTION)),
                        TextUtils.colorize("&2&l"),
                        TextUtils.colorize(msg.getMessage(Message.VOUCHER_VALIDITY)),
                        TextUtils.colorize("&2> Permanent"),
                        TextUtils.colorize("&8&m                                    ")
                )
                .setNBT("mtvehicles.item", carUUID)
                .toItemStack();
        return voucher;
    }
}
