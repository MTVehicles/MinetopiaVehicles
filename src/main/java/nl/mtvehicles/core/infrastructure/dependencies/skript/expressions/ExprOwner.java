package nl.mtvehicles.core.infrastructure.dependencies.skript.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import nl.mtvehicles.core.Main;
import nl.mtvehicles.core.infrastructure.vehicle.Vehicle;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("MTV Vehicle's owner")
@Description("Get the vehicle's owner's name")
@Examples({
        "set {_owner} to {_car}'s vehicle owner",
        "set {_owner} to vehicle owner of (mtv vehicle with license plate \"DF-4J-2R\")"
})
public class ExprOwner extends SimpleExpression<String> {

    static {
        Skript.registerExpression(ExprOwner.class,
                String.class,
                ExpressionType.SIMPLE,
                "%object%'s [mtv] vehicle owner",
                "[mtv] vehicle owner of %object%"
        );
    }

    @SuppressWarnings("null")
    private Expression<Object> vehicle;

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @SuppressWarnings({"unchecked", "null"})
    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parser) {
        this.vehicle = (Expression<Object>) expressions[0];
        return true;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "MTVehicles vehicle owner's name";
    }

    @Override
    protected String[] get(Event event) {
        if (vehicle.getSingle(event) instanceof Vehicle) {
            return new String[] {
                    ((Vehicle) vehicle.getSingle(event)).getOwnerName()
            };
        }

        Main.logSevere("Skript error: Provided variable is not a vehicle (\"vehicle owner of %vehicle%\").");
        return null;

    }

}
