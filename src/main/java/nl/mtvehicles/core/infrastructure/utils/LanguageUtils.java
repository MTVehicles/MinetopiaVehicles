package nl.mtvehicles.core.infrastructure.utils;

import nl.mtvehicles.core.Main;
import nl.mtvehicles.core.infrastructure.enums.InventoryTitle;
import nl.mtvehicles.core.infrastructure.enums.Language;
import nl.mtvehicles.core.infrastructure.enums.Message;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.UUID;

/**
 * Methods for changing plugin's language
 */
public class LanguageUtils {
    /**
     * Contains information about whether player has language GUI opened
     */
    public static HashMap<UUID, Boolean> languageCheck = new HashMap<>();

    /**
     * Open the language GUI selection to a player
     */
    public static void openLanguageGUI(Player p){
        Inventory inv = Bukkit.createInventory(null, 18, InventoryTitle.CHOOSE_LANGUAGE_MENU.getStringTitle());
        inv.setItem(0, ItemUtils.getMenuItem(Material.GOLD_BLOCK, 1, "&eEnglish", "&7Press to set all messages to English."));
        inv.setItem(1, ItemUtils.getMenuItem(Material.DIAMOND_BLOCK, 1, "&9Dutch (Nederlands)", "&7Druk om alle berichten op Nederlands te zetten."));
        inv.setItem(2, ItemUtils.getMenuItem(Material.EMERALD_BLOCK, 1, "&2Spanish (Español)", "&7Presione para configurar todos los mensajes en español."));
        inv.setItem(3, ItemUtils.getMenuItem(Material.REDSTONE_BLOCK, 1, "&4Czech (Čeština)", "&7Klikni pro nastavení všech zpráv do češtiny."));
        inv.setItem(4, ItemUtils.getMenuItem(Material.IRON_BLOCK, 1, "&fGerman (Deutsch)", "&7Drücken Sie, um alle Nachrichten auf Deutsch einzustellen."));
        inv.setItem(5, ItemUtils.getMenuItem(Material.LAPIS_BLOCK, 1, "&9Chinese (中國人)", "&7按 將所有消息設置為中文。"));
        inv.setItem(6, ItemUtils.getMenuItem(Material.SLIME_BLOCK, 1, "&aTurkish (Türk)", "&7Tüm mesajları Türkçe olarak ayarlamak için basın."));
        inv.setItem(7, ItemUtils.getMenuItem(Material.GLASS, 1, "&fJapanese (日本語)", "&7を押して、すべてのメッセージを日本語に設定します。"));
        inv.setItem(8, ItemUtils.getMenuItem(Material.STONE, 1, "&8Hebrew (עִברִית)", "&7.לחץ כדי להגדיר את כל ההודעות לעברית"));
        inv.setItem(9, ItemUtils.getMenuItem(Material.GOLD_ORE, 1, "&6Russian (Русский)", "&7Нажмите, чтобы перевести все сообщения на русский язык."));
        inv.setItem(17, ItemUtils.getMenuItem(Material.PAPER, 1, "&fThat's all for now!", "&7Do you want to help us by translating the plugin? &f&nClick here"));
        p.openInventory(inv);
        languageCheck.put(p.getUniqueId(), true);
    }

    /**
     * Called when a player clicks an item in /vehicle language menu, calls {@link #changeLanguage(Player, Language)}.
     * @param p Player who clicked
     * @param clickedSlot Clicked slot (= ordinal of {@link Language})
     */
    public static void changeLanguageMenu(Player p, int clickedSlot){
        if (clickedSlot == 17) {
            LanguageUtils.languageCheck.put(p.getUniqueId(), false);
            p.sendMessage("§6You may find more information here: §e§nhttps://wiki.mtvehicles.eu/translating.html");
            return;
        }
        Language language = Language.values()[clickedSlot];
        changeLanguage(p, language);
    }

    /**
     * Change language of the plugin
     * @param p Player who is changing the language
     * @param language New language ({@link Language} enum)
     */
    public static void changeLanguage(Player p, Language language){
        String languageCode = language.getLanguageCode();
        languageCheck.put(p.getUniqueId(), false);
        if (ConfigModule.messagesConfig.setLanguageFile(languageCode)){
            p.sendMessage(ConfigModule.messagesConfig.getMessage(Message.LANGUAGE_HAS_CHANGED));
            ConfigModule.secretSettings.setMessagesLanguage(language);
            ConfigModule.secretSettings.save();
        } else {
            p.sendMessage(ChatColor.RED + "An error occurred whilst trying to set a new language.");
            Main.instance.getLogger().severe(String.format("Could not find file messages/messages_%s.yml, aborting...", languageCode));
        }
    }
}
