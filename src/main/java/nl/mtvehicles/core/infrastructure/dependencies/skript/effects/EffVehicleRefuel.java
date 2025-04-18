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
public class EffVehicleRefuel extends Effect {

    static {
        Skript.registerEffect(EffVehicleRefuel.class,
                "(refuel|refill) [mtv] vehicle %object%",
                "(refuel|refill) [mtv] vehicle %object% by %number%"
        );
    }

    @SuppressWarnings("null")
    private Expression<Object> vehicle;

    @SuppressWarnings("null")
    private Expression<Double> fuelToAdd;

    private boolean partialFueling = false;

    @SuppressWarnings({"unchecked", "null"})
    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parser) {
        this.vehicle = (Expression<Object>) expressions[0];
        if (matchedPattern == 1) {
            partialFueling = true;
            this.fuelToAdd = (Expression<Double>) expressions[1];
        }
        return true;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        if (!partialFueling) return "Refuel vehicle.";
        else return "Refuel vehicle by " + this.fuelToAdd.toString(event, debug) + " liters.";
    }

    @Override
    protected void execute(Event event) {
        if (!(vehicle.getSingle(event) instanceof Vehicle) || vehicle.getSingle(event) == null) {
            Main.logSevere("Skript error: Provided variable is not a vehicle (\"refuel [mtv] vehicle %vehicle%\").");
            return;
        }

        Vehicle v = (Vehicle) vehicle.getSingle(event);
        String lp = v.getLicensePlate();
        double newFuel = 100.0;
        if (partialFueling) {
            final double currentFuel = Math.min(VehicleData.fuel.get(lp), v.getFuel());
            final double fuelToAdd = this.fuelToAdd.getSingle(event);
            newFuel = Math.min(currentFuel + fuelToAdd, 100.0);
        }
        v.setFuel(newFuel);
        v.save();
        VehicleData.fuel.put(lp, newFuel);

        if (VehicleData.fallDamage.get(v.getLicensePlate()) != null) VehicleData.fallDamage.remove(v.getLicensePlate());

    }
}
