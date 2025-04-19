package nl.mtvehicles.core.infrastructure.dependencies.skript.expressions;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import nl.mtvehicles.core.infrastructure.vehicle.Vehicle;
import nl.mtvehicles.core.infrastructure.vehicle.VehicleData;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("MTV Vehicle's vehicle speed")
@Description("Get the vehicle's vehicle speed")
@Examples({
        "set {_licensePlate} to {_car}'s vehicle speed",
        "add 0.5 to vehicle speed of (player's driven mtv vehicle)",
        "set mtv vehicle speed of {_helicopter} to 3"
})
@Since("2.5.5")
public class ExprSpeed extends SimplePropertyExpression<Vehicle, Double> {

    static {
        register(ExprSpeed.class, Double.class, "[mtv] vehicle speed", "vehicles");
    }

    @Override
    protected String getPropertyName() {
        return "[mtv] vehicle speed";
    }

    @Override
    public @Nullable Double convert(Vehicle vehicle) {
        return VehicleData.speed.get(vehicle.getLicensePlate());
    }

    @Override
    public Class<? extends Double> getReturnType() {
        return Double.class;
    }

    @Override
    public @Nullable Class<?> @NotNull [] acceptChange(Changer.ChangeMode mode) {
        if (mode == Changer.ChangeMode.SET || mode == Changer.ChangeMode.ADD || mode == Changer.ChangeMode.REMOVE) return new Class[]{Double.class, Number.class};
        else return null;
    }

    @Override
    public void change(@NotNull Event event, @Nullable Object @NotNull [] delta, Changer.@NotNull ChangeMode changeMode) {
        Vehicle vehicle = getExpr().getSingle(event);
        final String licensePlate = vehicle.getLicensePlate();

        if (delta == null || delta[0] == null) return;
        double changeValue = ((Number) delta[0]).doubleValue();

        switch (changeMode) {
            case SET:
                VehicleData.speed.put(licensePlate, changeValue);
                break;
            case ADD:
                VehicleData.speed.put(licensePlate, VehicleData.speed.get(licensePlate) + changeValue);
                break;
            case REMOVE:
                VehicleData.speed.put(licensePlate, VehicleData.speed.get(licensePlate) - changeValue);
                break;
            default:
                break;
        }
    }

}
