package nl.mtvehicles.core.infrastructure.dependencies;

import com.ticxo.modelengine.api.model.ActiveModel;
import nl.mtvehicles.core.Main;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import nl.mtvehicles.core.infrastructure.vehicle.Vehicle;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModelEngineUtils {
    public HashMap<ArmorStand, ActiveModel> activeModels = new HashMap<ArmorStand, ActiveModel>();

    public String getIDFromName(String name) {
        String modelID = "";

        List<Map<?, ?>> vehicles = ConfigModule.vehiclesConfig.getVehicles();

        for (Map<?, ?> vehicle : vehicles) {
            List<Map<?, ?>> cars = (List<Map<?, ?>>) vehicle.get("cars");

            for (Map<?, ?> car : cars) {
                if (car.get("name") != null && car.get("name").toString().equalsIgnoreCase(name)) {
                    if (car.get("modelID") != null && !car.get("modelID").toString().isEmpty()) {
                        modelID = car.get("modelID").toString();
                    }

                }
            }
        }
        return modelID;
    }

}
