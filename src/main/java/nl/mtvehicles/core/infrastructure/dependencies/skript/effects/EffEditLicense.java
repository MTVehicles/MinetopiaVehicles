package nl.mtvehicles.core.infrastructure.dependencies.skript.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import nl.mtvehicles.core.Main;
import nl.mtvehicles.core.infrastructure.vehicle.Vehicle;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Deprecated
public class EffEditLicense extends Effect {

    static {
        Skript.registerEffect(EffEditLicense.class,
                "edit [the] license [plate] of [a[n]] [mtv] vehicle %object% to %string%",
                "edit [a[n]] [mtv] [vehicle] %object%'s [mtv] license [plate] to %string%"
        );
    }

    @SuppressWarnings("null")
    private Expression<Object> vehicle;

    @SuppressWarnings("null")
    private Expression<String> newPlate;

    @SuppressWarnings({"unchecked", "null"})
    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parser) {
        this.newPlate = (Expression<String>) expressions[1];
        this.vehicle = (Expression<Object>) expressions[0];
        return true;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return String.format("Edit vehicle's license plate to %s.",
                this.newPlate.toString(event, debug)
        );
    }

    @Override
    protected void execute(Event event) {

        if (!(this.vehicle.getSingle(event) instanceof Vehicle) || vehicle.getSingle(event) == null) {
            Main.logSevere("Skript error: Provided variable is not a vehicle (\"edit license plate of [mtv] vehicle %vehicle% to %string%\").");
            return;
        }

        Vehicle vehicle = (Vehicle) this.vehicle.getSingle(event);

        vehicle.setLicensePlate(newPlate.getSingle(event));
        vehicle.save();

    }
}
