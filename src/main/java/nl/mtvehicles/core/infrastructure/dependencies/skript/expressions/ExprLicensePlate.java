package nl.mtvehicles.core.infrastructure.dependencies.skript.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import nl.mtvehicles.core.Main;
import nl.mtvehicles.core.infrastructure.vehicle.Vehicle;
import nl.mtvehicles.core.infrastructure.vehicle.VehicleData;
import nl.mtvehicles.core.infrastructure.vehicle.VehicleUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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

        if (delta == null || delta[0] == null) return;
        final String newLicense = delta[0].toString();

        vehicle.setLicensePlate(newLicense);
        vehicle.save();
    }

}
