package nl.mtvehicles.core.infrastructure.models;

/**
 * Basic interface for configuration files
 * @deprecated This interface may be removed as there is no real need for it.
 */
@Deprecated
public interface ConfigInterface {

    /**
     * Reload configuration file (e.g. if you've just edited it in a text editor)
     */
    void reload();

    /**
     * Save the newly assigned values to the configuration file
     *
     * @return Whether saving was successful
     */
    boolean save();

}
