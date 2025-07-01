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
import nl.mtvehicles.core.Main;
import nl.mtvehicles.core.infrastructure.vehicle.Vehicle;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import static nl.mtvehicles.core.Main.isNotNull;

@Name("Condition - Is player the owner of a vehicle")
@Description("Check if a player is the owner of an MTV Vehicle")
@Examples({
        "if player is the vehicle owner of {_car}:"
})
@Since("2.5.5")
public class CondIsOwner extends Condition {

    static {
        Skript.registerCondition(CondIsOwner.class,
                "[player] %player% is [the] [mtv] vehicle owner of %vehicle%",
                "[player] %player% (isn't|is not) [the] [mtv] vehicle owner of %vehicle%"
        );
    }

    @SuppressWarnings("null")
    private Expression<Vehicle> vehicle;

    private Expression<Player> player;

    @SuppressWarnings({"NullableProblems", "unchecked"})
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        setNegated(matchedPattern == 1);
        this.vehicle = (Expression<Vehicle>) exprs[1];
        this.player = (Expression<Player>) exprs[0];
        return true;
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public boolean check(Event event) {
        if (!isNotNull(vehicle.getSingle(event), player.getSingle(event))) return isNegated();
        boolean check = vehicle.getSingle(event).isOwner(player.getSingle(event));
        if (!isNegated()) return check;
        else return !check;
    }

    @Override
    public String toString(@Nullable Event e, boolean d) {
        final String neg = isNegated() ? " not" : "";
        return "Check if player is " + neg + " the owner of an MTV vehicle.";
    }

}
