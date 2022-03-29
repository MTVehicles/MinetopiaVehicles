package nl.mtvehicles.core.infrastructure.dataconfig;

import nl.mtvehicles.core.infrastructure.enums.ConfigType;
import nl.mtvehicles.core.infrastructure.models.Config;

import java.util.List;
import java.util.Map;

public class VehiclesConfig extends Config {
    public VehiclesConfig() {
        super(ConfigType.VEHICLES);
    }

    public List<Map<?,?>> getVehicles(){
        return getConfiguration().getMapList("voertuigen");
    }
}
