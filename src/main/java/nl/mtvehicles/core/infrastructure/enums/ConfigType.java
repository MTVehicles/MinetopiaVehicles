package nl.mtvehicles.core.infrastructure.enums;

/**
 * Type of configuration file
 */
public enum ConfigType {
    DEFAULT("config.yml"),
    VEHICLES("vehicles.yml"),
    VEHICLE_DATA("vehicleData.yml"),
    SUPERSECRETSETTINGS("supersecretsettings.yml"),
    MESSAGES;

    private String fileName = null;

    private ConfigType(){}

    private ConfigType(String fileName){
        this.fileName = fileName;
    }

    /**
     * Whether the config type is a type of messages
     * @return Whether the config type is a type of messages
     */
    public boolean isMessages(){
        return this.equals(MESSAGES);
    }

    /**
     * Get config's file name
     * @return Name / Path of config
     */
    public String getFileName(){
        return this.fileName;
    }
}
