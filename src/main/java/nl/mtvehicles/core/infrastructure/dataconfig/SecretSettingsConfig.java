package nl.mtvehicles.core.infrastructure.dataconfig;

import nl.mtvehicles.core.infrastructure.enums.ConfigType;
import nl.mtvehicles.core.infrastructure.models.Config;

public class SecretSettingsConfig extends Config {

    public SecretSettingsConfig(){
        super(ConfigType.SUPERSECRETSETTINGS);
    }

    public String getConfigVersion(){
        return this.getConfig().getString("configVersion");
    }

    public String getMessagesVersion(){
        return this.getConfig().getString("messagesVersion");
    }

    public String getMessagesLanguage(){
        return this.getConfig().getString("messagesLanguage");
    }

    public void setMessagesLanguage(String languageCode){
        this.getConfig().set("messagesLanguage", languageCode);
        this.save();
    }
}
