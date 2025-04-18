package nl.mtvehicles.core.infrastructure.dependencies.skript.expressions;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.registrations.Classes;
import nl.mtvehicles.core.infrastructure.vehicle.Vehicle;

public class Types {
    // Register custom Skript types
    static {
        if (Classes.getClassInfoNoError("vehicle") == null) {
            Classes.registerClass(new ClassInfo<>(Vehicle.class, "vehicle")
                    .user("vehicles?")
                    .name("MTVVehicle - Vehicle")
                    .description("Represents an MTV Vehicle.")
                    .since("2.6")
            );
        }
    }
}
