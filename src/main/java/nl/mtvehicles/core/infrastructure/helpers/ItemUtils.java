package nl.mtvehicles.core.infrastructure.helpers;

import nl.mtvehicles.core.infrastructure.annotations.ToDo;
import nl.mtvehicles.core.infrastructure.dataconfig.MessagesConfig;
import nl.mtvehicles.core.infrastructure.dataconfig.VehicleDataConfig;
import nl.mtvehicles.core.infrastructure.enums.Message;
import nl.mtvehicles.core.infrastructure.models.VehicleUtils;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import org.apache.commons.lang.RandomStringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static nl.mtvehicles.core.infrastructure.modules.VersionModule.getServerVersion;

@ToDo(comment = "Honestly, this code is awful. I hardly understand most of it. Also... why parsing to Strings and then parsing back to materials again?")
public class ItemUtils {
    public static HashMap<String, Boolean> edit = new HashMap<>();

    public static ItemStack carItem(int durability, String name, String material) {
        try {
            Material carMaterial = Material.getMaterial(material);
            assert carMaterial != null;
            ItemStack carItem = new ItemStack(carMaterial);
            ItemMeta itemMeta = new ItemStack(carMaterial).getItemMeta();
            assert itemMeta != null;
            itemMeta.setDisplayName(TextUtils.colorize("&6" + name));
            List<String> itemLore = new ArrayList<>();
            itemLore.add(TextUtils.colorize("&a"));
            itemMeta.setLore(itemLore);
            if (getServerVersion().is1_12()) carItem.setDurability((short) durability);
            else ((org.bukkit.inventory.meta.Damageable) itemMeta).setDamage(durability);
            itemMeta.setUnbreakable(true);
            carItem.setItemMeta(itemMeta);
            return carItem;
        } catch (Exception e) {
            try {
                Material carMaterial = Material.getMaterial(material, true);
                assert carMaterial != null;
                ItemStack carItem = new ItemStack(carMaterial);
                ItemMeta itemMeta = new ItemStack(carMaterial).getItemMeta();
                assert itemMeta != null;
                itemMeta.setDisplayName(TextUtils.colorize("&6" + name));
                List<String> itemLore = new ArrayList<>();
                itemLore.add(TextUtils.colorize("&a"));
                itemMeta.setLore(itemLore);
                if (getServerVersion().is1_12()) carItem.setDurability((short) durability);
                else ((org.bukkit.inventory.meta.Damageable) itemMeta).setDamage(durability);
                itemMeta.setUnbreakable(true);
                carItem.setItemMeta(itemMeta);
                return carItem;
            } catch (Exception e2) {
                e2.printStackTrace();
                return null;
            }
        }
    }

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

    public static ItemStack carItem2(int id, String name, String material) {
        try {
            String ken = generateLicencePlate();
            ItemStack car = (new ItemFactory(Material.getMaterial(material))).setDurability(id).setName(TextUtils.colorize("&6" + name)).setNBT("mtvehicles.kenteken", ken).setNBT("mtvehicles.naam", name).toItemStack();
            ItemMeta im = car.getItemMeta();
            List<String> itemlore = new ArrayList<>();
            itemlore.add(TextUtils.colorize("&a"));
            itemlore.add(TextUtils.colorize("&a" + ken));
            itemlore.add(TextUtils.colorize("&a"));
            im.setLore(itemlore);
            im.setUnbreakable(true);
            car.setItemMeta(im);
            return car;
        } catch (Exception e) {
            try {
                String ken = generateLicencePlate();
                ItemStack car = (new ItemFactory(Material.getMaterial(material, true))).setDurability(id).setName(TextUtils.colorize("&6" + name)).setNBT("mtvehicles.kenteken", ken).setNBT("mtvehicles.naam", name).toItemStack();
                ItemMeta im = car.getItemMeta();
                List<String> itemlore = new ArrayList<>();
                itemlore.add(TextUtils.colorize("&a"));
                itemlore.add(TextUtils.colorize("&a" + ken));
                itemlore.add(TextUtils.colorize("&a"));
                im.setLore(itemlore);
                im.setUnbreakable(true);
                car.setItemMeta(im);
                return car;
            } catch (Exception e2) {
                e2.printStackTrace();
                return null;
            }
        }
    }

