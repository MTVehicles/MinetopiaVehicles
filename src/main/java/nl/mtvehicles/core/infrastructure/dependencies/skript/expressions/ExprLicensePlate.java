package nl.mtvehicles.core.infrastructure.dependencies.skript.expressions;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import nl.mtvehicles.core.infrastructure.vehicle.Vehicle;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static nl.mtvehicles.core.Main.isNotNull;

@Name("MTV Vehicle's license plate")
@Description("Get the vehicle's license plate")
@Examples({
        "set {_licensePlate} to {_car}'s vehicle license plate",
        "set {_licensePlate} to vehicle license plate of (player's driven mtv vehicle)",
        "set {_car}'s vehicle license plate to \"RW-2K-7I\""
})
public class ExprLicensePlate extends SimplePropertyExpression<Vehicle, String> {

    static {
        register(ExprLicensePlate.class, String.class, "[mtv] vehicle license [plate]", "vehicles");
    }

    @Override
    protected String getPropertyName() {
        return "[mtv] vehicle license plate";
    }

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    public @Nullable String convert(Vehicle vehicle) {
        if (vehicle == null) return null;
        return vehicle.getLicensePlate();
    }

    @Override
    public @Nullable Class<?> @NotNull [] acceptChange(Changer.ChangeMode mode) {
        if (mode == Changer.ChangeMode.SET) return new Class[]{String.class};
        else return null;
    }

    @Override
    public void change(@NotNull Event event, @Nullable Object @NotNull [] delta, Changer.@NotNull ChangeMode changeMode) {
        if (changeMode != Changer.ChangeMode.SET) return;
        Vehicle vehicle = getExpr().getSingle(event);

        if (!isNotNull(delta, delta[0], vehicle)) return;
        final String newLicense = delta[0].toString();

        vehicle.setLicensePlate(newLicense);
        vehicle.save();
    }

}
