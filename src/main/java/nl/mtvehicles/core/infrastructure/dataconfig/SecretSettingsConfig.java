package nl.mtvehicles.core.infrastructure.dataconfig;

import nl.mtvehicles.core.infrastructure.enums.ConfigType;
import nl.mtvehicles.core.infrastructure.models.Config;

public class SecretSettingsConfig extends Config {

    public SecretSettingsConfig(){
        super(ConfigType.SUPERSECRETSETTINGS);
    }

    public String getConfigVersion(){
        return this.getConfiguration().getString("configVersion");
    }

    public String getMessagesVersion(){
        return this.getConfiguration().getString("messagesVersion");
    }

    public String getMessagesLanguage(){
        return this.getConfiguration().getString("messagesLanguage");
    }

    public void setMessagesLanguage(String languageCode){
        this.getConfiguration().set("messagesLanguage", languageCode);
        this.save();
    }
}
