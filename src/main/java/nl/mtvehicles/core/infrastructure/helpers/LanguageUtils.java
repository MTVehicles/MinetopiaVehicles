package nl.mtvehicles.core.infrastructure.helpers;

import nl.mtvehicles.core.Main;
import nl.mtvehicles.core.infrastructure.enums.Language;
import nl.mtvehicles.core.infrastructure.enums.Message;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.UUID;

public class LanguageUtils {
    public static HashMap<UUID, Boolean> languageCheck = new HashMap<>();

    public static void openLanguageGUI(Player p){
        Inventory inv = Bukkit.createInventory(null, 27, "Choose your language");
        inv.setItem(9, ItemUtils.mItem("GOLD_BLOCK", 1, (short) 0, "&eEnglish", "&7Press to set all messages to English."));
        inv.setItem(11, ItemUtils.mItem("DIAMOND_BLOCK", 1, (short) 0, "&9Dutch (Nederlands)", "&7Druk om alle berichten op Nederlands te zetten."));
        inv.setItem(13, ItemUtils.mItem("EMERALD_BLOCK", 1, (short) 0, "&2Spanish (Español)", "&7Presione para configurar todos los mensajes en español."));
        inv.setItem(15, ItemUtils.mItem("REDSTONE_BLOCK", 1, (short) 0, "&4Czech (Čeština)", "&7Klikni pro nastavení všech zpráv do češtiny."));
        inv.setItem(17, ItemUtils.mItem("PAPER", 1, (short) 0, "&fThat's all for now!", "&7Wanna help us by translating the plugin? &f&ndiscord.gg/vehicle"));
        p.openInventory(inv);
        languageCheck.put(p.getUniqueId(), true);
    }

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
