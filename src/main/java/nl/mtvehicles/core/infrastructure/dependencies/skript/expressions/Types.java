package nl.mtvehicles.core.infrastructure.dependencies.skript.expressions;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.registrations.Classes;
import nl.mtvehicles.core.infrastructure.vehicle.Vehicle;

/**
 * Custom Skript types for the MTV plugin.
 * Registers the "vehicle" type to represent MTV vehicles (instead of just using Objects).
 * @since 2.5.6
 */
public class Types {
    // Register custom Skript types
    static {
        if (Classes.getClassInfoNoError("vehicle") == null) {
            Classes.registerClass(new ClassInfo<>(Vehicle.class, "vehicle")
                    .user("vehicles?")
                    .name("MTVVehicle - Vehicle")
                    .description("Represents an MTV Vehicle.")
                    .since("2.5.5")
            );
        }
    }
}
