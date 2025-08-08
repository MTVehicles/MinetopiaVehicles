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
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Despawn an MTV vehicle")
@Description("Despawn a vehicle from all locations where it may be placed")
@Examples({
        "despawn mtv vehicle {_car}"
})
public class EffDespawnVehicle extends Effect {

    static {
        Skript.registerEffect(EffDespawnVehicle.class,
                "(despawn|hide) [mtv] vehicle %mtvehicle%"
        );
    }

    @SuppressWarnings("null")
    private Expression<Vehicle> vehicle;

    @SuppressWarnings({"unchecked", "null"})
    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parser) {
        this.vehicle = (Expression<Vehicle>) expressions[0];
        return true;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "Despawn vehicle.";
    }

    @Override
    protected void execute(Event event) {
        if (vehicle.getSingle(event) == null) return;
        VehicleUtils.despawnVehicle(vehicle.getSingle(event).getLicensePlate());
    }
}
