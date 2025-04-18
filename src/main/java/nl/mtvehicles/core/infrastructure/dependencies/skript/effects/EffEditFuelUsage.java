package nl.mtvehicles.core.infrastructure.dependencies.skript.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import nl.mtvehicles.core.Main;
import nl.mtvehicles.core.infrastructure.vehicle.Vehicle;
import nl.mtvehicles.core.infrastructure.vehicle.VehicleData;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Deprecated
public class EffEditFuelUsage extends Effect {

    static {
        Skript.registerEffect(EffEditFuelUsage.class,
                "edit [the] [mtv] fuel usage of [a[n]] [mtv] vehicle %object% to %double%",
                "edit [a[n]] [mtv] [vehicle] %object%'s [mtv] fuel usage to %double%"
        );
    }

    @SuppressWarnings("null")
    private Expression<Object> vehicle;

    @SuppressWarnings("null")
    private Expression<Double> newUsage;

    @SuppressWarnings({"unchecked", "null"})
    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parser) {
        this.newUsage = (Expression<Double>) expressions[1];
        this.vehicle = (Expression<Object>) expressions[0];
        return true;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return String.format("Edit vehicle's fuel usage to %s.",
                this.newUsage.toString(event, debug)
        );
    }

    @Override
    protected void execute(Event event) {

        if (!(this.vehicle.getSingle(event) instanceof Vehicle) || vehicle.getSingle(event) == null) {
            Main.logSevere("Skript error: Provided variable is not a vehicle (\"edit license plate of [mtv] vehicle %vehicle% to %string%\").");
            return;
        }

        Vehicle vehicle = (Vehicle) this.vehicle.getSingle(event);

        vehicle.setFuelUsage(this.newUsage.getSingle(event));
        vehicle.save();

        if (VehicleData.fuelUsage.containsKey(vehicle.getLicensePlate()))
            VehicleData.fuelUsage.put(vehicle.getLicensePlate(), this.newUsage.getSingle(event));

    }
}
