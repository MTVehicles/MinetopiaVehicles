package nl.mtvehicles.core.infrastructure.dataconfig;

import nl.mtvehicles.core.Main;
import nl.mtvehicles.core.infrastructure.helpers.TextUtils;
import nl.mtvehicles.core.infrastructure.models.ConfigUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.List;

public class MessagesConfig extends ConfigUtils {
    public String[] foreignLanguages = {"nl", "es", "cs"}; //All the message files, except English, which is default

    public MessagesConfig() {
        this.setFileName("messages/messages_en.yml");
        for (String lang : foreignLanguages) {
            saveLanguageFile(lang);
        }
    }

    public String getMessage(String key) {
        return TextUtils.colorize((String) this.getConfig().get(key));
    }

    public void sendMessage(CommandSender sender, String key) {
        Object object = this.getConfig().get(key);
        if (object instanceof List) {
            for (String s : this.getConfig().getStringList(key)) {
                sender.sendMessage(TextUtils.colorize(s));
            }
        }
        sender.sendMessage(TextUtils.colorize(String.valueOf(object)));
    }

    public void sendMessage(Player player, String key) {
        Object object = this.getConfig().get(key);
        if (object instanceof List) {
            for (String s : this.getConfig().getStringList(key)) {
                player.sendMessage(TextUtils.colorize(s));
            }
        }
        player.sendMessage(TextUtils.colorize(String.valueOf(object)));
    }

    public boolean setLanguageFile(String countryCode){
        String fileName = "messages/messages_" + countryCode + ".yml";
        File languageFile = new File(Main.instance.getDataFolder(), fileName);
        if (!languageFile.exists()) return false;

        this.setFileName(fileName);
        this.setCustomConfigFile(new File(Main.instance.getDataFolder(), fileName));
        this.reload();
        return true;
    }

    private void saveLanguageFile(String countryCode){
        String fileName = "messages/messages_" + countryCode + ".yml";

        File languageFile = new File(Main.instance.getDataFolder(), fileName);
        if (!languageFile.exists()){
            Main.instance.saveResource(fileName, false);
        }
    }

    public void saveNewLanguageFiles(String time){
        File enMessagesFile = new File(Main.instance.getDataFolder(), "messages/messages_en.yml");
        enMessagesFile.renameTo(new File(Main.instance.getDataFolder(), "messages/messages_enOld_" + time + ".yml"));
        Main.instance.saveResource("messages/messages_en.yml", true);

        for (String lang : foreignLanguages) {
            File messagesFile = new File(Main.instance.getDataFolder(), "messages/messages_" + lang + ".yml");
            if (!messagesFile.exists()) continue;
            messagesFile.renameTo(new File(Main.instance.getDataFolder(), "messages/messages_" + lang + "Old_" + time + ".yml"));
            Main.instance.saveResource("messages/messages_" + lang + ".yml", true);
        }
    }
}