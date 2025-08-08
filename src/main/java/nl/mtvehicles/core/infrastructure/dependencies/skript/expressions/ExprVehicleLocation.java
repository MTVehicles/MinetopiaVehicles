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
import nl.mtvehicles.core.infrastructure.vehicle.VehicleUtils;
import org.bukkit.Location;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("MTV Vehicle's location")
@Description("Get the vehicle location")
@Examples({
        "set {_loc} to {_car}'s vehicle location",
        "set {_loc} to vehicle location of {_car}",
        "set {_loc} to the vehicle location of (mtv vehicle with license plate \"DF-4J-2R\")"
})
public class ExprVehicleLocation extends SimpleExpression<Location> {

    static {
        Skript.registerExpression(ExprVehicleLocation.class,
                Location.class,
                ExpressionType.PROPERTY,
                "%mtvehicle%'s [mtv] vehicle location",
                "[the] [mtv] vehicle location of %mtvehicle%"
        );
    }

    @SuppressWarnings("null")
    private Expression<Vehicle> vehicle;

    @Override
    public Class<? extends Location> getReturnType() {
        return Location.class;
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
        return "MTVehicle's location";
    }

    @Override
    protected Location[] get(Event event) {
        if (vehicle.getSingle(event) == null) return null;
        return new Location[] {
                VehicleUtils.getLocation(vehicle.getSingle(event))
        };

    }

}
