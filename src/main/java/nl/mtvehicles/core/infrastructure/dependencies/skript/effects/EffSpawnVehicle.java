package nl.mtvehicles.core.infrastructure.dependencies.skript.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import nl.mtvehicles.core.Main;
import nl.mtvehicles.core.infrastructure.vehicle.Vehicle;
import nl.mtvehicles.core.infrastructure.vehicle.VehicleUtils;
import org.bukkit.Location;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

public class EffSpawnVehicle extends Effect {

    static {
        Skript.registerEffect(EffSpawnVehicle.class,
                "spawn [mtv] vehicle %object% at [location] %location%",
                "spawn [mtv] vehicle by license [plate] %string% at [location] %location%"
        );
    }

    @SuppressWarnings("null")
    private Expression<Object> vehicle;

    @SuppressWarnings("null")
    private Expression<String> licensePlate;

    @SuppressWarnings("null")
    private Expression<Location> location;

    private int pattern;

    @SuppressWarnings({"unchecked", "null"})
    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parser) {
        if (matchedPattern == 0){
            this.vehicle = (Expression<Object>) expressions[0];
            this.location = (Expression<Location>) expressions[1];
        } else {
            this.licensePlate = (Expression<String>) expressions[0];
            this.location = (Expression<Location>) expressions[1];
        }
        pattern = matchedPattern;
        return true;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return String.format("Spawn vehicle at location %s",
                this.location.toString(event, debug)
        );
    }

    @Override
    protected void execute(Event event) {
        if (pattern == 0){
            if (vehicle.getSingle(event) instanceof Vehicle) {
                VehicleUtils.spawnVehicle(((Vehicle) vehicle.getSingle(event)).getLicensePlate(), location.getSingle(event));
                return;
            }
            Main.logSevere("Skript error: Provided variable is not a vehicle (\"spawn vehicle %vehicle% at location %location%\").");
        }
        else {
            try {
                VehicleUtils.spawnVehicle(licensePlate.getSingle(event), location.getSingle(event));
            } catch (IllegalArgumentException e) {
                Main.logSevere("Skript error: Provided license plate is not valid (\"spawn vehicle by license plate %string% at location %location%\").");
            }
            return;
        }
    }
}
