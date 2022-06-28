package nl.mtvehicles.core.infrastructure.enums;

import nl.mtvehicles.core.infrastructure.annotations.ToDo;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Languages which are offered by the plugin
 */
public enum Language {
    /**
     * English
     */
    EN("English"),
    /**
     * Dutch
     */
    NL("Nederlands"),
    /**
     * Spanish
     */
    ES("Español"),
    /**
     * Czech
     */
    CS("Čeština"),
    /**
     * German
     */
    DE("Deutsch"),
    /**
     * Chinese
     */
    CN("中國人"),
    /**
     * Turkish
     */
    TR("Türk"),
    /**
     * Japanese
     */
    JA("日本語"),
    /**
     * Hebrew
     */
    HE("עִברִית"),
    /**
     * Russian
     */
    RU("Русский"),
    /**
     * Custom language one can create and use
     */
    CUSTOM("Custom language");

    final private String languageName;

    private Language(String languageName){
        this.languageName = languageName;
    }

    /**
     * Get the language name
     * @return Language name (in its own language)
     */
    public String getLanguageName(){
        return this.languageName;
    }

    /**
     * Get the language code
     * @return Language code (e.g. "en")
     */
    public String getLanguageCode(){
        if (this.equals(CUSTOM)) {
            return ConfigModule.secretSettings.getMessagesLanguage();
        }
        return this.toString().toLowerCase(Locale.ROOT);
    }

    /**
     * Get an array of all languages (in their country codes)
     * @return Array of all supported languages
     */
    @ToDo("make this automatic from the enum's values")
    public static String[] getAllLanguages(){
        String[] languages = {"en", "nl", "es", "cs", "cn", "de", "tr", "ja", "he", "ru"};
        return languages;
    }

    /**
     * Check whether the language is supported
     *
     * @param languageCode Language code (e.g. "en")
     * @return Whether the language is supported
     */
    public static boolean isSupported(String languageCode){
        List<String> languages = new ArrayList<>(Arrays.asList(getAllLanguages()));
        return languages.contains(languageCode.toLowerCase(Locale.ROOT));
    }
}
