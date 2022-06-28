package nl.mtvehicles.core.infrastructure.dataconfig;

import nl.mtvehicles.core.infrastructure.enums.ConfigType;
import nl.mtvehicles.core.infrastructure.enums.Language;
import nl.mtvehicles.core.infrastructure.models.MTVConfig;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;

/**
 * Methods for supersecretsettings.yml.<br>
 * <b>Do not initialise this class directly. Use {@link ConfigModule#secretSettings} instead.</b>
 */
public class SecretSettingsConfig extends MTVConfig {

    /**
     * Default constructor - <b>do not use this.</b><br>
     * Use {@link ConfigModule#secretSettings} instead.
     */
    public SecretSettingsConfig(){
        super(ConfigType.SUPERSECRETSETTINGS);
    }

    /**
     * Get config version.
     * @see nl.mtvehicles.core.Main#configVersion
     */
    public String getConfigVersion(){
        return this.getConfiguration().getString("configVersion");
    }

    /**
     * Get message files version.
     * @see nl.mtvehicles.core.Main#messagesVersion
     */
    public String getMessagesVersion(){
        return this.getConfiguration().getString("messagesVersion");
    }

    /**
     * Get language used by the plugin.
     * @see Language
     */
    public String getMessagesLanguage(){
        return this.getConfiguration().getString("messagesLanguage");
    }

    /**
     * Set language used by the plugin
     * @param language New language
     * @throws IllegalArgumentException If language is specified as CUSTOM - custom language may only be set manually, not via this method.
     */
    public void setMessagesLanguage(Language language) throws IllegalArgumentException {
        if (language == Language.CUSTOM) throw new IllegalArgumentException("CUSTOM language can't be used in this method.");

        String languageCode = language.getLanguageCode();
        this.getConfiguration().set("messagesLanguage", languageCode);
        this.save();
    }
}
