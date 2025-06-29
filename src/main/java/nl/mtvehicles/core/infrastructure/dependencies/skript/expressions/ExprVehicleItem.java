package nl.mtvehicles.core.infrastructure.dependencies.skript.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import nl.mtvehicles.core.Main;
import nl.mtvehicles.core.infrastructure.utils.ItemUtils;
import nl.mtvehicles.core.infrastructure.vehicle.VehicleUtils;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

@Name("MTV Vehicle Item")
@Description("Get the vehicle item (functional by license plate, or only aesthetic by UUID).")
@Examples({
        "set {_car} to mtv vehicle with license plate \"DF-4J-2R\"",
        "set {_helicopter} to player's driven mtv vehicle",
        "set {_car} to a new mtv vehicle with UUID \"C4UQZJ\" and owner player"
})
@Since("2.5.6")
public class ExprVehicleItem extends SimpleExpression<ItemStack> {

    static {
        Skript.registerExpression(ExprVehicleItem.class,
                ItemStack.class,
                ExpressionType.SIMPLE,
                "[a[n]] mtv vehicle item (by|with) license [plate] %string%",
                "[a[n]] aesthetic mtv vehicle item (by|with) (uuid|UUID) %string%"
        );
    }

    @SuppressWarnings("null")
    private Expression<String> identifier; //LP or UUID
    private int pattern;

    @Override
    public Class<? extends ItemStack> getReturnType() {
        return ItemStack.class;
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @SuppressWarnings({"unchecked", "null"})
    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parser) {
        this.identifier = (Expression<String>) expressions[0];
        this.pattern = matchedPattern;
        return true;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "MTVehicles (aesthetic) vehicle item";
    }

    @Override
    protected ItemStack[] get(Event event) {
        if (this.pattern == 0) {
            return new ItemStack[] {
                    ItemUtils.getVehicleItem(identifier.getSingle(event))
            };
        }

        else {
            if (!VehicleUtils.vehicleUUIDExists(identifier.getSingle(event))) {
                Main.logSevere("Skript error: Provided UUID does not exist (\"an aesthetic mtv vehicle item with uuid|UUID %string%\").");
                return null;
            }
            return new ItemStack[] {
                    VehicleUtils.getItem(identifier.getSingle(event))
            };
        }


    }

}
