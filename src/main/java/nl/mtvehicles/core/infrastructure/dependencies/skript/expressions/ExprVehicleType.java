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

@Name("MTV Vehicle's type")
@Description("Get the vehicle's type as String")
@Examples({
        "set {_type} to {_car}'s vehicle type",
        "set {_type} to vehicle type of (player's driven mtv vehicle)"
})
public class ExprVehicleType extends SimpleExpression<String> {

    static {
        Skript.registerExpression(ExprVehicleType.class,
                String.class,
                ExpressionType.PROPERTY,
                "%vehicle%'s [mtv] vehicle type",
                "[mtv] vehicle type of %vehicle%"
        );
    }

    @SuppressWarnings("null")
    private Expression<Vehicle> vehicle;

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
        this.vehicle = (Expression<Vehicle>) expressions[0];
        return true;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "MTVehicles vehicle type";
    }

    @Override
    protected String[] get(Event event) {
        return new String[] {
                vehicle.getSingle(event).getVehicleType().toString()
        };

    }

}
