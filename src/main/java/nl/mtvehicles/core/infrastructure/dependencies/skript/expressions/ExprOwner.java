package nl.mtvehicles.core.infrastructure.dependencies.skript.expressions;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import nl.mtvehicles.core.infrastructure.vehicle.Vehicle;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static nl.mtvehicles.core.Main.isNotNull;

@Name("MTV Vehicle's owner")
@Description("Get/Set the vehicle's owner (as OfflinePlayer)")
@Examples({
        "set {_owner} to {_car}'s vehicle owner",
        "set {_owner} to vehicle owner of (mtv vehicle with license plate \"DF-4J-2R\")"
})
@Since("2.5.5")
public class ExprOwner extends SimplePropertyExpression<Vehicle, OfflinePlayer> {

    static {
        register(ExprOwner.class, OfflinePlayer.class, "[mtv] vehicle owner", "vehicles");
    }

    @Override
    protected String getPropertyName() {
        return "[mtv] vehicle owner";
    }

    @Override
    public Class<? extends OfflinePlayer> getReturnType() {
        return OfflinePlayer.class;
    }

    @Override
    public @Nullable OfflinePlayer convert(Vehicle vehicle) {
        if (vehicle == null) return null;
        return Bukkit.getOfflinePlayer(vehicle.getOwnerUUID());
    }

    @Override
    public @Nullable Class<?> @NotNull [] acceptChange(Changer.ChangeMode mode) {
        if (mode == Changer.ChangeMode.SET) return new Class[]{OfflinePlayer.class};
        else return null;
    }

    @Override
    public void change(@NotNull Event event, @Nullable Object @NotNull [] delta, Changer.@NotNull ChangeMode changeMode) {
        if (changeMode != Changer.ChangeMode.SET) return;
        Vehicle vehicle = getExpr().getSingle(event);

        if (!isNotNull(delta, delta[0], vehicle)) return;
        if (!(delta[0] instanceof OfflinePlayer)) return;
        final OfflinePlayer newOwner = (OfflinePlayer) delta[0];

        vehicle.setOwner(newOwner.getUniqueId());
        vehicle.save();
    }

}
