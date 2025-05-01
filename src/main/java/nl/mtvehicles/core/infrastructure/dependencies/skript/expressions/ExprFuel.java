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

@Name("MTV Vehicle's vehicle fuel")
@Description("Get the vehicle's vehicle fuel (from VehicleData or VehicleData.yml – whatever is lower)")
@Examples({
        "set {_fuel} to {_car}'s vehicle fuel level",
        "set {_fuel} to vehicle fuel level of (player's driven mtv vehicle)",
        "set vehicle fuel level of {_car} to 96",
        "remove 10 from {_car}'s vehicle fuel level"
})
@Since("2.5.6")
public class ExprFuel extends SimplePropertyExpression<Vehicle, Double> {

    static {
        register(ExprFuel.class, Double.class, "[mtv] vehicle fuel level", "vehicles");
    }

    @Override
    protected String getPropertyName() {
        return "[mtv] vehicle fuel level";
    }

    @Override
    public Class<? extends Double> getReturnType() {
        return Double.class;
    }

    @Override
    public @Nullable Double convert(Vehicle vehicle) {
        final String license = vehicle.getLicensePlate();
        Double dataFuel = VehicleData.fuel.get(license);
        if (dataFuel == null) dataFuel = 100.0; //return 100 and it will take the lower configFuel value
        double configFuel = vehicle.getFuel();
        return Math.min(dataFuel, configFuel);
    }

    @Override
    public @Nullable Class<?> @NotNull [] acceptChange(Changer.ChangeMode mode) {
        if (mode == Changer.ChangeMode.SET || mode == Changer.ChangeMode.ADD || mode == Changer.ChangeMode.REMOVE) return new Class[]{Double.class, Number.class};
        else return null;
    }

    @Override
    public void change(@NotNull Event event, @Nullable Object @NotNull [] delta, Changer.@NotNull ChangeMode changeMode) {
        Vehicle vehicle = getExpr().getSingle(event);

        if (delta == null || delta[0] == null) return;
        double changeValue = ((Number) delta[0]).doubleValue();
        final double currentFuel = Math.min(VehicleData.fuel.get(vehicle.getLicensePlate()), vehicle.getFuel());

        switch (changeMode) {
            case SET:
                setFuel(vehicle, currentFuel, changeValue);
                break;
            case ADD:
                setFuel(vehicle, currentFuel, currentFuel + changeValue);
                break;
            case REMOVE:
                setFuel(vehicle, currentFuel, currentFuel - changeValue);
                break;
            default:
                break;
        }
    }

    private void setFuel(Vehicle vehicle, double currentFuel, double newFuel){
        String licensePlate = vehicle.getLicensePlate();
        final double finalFuel = Math.max(0, Math.min(100, newFuel)); //assures it is in the range 0 - 100

        vehicle.setFuel(finalFuel);
        vehicle.save();
        VehicleData.fuel.put(licensePlate, finalFuel);

        //only on refill:
        if (VehicleData.fallDamage.get(vehicle.getLicensePlate()) != null && finalFuel > currentFuel) VehicleData.fallDamage.remove(vehicle.getLicensePlate());
    }

}
