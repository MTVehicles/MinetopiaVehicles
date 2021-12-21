package nl.mtvehicles.core.infrastructure.helpers;

import nl.mtvehicles.core.Main;
import nl.mtvehicles.core.infrastructure.models.Vehicle;
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
            itemMeta.setUnbreakable(true);
            carItem.setItemMeta(itemMeta);
            carItem.setDurability((short) durability);
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
                itemMeta.setUnbreakable(true);
                carItem.setItemMeta(itemMeta);
                carItem.setDurability((short) durability);
                return carItem;
            } catch (Exception e2) {
                e2.printStackTrace();
                return null;
            }
        }
    }

    public static ItemStack carItem9(int durability, String name, String material, String model, String nbt) {
        ItemStack car = (new ItemFactory(Material.getMaterial(material))).setDurability((short) durability).setName(TextUtils.colorize("&6" + name)).toItemStack();
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
        ItemStack car = (new ItemFactory(Material.getMaterial(material))).setDurability((short) id).setName(TextUtils.colorize("&6" + name)).setNBT("mtvehicles.kenteken", ken).setNBT("mtvehicles.naam", name).toItemStack();
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
                ItemStack car = (new ItemFactory(Material.getMaterial(material, true))).setDurability((short) id).setName(TextUtils.colorize("&6" + name)).setNBT("mtvehicles.kenteken", ken).setNBT("mtvehicles.naam", name).toItemStack();
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
            ItemStack car = new ItemFactory(Material.getMaterial(material)).setDurability((short) id).setName(TextUtils.colorize("&6" + name)).setNBT("mtvehicles.kenteken", ken).setNBT("mtvehicles.naam", name).toItemStack();
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
                ItemStack car = new ItemFactory(Material.getMaterial(material, true)).setDurability((short) id).setName(TextUtils.colorize("&6" + name)).setNBT("mtvehicles.kenteken", ken).setNBT("mtvehicles.naam", name).toItemStack();
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
            ItemStack car = new ItemFactory(Material.getMaterial(material)).setDurability((short) id).setName(TextUtils.colorize("&6" + name)).setNBT("mtvehicles.kenteken", ken).setNBT("mtvehicles.naam", name).setNBT(model, nbt).toItemStack();
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
                ItemStack car = new ItemFactory(Material.getMaterial(material, true)).setDurability((short) id).setName(TextUtils.colorize("&6" + name)).setNBT("mtvehicles.kenteken", ken).setNBT("mtvehicles.naam", name).setNBT(model, nbt).toItemStack();
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
            ItemStack car = new ItemFactory(Material.getMaterial(material)).setDurability((short) id).setName(TextUtils.colorize("&6" + name)).setNBT("mtvehicles.kenteken", ken).setNBT("mtvehicles.naam", name).setNBT(model, nbt).toItemStack();
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
                ItemStack car = new ItemFactory(Material.getMaterial(material, true)).setDurability((short) id).setName(TextUtils.colorize("&6" + name)).setNBT("mtvehicles.kenteken", ken).setNBT("mtvehicles.naam", name).setNBT(model, nbt).toItemStack();
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
            ItemStack is = new ItemStack(Material.getMaterial(mat1.toUpperCase()), amount, durability);
            ItemMeta im = is.getItemMeta();
            List<String> itemlore = new ArrayList<>();
            String[] lorem = lores.split("@");
            for (String s : lorem) {
                itemlore.add((TextUtils.colorize(s)));
            }
            im.setLore(itemlore);
            im.setDisplayName(TextUtils.colorize(text));
            is.setItemMeta(im);
            return is;
        } catch (Exception e) {
            try {
                ItemStack is = new ItemStack(Material.matchMaterial(mat2), amount, durability);
                ItemMeta im = is.getItemMeta();
                List<String> itemlore = new ArrayList<>();
                String[] lorem = lores.split("@");
                for (String s : lorem) {
                    itemlore.add((TextUtils.colorize(s)));
                }
                im.setLore(itemlore);
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
            ItemStack is = new ItemStack(Material.getMaterial(material.toUpperCase()), amount, durability);
            ItemMeta im = is.getItemMeta();
            List<String> itemlore = new ArrayList<>();
            itemlore.add(TextUtils.colorize(lores));
            im.setLore(itemlore);
            im.setDisplayName(TextUtils.colorize(text));
            is.setItemMeta(im);
            return is;
        } catch (Exception e) {
            try {
                ItemStack is = new ItemStack(Material.matchMaterial(material, true), amount, durability);
                ItemMeta im = is.getItemMeta();
                List<String> itemlore = new ArrayList<>();
                itemlore.add(TextUtils.colorize(lores));
                im.setLore(itemlore);
                im.setDisplayName(TextUtils.colorize(text));
                is.setItemMeta(im);
                return is;
            } catch (Exception e2) {
                e2.printStackTrace();
                return null;
            }
        }
    }

    public static ItemStack mItemRiders(String material, int amount, short durability, String text, String ken) {
        try {
            ItemStack is = new ItemStack(Material.getMaterial(material.toUpperCase()), amount, durability);
            ItemMeta im = is.getItemMeta();
            List<String> itemlore = new ArrayList<>();
            if (ConfigModule.vehicleDataConfig.getConfig().getStringList("vehicle." + ken + ".riders").size() == 0) {
                itemlore.add(TextUtils.colorize((("&7Riders: &eGeen"))));
            } else {
                for (String subj : ConfigModule.vehicleDataConfig.getConfig().getStringList("vehicle." + ken + ".riders")) {
                    itemlore.add(TextUtils.colorize(("&7- &e" + Bukkit.getOfflinePlayer(UUID.fromString(subj)).getName())));
                }
            }
            im.setLore(itemlore);
            im.setDisplayName(TextUtils.colorize(text));
            is.setItemMeta(im);
            return is;
        } catch (Exception e) {
            try {
                ItemStack is = new ItemStack(Material.matchMaterial(material, true), amount, durability);
                ItemMeta im = is.getItemMeta();
                List<String> itemlore = new ArrayList<>();
                if (ConfigModule.vehicleDataConfig.getConfig().getStringList("vehicle." + ken + ".riders").size() == 0) {
                    itemlore.add(TextUtils.colorize((("&7Riders: &eGeen"))));
                } else {
                    for (String subj : ConfigModule.vehicleDataConfig.getConfig().getStringList("vehicle." + ken + ".riders")) {
                        itemlore.add(TextUtils.colorize(("&7- &e" + Bukkit.getOfflinePlayer(UUID.fromString(subj)).getName())));
                    }
                }
                im.setLore(itemlore);
                im.setDisplayName(TextUtils.colorize(text));
                is.setItemMeta(im);
                return is;
            } catch (Exception e2) {
                e2.printStackTrace();
                return null;
            }
        }
    }

    public static ItemStack mItemMembers(String material, int amount, short durability, String text, String ken) {
        try {
            ItemStack is = new ItemStack(Material.getMaterial(material.toUpperCase()), amount, durability);
            ItemMeta im = is.getItemMeta();
            List<String> itemlore = new ArrayList<>();
            if (ConfigModule.vehicleDataConfig.getConfig().getStringList("vehicle." + ken + ".members").size() == 0) {
                itemlore.add(TextUtils.colorize((("&7Members: &eGeen"))));
            } else {
                for (String subj : ConfigModule.vehicleDataConfig.getConfig().getStringList("vehicle." + ken + ".members")) {
                    itemlore.add(TextUtils.colorize(("&7- &e" + Bukkit.getOfflinePlayer(UUID.fromString(subj)).getName())));
                }
            }
            im.setLore(itemlore);
            im.setDisplayName(TextUtils.colorize(text));
            is.setItemMeta(im);
            return is;
        } catch (Exception e) {
            try {
                ItemStack is = new ItemStack(Material.matchMaterial(material, true), amount, durability);
                ItemMeta im = is.getItemMeta();
                List<String> itemlore = new ArrayList<>();
                if (ConfigModule.vehicleDataConfig.getConfig().getStringList("vehicle." + ken + ".members").size() == 0) {
                    itemlore.add(TextUtils.colorize((("&7Members: &eGeen"))));
                } else {
                    for (String subj : ConfigModule.vehicleDataConfig.getConfig().getStringList("vehicle." + ken + ".members")) {
                        itemlore.add(TextUtils.colorize(("&7- &e" + Bukkit.getOfflinePlayer(UUID.fromString(subj)).getName())));
                    }
                }
                im.setLore(itemlore);
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
            ItemStack car = new ItemStack(Material.getMaterial(material.toUpperCase()), amount, durability);
            ItemStack is = new ItemFactory(car).setNBT(model, nbt).toItemStack();
            ItemMeta im = is.getItemMeta();
            List<String> itemlore = new ArrayList<>();
            itemlore.add(TextUtils.colorize(lores));
            im.setLore(itemlore);
            im.setUnbreakable(true);
            im.setDisplayName(TextUtils.colorize(text));
            is.setItemMeta(im);
            return is;
        } catch (Exception e) {
            try {
                ItemStack is = new ItemStack(Material.getMaterial(material.toUpperCase(),true), amount, durability);
                ItemMeta im = is.getItemMeta();
                List<String> itemlore = new ArrayList<>();
                itemlore.add(TextUtils.colorize(lores));
                im.setLore(itemlore);
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
            ItemStack is = new ItemStack(Material.getMaterial(material.toUpperCase()), amount, durability);
            ItemMeta im = is.getItemMeta();
            List<String> itemlore = new ArrayList<>();
            itemlore.add(TextUtils.colorize(lores));
            im.setLore(itemlore);
            im.setUnbreakable(true);
            im.setDisplayName(TextUtils.colorize(text));
            is.setItemMeta(im);
            return is;
        } catch (Exception e) {
            try {
                ItemStack is = new ItemStack(Material.getMaterial(material.toUpperCase(),true), amount, durability);
                ItemMeta im = is.getItemMeta();
                List<String> itemlore = new ArrayList<>();
                itemlore.add(TextUtils.colorize(lores));
                im.setLore(itemlore);
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
        ItemStack car = (new ItemFactory(Material.getMaterial(material))).setDurability((short) id).setName(TextUtils.colorize("&6" + name)).setNBT("mtvehicles.kenteken", ken).setNBT("mtvehicles.naam", name).toItemStack();
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
        ItemStack car = (new ItemFactory(Material.getMaterial(material))).setDurability((short) id).setName(TextUtils.colorize("&6" + name)).setNBT("mtvehicles.kenteken", ken).setNBT("mtvehicles.naam", name).toItemStack();
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
        List<String> goldlore = new ArrayList<>();
        goldlore.add(TextUtils.colorize("&8&m                                    "));
        goldlore.add(TextUtils.colorize("&7Gebruik &2rechterklik&r&7 op deze voucher om"));
        goldlore.add(TextUtils.colorize("&7hem te verzilveren"));
        goldlore.add(TextUtils.colorize("&2&l"));
        goldlore.add(TextUtils.colorize("&7Deze &2voucher &r&7is houdbaar tot:"));
        goldlore.add(TextUtils.colorize("&2permanent"));
        goldlore.add(TextUtils.colorize("&8&m                                    "));
        im.setLore(goldlore);
        im.setDisplayName(TextUtils.colorize("&2&l" + Vehicle.getCar(name).getItemMeta().getDisplayName()));
        is.setItemMeta(im);
        p.getInventory().addItem(new ItemStack[]{is});
    }
}
