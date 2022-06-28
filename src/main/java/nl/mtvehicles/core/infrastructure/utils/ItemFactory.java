package nl.mtvehicles.core.infrastructure.utils;

import de.tr7zw.changeme.nbtapi.NBTItem;
import nl.mtvehicles.core.Main;
import nl.mtvehicles.core.infrastructure.annotations.VersionSpecific;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static nl.mtvehicles.core.infrastructure.modules.VersionModule.getServerVersion;

/**
 * Class for an easy creation of items
 */
public class ItemFactory {
    private ItemStack item;
    private @Nullable String skullOwner;

    public ItemFactory(Material material) {
        this(material, 1);
    }

    public ItemFactory(ItemStack itemStack) {
        this.item = itemStack;
    }

    public ItemFactory(Material material, int amount) {
        this.item = new ItemStack(material, amount);
    }

    public ItemFactory(Material material, int amount, int durability) {
        this.item = new ItemStack(material, amount);
        this.setDurability(durability);
    }

    public ItemFactory clone() {
        return new ItemFactory(this.item);
    }

    @VersionSpecific
    public ItemFactory setDurability(int durability) {
        if (getServerVersion().is1_12()) this.item.setDurability((short) durability);
        else {
            ItemMeta im = this.item.getItemMeta();
            ((org.bukkit.inventory.meta.Damageable) im).setDamage(durability);
            this.item.setItemMeta(im);
        }
        return this;
    }

    public ItemFactory setType(Material material) {
        this.item.setType(material);
        return this;
    }

    public ItemFactory setName(String name) {
        ItemMeta im = this.item.getItemMeta();
        im.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        this.item.setItemMeta(im);
        return this;
    }

    public ItemFactory setAmount(int amount) {
        this.item.setAmount(amount);
        return this;
    }

    public ItemFactory setUnbreakable(boolean unbreakable){
        ItemMeta im = this.item.getItemMeta();
        im.setUnbreakable(unbreakable);
        this.item.setItemMeta(im);
        return this;
    }

    public List<String> getLore() {
        return this.item.getItemMeta().getLore();
    }

    public ItemFactory setLore(List<String> lore) {
        ItemMeta im = this.item.getItemMeta();
        List<String> formatted = new ArrayList<>();
        for (String str : lore)
            formatted.add(ChatColor.translateAlternateColorCodes('&', str));
        im.setLore(formatted);
        this.item.setItemMeta(im);
        return this;
    }

    public ItemFactory removeLore() {
        ItemMeta im = this.item.getItemMeta();
        List<String> lore = new ArrayList<>(im.getLore());
        lore.clear();
        im.setLore(lore);
        this.item.setItemMeta(im);
        return this;
    }

    public ItemFactory hideAttributes() {
        ItemMeta im = this.item.getItemMeta();
        assert im != null;
        im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        im.addItemFlags(ItemFlag.HIDE_PLACED_ON);
        im.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        im.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        this.item.setItemMeta(im);
        return this;
    }

    public ItemFactory addUnsafeEnchantment(Enchantment enchantment, int level) {
        this.item.addUnsafeEnchantment(enchantment, level);
        return this;
    }

    public ItemFactory removeEnchantment(Enchantment enchantment) {
        this.item.removeEnchantment(enchantment);
        return this;
    }

    public ItemFactory addEnchant(Enchantment enchantment, int level) {
        ItemMeta im = this.item.getItemMeta();
        assert im != null;
        im.addEnchant(enchantment, level, true);
        this.item.setItemMeta(im);
        return this;
    }

    public ItemFactory setGlowing(boolean glowing){
        if (glowing) {
            try {
                if (!this.item.getItemMeta().hasEnchant(Enchantment.ARROW_INFINITE))
                    addEnchantGlow();
            } catch (Exception e){
                Main.logSevere("Unable to set glowing state to true.");
            }
        } else {
            if (this.item.getItemMeta().hasEnchant(Enchantment.ARROW_INFINITE))
                removeEnchantment(Enchantment.ARROW_INFINITE);
        }
        return this;
    }

