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
import nl.mtvehicles.core.infrastructure.vehicle.VehicleUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Condition - Is inside a vehicle and is driving it")
@Description("Check if a player is seated in an MTV Vehicle and is driving it (= not a passenger)")
@Examples({
        "if player {_p} is driving an mtv vehicle:",
        "if player is not driving mtv vehicle:"
})
@Since("2.5.6")
public class CondIsRidingVehicle extends Condition {

    static {
        Skript.registerCondition(CondIsRidingVehicle.class,
                "[player] %player% is (driving|riding) [(a|an)] mtv vehicle",
                "[player] %player% (isn't|is not) (driving|riding) [(a|an)] mtv vehicle"
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
        boolean check = player.getSingle(event) != null && isRidingVehicle(player.getSingle(event));
        if (!isNegated()) return check;
        else return !check;
    }

    private static boolean isRidingVehicle(Player p){
        return VehicleUtils.isInsideVehicle(p) && VehicleUtils.getSeat(p).isDriver();
    }

    @Override
    public String toString(@Nullable Event e, boolean d) {
        final String neg = isNegated() ? " not" : "";
        return "Check if player is" + neg + " driving an MTV vehicle.";
    }

}
