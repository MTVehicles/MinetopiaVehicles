package nl.mtvehicles.core.commands.vehiclesubs;

import nl.mtvehicles.core.events.inventory.JerryCanMenuOpen;
import nl.mtvehicles.core.infrastructure.dataconfig.DefaultConfig;
import nl.mtvehicles.core.infrastructure.enums.InventoryTitle;
import nl.mtvehicles.core.infrastructure.enums.Message;
import nl.mtvehicles.core.infrastructure.utils.ItemFactory;
import nl.mtvehicles.core.infrastructure.utils.TextUtils;
import nl.mtvehicles.core.infrastructure.models.MTVSubCommand;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * <b>/vehicle fuel</b> - open a GUI with different jerrycans.
 */
public class VehicleFuel extends MTVSubCommand {
    public VehicleFuel() {
        this.setPlayerCommand(true);
    }

    @Override
    public boolean execute() {
        if (!checkPermission("mtvehicles.benzine")) return true;

        Inventory inv = Bukkit.createInventory(null, 9, InventoryTitle.JERRYCAN_MENU.getStringTitle());

        List<Integer> jerrycans = (List<Integer>) ConfigModule.defaultConfig.get(DefaultConfig.Option.JERRYCANS);
        assert jerrycans != null;

        for (int jerrycan : jerrycans) {
            inv.addItem(jerrycanItem(jerrycan, jerrycan));
        }

        JerryCanMenuOpen api = new JerryCanMenuOpen(player);
        api.call();
        if (api.isCancelled()) return true;

        player.openInventory(inv);

        return true;
    }

    /**
     * Get a jerrycan item
     * @param maxFuel Size of the jerrycan
     * @param currentFuel Current amount of fuel (should not be higher than maxFuel)
     * @return Jerrycan
     */
    public static ItemStack jerrycanItem(int maxFuel, int currentFuel) {
        ItemStack is = new ItemFactory(Material.getMaterial("DIAMOND_HOE")).setAmount(1).setDurability((short) 58).setNBT("mtvehicles.benzineval", "" + currentFuel).setNBT("mtvehicles.benzinesize", "" + maxFuel).toItemStack();
        ItemMeta im = is.getItemMeta();
        List<String> itemlore = new ArrayList<>();
        itemlore.add(TextUtils.colorize("&8"));
        itemlore.add(TextUtils.colorize("&7" + ConfigModule.messagesConfig.getMessage(Message.JERRYCAN) + " &e" + currentFuel + "&7/&e" + maxFuel + "&7l"));
        assert im != null;
        im.setLore(itemlore);
        im.setUnbreakable(true);
        im.setDisplayName(TextUtils.colorize("&6" + ConfigModule.messagesConfig.getMessage(Message.JERRYCAN) + " " + maxFuel + "L"));
        is.setItemMeta(im);
        return is;
    }
}
