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

@Name("MTV Vehicle's vehicle health")
@Description("Get the vehicle's vehicle fuel usage")
@Examples({
        "set {_currentHealth} to {_car}'s vehicle health",
        "set {_health} to vehicle health of (player's driven mtv vehicle)",
        "set vehicle health of {_car} to 100",
        "remove 55.5 from {_car}'s vehicle health",
        "add 0.5 to {_car}'s vehicle health"
})
@Since("2.5.6")
public class ExprHealth extends SimplePropertyExpression<Vehicle, Double> {

    static {
        register(ExprHealth.class, Double.class, "[mtv] vehicle health", "vehicles");
    }

    @Override
    protected String getPropertyName() {
        return "[mtv] vehicle health";
    }

    @Override
    public Class<? extends Double> getReturnType() {
        return Double.class;
    }

    @Override
    public @Nullable Double convert(Vehicle vehicle) {
        return vehicle.getHealth();
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
        final double currentHealth = vehicle.getHealth();

        switch (changeMode) {
            case SET:
                setHealth(vehicle, changeValue);
                break;
            case ADD:
                setHealth(vehicle, currentHealth + changeValue);
                break;
            case REMOVE:
                setHealth(vehicle, currentHealth - changeValue);
                break;
            default:
                break;
        }
    }

    private void setHealth(@NotNull Vehicle vehicle, double newHealth){
        final double health = (newHealth < 0) ? 0 : newHealth;
        if (health > 0) VehicleData.markVehicleAsRepaired(vehicle.getLicensePlate());
        else VehicleData.markVehicleAsDestroyed(vehicle.getLicensePlate());
        vehicle.setHealth(health);
        vehicle.save();
    }

}
