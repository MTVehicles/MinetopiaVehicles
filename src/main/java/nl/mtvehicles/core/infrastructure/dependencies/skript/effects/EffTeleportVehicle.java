package nl.mtvehicles.core.infrastructure.dependencies.skript.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
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

@Name("Teleport vehicle")
@Description("Teleport a vehicle to a location")
@Examples({
        "teleport mtv vehicle {_car} to location {_loc}"
})
public class EffTeleportVehicle extends Effect {

    static {
        Skript.registerEffect(EffTeleportVehicle.class,
                "(teleport|tp) [a[n]] [mtv] vehicle %vehicle% to [location] %location%"
        );
    }

    @SuppressWarnings("null")
    private Expression<Vehicle> vehicle;

    @SuppressWarnings("null")
    private Expression<Location> location;

    @SuppressWarnings({"unchecked", "null"})
    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parser) {
        this.location = (Expression<Location>) expressions[1];
        this.vehicle = (Expression<Vehicle>) expressions[0];
        return true;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return String.format("Teleport vehicle to location %s.",
                this.location.toString(event, debug)
        );
    }

    @Override
    protected void execute(Event event) {
        if (vehicle.getSingle(event) == null) return;
        VehicleUtils.teleportVehicle(vehicle.getSingle(event).getLicensePlate(), location.getSingle(event));
    }
}
