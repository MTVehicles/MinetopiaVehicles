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
import nl.mtvehicles.core.infrastructure.vehicle.VehicleData;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Condition - Is vehicle friction blocked")
@Description("Check if the vehicle friction of an MTV Vehicle is blocked")
@Examples({
        "if mtv vehicle friction of vehicle {_car} is blocked:",
        "if vehicle friction of license plate \"MT-12-34\" is not blocked:"
})
@Since("2.5.6")
public class CondIsFrictionBlocked extends Condition {

    static {
        Skript.registerCondition(CondIsFrictionBlocked.class,
                "[mtv] vehicle friction of [mtv] vehicle %vehicle% is blocked",
                "[mtv] vehicle friction of license [plate] %string% is blocked",
                "[mtv] vehicle friction of [mtv] vehicle %vehicle% (isn't|is not) blocked",
                "[mtv] vehicle friction of license [plate] %string% (isn't|is not) blocked"
        );
    }

    @SuppressWarnings("null")
    private Expression<Vehicle> vehicle;
    @SuppressWarnings("null")
    private Expression<String> licensePlate;
    private boolean usingLicensePlate;

    @SuppressWarnings({"NullableProblems", "unchecked"})
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        setNegated(matchedPattern > 1);
        this.usingLicensePlate = (matchedPattern == 1 || matchedPattern == 3);
        if (!usingLicensePlate) this.vehicle = (Expression<Vehicle>) exprs[0];
        else this.licensePlate = (Expression<String>) exprs[0];
        return true;
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public boolean check(Event event) {
        boolean check;
        if (usingLicensePlate) check = VehicleData.frictionBlocked.contains(licensePlate.getSingle(event));
        else check = VehicleData.frictionBlocked.contains(vehicle.getSingle(event).getLicensePlate());
        if (!isNegated()) return check;
        else return !check;
    }

    @Override
    public String toString(@Nullable Event e, boolean d) {
        final String neg = isNegated() ? " not" : "";
        return "Check if vehicle friction is" + neg + " blocked.";
    }

}
