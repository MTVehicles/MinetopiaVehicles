package nl.mtvehicles.core.infrastructure.vehicle;

import de.tr7zw.changeme.nbtapi.NBTItem;
import nl.mtvehicles.core.Main;
import nl.mtvehicles.core.infrastructure.annotations.ToDo;
import nl.mtvehicles.core.infrastructure.dataconfig.DefaultConfig;
import nl.mtvehicles.core.infrastructure.dataconfig.VehicleDataConfig;
import nl.mtvehicles.core.infrastructure.enums.InventoryTitle;
import nl.mtvehicles.core.infrastructure.enums.Message;
import nl.mtvehicles.core.infrastructure.enums.RegionAction;
import nl.mtvehicles.core.infrastructure.enums.VehicleType;
import nl.mtvehicles.core.infrastructure.utils.PaperUtils;
import nl.mtvehicles.core.infrastructure.utils.*;
import nl.mtvehicles.core.infrastructure.models.MTVConfig;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * Useful methods for vehicles
 * @see Vehicle
 */
public final class VehicleUtils {

    /**
     * A private constructor - makes this a "static class"
     */
    private VehicleUtils(){}

    /**
     * HashMap containing information about which trunk a player has opened (determined by vehicle's license plate)
     */
    public static HashMap<Player, String> openedTrunk = new HashMap<>();

