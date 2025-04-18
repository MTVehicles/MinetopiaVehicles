package nl.mtvehicles.core.infrastructure.dependencies.skript.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import nl.mtvehicles.core.Main;
import nl.mtvehicles.core.infrastructure.vehicle.Vehicle;
import nl.mtvehicles.core.infrastructure.vehicle.VehicleData;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Block/Unblock friction for an MTV vehicle")
@Description("Block or unblock friction for an MTV vehicle")
@Examples({
        "block vehicle friction of {_car}",
        "block mtv vehicle friction of license \"MT-12-34\"",
        "unblock mtv vehicle friction of mtv vehicle {_car}",
        "unblock vehicle friction of license plate \"MT-12-34\""
})
public class EffBlockFriction extends Effect {

    static {
        Skript.registerEffect(EffBlockFriction.class,
                "block [mtv] vehicle friction of [mtv] [vehicle] %vehicle%",
                "block [mtv] vehicle friction of license [plate] %string%",
                "unblock [mtv] vehicle friction of [mtv] [vehicle] %vehicle%",
                "unblock [mtv] vehicle friction of license [plate] %string%"
        );
    }

    @SuppressWarnings("null")
    private Expression<Vehicle> vehicle;
    @SuppressWarnings("null")
    private Expression<String> licensePlate;

    private boolean block;
    private boolean usingLicensePlate;

    @SuppressWarnings({"unchecked", "null"})
    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parser) {
        this.block = matchedPattern < 2;
        this.usingLicensePlate = (matchedPattern == 1 || matchedPattern == 3);
        if (!usingLicensePlate) this.vehicle = (Expression<Vehicle>) expressions[0];
        else this.licensePlate = (Expression<String>) expressions[0];
        return true;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "(Un)Block braking of a vehicle.";
    }

    @Override
    protected void execute(Event event) {

        if (usingLicensePlate) {
            if (block) VehicleData.frictionBlocked.add(licensePlate.getSingle(event));
            else VehicleData.frictionBlocked.remove(licensePlate.getSingle(event));
            return;
        }

        String license = vehicle.getSingle(event).getLicensePlate();
        if (block) VehicleData.frictionBlocked.add(license);
        else VehicleData.frictionBlocked.remove(license);

    }
}
