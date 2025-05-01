package nl.mtvehicles.core.infrastructure.dependencies.skript.conditions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import nl.mtvehicles.core.Main;
import nl.mtvehicles.core.infrastructure.vehicle.Vehicle;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Condition - Is variable a vehicle")
@Description("Check if a variable is an MTV Vehicle")
@Examples({
        "if {_car} is an mtv vehicle:"
})

@Deprecated
public class CondIsVehicle extends Condition {

    static {
        Skript.registerCondition(CondIsVehicle.class,
                "%object% is [(a|an)] [mtv] vehicle",
                "%object% (isn't|is not) [(a|an)] [mtv] vehicle"
        );
    }

    @SuppressWarnings("null")
    private Expression<Object> vehicle;

    @SuppressWarnings({"NullableProblems", "unchecked"})
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        setNegated(matchedPattern == 1);
        this.vehicle = (Expression<Object>) exprs[0];
        return true;
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public boolean check(Event event) {
        return this.vehicle.check(event, CondIsVehicle::isVehicle, isNegated());
    }

    private static boolean isVehicle(Object var){
        return (var instanceof Vehicle);
    }

    @Override
    public String toString(@Nullable Event e, boolean d) {
        final String neg = isNegated() ? " not" : "";
        return "Check if variable is" + neg + " an MTV vehicle.";
    }

}
