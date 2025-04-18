package nl.mtvehicles.core.infrastructure.dependencies.skript.conditions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import nl.mtvehicles.core.Main;
import nl.mtvehicles.core.infrastructure.vehicle.Vehicle;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Condition - Is vehicle occupied")
@Description("Check if an MTV Vehicle is occupied")
@Examples({
        "if the vehicle {_car} is occupied:",
        "if the vehicle {_car} is not occupied:"
})
public class CondIsOccupied extends Condition {

    static {
        Skript.registerCondition(CondIsOccupied.class,
                "[the] [mtv] vehicle %vehicle% is occupied",
                "[the] [mtv] vehicle %vehicle% (isn't|is not) occupied"
        );
    }

    @SuppressWarnings("null")
    private Expression<Vehicle> vehicle;

    @SuppressWarnings({"NullableProblems", "unchecked"})
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        setNegated(matchedPattern == 1);
        this.vehicle = (Expression<Vehicle>) exprs[0];
        return true;
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public boolean check(Event event) {
        boolean check = vehicle.getSingle(event).isOccupied();
        if (!isNegated()) return check;
        else return !check;
    }

    @Override
    public String toString(@Nullable Event e, boolean d) {
        final String neg = isNegated() ? " not" : "";
        return "Check if player is " + neg + " the owner of an MTV vehicle.";
    }

}
