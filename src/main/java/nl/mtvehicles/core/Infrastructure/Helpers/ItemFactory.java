package nl.mtvehicles.core.Infrastructure.Helpers;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * ItemFactory - Build your {@link ItemStack} the easy way. (1.13)
 * @version 1.0.0
 * @author Melvin Snijders
 * @since 20/11/2018
 */

public class ItemFactory {

    private ItemStack itemStack;
    private ItemMeta itemMeta;
    private List<String> lore = new ArrayList<>();



    public ItemFactory(Material material) {

        this.itemStack = new ItemStack(material);
        this.itemMeta = this.itemStack.getItemMeta();
    }



    public ItemFactory(Material material, int amount) {
        this.itemStack = new ItemStack(material, amount);
        this.itemMeta = this.itemStack.getItemMeta();
    }



    /**
     * Construct ItemFactory class with given {@link ItemStack}.
     * @param itemStack The {@link ItemStack} for the ItemFactory.
     */

    public ItemFactory(ItemStack itemStack) {


        this.itemStack = itemStack;
        this.itemMeta = itemStack.getItemMeta();
        this.lore = this.itemMeta.getLore();

    }

    /**
     * Create a new ItemFactory with a static method (lazy people)
     * @param material The {@link Material} for the ItemFactory.
     * @return The new ItemFactory instance.
     */

    public static ItemFactory newFactory(Material material) {

        return new ItemFactory(material);

    }

    /**
     * Set the {@link ItemMeta} for the ItemStack.
     * @param itemMeta The {@link ItemMeta}.
     * @return The existing ItemFactory instance.
     */

    public ItemFactory setItemMeta(ItemMeta itemMeta) {

        this.itemMeta = itemMeta;
        return this;

    }

    /**
     * Set the {@link Material} for the ItemStack.
     * @param material The {@link Material}.
     * @return The existing ItemFactory instance.
     */

    public ItemFactory setType(Material material) {

        this.itemStack.setType(material);
        return this;

    }

    /**
     * Set the amount for the ItemStack.
     * @param amount The amount.
     * @return The existing ItemFactory instance.
     */

    public ItemFactory setAmount(int amount) {

        this.itemStack.setAmount(amount);
        return this;

    }

    /**
     * Add an {@link Enchantment} to the ItemStack.
     * @param enchantment The {@link Enchantment} to add.
     * @param level The level of the {@link Enchantment}.
     * @return The existing ItemFactory instance.
     */

    public ItemFactory addEnchantment(Enchantment enchantment, int level) {

        this.itemStack.addEnchantment(enchantment, level);
        return this;

    }

    /**
     * Add multiple {@link Enchantment} to the ItemStack.
     * @param enchantments Map of enchantments to be added.
     * @return The existing ItemFactory instance.
     */

    public ItemFactory addEnchantments(Map<Enchantment, Integer> enchantments) {

        this.itemStack.addEnchantments(enchantments);
        return this;

    }

    /**
     * Add an unsafe {@link Enchantment} to the ItemStack.
     * @param enchantment The unsafe {@link Enchantment} to add.
     * @param level The level of the {@link Enchantment}.
     * @return The existing ItemFactory instance.
     */

    public ItemFactory addUnsafeEnchantment(Enchantment enchantment, int level) {

        this.itemStack.addUnsafeEnchantment(enchantment, level);
        return this;

    }

    /**
     * Add multiple unsafe {@link Enchantment} to the ItemStack.
     * @param enchantments Map of unsafe enchantments to be added.
     * @return The existing ItemFactory instance.
     */

    public ItemFactory addUnsafeEnchantments(Map<Enchantment, Integer> enchantments) {

        this.itemStack.addUnsafeEnchantments(enchantments);
        return this;

    }

    /**
     * Remove an {@link Enchantment} from the ItemStack.
     * @param enchantment The {@link Enchantment} to remove.
     * @return The existing ItemFactory instance.
     */

    public ItemFactory removeEnchantment(Enchantment enchantment) {

        this.itemStack.removeEnchantment(enchantment);
        return this;

    }

    /**
     * Set the display name of the ItemStack.
     * @param displayName The display name to set.
     * @return The existing ItemFactory instance.
     */

    public ItemFactory setDisplayName(String displayName) {

        this.itemMeta.setDisplayName(color(displayName));
        return this;

    }

    /**
     * Add a new lore line to the ItemStack.
     * @param line The line to add.
     * @return The existing ItemFactory instance.
     */

    public ItemFactory addLoreLine(String line) {

        this.lore.add(line);
        return this;

    }

    /**
     * Add multiple lore lines to the ItemStack.
     * @param lines The lines to add.
     * @return The existing ItemFactory instance.
     */

    public ItemFactory addLoreLines(String... lines) {

        this.lore.addAll(Arrays.asList(lines));
        return this;

    }

    /**
     * Set the lore of the ItemStack.
     * @param lore The lore as {@link List} to set.
     * @return The existing ItemFactory instance.
     */

    public ItemFactory setLore(List<String> lore) {

        this.lore = lore;
        return this;

    }

    /**
     * Set the lore of the ItemStack.
     * @param lore The lore as {@link String[]} to set.
     * @return The existing ItemFactory instance.
     */

    public ItemFactory setLore(String[] lore) {

        this.lore = Arrays.asList(lore);
        return this;

    }

    /**
     * Remove a lore line by index.
     * @param index The index of the line to remove.
     * @return The existing ItemFactory instance.
     */

