package nl.mtvehicles.core.infrastructure.helpers;

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
        inv.setItem(10, ItemUtils.mItem("GOLD_BLOCK", 1, (short) 0, "&eEnglish", "&7Press to set all messages to English."));
        inv.setItem(12, ItemUtils.mItem("DIAMOND_BLOCK", 1, (short) 0, "&9Dutch (Nederlands)", "&7Druk om alle berichten op Nederlands te zetten."));
        inv.setItem(14, ItemUtils.mItem("EMERALD_BLOCK", 1, (short) 0, "&2Spanish (Español)", "&7Presione para configurar todos los mensajes en español."));
        inv.setItem(16, ItemUtils.mItem("REDSTONE_BLOCK", 1, (short) 0, "&4Czech (Čeština)", "&7Klikni pro nastavení všech zpráv do češtiny."));
        p.openInventory(inv);
        languageCheck.put(p.getUniqueId(), true);
    }

    public static void changeLanguage(Player p, String languageCode){
        languageCheck.put(p.getUniqueId(), false);
        if (ConfigModule.messagesConfig.setLanguageFile(languageCode)){
            p.sendMessage(ConfigModule.messagesConfig.getMessage("languageHasChanged"));
            ConfigModule.defaultConfig.getConfig().set("messagesLanguage", languageCode);
            ConfigModule.defaultConfig.save();
        } else {
            p.sendMessage(ChatColor.RED + "An error occurred whilst trying to set a new language.");
        }
    }
}
