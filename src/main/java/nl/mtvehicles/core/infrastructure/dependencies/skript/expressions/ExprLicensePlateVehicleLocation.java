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
import nl.mtvehicles.core.infrastructure.vehicle.VehicleUtils;
import org.bukkit.Location;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("MTV Vehicle's location")
@Description("Get the vehicle location")
@Examples({
        "set {_loc} to vehicle location of license plate \"DF-4J-2R\""
})
public class ExprLicensePlateVehicleLocation extends SimpleExpression<Location> {

    static {
        Skript.registerExpression(ExprLicensePlateVehicleLocation.class,
                Location.class,
                ExpressionType.PROPERTY,
                "[the] [mtv] vehicle location of license [plate] %string%"
        );
    }

    @SuppressWarnings("null")
    private Expression<String> license;

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
        this.license = (Expression<String>) expressions[0];
        return true;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "MTVehicle's location";
    }

    @Override
    protected Location[] get(Event event) {
        if (license.getSingle(event) == null) return null;
        return new Location[] {
                VehicleUtils.getLocation(license.getSingle(event))
        };

    }

}