    public ItemFactory removeLore(int index) {

        this.lore.remove(index);
        return this;

    }

    /**
     * Clear the lore of the ItemStack.
     * @return The existing ItemFactory instance.
     */

    public ItemFactory clearLore() {

        this.lore.clear();
        return this;

    }

    /**
     * Set the ItemStack unbreakable or not.
     * @param unbreakable Whether it should be unbreakable.
     * @return The existing ItemFactory instance.
     */

    public ItemFactory setUnbreakable(boolean unbreakable) {

        this.itemMeta.setUnbreakable(unbreakable);
        return this;

    }

    /**
     * Set the damage of the ItemStack.
     * @param damage The damage to set.
     * @return The existing ItemFactory instance.
     */

    public ItemFactory setDamage(short damage) {

        ((Damageable) this.itemMeta).setDamage(damage);
        return this;

    }

    /**
     * Add one {@link ItemFlag} to the ItemStack.
     * @param flag The flag to add.
     * @return The existing ItemFactory instance.
     */

    public ItemFactory addItemFlag(ItemFlag flag) {

        this.itemMeta.addItemFlags(flag);
        return this;

    }

    /**
     * Add multiple {@link ItemFlag} to the ItemStack.
     * @param flags The flags to add.
     * @return The existing ItemFactory instance.
     */

    public ItemFactory addItemFlag(ItemFlag... flags) {

        this.itemMeta.addItemFlags(flags);
        return this;

    }

    /**
     * Remove an {@link ItemFlag} from the ItemStack.
     * @param flag The flag to remove.
     * @return The existing ItemFactory instance.
     */

    public ItemFactory removeItemFlag(ItemFlag flag) {

        this.itemMeta.removeItemFlags(flag);
        return this;

    }

    /**
     * Remove multiple {@link ItemFlag} from the ItemStack.
     * @param flags The flags to remove.
     * @return The existing ItemFactory instance.
     */

    public ItemFactory removeItemFlag(ItemFlag... flags) {

        this.itemMeta.removeItemFlags(flags);
        return this;

    }

    /**
     * Add an {@link AttributeModifier} to the ItemStack.
     * @param attribute The {@link Attribute} for the modifier.
     * @param modifier The {@link AttributeModifier} to add.
     * @return The existing ItemFactory instance.
     */

    public ItemFactory addAttributeModifier(Attribute attribute, AttributeModifier modifier) {

        this.itemMeta.addAttributeModifier(attribute, modifier);
        return this;

    }

    /**
     * Remove an {@link AttributeModifier} by {@link Attribute}
     * @param attribute The {@link Attribute} of the modifier to remove.
     * @return The existing ItemFactory instance.
     */

    public ItemFactory removeAttributeModifier(Attribute attribute) {

        this.itemMeta.removeAttributeModifier(attribute);
        return this;

    }

    /**
     * Remove an {@link AttributeModifier} by {@link EquipmentSlot}
     * @param attribute The {@link EquipmentSlot} of the modifier to remove.
     * @return The existing ItemFactory instance.
     */

    public ItemFactory removeAttributeModifier(EquipmentSlot attribute) {

        this.itemMeta.removeAttributeModifier(attribute);
        return this;

    }

    /**
     * Remove an {@link AttributeModifier} by {@link Attribute} and modifier.
     * @param attribute The {@link Attribute} of the modifier to remove.
     * @param modifier The {@link AttributeModifier} to remove.
     * @return The existing ItemFactory instance.
     */

    public ItemFactory removeAttributeModifier(Attribute attribute, AttributeModifier modifier) {

        this.itemMeta.removeAttributeModifier(attribute, modifier);
        return this;

    }

    /**
     * Change if the ItemStack should glow or not.
     * @param glowing Whether the ItemStack shoud glow.
     * @return The existing ItemFactory instance.
     */

    public ItemFactory setGlowing(boolean glowing) {

        Enchantment enchantment = this.itemStack.getType() != Material.BOW ? Enchantment.ARROW_INFINITE : Enchantment.LUCK;

        if(glowing) {
            this.removeEnchantment(enchantment);
            this.removeItemFlag(ItemFlag.HIDE_ENCHANTS);
        } else {
            this.addEnchantment(enchantment, 10);
            this.addItemFlag(ItemFlag.HIDE_ENCHANTS);
        }

        return this;

    }

    /**
     * Hide all attributes of the ItemStack.
     * @return The existing ItemFactory instance.
     */

    public ItemFactory hideAttributes() {

        this.addItemFlag(ItemFlag.HIDE_ATTRIBUTES);
        return this;

    }

    /**
     * Show all attributes of the ItemStack.
     * @return The existing ItemFactory instance.
     */

    public ItemFactory showAttributes() {

        this.removeItemFlag(ItemFlag.HIDE_ATTRIBUTES);
        return this;

    }

    /**
     * Build the {@link ItemStack}.
     * @return The newly created {@link ItemStack}.
     * @since 1.0.0
     */

    public ItemStack build() {

        List<String> newLore = new ArrayList<>();
        this.lore.forEach((lore) -> newLore.add(color(lore)));

        this.itemMeta.setLore(newLore);
        this.itemStack.setItemMeta(this.itemMeta);

        return itemStack;

    }

    /**
     * Color format a String (replace '&').
     * @param color The String to format.
     * @return The color formatted String.
     */

    private String color(String color) {

        return ChatColor.translateAlternateColorCodes('&', color);

    }

}