    /**
     * Spawn a vehicle
     * @param licensePlate Vehicle's license plate
     * @param location Location where the vehicle should be spawned
     *
     * @throws IllegalArgumentException If vehicle with given license plate does not exist
     */
    public static void spawnVehicle(String licensePlate, Location location) throws IllegalArgumentException {
        if (!existsByLicensePlate(licensePlate)) throw new IllegalArgumentException("Vehicle does not exists.");

        ArmorStand standSkin = location.getWorld().spawn(location, ArmorStand.class);
        allowTicking(standSkin);
        standSkin.setVisible(false);
        standSkin.setCustomName("MTVEHICLES_SKIN_" + licensePlate);
        standSkin.getEquipment().setHelmet(
                ItemUtils.getVehicleItem(
                        ItemUtils.getMaterial(ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.SKIN_ITEM).toString()),
                        (int) ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.SKIN_DAMAGE),
                        false,
                        ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.NAME).toString(),
                        licensePlate));

        ArmorStand standMain = location.getWorld().spawn(location, ArmorStand.class);
        standMain.setVisible(false);
        standMain.setCustomName("MTVEHICLES_MAIN_" + licensePlate);

        Vehicle vehicle = getVehicle(licensePlate);

        List<Map<String, Double>> seats = (List<Map<String, Double>>) vehicle.getVehicleData().get("seats");
        Map<String, Double> mainSeat = seats.get(0);
        Location locationMainSeat = new Location(location.getWorld(), location.getX() + mainSeat.get("x"), location.getY() + mainSeat.get("y"), location.getZ() + mainSeat.get("z"));
        ArmorStand standMainSeat = locationMainSeat.getWorld().spawn(locationMainSeat, ArmorStand.class);
        standMainSeat.setCustomName("MTVEHICLES_MAINSEAT_" + licensePlate);
        standMainSeat.setGravity(false);
        standMainSeat.setVisible(false);

        if (ConfigModule.vehicleDataConfig.getType(licensePlate).isBoat()){
            standMain.setGravity(false);
            standSkin.setGravity(false);
        }

        if (ConfigModule.vehicleDataConfig.getType(licensePlate).isHelicopter()) {
            List<Map<String, Double>> helicopterBlades = (List<Map<String, Double>>) vehicle.getVehicleData().get("wiekens");
            Map<?, ?> blade = helicopterBlades.get(0);
            Location locationBlade = new Location(location.getWorld(), location.getX() + (double) blade.get("z"), location.getY() + (double) blade.get("y"), location.getZ() + (double) blade.get("x"));
            ArmorStand standRotors = locationBlade.getWorld().spawn(locationBlade, ArmorStand.class);
            standRotors.setCustomName("MTVEHICLES_WIEKENS_" + licensePlate);
            standRotors.setGravity(false);
            standRotors.setVisible(false);

            if ((boolean) ConfigModule.defaultConfig.get(DefaultConfig.Option.HELICOPTER_BLADES_ALWAYS_ON)) {
                ItemStack rotor = (new ItemFactory(Material.getMaterial("DIAMOND_HOE"))).setDurability((short) 1058).setName(TextUtils.colorize("&6Wieken")).setNBT("mtvehicles.kenteken", licensePlate).toItemStack();
                ItemMeta itemMeta = rotor.getItemMeta();
                List<String> lore = new ArrayList<>();
                lore.add(TextUtils.colorize("&a"));
                lore.add(TextUtils.colorize("&a" + licensePlate));
                lore.add(TextUtils.colorize("&a"));
                itemMeta.setLore(lore);
                itemMeta.setUnbreakable(true);
                rotor.setItemMeta(itemMeta);

                allowTicking(standRotors);
                standRotors.setHelmet((ItemStack) blade.get("item"));
            }
        }
    }

    /**
     * Get license plate from a vehicle item
     * @param item Vehicle as Item
     * @return Vehicle's license plate
     */
    public static String getLicensePlate(ItemStack item){
        NBTItem nbt = new NBTItem(item);
        return nbt.getString("mtvehicles.kenteken");
    }

    /**
     * Create a vehicle and get its item by UUID (UUID may be found in vehicles.yml)
     * @param p Vehicle's owner
     * @param uuid Vehicle's UUID (UUID may be found in vehicles.yml)
     * @return Null if vehicle was not found by given UUID; otherwise, vehicle item
     */
    public static ItemStack getItemByUUID(Player p, String uuid) {
        List<Map<?, ?>> vehicles = ConfigModule.vehiclesConfig.getVehicles();
        List<Map<?, ?>> matchedVehicles = new ArrayList<>();
        for (Map<?, ?> configVehicle : vehicles) {
            List<Map<?, ?>> skins = (List<Map<?, ?>>) configVehicle.get("cars");
            for (Map<?, ?> skin : skins) {
                if (skin.get("uuid") != null) {
                    if (skin.get("uuid").equals(uuid)) {
                        String nbtVal;
                        if (skin.get("nbtValue") == null) {
                            nbtVal = "null";
                        } else {
                            nbtVal = skin.get("nbtValue").toString();
                        }
                        ItemStack is = ItemUtils.getVehicleItem(ItemUtils.getMaterial(skin.get("SkinItem").toString()), (int) skin.get("itemDamage"), ((String) skin.get("name")), "mtcustom", nbtVal);
                        NBTItem nbt = new NBTItem(is);
                        String licensePlate = nbt.getString("mtvehicles.kenteken");
                        matchedVehicles.add(configVehicle);

                        Vehicle vehicle = new Vehicle();
                        List<String> members = ConfigModule.vehicleDataConfig.getMembers(licensePlate);
                        List<String> riders = ConfigModule.vehicleDataConfig.getRiders(licensePlate);
                        List<String> trunkData = ConfigModule.vehicleDataConfig.getTrunkData(licensePlate);

                        vehicle.setLicensePlate(licensePlate);
                        vehicle.setName((String) skin.get("name"));
                        vehicle.setVehicleType((String) configVehicle.get("vehicleType"));
                        vehicle.setSkinDamage((Integer) skin.get("itemDamage"));
                        vehicle.setSkinItem((String) skin.get("SkinItem"));
                        vehicle.setGlow(false);
                        vehicle.setBenzineEnabled((Boolean) configVehicle.get("benzineEnabled"));
                        vehicle.setFuel(100);
                        vehicle.setHornEnabled((Boolean) configVehicle.get("hornEnabled"));
                        vehicle.setHealth((double) configVehicle.get("maxHealth"));
                        vehicle.setTrunk((Boolean) configVehicle.get("kofferbakEnabled"));
                        vehicle.setTrunkRows(1);
                        vehicle.setFuelUsage(0.01);
                        vehicle.setTrunkData(trunkData);
                        vehicle.setAccelerationSpeed((Double) configVehicle.get("acceleratieSpeed"));
                        vehicle.setMaxSpeed((Double) configVehicle.get("maxSpeed"));
                        vehicle.setBrakingSpeed((Double) configVehicle.get("brakingSpeed"));
                        vehicle.setFrictionSpeed((Double) configVehicle.get("aftrekkenSpeed"));
                        vehicle.setRotateSpeed((Integer) configVehicle.get("rotateSpeed"));
                        vehicle.setMaxSpeedBackwards((Double) configVehicle.get("maxSpeedBackwards"));
                        vehicle.setOwner(p.getUniqueId().toString());
                        vehicle.setRiders(riders);
                        vehicle.setMembers(members);
                        vehicle.setNbtValue(((String) skin.get("nbtValue")));
                        vehicle.save();
                        return is;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Check whether horn is enabled on this vehicle.
     * @param damage The vehicle item's durability
     * @return True if horn is enabled
     */
    public static boolean getHornByDamage(int damage){
        List<Map<?, ?>> vehicles = ConfigModule.vehiclesConfig.getVehicles();
        for (Map<?, ?> configVehicle : vehicles) {
            List<Map<?, ?>> skins = (List<Map<?, ?>>) configVehicle.get("cars");
            for (Map<?, ?> skin : skins) {
                if (skin.get("itemDamage") != null) {
                    if (skin.get("itemDamage").equals(damage)) {
                        return (boolean) configVehicle.get("hornEnabled");
                    }
                }
            }
        }
        return false;
    }

    /**
     * Check what is the max health of this vehicle.
     * @param damage The vehicle item's durability
     * @return Max health of the vehicle
     */
    public static double getMaxHealthByDamage(int damage){
        List<Map<?, ?>> vehicles = ConfigModule.vehiclesConfig.getVehicles();
        for (Map<?, ?> configVehicle : vehicles) {
            List<Map<?, ?>> skins = (List<Map<?, ?>>) configVehicle.get("cars");
            for (Map<?, ?> skin : skins) {
                if (skin.get("itemDamage") != null) {
                    if (skin.get("itemDamage").equals(damage)) {
                        return (double) configVehicle.get("maxHealth");
                    }
                }
            }
        }
        return 0;
    }

    /**
     * @deprecated Renamed to {@link #getItem(String)}.
     */
    @Deprecated
    public static ItemStack getCarItem(String carUUID) {
        return getItem(carUUID);
    }

    /**
     * Get a vehicle item by UUID. <b>Does not create a new vehicle - just for aesthetic purposes.</b> (Otherwise, use {@link #getItemByUUID(Player, String)})
     * @param carUUID Vehicle's UUID (UUID may be found in vehicles.yml)
     * @return The vehicle item - just aesthetic (null if UUID is not found)
     *
     */
    public static ItemStack getItem(String carUUID) {
        List<Map<?, ?>> vehicles = ConfigModule.vehiclesConfig.getVehicles();
        List<Map<?, ?>> matchedVehicles = new ArrayList<>();
        for (Map<?, ?> configVehicle : vehicles) {
            List<Map<?, ?>> skins = (List<Map<?, ?>>) configVehicle.get("cars");
            for (Map<?, ?> skin : skins) {
                if (skin.get("uuid") != null) {
                    if (skin.get("uuid").equals(carUUID)) {
                        if (skin.get("uuid") != null) {
                            ItemStack is = ItemUtils.getVehicleItem(ItemUtils.getMaterial(skin.get("SkinItem").toString()), (int) skin.get("itemDamage"), ((String) skin.get("name")));
                            matchedVehicles.add(configVehicle);
                            return is;
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * Check whether an entity is a vehicle
     * @param entity Checked entity
     * @return True if the entity is a vehicle
     */
    public static boolean isVehicle(Entity entity){
        return entity.getCustomName() != null && entity instanceof ArmorStand && entity.getCustomName().contains("MTVEHICLES");
    }

    /**
     * Get license plate of an entity (which should be a vehicle - see {@link #isVehicle(Entity)}.
     * @param entity Vehicle's main armor stand
     * @return Vehicle's license plate
     */
    public static String getLicensePlate(@Nullable Entity entity){
        if (entity == null) return null;
        final String name = entity.getCustomName();
        if (name.split("_").length > 1) {
            return name.split("_")[2];
        }
        return null;
    }

    /**
     * @deprecated Renamed to {@link #getUUID(String)}.
     */
    @Deprecated
    public static String getCarUUID(String licensePlate) {
        return getUUID(licensePlate);
    }

    /**
     * Get the UUID of a car by its license plate
     * @param licensePlate Vehicle's license plate
     * @return Vehicle's UUID
     */
    public static String getUUID(String licensePlate) {
        if (!existsByLicensePlate(licensePlate)) return null;

        Map<?, ?> vehicleData = ConfigModule.vehicleDataConfig.getConfig()
                .getConfigurationSection(String.format("vehicle.%s", licensePlate)).getValues(true);
        List<Map<?, ?>> vehicles = ConfigModule.vehiclesConfig.getVehicles();
        for (Map<?, ?> configVehicle : vehicles) {
            List<Map<?, ?>> skins = (List<Map<?, ?>>) configVehicle.get("cars");
            for (Map<?, ?> skin : skins) {
                if (skin.get("itemDamage").equals(vehicleData.get("skinDamage"))) {
                    if (skin.get("SkinItem").equals(vehicleData.get("skinItem"))) {
                        if (skin.get("nbtValue") != null) {
                            if (skin.get("nbtValue").equals(vehicleData.get("nbtValue"))) {
                                return skin.get("uuid").toString();
                            }
                        } else {
                            return skin.get("uuid").toString();
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * Get the Vehicle instance by a vehicle's license place
     * @param licensePlate Vehicle's license plate
     * @return Vehicle instance
     *
     * @see Vehicle
     */
    @ToDo("Beautify the code inside this method, use enums & move some code to VehiclesConfig.")
    public static Vehicle getVehicle(String licensePlate) {
        if (!existsByLicensePlate(licensePlate)) return null;

        ConfigModule.vehicleDataConfig.reload();
        ConfigModule.vehiclesConfig.reload();

        Map<?, ?> vehicleData = ConfigModule.vehicleDataConfig.getConfig()
                .getConfigurationSection(String.format("vehicle.%s", licensePlate)).getValues(true);
        List<Map<?, ?>> vehicles = ConfigModule.vehiclesConfig.getVehicles();
        List<Map<?, ?>> matchedVehicles = new ArrayList<>();
        double price = 0.0;
        for (Map<?, ?> configVehicle : vehicles) {
            List<Map<?, ?>> skins = (List<Map<?, ?>>) configVehicle.get("cars");
            for (Map<?, ?> skin : skins) {
                if (skin.get("itemDamage").equals(vehicleData.get("skinDamage"))) {
                    if (skin.get("SkinItem").equals(vehicleData.get("skinItem"))) {
                        if (skin.get("nbtValue") != null) {
                            if (skin.get("nbtValue").equals(vehicleData.get("nbtValue"))) {
                                matchedVehicles.add(configVehicle);
                                price = (double) skin.get("price");
                            }
                        } else {
                            matchedVehicles.add(configVehicle);
                            price = (double) skin.get("price");
                        }
                    }
                }
            }
        }
        if (matchedVehicles.size() == 0) return null;
        if (matchedVehicles.size() > 1) return null;
        Vehicle vehicle = new Vehicle();
        vehicle.setVehicleData(matchedVehicles.get(0));
        vehicle.setLicensePlate(licensePlate);
        vehicle.setOpen(false);
        vehicle.setName((String) vehicleData.get("name"));
        vehicle.setVehicleType((String) vehicleData.get("vehicleType"));
        vehicle.setSkinDamage((Integer) vehicleData.get("skinDamage"));
        vehicle.setSkinItem((String) vehicleData.get("skinItem"));
        vehicle.setGlow((Boolean) vehicleData.get("isGlow"));
        vehicle.setHornEnabled(ConfigModule.vehicleDataConfig.isHornSet(licensePlate) ? (boolean) vehicleData.get("hornEnabled") : ConfigModule.vehicleDataConfig.isHornEnabled(licensePlate));
        vehicle.setHealth(ConfigModule.vehicleDataConfig.isHealthSet(licensePlate) ? (double) vehicleData.get("health") : ConfigModule.vehicleDataConfig.getHealth(licensePlate));
        vehicle.setBenzineEnabled((Boolean) vehicleData.get("benzineEnabled"));
        vehicle.setFuel((Double) vehicleData.get("benzine"));
        vehicle.setFuelUsage((Double) vehicleData.get("benzineVerbruik"));
        vehicle.setTrunk((Boolean) vehicleData.get("kofferbak"));
        vehicle.setTrunkRows((Integer) vehicleData.get("kofferbakRows"));
        vehicle.setTrunkData((List<String>) vehicleData.get("kofferbakData"));
        vehicle.setAccelerationSpeed((Double) vehicleData.get("acceleratieSpeed"));
        vehicle.setMaxSpeed((Double) vehicleData.get("maxSpeed"));
        vehicle.setBrakingSpeed((Double) vehicleData.get("brakingSpeed"));
        vehicle.setFrictionSpeed((Double) vehicleData.get("aftrekkenSpeed"));
        vehicle.setRotateSpeed((Integer) vehicleData.get("rotateSpeed"));
        vehicle.setMaxSpeedBackwards((Double) vehicleData.get("maxSpeedBackwards"));
        vehicle.setOwner((String) vehicleData.get("owner"));
        vehicle.setRiders((List<String>) vehicleData.get("riders"));
        vehicle.setMembers((List<String>) vehicleData.get("members"));
        vehicle.setPrice(price);
        return vehicle;
    }

    /**
     * Check whether this vehicle exists in the database (vehicleData.yml)
     * @param licensePlate Vehicle's license plate
     * @return True if vehicle is in the database (vehicleData.yml)
     */
    public static boolean existsByLicensePlate(String licensePlate) {
        return ConfigModule.vehicleDataConfig.getConfig().get(String.format("vehicle.%s", licensePlate)) != null;
    }

    /**
     * Check whether a player can ride/drive the vehicle.
     * @param player Player
     * @param licensePlate Vehicle's license plate
     * @return True if player is the vehicle's set rider.
     */
    public static boolean canRide(Player player, String licensePlate) {
        return ConfigModule.vehicleDataConfig.getRiders(licensePlate).contains(player.getUniqueId().toString());
    }

    /**
     * Check whether a player can sit in the vehicle.
     * @param player Player
     * @param licensePlate Vehicle's license plate
     * @return True if player is the vehicle's set passenger/member.
     */
    public static boolean canSit(Player player, String licensePlate) {
        return ConfigModule.vehicleDataConfig.getMembers(licensePlate).contains(player.getUniqueId().toString());
    }

    /**
     * Get the UUID of the vehicle's owner
     * @param licensePlate Vehicle's license plate
     * @return UUID of vehicle's owner
     */
    public static UUID getOwnerUUID(String licensePlate) {
        if (ConfigModule.vehicleDataConfig.getConfig().getString("vehicle." + licensePlate + ".owner") == null) {
            return null;
        }
        return UUID.fromString(ConfigModule.vehicleDataConfig.getConfig().getString("vehicle." + licensePlate + ".owner"));
    }

    /**
     * Open a vehicle's trunk to a player
     * @param p Player who is opening the trunk
     * @param license Vehicle's license plate
     */
    public static void openTrunk(Player p, String license) {
        if ((boolean) ConfigModule.defaultConfig.get(DefaultConfig.Option.TRUNK_ENABLED)) {
            if (VehicleUtils.getVehicle(license) == null) {
                ConfigModule.messagesConfig.sendMessage(p, Message.VEHICLE_NOT_FOUND);
                return;
            }

            if (VehicleUtils.getVehicle(license).isOwner(p) || p.hasPermission("mtvehicles.kofferbak")) {
                ConfigModule.configList.forEach(MTVConfig::reload);
                Inventory inv = Bukkit.createInventory(null, (int) ConfigModule.vehicleDataConfig.get(license, VehicleDataConfig.Option.TRUNK_ROWS) * 9, InventoryTitle.VEHICLE_TRUNK.getStringTitle());

                if (ConfigModule.vehicleDataConfig.get(license, VehicleDataConfig.Option.TRUNK_DATA) != null) {
                    List<ItemStack> chestContentsFromConfig = (List<ItemStack>) ConfigModule.vehicleDataConfig.get(license, VehicleDataConfig.Option.TRUNK_DATA);

                    for (ItemStack item : chestContentsFromConfig) {
                        if (item != null) inv.addItem(item);
                    }
                }

                openedTrunk.put(p, license);
                p.openInventory(inv);

            } else {
                p.sendMessage(TextUtils.colorize(ConfigModule.messagesConfig.getMessage(Message.VEHICLE_NO_RIDER_TRUNK).replace("%p%", VehicleUtils.getVehicle(license).getOwnerName())));
            }
        }
    }

    /**
     * Check whether a player is inside a vehicle
     * @param p Player
     * @return True if player is inside any MTV vehicle
     */
    public static boolean isInsideVehicle(Player p){
        if (p == null) return false;
        if (!p.isInsideVehicle()) return false;
        return VehicleUtils.isVehicle(p.getVehicle());
    }

    /**
     * Get all the vehicle's set drivers/riders.
     * @param licensePlate Vehicle's license plate
     * @return String of all the drivers/riders separated by commas
     *
     * @deprecated Use {@link #canRide(Player, String)} instead.
     */
    @Deprecated
    public static String getRidersAsString(String licensePlate) {
        StringBuilder sb = new StringBuilder();
        for (String s : ConfigModule.vehicleDataConfig.getConfig().getStringList("vehicle." + licensePlate + ".riders")) {
            if (!UUID.fromString(s).equals(getOwnerUUID(licensePlate))) {
                sb.append(Bukkit.getOfflinePlayer(UUID.fromString(s)).getName()).append(", ");
            }
        }
        if (sb.toString().isEmpty()) {
            sb.append("Niemand");
        }
        return sb.toString();
    }

    /**
     * Pick up a vehicle and put it to player's inventory
     * @param license Vehicle's license plate
     * @param player Player
     */
    public static void pickupVehicle(String license, Player player) {
        if (getVehicle(license) == null) {
            for (World world : Bukkit.getServer().getWorlds()) {
                for (Entity entity : world.getEntities()) {
                    if (entity.getCustomName() != null && entity.getCustomName().contains(license)) {
                        ArmorStand test = (ArmorStand) entity;
                        if (test.getCustomName().contains("MTVEHICLES_SKIN_" + license)) {
                            if (!TextUtils.checkInvFull(player)) {
                                player.getInventory().addItem(test.getHelmet());
                            } else {
                                ConfigModule.messagesConfig.sendMessage(player, Message.INVENTORY_FULL);
                                return;
                            }
                        }
                        test.remove();
                    }
                }
            }
            ConfigModule.messagesConfig.sendMessage(player, Message.VEHICLE_NOT_FOUND);
            return;
        }

        if (getVehicle(license).getOwnerName() == null) {
            ConfigModule.messagesConfig.sendMessage(player, Message.VEHICLE_NOT_FOUND);
            Main.logSevere("Could not find the owner of vehicle " + license + "! The vehicleData.yml must be malformed!");
            return;
        }

        if (getVehicle(license).isOwner(player) && !((boolean) ConfigModule.defaultConfig.get(DefaultConfig.Option.CAR_PICKUP)) || player.hasPermission("mtvehicles.oppakken")) {
            for (World world : Bukkit.getServer().getWorlds()) {
                for (Entity entity : world.getEntities()) {
                    if (entity.getCustomName() != null && entity.getCustomName().contains(license)) {
                        ArmorStand test = (ArmorStand) entity;
                        if (test.getCustomName().contains("MTVEHICLES_SKIN_" + license)) {
                            if (!TextUtils.checkInvFull(player)) {
                                player.getInventory().addItem(test.getHelmet());
                                player.sendMessage(TextUtils.colorize(ConfigModule.messagesConfig.getMessage(Message.VEHICLE_PICKUP).replace("%p%", getVehicle(license).getOwnerName())));
                            } else {
                                ConfigModule.messagesConfig.sendMessage(player, Message.INVENTORY_FULL);
                                return;
                            }
                        }
                        test.remove();
                    }
                }
            }
        } else {
            if ((boolean) ConfigModule.defaultConfig.get(DefaultConfig.Option.CAR_PICKUP)) {
                player.sendMessage(TextUtils.colorize(ConfigModule.messagesConfig.getMessage(Message.CANNOT_DO_THAT_HERE)));
                return;
            }
            player.sendMessage(TextUtils.colorize(ConfigModule.messagesConfig.getMessage(Message.VEHICLE_NO_OWNER_PICKUP).replace("%p%", getVehicle(license).getOwnerName())));
            return;
        }
    }

    /**
     * Create {@link VehicleData} (necessary for driving to work), helicopter blades, and make player enter a vehicle.
     * @param licensePlate Vehicle's license plate
     * @param p Player who is entering the vehicle
     */
    @ToDo("Beautify the code inside this method.")
    public static void enterVehicle(String licensePlate, Player p) {
        if (!(VehicleData.autostand2.get(licensePlate) == null)) {
            if (!VehicleData.autostand2.get(licensePlate).isEmpty()) {
                return;
            }
        }

        Vehicle vehicle = getVehicle(licensePlate);

        if (vehicle == null) {
            ConfigModule.messagesConfig.sendMessage(p, Message.VEHICLE_NOT_FOUND);
            return;
        }

        if (vehicle.getOwnerName() == null) {
            ConfigModule.messagesConfig.sendMessage(p, Message.VEHICLE_NOT_FOUND);
            Main.logSevere("Could not find the owner of vehicle " + licensePlate + "! The vehicleData.yml must be malformed!");
            return;
        }

        if (!vehicle.isOpen() && !vehicle.isOwner(p) && !vehicle.canRide(p) && !p.hasPermission("mtvehicles.ride")){
            p.sendMessage(ConfigModule.messagesConfig.getMessage(Message.VEHICLE_NO_RIDER_ENTER).replace("%p%", vehicle.getOwnerName()));
            return;
        }

        for (Entity entity : p.getWorld().getEntities()) {

            if (entity.getCustomName() != null && entity.getCustomName().contains(licensePlate)) {
                ArmorStand vehicleAs = (ArmorStand) entity;
                if (!entity.isEmpty()) {
                    return;
                }
                VehicleData.fuel.put(licensePlate, vehicle.getFuel());
                VehicleData.fuelUsage.put(licensePlate, (double) ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.FUEL_USAGE));
                VehicleData.type.put(licensePlate, VehicleType.valueOf(ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.VEHICLE_TYPE).toString().toUpperCase(Locale.ROOT)));

                VehicleData.RotationSpeed.put(licensePlate, (int) ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.ROTATION_SPEED));
                VehicleData.MaxSpeed.put(licensePlate, (double) ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.MAX_SPEED));
                VehicleData.AccelerationSpeed.put(licensePlate, (double) ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.ACCELARATION_SPEED));
                VehicleData.BrakingSpeed.put(licensePlate, (double) ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.BRAKING_SPEED));
                VehicleData.MaxSpeedBackwards.put(licensePlate, (double) ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.MAX_SPEED_BACKWARDS));
                VehicleData.FrictionSpeed.put(licensePlate, (double) ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.FRICTION_SPEED));

                Location location = new Location(entity.getWorld(), entity.getLocation().getX(), entity.getLocation().getY(), entity.getLocation().getZ(), entity.getLocation().getYaw(), entity.getLocation().getPitch());

                if (!ConfigModule.defaultConfig.canProceedWithAction(RegionAction.ENTER, vehicle.getVehicleType(), location, p)){
                    ConfigModule.messagesConfig.sendMessage(p, Message.CANNOT_DO_THAT_HERE);
                    return;
                }

                if (vehicleAs.getCustomName().contains("MTVEHICLES_SKIN_" + licensePlate)) {
                    basicStandCreator(licensePlate, "SKIN", location, vehicleAs.getHelmet(), false);
                    basicStandCreator(licensePlate, "MAIN", location, null, true);
                    vehicle.saveSeats();
                    List<Map<String, Double>> seats = getSeats(licensePlate);
                    for (int i = 1; i <= seats.size(); i++) {
                        Map<String, Double> seat = seats.get(i - 1);
                        if (i == 1) {
                            mainSeatStandCreator(licensePlate, location, p, seat.get("x"), seat.get("y"), seat.get("z"));
                            BossBarUtils.addBossBar(p, licensePlate);
                            p.sendMessage(TextUtils.colorize(ConfigModule.messagesConfig.getMessage(Message.VEHICLE_ENTER_RIDER).replace("%p%", getVehicle(licensePlate).getOwnerName())));
                        }
                        if (i > 1) {
                            Location location2 = new Location(location.getWorld(), location.getX() + Double.valueOf(seat.get("z")), location.getY() + Double.valueOf(seat.get("y")), location.getZ() + Double.valueOf(seat.get("x")));

                            ArmorStand as = location2.getWorld().spawn(location2, ArmorStand.class);
                            allowTicking(as);
                            as.setVisible(false);
                            as.setCustomName("MTVEHICLES_SEAT" + i + "_" + licensePlate);
                            as.setGravity(false);

                            VehicleData.autostand.put("MTVEHICLES_SEAT" + i + "_" + licensePlate, as);
                        }
                    }
                    List<Map<String, Double>> wiekens = (List<Map<String, Double>>) vehicle.getVehicleData().get("wiekens");
                    VehicleType vehicleType = ConfigModule.vehicleDataConfig.getType(licensePlate);
                    if (vehicleType.isHelicopter()) {
                        VehicleData.maxheight.put(licensePlate, (int) ConfigModule.defaultConfig.get(DefaultConfig.Option.MAX_FLYING_HEIGHT));
                        for (int i = 1; i <= wiekens.size(); i++) {
                            Map<?, ?> seat = wiekens.get(i - 1);
                            if (i == 1) {
                                Location location2 = new Location(location.getWorld(), location.getX() + (Double) seat.get("z"), (Double) location.getY() + (Double) seat.get("y"), location.getZ() + (Double) seat.get("x"));
                                VehicleData.wiekenx.put("MTVEHICLES_WIEKENS_" + licensePlate, (Double) seat.get("x"));
                                VehicleData.wiekeny.put("MTVEHICLES_WIEKENS_" + licensePlate, (Double) seat.get("y"));
                                VehicleData.wiekenz.put("MTVEHICLES_WIEKENS_" + licensePlate, (Double) seat.get("z"));

                                ArmorStand as = location2.getWorld().spawn(location2, ArmorStand.class);
                                allowTicking(as);
                                as.setVisible(false);
                                as.setCustomName("MTVEHICLES_WIEKENS_" + licensePlate);
                                as.setGravity(false);
                                as.setHelmet((ItemStack) seat.get("item"));

                                VehicleData.autostand.put("MTVEHICLES_WIEKENS_" + licensePlate, as);
                            }
                        }
                    }
                }
                vehicleAs.remove();
            }
        }
    }

    /**
     * Used in {@link #enterVehicle(String, Player)}.
     */
    private static void basicStandCreator(String license, String type, Location location, ItemStack item, Boolean gravity) {
        ArmorStand as = location.getWorld().spawn(location, ArmorStand.class);
        allowTicking(as);
        as.setVisible(false);
        as.setCustomName("MTVEHICLES_" + type + "_" + license);
        as.setHelmet(item);
        as.setGravity(gravity);

        VehicleData.autostand.put("MTVEHICLES_" + type + "_" + license, as);
    }

    private static void allowTicking(ArmorStand armorStand) {
        if (PaperUtils.isRunningPaper) {
            armorStand.setCanTick(true);
        }
    }

    /**
     * Used in {@link #enterVehicle(String, Player)}.
     */
    private static void mainSeatStandCreator(String license, Location location, Player p, double x, double y, double z) {
        Location location2 = new Location(location.getWorld(), location.getX() + Double.valueOf(z), location.getY() + Double.valueOf(y), location.getZ() + Double.valueOf(z));
        ArmorStand as = location2.getWorld().spawn(location2, ArmorStand.class);
        allowTicking(as);
        as.setVisible(false);
        as.setCustomName("MTVEHICLES_MAINSEAT_" + license);
        as.setGravity(false);

        VehicleData.autostand.put("MTVEHICLES_MAINSEAT_" + license, as);
        VehicleData.speed.put(license, 0.0);
        VehicleData.speedhigh.put(license, 0.0);

        as.setPassenger(p);
        VehicleData.autostand2.put(license, as);
    }

    /**
     * Shortcut for {@link Vehicle.Seat#getSeat(Player)}
     */
    public static Vehicle.Seat getSeat(Player player){
        return Vehicle.Seat.getSeat(player);
    }

    /**
     * Kick a player out of a vehicle; if the player is a driver, {@link #turnOff(Vehicle)} is called as well.
     * @return True if successful
     * @throws IllegalStateException If player is not seated in a (valid) vehicle
     */
    public static boolean kickOut(Player player) throws IllegalStateException {
        if (getSeat(player) == null) throw new IllegalStateException("Player is not seated in a vehicle!");

        Entity seat = player.getVehicle();
        if (!getSeat(player).isDriver()) {
            return seat.removePassenger(player);
        }

        final String license = getLicensePlate(seat);
        if (seat.removePassenger(player)){
            BossBarUtils.removeBossBar(player, license);
            return turnOff(license);
        }
        return false;
    }

    /**
     * Delete {@link VehicleData}, helicopter blades; save fuel, etc... <b>after a driver has left the vehicle</b>.
     * @param vehicle Vehicle
     * @return False if the driver is seated in the vehicle, or if the vehicle doesn't have {@link VehicleData} and thus is not created (see {@link #enterVehicle(String, Player)} -> the vehicle can't be turned off. Otherwise, true.
     *
     * @warning Do not call this method if a vehicle is being used! Use {@link #kickOut(Player)} instead.
     */
    public static boolean turnOff(@NotNull Vehicle vehicle){
        return turnOff(vehicle.getLicensePlate());
    }

    /**
     * @param licensePlate Vehicle's license plate
     * @see #turnOff(Vehicle)
     */
    public static boolean turnOff(@NotNull String licensePlate){
        if (!existsByLicensePlate(licensePlate)) return false;

        if (VehicleData.autostand.get("MTVEHICLES_MAIN_" + licensePlate) == null) return false;

        final ArmorStand standMain = VehicleData.autostand.get("MTVEHICLES_MAIN_" + licensePlate);
        final ArmorStand standSkin = VehicleData.autostand.get("MTVEHICLES_SKIN_" + licensePlate);
        final ArmorStand standMainSeat = VehicleData.autostand.get("MTVEHICLES_MAINSEAT_" + licensePlate);
        VehicleType vehicleType = VehicleData.type.get(licensePlate);

        if (vehicleType.isHelicopter()) {
            ArmorStand blades = VehicleData.autostand.get("MTVEHICLES_WIEKENS_" + licensePlate);
            Location locBelow = new Location(blades.getLocation().getWorld(), blades.getLocation().getX(), blades.getLocation().getY() - 0.2, blades.getLocation().getZ(), blades.getLocation().getYaw(), blades.getLocation().getPitch());
            blades.setGravity(locBelow.getBlock().getType().equals(Material.AIR)); // Blades should not fall if the helicopter is on the ground
        }

        // If a helicopter is 'extremely falling' and player manages to leave it beforehand
        if (vehicleType.isHelicopter() && (boolean) ConfigModule.defaultConfig.get(DefaultConfig.Option.EXTREME_HELICOPTER_FALL) && !standMainSeat.isOnGround()){
            VehicleData.fallDamage.put(licensePlate, true); // Do not damage when entering afterwards
        }

        if (!vehicleType.isBoat()) {
            standMain.setGravity(true);
            standSkin.setGravity(true);
        }
        List<Map<String, Double>> seats = getSeats(licensePlate);
        for (int i = 2; i <= seats.size(); i++) {
            if (VehicleData.autostand.get("MTVEHICLES_SEAT" + i + "_" + licensePlate) != null)
                VehicleData.autostand.get("MTVEHICLES_SEAT" + i + "_" + licensePlate).remove();
        }
        VehicleData.type.remove(licensePlate); //.remove(license+"b") used to be here... why? maybe i'm missing something?

        if ((boolean) ConfigModule.defaultConfig.get(DefaultConfig.Option.FUEL_ENABLED) && (boolean) ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.FUEL_ENABLED)) {
            double fuel = VehicleData.fuel.get(licensePlate);
            ConfigModule.vehicleDataConfig.set(licensePlate, VehicleDataConfig.Option.FUEL, fuel);
            ConfigModule.vehicleDataConfig.save();
        }

        return true;
    }

    /**
     * Get list of seats for a vehicle (specified by license plate)
     * @see Vehicle#getSeats()
     */
    public static List<Map<String, Double>> getSeats(String licensePlate){
        final Integer seatSize = VehicleData.seatsize.get(licensePlate);
        List<Map<String, Double>> seatList = new ArrayList<>();
        for (int i = 1; i <= seatSize; i++) {
            Map<String, Double> seat = new HashMap<>();
            if (i == 1) {
                seat.put("x", VehicleData.mainx.get("MTVEHICLES_MAINSEAT_" + licensePlate));
                seat.put("y", VehicleData.mainy.get("MTVEHICLES_MAINSEAT_" + licensePlate));
                seat.put("z", VehicleData.mainz.get("MTVEHICLES_MAINSEAT_" + licensePlate));
            } else {
                seat.put("x", VehicleData.seatx.get("MTVEHICLES_SEAT" + i + "_" + licensePlate));
                seat.put("y", VehicleData.seatx.get("MTVEHICLES_SEAT" + i + "_" + licensePlate));
                seat.put("z", VehicleData.seatx.get("MTVEHICLES_SEAT" + i + "_" + licensePlate));
            }
            seatList.add(seat);
        }
        return seatList;
    }

    /**
     * Get vehicle's price
     * @param carUUID Vehicle's UUID
     * @return Price of the vehicle, null if UUID is not found
     */
    public static Double getPrice(String carUUID){
        List<Map<?, ?>> vehicles = ConfigModule.vehiclesConfig.getVehicles();
        for (Map<?, ?> configVehicle : vehicles) {
            List<Map<?, ?>> skins = (List<Map<?, ?>>) configVehicle.get("cars");
            for (Map<?, ?> skin : skins) {
                if (skin.get("uuid") != null) {
                    if (skin.get("uuid").equals(carUUID)) {
                        if (skin.get("uuid") != null) {
                            return (double) skin.get("price");
                        }
                    }
                }
            }
        }
        return null;
    }
}
