package nl.mtvehicles.core.infrastructure.dataconfig;

import nl.mtvehicles.core.infrastructure.enums.ConfigType;
import nl.mtvehicles.core.infrastructure.models.MTVConfig;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;

import java.util.List;
import java.util.Map;

/**
 * Methods for vehicles.yml.<br>
 * <b>Do not initialise this class directly. Use {@link ConfigModule#vehiclesConfig} instead.</b>
 */
public class VehiclesConfig extends MTVConfig {
    /**
     * Default constructor - <b>do not use this.</b><br>
     * Use {@link ConfigModule#vehiclesConfig} instead.
     */
    public VehiclesConfig() {
        super(ConfigType.VEHICLES);
    }

    /**
     * Get Map of the whole file.
     */
    public List<Map<?,?>> getVehicles(){
        return getConfiguration().getMapList("voertuigen");
    }
}
