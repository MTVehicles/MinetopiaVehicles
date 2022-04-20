package nl.mtvehicles.core.infrastructure.dataconfig;

import nl.mtvehicles.core.Main;
import nl.mtvehicles.core.infrastructure.enums.ConfigType;
import nl.mtvehicles.core.infrastructure.enums.Language;
import nl.mtvehicles.core.infrastructure.enums.Message;
import nl.mtvehicles.core.infrastructure.helpers.TextUtils;
import nl.mtvehicles.core.infrastructure.models.Config;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import org.bukkit.command.CommandSender;

import java.io.File;
import java.util.Locale;

public class MessagesConfig extends Config {
    private Language language;

    public MessagesConfig() {
        super(ConfigType.MESSAGES);
        for (String lang : Language.getAllLanguages()) {
            saveLanguageFile(lang);
        }
        if (!setLanguageFile(ConfigModule.secretSettings.getMessagesLanguage())){
            Main.instance.getLogger().severe("Messages.yml for your desired language could not be found. Disabling the plugin...");
            Main.disablePlugin();
        }
    }

    @Deprecated
    public String getMessage(String key) {
        String msg = "";
        try {
            msg = TextUtils.colorize((String) this.getConfiguration().get(key));
        } catch (Exception e){
            Main.instance.getLogger().severe("An error occurred while retrieving a custom message from the messages.yml!");
        }
        return msg;
    }

    public String getMessage(Message message){
        String msg = "";
        try {
            msg = TextUtils.colorize(this.getConfiguration().getString(message.getKey()));
        } catch (Exception e){
            Main.instance.getLogger().severe("An error occurred while retrieving a custom message from the messages.yml!");
        }
        return msg;
    }

    @Deprecated
    public void sendMessage(CommandSender sender, String key) {
        sender.sendMessage(getMessage(key));
    }

    public void sendMessage(CommandSender sender, Message message) {
        sender.sendMessage(getMessage(message));
    }

    public boolean setLanguageFile(String languageCode){
        String countryCode = (languageCode.equals("ns")) ? "en" : languageCode;
        this.language = (Language.isSupported(languageCode)) ? Language.valueOf(languageCode.toUpperCase(Locale.ROOT)) : Language.CUSTOM;
        String fileName = "messages/messages_" + countryCode + ".yml";
        File languageFile = new File(Main.instance.getDataFolder(), fileName);
        if (!languageFile.exists()) return false;

        this.setFileName(fileName);
        this.setConfigFile(new File(Main.instance.getDataFolder(), fileName));
        this.reload();
        return true;
    }

    private void saveLanguageFile(String countryCode){
        String fileName = "messages/messages_" + countryCode + ".yml";

        File languageFile = new File(Main.instance.getDataFolder(), fileName);
        if (!languageFile.exists()) Main.instance.saveResource(fileName, false);
    }

    public void saveNewLanguageFiles(String time){
        for (String lang : Language.getAllLanguages()) {
            File messagesFile = new File(Main.instance.getDataFolder(), "messages/messages_" + lang + ".yml");
            if (!messagesFile.exists()) continue;
            messagesFile.renameTo(new File(Main.instance.getDataFolder(), "messages/messages_" + lang + "Old_" + time + ".yml"));
            Main.instance.saveResource("messages/messages_" + lang + ".yml", true);
        }
    }
}
