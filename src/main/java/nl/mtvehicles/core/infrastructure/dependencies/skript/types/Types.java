package nl.mtvehicles.core.infrastructure.dependencies.skript.types;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.lang.ParseContext;
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
        if (Classes.getClassInfoNoError("mtvehicle") == null) {
            Classes.registerClass(new ClassInfo<>(Vehicle.class, "mtvehicle")
                    .user("mtvehicles?")
                    .name("MTVehicle")
                    .description("Represents an MTV Vehicle.")
                    .since("2.5.5")
                    .parser(new Parser<Vehicle>() {
                        @Override
                        public boolean canParse(ParseContext context) {
                            return false; // we don't want direct parsing from strings
                        }

                        @Override
                        public String toString(Vehicle v, int flags) {
                            return v.toString();
                        }

                        @Override
                        public String toVariableNameString(Vehicle v) {
                            return v.toString();
                        }
                    })
            );
        }
    }
}
