package nl.mtvehicles.core.infrastructure.helpers;

import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ItemFactory {
    private ItemStack is;
    private String skullOwner;
    private static Enchantment glow;

    public ItemFactory(Material material) {
        this(material, 1);
    }

    public ItemFactory(ItemStack itemStack) {
        this.is = itemStack;
    }

    public ItemFactory(Material material, int amount) {
        this.is = new ItemStack(material, amount);
    }

    public ItemFactory(Material material, int amount, byte durability) {
        this.is = new ItemStack(material, amount, durability);
    }

    public ItemFactory clone() {
        return new ItemFactory(this.is);
    }

    public ItemFactory setDurability(short durability) {
        this.is.setDurability(durability);
        return this;
    }

    public ItemFactory setType(Material material) {
        this.is.setType(material);
        return this;
    }

    public ItemFactory setName(String name) {
        ItemMeta im = this.is.getItemMeta();
        im.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        this.is.setItemMeta(im);
        return this;
    }

    public ItemFactory setAmount(int amount) {
        this.is.setAmount(amount);
        return this;
    }

    public List<String> getLore() {
        return this.is.getItemMeta().getLore();
    }

    public ItemFactory setLore(List<String> lore) {
        ItemMeta im = this.is.getItemMeta();
        List<String> formatted = new ArrayList<>();
        for (String str : lore)
            formatted.add(ChatColor.translateAlternateColorCodes('&', str));
        im.setLore(formatted);
        this.is.setItemMeta(im);
        return this;
    }

    public ItemFactory removeLore() {
        ItemMeta im = this.is.getItemMeta();
        List<String> lore = new ArrayList<>(im.getLore());
        lore.clear();
        im.setLore(lore);
        this.is.setItemMeta(im);
        return this;
    }

    public ItemFactory hideAttributes() {
        ItemMeta im = this.is.getItemMeta();
        assert im != null;
        im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        im.addItemFlags(ItemFlag.HIDE_PLACED_ON);
        im.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        im.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        this.is.setItemMeta(im);
        return this;
    }

    public ItemFactory addUnsafeEnchantment(Enchantment enchantment, int level) {
        this.is.addUnsafeEnchantment(enchantment, level);
        return this;
    }

    public ItemFactory removeEnchantment(Enchantment enchantment) {
        this.is.removeEnchantment(enchantment);
        return this;
    }

    public ItemFactory addEnchant(Enchantment enchantment, int level) {
        ItemMeta im = this.is.getItemMeta();
        assert im != null;
        im.addEnchant(enchantment, level, true);
        this.is.setItemMeta(im);
        return this;
    }

    public ItemFactory addEnchantGlow(Enchantment enchantment, int level) {
        ItemMeta im = this.is.getItemMeta();
        assert im != null;
        im.addEnchant(enchantment, level, true);
        im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        this.is.setItemMeta(im);
        return this;
    }

    public ItemFactory addEnchantments(Map<Enchantment, Integer> enchantments) {
        this.is.addEnchantments(enchantments);
        return this;
    }


    public ItemFactory setLore(String... lore) {
        List<String> loreList = new ArrayList<>();
        byte b;
        int i;
        String[] arrayOfString;
        for (i = (arrayOfString = lore).length, b = 0; b < i; ) {
            String loreLine = arrayOfString[b];
            loreList.add(ChatColor.translateAlternateColorCodes('&', loreLine));
            b++;
        }
        ItemMeta im = this.is.getItemMeta();
        assert im != null;
        im.setLore(loreList);
        this.is.setItemMeta(im);
        return this;
    }

    public ItemFactory addLoreLines(List<String> line) {
        ItemMeta im = this.is.getItemMeta();
        List<String> lore = new ArrayList<>();
        assert im != null;
        if (im.hasLore())
            lore = new ArrayList<>(im.getLore());
        for (String s : line)
            lore.add(ChatColor.translateAlternateColorCodes('&', s));
        im.setLore(lore);
        this.is.setItemMeta(im);
        return this;
    }

    public ItemFactory setNBT(String key, String value) {
        is = NBTUtils.set(is, value, key);
        return this;
    }

    public ItemFactory removeLoreLine(String line) {
        ItemMeta im = this.is.getItemMeta();
        List<String> lore = new ArrayList<>(im.getLore());
        if (!lore.contains(line))
            return this;
        lore.remove(line);
        im.setLore(lore);
        this.is.setItemMeta(im);
        return this;
    }

    public ItemFactory removeLoreLine(int index) {
        ItemMeta im = this.is.getItemMeta();
        List<String> lore = new ArrayList<>(im.getLore());
        if (index < 0 || index > lore.size())
            return this;
        lore.remove(index);
        im.setLore(lore);
        this.is.setItemMeta(im);
        return this;
    }

    public ItemFactory addLoreLine(String line) {
        ItemMeta im = this.is.getItemMeta();
        List<String> lore = new ArrayList<>();
        if (im.hasLore())
            lore = new ArrayList<>(im.getLore());
        lore.add(ChatColor.translateAlternateColorCodes('&', line));
        im.setLore(lore);
        this.is.setItemMeta(im);
        return this;
    }

    public ItemFactory addLoreLine(String line, int pos) {
        ItemMeta im = this.is.getItemMeta();
        List<String> lore = new ArrayList<>(im.getLore());
        lore.set(pos, ChatColor.translateAlternateColorCodes('&', line));
        im.setLore(lore);
        this.is.setItemMeta(im);
        return this;
    }

    public ItemFactory setDyeColor(DyeColor color) {
        this.is.setDurability(color.getDyeData());
        return this;
    }

    public ItemFactory setLeatherArmorColor(Color color) {
        try {
            LeatherArmorMeta im = (LeatherArmorMeta) this.is.getItemMeta();
            im.setColor(color);
            this.is.setItemMeta(im);
        } catch (ClassCastException classCastException) {
        }
        return this;
    }

    public ItemStack toItemStack() {
        return this.is;
    }

    public String getSkullOwner() {
        return this.skullOwner;
    }

    public ItemFactory setSkullOwner(String owner) {
        try {
            SkullMeta im = (SkullMeta) this.is.getItemMeta();
            im.setOwner(owner);
            this.is.setItemMeta(im);
        } catch (ClassCastException classCastException) {
        }
        return this;
    }

    public ItemFactory unbreakable() {
        try {
            Object compound;
            Class<?> craftItemStack = getBukkitClass("inventory", "CraftItemStack");
            Method nmsCopy = craftItemStack.getMethod("asNMSCopy", new Class[]{ItemStack.class});
            Object nmsStack = nmsCopy.invoke(null, new Object[]{this.is});
            Class<?> nmsStackClass = nmsStack.getClass();
            Method hasTag = nmsStackClass.getMethod("hasTag", new Class[0]);
            Method getTag = nmsStackClass.getMethod("getTag", new Class[0]);
            Boolean tag = (Boolean) hasTag.invoke(nmsStack, new Object[0]);
            Class<?> nbtTagCompound = getNMSClass("NBTTagCompound");
            if (tag.booleanValue()) {
                compound = getTag.invoke(nmsStack, new Object[0]);
            } else {
                compound = nbtTagCompound.newInstance();
            }
            Method setTag = nmsStackClass.getMethod("setTag", new Class[]{nbtTagCompound});
            Class<?> compoundClass = compound.getClass();
            Class<?> nbtTagBase = getNMSClass("NBTBase");
            Class<?> nbtTagInt = getNMSClass("NBTTagInt");
            Method set = compoundClass.getMethod("set", new Class[]{String.class, nbtTagBase});
            set.invoke(compound, new Object[]{"Unbreakable", nbtTagInt.getConstructor(new Class[]{int.class}).newInstance(new Object[]{Integer.valueOf(1)})});
            setTag.invoke(nmsStack, new Object[]{compound});
            Method asBukkitCopy = craftItemStack.getMethod("asBukkitCopy", new Class[]{nmsStackClass});
            this.is = (ItemStack) asBukkitCopy.invoke(null, new Object[]{nmsStack});
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    private Class<?> getNMSClass(String nmsClassString) {
        try {
            String version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3] + ".";
            String name = "net.minecraft.server." + version + nmsClassString;
            return Class.forName(name);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private Class<?> getBukkitClass(String packageName, String className) {
        try {
            String version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3] + ".";
            String name = "org.bukkit.craftbukkit." + version + packageName + "." + className;
            return Class.forName(name);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