    public ItemFactory addEnchantGlow() {
        ItemMeta im = this.item.getItemMeta();
        assert im != null;
        im.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
        im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        this.item.setItemMeta(im);
        return this;
    }

    public ItemFactory addEnchantments(Map<Enchantment, Integer> enchantments) {
        this.item.addEnchantments(enchantments);
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
        ItemMeta im = this.item.getItemMeta();
        assert im != null;
        im.setLore(loreList);
        this.item.setItemMeta(im);
        return this;
    }

    public ItemFactory addLoreLines(List<String> line) {
        ItemMeta im = this.item.getItemMeta();
        List<String> lore = new ArrayList<>();
        assert im != null;
        if (im.hasLore())
            lore = new ArrayList<>(im.getLore());
        for (String s : line)
            lore.add(ChatColor.translateAlternateColorCodes('&', s));
        im.setLore(lore);
        this.item.setItemMeta(im);
        return this;
    }

    public ItemFactory setNBT(String key, String value) {
        NBTItem nbt = new NBTItem(this.item);
        nbt.setString(key, value);
        nbt.mergeNBT(this.item);
        return this;
    }

    public ItemFactory removeLoreLine(String line) {
        ItemMeta im = this.item.getItemMeta();
        List<String> lore = new ArrayList<>(im.getLore());
        if (!lore.contains(line))
            return this;
        lore.remove(line);
        im.setLore(lore);
        this.item.setItemMeta(im);
        return this;
    }

    public ItemFactory removeLoreLine(int index) {
        ItemMeta im = this.item.getItemMeta();
        List<String> lore = new ArrayList<>(im.getLore());
        if (index < 0 || index > lore.size())
            return this;
        lore.remove(index);
        im.setLore(lore);
        this.item.setItemMeta(im);
        return this;
    }

    public ItemFactory addLoreLine(String line) {
        ItemMeta im = this.item.getItemMeta();
        List<String> lore = new ArrayList<>();
        if (im.hasLore())
            lore = new ArrayList<>(im.getLore());
        lore.add(ChatColor.translateAlternateColorCodes('&', line));
        im.setLore(lore);
        this.item.setItemMeta(im);
        return this;
    }

    public ItemFactory addLoreLine(String line, int pos) {
        ItemMeta im = this.item.getItemMeta();
        List<String> lore = new ArrayList<>(im.getLore());
        lore.set(pos, ChatColor.translateAlternateColorCodes('&', line));
        im.setLore(lore);
        this.item.setItemMeta(im);
        return this;
    }

    public ItemFactory setDyeColor(DyeColor color) {
        this.item.setDurability(color.getDyeData());
        return this;
    }

    public ItemFactory setLeatherArmorColor(Color color) {
        try {
            LeatherArmorMeta im = (LeatherArmorMeta) this.item.getItemMeta();
            im.setColor(color);
            this.item.setItemMeta(im);
        } catch (ClassCastException classCastException) {
        }
        return this;
    }

    public ItemStack toItemStack() {
        return this.item;
    }

    public String getSkullOwner() {
        return this.skullOwner;
    }

    /**
     * @deprecated Use {@link #setSkullOwner(OfflinePlayer)} instead.
     */
    @Deprecated
    public ItemFactory setSkullOwner(String owner) {
        try {
            SkullMeta im = (SkullMeta) this.item.getItemMeta();
            im.setOwner(owner);
            this.item.setItemMeta(im);
        } catch (ClassCastException e) {
            Main.logSevere("Cannot use ItemFactory#setSkullOwner(String) on given item!");
            e.printStackTrace();
        }
        return this;
    }

    public ItemFactory setSkullOwner(OfflinePlayer owner) {
        try {
            SkullMeta im = (SkullMeta) this.item.getItemMeta();
            im.setOwningPlayer(owner);
            this.item.setItemMeta(im);
        } catch (ClassCastException e) {
            Main.logSevere("Cannot use ItemFactory#setSkullOwner(OfflinePlayer) on given item!");
            e.printStackTrace();
        }
        return this;
    }
}
