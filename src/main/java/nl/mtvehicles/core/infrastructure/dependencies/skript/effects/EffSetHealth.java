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
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Deprecated
public class EffSetHealth extends Effect {

    static {
        Skript.registerEffect(EffSetHealth.class,
                "edit [the] vehicle health of [a[n]] [mtv] vehicle %object% to %number%",
                "edit [a[n]] [mtv] [vehicle] %object%'s [mtv] vehicle health to %number%"
        );
    }

    @SuppressWarnings("null")
    private Expression<Object> vehicle;

    @SuppressWarnings("null")
    private Expression<Double> newHealth;

    @SuppressWarnings({"unchecked", "null"})
    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parser) {
        this.newHealth = (Expression<Double>) expressions[1];
        this.vehicle = (Expression<Object>) expressions[0];
        return true;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return String.format("Set vehicle health to %s.",
                this.newHealth.toString(event, debug)
        );
    }

    @Override
    protected void execute(Event event) {

        if (!(this.vehicle.getSingle(event) instanceof Vehicle) || vehicle.getSingle(event) == null) {
            Main.logSevere("Skript error: Provided variable is not a vehicle (\"set vehicle health of [mtv] vehicle %vehicle% to %number%\").");
            return;
        }

        Vehicle vehicle = (Vehicle) this.vehicle.getSingle(event);
        vehicle.setHealth(this.newHealth.getSingle(event));
        vehicle.save();

    }
}
