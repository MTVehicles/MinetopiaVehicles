package nl.mtvehicles.core.infrastructure.dependencies.skript.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import nl.mtvehicles.core.Main;
import nl.mtvehicles.core.infrastructure.vehicle.Vehicle;
import nl.mtvehicles.core.infrastructure.vehicle.VehicleData;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Block/Unblock braking for an MTV vehicle")
@Description("Block or unblock braking for an MTV vehicle")
@Examples({
        "block vehicle breaking of mtv vehicle {_car}",
        "block mtv vehicle breaking of license \"MT-12-34\"",
        "unblock mtv vehicle breaking of vehicle {_car}",
        "unblock vehicle breaking of license plate \"MT-12-34\""
})
@Since("2.5.6")
public class EffBlockBraking extends Effect {

    static {
        Skript.registerEffect(EffBlockBraking.class,
                "block [mtv] vehicle breaking of [mtv] [vehicle] %vehicle%",
                "block [mtv] vehicle breaking of license [plate] %string%",
                "unblock [mtv] vehicle breaking of [mtv] [vehicle] %vehicle%",
                "unblock [mtv] vehicle breaking of license [plate] %string%"
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
            if (block) VehicleData.brakingBlocked.add(licensePlate.getSingle(event));
            else VehicleData.brakingBlocked.remove(licensePlate.getSingle(event));
            return;
        }

        if (vehicle.getSingle(event) == null) return;

        String license = vehicle.getSingle(event).getLicensePlate();
        if (block) VehicleData.brakingBlocked.add(license);
        else VehicleData.brakingBlocked.remove(license);

    }
}
