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

@Name("Delete an MTV vehicle")
@Description("Delete an MTV from the database (VehicleData.yml)")
@Examples({
        "delete mtv vehicle {_car}"
})
public class EffDeleteVehicle extends Effect {

    static {
        Skript.registerEffect(EffDeleteVehicle.class,
                "(delete|clear) [mtv] vehicle %vehicle%"
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
        VehicleUtils.deleteVehicle(vehicle.getSingle(event).getLicensePlate());
    }
}
