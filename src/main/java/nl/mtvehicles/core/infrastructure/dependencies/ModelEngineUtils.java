package nl.mtvehicles.core.infrastructure.dependencies;

import com.ticxo.modelengine.api.model.ActiveModel;
import org.bukkit.entity.ArmorStand;

import java.util.HashMap;

public class ModelEngineUtils {
    public HashMap<ArmorStand, ActiveModel> activeModels = new HashMap<ArmorStand, ActiveModel>();
}
