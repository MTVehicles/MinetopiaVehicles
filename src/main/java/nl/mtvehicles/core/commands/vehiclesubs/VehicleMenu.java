package nl.mtvehicles.core.commands.vehiclesubs;

import nl.mtvehicles.core.events.inventory.VehicleMenuOpenEvent;
import nl.mtvehicles.core.infrastructure.dataconfig.DefaultConfig;
import nl.mtvehicles.core.infrastructure.enums.InventoryTitle;
import nl.mtvehicles.core.infrastructure.enums.Message;
import nl.mtvehicles.core.infrastructure.utils.ItemFactory;
import nl.mtvehicles.core.infrastructure.utils.ItemUtils;
import nl.mtvehicles.core.infrastructure.models.MTVSubCommand;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * <b>/vehicle menu</b> - open a GUI menu of all the vehicles.
 */
public class VehicleMenu extends MTVSubCommand {
    public static HashMap<UUID, Inventory> beginMenu = new HashMap<>();

    public VehicleMenu() {
        this.setPlayerCommand(true);
    }

    @Override
    public boolean execute() {
        if (!checkPermission("mtvehicles.menu")) return true;

        sendMessage(Message.MENU_OPEN);

        int menuRows = (int) ConfigModule.defaultConfig.get(DefaultConfig.Option.VEHICLE_MENU_SIZE);
        final int menuSize = (menuRows >= 3 && menuRows <= 6) ? menuRows * 9 : 27;

        Inventory inv = Bukkit.createInventory(null, menuSize, InventoryTitle.VEHICLE_MENU.getStringTitle());

        for (Map<?, ?> vehicle : ConfigModule.vehiclesConfig.getVehicles()) {
            int itemDamage = (Integer) vehicle.get("itemDamage");
            String name = (String) vehicle.get("name");
            String skinItem = (String) vehicle.get("skinItem");
            ItemStack itemStack = ItemUtils.getMenuVehicle(ItemUtils.getMaterial(skinItem), itemDamage, name);

            if (vehicle.get("nbtValue") == null) {
                inv.addItem(itemStack);
                continue;
            }
            inv.addItem(new ItemFactory(itemStack).setNBT((String) vehicle.get("nbtKey"), (String)vehicle.get("nbtValue")).toItemStack());
        }

        VehicleMenuOpenEvent api = new VehicleMenuOpenEvent(player);
        api.call();
        if (api.isCancelled()) return true;

        beginMenu.put(player.getUniqueId(), inv);
        player.openInventory(inv);

        return true;
    }
}
