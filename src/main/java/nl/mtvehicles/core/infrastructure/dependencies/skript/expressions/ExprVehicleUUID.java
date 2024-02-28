package nl.mtvehicles.core.infrastructure.dependencies.skript.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import nl.mtvehicles.core.Main;
import nl.mtvehicles.core.infrastructure.vehicle.Vehicle;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("MTV Vehicle's UUID")
@Description("Get the vehicle's UUID")
@Examples({
        "set {_uuid} to {_car}'s vehicle uuid",
        "set {_uuid} to vehicle UUID of (player's driven mtv vehicle)"
})
public class ExprVehicleUUID extends SimpleExpression<String> {

    static {
        Skript.registerExpression(ExprVehicleUUID.class,
                String.class,
                ExpressionType.SIMPLE,
                "%object%'s [mtv] vehicle (UUID|uuid)",
                "[mtv] vehicle (UUID|uuid) of %object%"
        );
    }

    @SuppressWarnings("null")
    private Expression<Object> vehicle;

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @SuppressWarnings({"unchecked", "null"})
    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parser) {
        this.vehicle = (Expression<Object>) expressions[0];
        return true;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "MTVehicles vehicle";
    }

    @Override
    protected String[] get(Event event) {
        if (vehicle.getSingle(event) instanceof Vehicle) {
            return new String[] {
                    ((Vehicle) vehicle.getSingle(event)).getUUID()
            };
        }

        Main.logSevere("Skript error: Provided variable is not a vehicle (\"vehicle type of %vehicle%\").");
        return null;

    }

}