    public static ItemStack carItem5(int id, String name, String material, String ken) {
        try {
            ItemStack car = new ItemFactory(Material.getMaterial(material)).setDurability(id).setName(TextUtils.colorize("&6" + name)).setNBT("mtvehicles.kenteken", ken).setNBT("mtvehicles.naam", name).toItemStack();
            ItemMeta im = car.getItemMeta();
            List<String> itemlore = new ArrayList<>();
            itemlore.add(TextUtils.colorize("&a"));
            itemlore.add(TextUtils.colorize("&a" + ken));
            itemlore.add(TextUtils.colorize("&a"));
            im.setLore(itemlore);
            im.setUnbreakable(true);
            car.setItemMeta(im);
            return car;
        } catch (Exception e) {
            try {
                ItemStack car = new ItemFactory(Material.getMaterial(material, true)).setDurability(id).setName(TextUtils.colorize("&6" + name)).setNBT("mtvehicles.kenteken", ken).setNBT("mtvehicles.naam", name).toItemStack();
                ItemMeta im = car.getItemMeta();
                List<String> itemlore = new ArrayList<>();
                itemlore.add(TextUtils.colorize("&a"));
                itemlore.add(TextUtils.colorize("&a" + ken));
                itemlore.add(TextUtils.colorize("&a"));
                im.setLore(itemlore);
                im.setUnbreakable(true);
                car.setItemMeta(im);
                return car;
            } catch (Exception e2) {
                e2.printStackTrace();
                return null;
            }
        }
    }

    public static ItemStack carItem4(int id, String name, String material, String model, String nbt, String ken) {
        try {
            ItemStack car = new ItemFactory(Material.getMaterial(material)).setDurability(id).setName(TextUtils.colorize("&6" + name)).setNBT("mtvehicles.kenteken", ken).setNBT("mtvehicles.naam", name).setNBT(model, nbt).toItemStack();
            ItemMeta im = car.getItemMeta();
            List<String> itemlore = new ArrayList<>();
            itemlore.add(TextUtils.colorize("&a"));
            itemlore.add(TextUtils.colorize("&a" + ken));
            itemlore.add(TextUtils.colorize("&a"));
            im.setLore(itemlore);
            im.setUnbreakable(true);
            car.setItemMeta(im);
            return car;
        } catch (Exception e) {
            try {
                ItemStack car = new ItemFactory(Material.getMaterial(material, true)).setDurability(id).setName(TextUtils.colorize("&6" + name)).setNBT("mtvehicles.kenteken", ken).setNBT("mtvehicles.naam", name).setNBT(model, nbt).toItemStack();
                ItemMeta im = car.getItemMeta();
                List<String> itemlore = new ArrayList<>();
                itemlore.add(TextUtils.colorize("&a"));
                itemlore.add(TextUtils.colorize("&a" + ken));
                itemlore.add(TextUtils.colorize("&a"));
                im.setLore(itemlore);
                im.setUnbreakable(true);
                car.setItemMeta(im);
                return car;
            } catch (Exception e2) {
                e2.printStackTrace();
                return null;
            }
        }
    }

    public static ItemStack carItem3(int id, String name, String material, String model, String nbt) {
        try {
            String ken = generateLicencePlate();
            ItemStack car = new ItemFactory(Material.getMaterial(material)).setDurability(id).setName(TextUtils.colorize("&6" + name)).setNBT("mtvehicles.kenteken", ken).setNBT("mtvehicles.naam", name).setNBT(model, nbt).toItemStack();
            ItemMeta im = car.getItemMeta();
            List<String> itemlore = new ArrayList<>();
            itemlore.add(TextUtils.colorize("&a"));
            itemlore.add(TextUtils.colorize("&a" + ken));
            itemlore.add(TextUtils.colorize("&a"));
            im.setLore(itemlore);
            im.setUnbreakable(true);
            car.setItemMeta(im);
            return car;
        } catch (Exception e) {
            try {
                String ken = generateLicencePlate();
                ItemStack car = new ItemFactory(Material.getMaterial(material, true)).setDurability(id).setName(TextUtils.colorize("&6" + name)).setNBT("mtvehicles.kenteken", ken).setNBT("mtvehicles.naam", name).setNBT(model, nbt).toItemStack();
                ItemMeta im = car.getItemMeta();
                List<String> itemlore = new ArrayList<>();
                itemlore.add(TextUtils.colorize("&a"));
                itemlore.add(TextUtils.colorize("&a" + ken));
                itemlore.add(TextUtils.colorize("&a"));
                im.setLore(itemlore);
                im.setUnbreakable(true);
                car.setItemMeta(im);
                return car;
            } catch (Exception e2) {
                e2.printStackTrace();
                return null;
            }
        }
    }

