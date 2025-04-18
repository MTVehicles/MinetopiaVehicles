package nl.mtvehicles.core.infrastructure.dependencies.skript.conditions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import nl.mtvehicles.core.infrastructure.vehicle.Vehicle;
import nl.mtvehicles.core.infrastructure.vehicle.VehicleUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Condition - Is inside a vehicle")
@Description("Check if a player is seated in an MTV Vehicle")
@Examples({
        "if player {_p} is seated in an mtv vehicle:",
        "if player is not inside mtv vehicle:"
})
public class CondIsInsideVehicle extends Condition {

    static {
        Skript.registerCondition(CondIsInsideVehicle.class,
                "[player] %player% is [(seated in|inside)] [(a|an)] mtv vehicle",
                "[player] %player% (isn't|is not) [(seated in|inside)] [(a|an)] mtv vehicle"
        );
    }

    @SuppressWarnings("null")
    private Expression<Player> player;

    @SuppressWarnings({"NullableProblems", "unchecked"})
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        setNegated(matchedPattern == 1);
        this.player = (Expression<Player>) exprs[0];
        return true;
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public boolean check(Event event) {
        return this.player.check(event, CondIsInsideVehicle::isInsideVehicle, isNegated());
    }

    private static boolean isInsideVehicle(Player p){
        return VehicleUtils.isInsideVehicle(p);
    }

    @Override
    public String toString(@Nullable Event e, boolean d) {
        final String neg = isNegated() ? " not" : "";
        return "Check if player is" + neg + " seated in MTV vehicle.";
    }

}
