package nl.mtvehicles.core.infrastructure.dependencies.skript.conditions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import nl.mtvehicles.core.infrastructure.vehicle.Vehicle;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Condition - Vehicle exists")
@Description("Check if an MTV Vehicle exists (is not deleted)")
@Examples({
        "if the vehicle {_car} exists:",
        "if the vehicle {_car} is not deleted:"
})
@Since("2.5.6")
public class CondVehicleExists extends Condition {

    static {
        Skript.registerCondition(CondVehicleExists.class,
                "[the] [mtv] vehicle %vehicle% exist[s]",
                "[the] [mtv] vehicle %vehicle% (isn't|is not) deleted"
        );
    }

    @SuppressWarnings("null")
    private Expression<Vehicle> vehicle;

    @SuppressWarnings({"NullableProblems", "unchecked"})
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        this.vehicle = (Expression<Vehicle>) exprs[0];
        return true;
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public boolean check(Event event) {
        return !(vehicle.getSingle(event) == null);
    }

    @Override
    public String toString(@Nullable Event e, boolean d) {
        return "Check if vehicle exists.";
    }

}
