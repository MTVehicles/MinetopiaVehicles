package nl.mtvehicles.core.infrastructure.dependencies.skript.expressions;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import nl.mtvehicles.core.infrastructure.vehicle.Vehicle;
import nl.mtvehicles.core.infrastructure.vehicle.VehicleData;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("MTV Vehicle's vehicle fuel usage")
@Description("Get the vehicle's vehicle fuel usage")
@Examples({
        "set {_fuel} to {_car}'s vehicle fuel usage",
        "set {_fuel} to vehicle fuel of (player's driven mtv vehicle)",
        "set vehicle fuel usage of {_car} to 0.5"
})
public class ExprFuelUsage extends SimplePropertyExpression<Vehicle, Double> {

    static {
        register(ExprFuelUsage.class, Double.class, "[mtv] vehicle fuel usage", "vehicles");
    }

    @Override
    protected String getPropertyName() {
        return "[mtv] vehicle fuel usage";
    }

    @Override
    public Class<? extends Double> getReturnType() {
        return Double.class;
    }

    @Override
    public @Nullable Double convert(Vehicle vehicle) {
        return vehicle.getFuelUsage();
    }

    @Override
    public @Nullable Class<?> @NotNull [] acceptChange(Changer.ChangeMode mode) {
        if (mode == Changer.ChangeMode.SET) return new Class[]{Double.class, Number.class};
        else return null;
    }

    @Override
    public void change(@NotNull Event event, @Nullable Object @NotNull [] delta, Changer.@NotNull ChangeMode changeMode) {
        if (changeMode != Changer.ChangeMode.SET) return;
        Vehicle vehicle = getExpr().getSingle(event);

        if (delta == null || delta[0] == null) return;
        double changeValue = ((Number) delta[0]).doubleValue();

        vehicle.setFuelUsage(Math.min(0, changeValue)); //cannot be lower than zero (it would be adding fuel :D)
        vehicle.save();

        if (VehicleData.fuelUsage.containsKey(vehicle.getLicensePlate()))
            VehicleData.fuelUsage.put(vehicle.getLicensePlate(), Math.min(0, changeValue));
    }

}