    public static String generateLicencePlate() {
        String plate = String.format("%s-%s-%s", RandomStringUtils.random(2, true, false), RandomStringUtils.random(2, true, false), RandomStringUtils.random(2, true, false));
        return plate.toUpperCase();
    }

    public static ItemStack woolItem(String mat1, String mat2, int amount, short durability, String text, String lores) {
        try {
            ItemStack is = new ItemStack(Material.getMaterial(mat1.toUpperCase()), amount);
            ItemMeta im = is.getItemMeta();
            List<String> itemlore = new ArrayList<>();
            String[] lorem = lores.split("@");
            for (String s : lorem) {
                itemlore.add((TextUtils.colorize(s)));
            }
            im.setLore(itemlore);
            if (getServerVersion().is1_12()) is.setDurability(durability);
            else ((org.bukkit.inventory.meta.Damageable) im).setDamage(durability);
            im.setDisplayName(TextUtils.colorize(text));
            is.setItemMeta(im);
            return is;
        } catch (Exception e) {
            try {
                ItemStack is = new ItemStack(Material.matchMaterial(mat2), amount);
                ItemMeta im = is.getItemMeta();
                List<String> itemlore = new ArrayList<>();
                String[] lorem = lores.split("@");
                for (String s : lorem) {
                    itemlore.add((TextUtils.colorize(s)));
                }
                im.setLore(itemlore);
                if (getServerVersion().is1_12()) is.setDurability(durability);
                else ((org.bukkit.inventory.meta.Damageable) im).setDamage(durability);
                im.setDisplayName(TextUtils.colorize(text));
                is.setItemMeta(im);
                return is;
            } catch (Exception e2) {
                e2.printStackTrace();

                return null;
            }
        }
    }

    public static ItemStack mItem(String material, int amount, short durability, String text, String lores) {
        try {
            ItemStack is = new ItemStack(Material.getMaterial(material.toUpperCase()), amount);
            ItemMeta im = is.getItemMeta();
            List<String> itemlore = new ArrayList<>();
            itemlore.add(TextUtils.colorize(lores));
            im.setLore(itemlore);
            if (getServerVersion().is1_12()) is.setDurability(durability);
            else ((org.bukkit.inventory.meta.Damageable) im).setDamage(durability);
            im.setDisplayName(TextUtils.colorize(text));
            is.setItemMeta(im);
            return is;
        } catch (Exception e) {
            try {
                ItemStack is = new ItemStack(Material.matchMaterial(material, true), amount);
                ItemMeta im = is.getItemMeta();
                List<String> itemlore = new ArrayList<>();
                itemlore.add(TextUtils.colorize(lores));
                im.setLore(itemlore);
                if (getServerVersion().is1_12()) is.setDurability(durability);
                else ((org.bukkit.inventory.meta.Damageable) im).setDamage(durability);
                im.setDisplayName(TextUtils.colorize(text));
                is.setItemMeta(im);
                return is;
            } catch (Exception e2) {
                e2.printStackTrace();
                return null;
            }
        }
    }

    public static ItemStack mItemRiders(String material, int amount, short durability, String text, String licensePlate) {
        try {
            ItemStack is = new ItemStack(Material.getMaterial(material.toUpperCase()), amount);
            ItemMeta im = is.getItemMeta();
            List<String> itemlore = new ArrayList<>();
            List<String> riders = (List<String>) ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.RIDERS);
            if (riders.size() == 0) {
                itemlore.add(TextUtils.colorize((("&7Riders: &e---"))));
            } else {
                for (String subj : riders) {
                    itemlore.add(TextUtils.colorize(("&7- &e" + Bukkit.getOfflinePlayer(UUID.fromString(subj)).getName())));
                }
            }
            im.setLore(itemlore);
            if (getServerVersion().is1_12()) is.setDurability(durability);
            else ((org.bukkit.inventory.meta.Damageable) im).setDamage(durability);
            im.setDisplayName(TextUtils.colorize(text));
            is.setItemMeta(im);
            return is;
        } catch (Exception e) {
            try {
                ItemStack is = new ItemStack(Material.matchMaterial(material, true), amount);
                ItemMeta im = is.getItemMeta();
                List<String> itemlore = new ArrayList<>();
                List<String> riders = (List<String>) ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.RIDERS);
                if (riders.size() == 0) {
                    itemlore.add(TextUtils.colorize((("&7Riders: &eGeen"))));
                } else {
                    for (String subj : riders) {
                        itemlore.add(TextUtils.colorize(("&7- &e" + Bukkit.getOfflinePlayer(UUID.fromString(subj)).getName())));
                    }
                }
                im.setLore(itemlore);
                if (getServerVersion().is1_12()) is.setDurability(durability);
                else ((org.bukkit.inventory.meta.Damageable) im).setDamage(durability);
                im.setDisplayName(TextUtils.colorize(text));
                is.setItemMeta(im);
                return is;
            } catch (Exception e2) {
                e2.printStackTrace();
                return null;
            }
        }
    }

    public static ItemStack mItemMembers(String material, int amount, short durability, String text, String licensePlate) {
        try {
            ItemStack is = new ItemStack(Material.getMaterial(material.toUpperCase()), amount);
            ItemMeta im = is.getItemMeta();
            List<String> itemlore = new ArrayList<>();
            List<String> members = (List<String>) ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.MEMBERS);
            if (members.size() == 0) {
                itemlore.add(TextUtils.colorize((("&7Members: &eGeen"))));
            } else {
                for (String subj : members) {
                    itemlore.add(TextUtils.colorize(("&7- &e" + Bukkit.getOfflinePlayer(UUID.fromString(subj)).getName())));
                }
            }
            im.setLore(itemlore);
            if (getServerVersion().is1_12()) is.setDurability(durability);
            else ((org.bukkit.inventory.meta.Damageable) im).setDamage(durability);
            im.setDisplayName(TextUtils.colorize(text));
            is.setItemMeta(im);
            return is;
        } catch (Exception e) {
            try {
                ItemStack is = new ItemStack(Material.matchMaterial(material, true), amount);
                ItemMeta im = is.getItemMeta();
                List<String> members = (List<String>) ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.MEMBERS);
                List<String> itemlore = new ArrayList<>();
                if (members.size() == 0) {
                    itemlore.add(TextUtils.colorize((("&7Members: &eGeen"))));
                } else {
                    for (String subj : members) {
                        itemlore.add(TextUtils.colorize(("&7- &e" + Bukkit.getOfflinePlayer(UUID.fromString(subj)).getName())));
                    }
                }
                im.setLore(itemlore);
                if (getServerVersion().is1_12()) is.setDurability(durability);
                else ((org.bukkit.inventory.meta.Damageable) im).setDamage(durability);
                im.setDisplayName(TextUtils.colorize(text));
                is.setItemMeta(im);
                return is;
            } catch (Exception e2) {
                e2.printStackTrace();
                return null;
            }
        }
    }

    public static ItemStack mItem3(String material, int amount, short durability, String text, String lores, String model, String nbt) {
        try {
            ItemStack car = new ItemStack(Material.getMaterial(material.toUpperCase()), amount);
            ItemStack is = new ItemFactory(car).setNBT(model, nbt).toItemStack();
            ItemMeta im = is.getItemMeta();
            List<String> itemlore = new ArrayList<>();
            itemlore.add(TextUtils.colorize(lores));
            im.setLore(itemlore);
            if (getServerVersion().is1_12()) is.setDurability(durability);
            else ((org.bukkit.inventory.meta.Damageable) im).setDamage(durability);
            im.setUnbreakable(true);
            im.setDisplayName(TextUtils.colorize(text));
            is.setItemMeta(im);
            return is;
        } catch (Exception e) {
            try {
                ItemStack is = new ItemStack(Material.getMaterial(material.toUpperCase(),true), amount);
                ItemMeta im = is.getItemMeta();
                List<String> itemlore = new ArrayList<>();
                itemlore.add(TextUtils.colorize(lores));
                im.setLore(itemlore);
                if (getServerVersion().is1_12()) is.setDurability(durability);
                else ((org.bukkit.inventory.meta.Damageable) im).setDamage(durability);
                im.setUnbreakable(true);
                im.setDisplayName(TextUtils.colorize(text));
                is.setItemMeta(im);
                return is;
            } catch (Exception e2) {
                e2.printStackTrace();
                return null;
            }
        }
    }

    public static ItemStack mItem2(String material, int amount, short durability, String text, String lores) {
        try {
            ItemStack is = new ItemStack(Material.getMaterial(material.toUpperCase()), amount);
            ItemMeta im = is.getItemMeta();
            List<String> itemlore = new ArrayList<>();
            itemlore.add(TextUtils.colorize(lores));
            im.setLore(itemlore);
            if (getServerVersion().is1_12()) is.setDurability(durability);
            else ((org.bukkit.inventory.meta.Damageable) im).setDamage(durability);
            im.setUnbreakable(true);
            im.setDisplayName(TextUtils.colorize(text));
            is.setItemMeta(im);
            return is;
        } catch (Exception e) {
            try {
                ItemStack is = new ItemStack(Material.getMaterial(material.toUpperCase(),true), amount);
                ItemMeta im = is.getItemMeta();
                List<String> itemlore = new ArrayList<>();
                itemlore.add(TextUtils.colorize(lores));
                im.setLore(itemlore);
                if (getServerVersion().is1_12()) is.setDurability(durability);
                else ((org.bukkit.inventory.meta.Damageable) im).setDamage(durability);
                im.setUnbreakable(true);
                im.setDisplayName(TextUtils.colorize(text));
                is.setItemMeta(im);
                return is;
            } catch (Exception e2) {
                e2.printStackTrace();
                return null;
            }
        }
    }

    public static ItemStack carItem2(int id, String name, String material, String ken) {
        ItemStack car = (new ItemFactory(Material.getMaterial(material))).setDurability(id).setName(TextUtils.colorize("&6" + name)).setNBT("mtvehicles.kenteken", ken).setNBT("mtvehicles.naam", name).toItemStack();
        ItemMeta im = car.getItemMeta();
        List<String> itemlore = new ArrayList<>();
        itemlore.add(TextUtils.colorize("&a"));
        itemlore.add(TextUtils.colorize("&a" + ken));
        itemlore.add(TextUtils.colorize("&a"));
        im.setLore(itemlore);
        im.setUnbreakable(true);
        car.setItemMeta(im);
        return car;
    }

    public static ItemStack carItem2glow(int id, String name, String material, String ken) {
        ItemStack car = (new ItemFactory(Material.getMaterial(material))).setDurability(id).setName(TextUtils.colorize("&6" + name)).setNBT("mtvehicles.kenteken", ken).setNBT("mtvehicles.naam", name).toItemStack();
        ItemMeta im = car.getItemMeta();
        List<String> itemlore = new ArrayList<>();
        itemlore.add(TextUtils.colorize("&a"));
        itemlore.add(TextUtils.colorize("&a" + ken));
        itemlore.add(TextUtils.colorize("&a"));
        im.setLore(itemlore);
        im.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
        im.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ENCHANTS});
        im.setUnbreakable(true);
        car.setItemMeta(im);
        return car;
    }

    public static ItemStack glowItem(String material, String name, String ken) {
        ItemStack car = (new ItemFactory(Material.getMaterial(material))).setName(name).toItemStack();
        ItemMeta im = car.getItemMeta();
        List<String> itemlore = new ArrayList<>();
        itemlore.add(TextUtils.colorize(ken));
        im.setLore(itemlore);
        im.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
        im.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ENCHANTS});
        car.setItemMeta(im);
        return car;
    }

    public static void createVoucher(String name, Player p) {
        ItemStack is = (new ItemFactory(Material.PAPER)).setNBT("mtvehicles.item", name).toItemStack();
        ItemMeta im = is.getItemMeta();
        MessagesConfig msg = ConfigModule.messagesConfig;
        List<String> goldlore = new ArrayList<>();
        goldlore.add(TextUtils.colorize("&8&m                                    "));
        goldlore.add(TextUtils.colorize(msg.getMessage(Message.VOUCHER_DESCRIPTION)));
        goldlore.add(TextUtils.colorize("&2&l"));
        goldlore.add(TextUtils.colorize(msg.getMessage(Message.VOUCHER_VALIDITY)));
        goldlore.add(TextUtils.colorize("&2> Permanent"));
        goldlore.add(TextUtils.colorize("&8&m                                    "));
        im.setLore(goldlore);
        im.setDisplayName(TextUtils.colorize(VehicleUtils.getCarItem(name).getItemMeta().getDisplayName() + " Voucher"));
        is.setItemMeta(im);
        p.getInventory().addItem(new ItemStack[]{is});
    }
}
