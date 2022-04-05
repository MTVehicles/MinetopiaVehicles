package nl.mtvehicles.core.commands.vehiclesubs;

import nl.mtvehicles.core.infrastructure.dataconfig.DefaultConfig;
import nl.mtvehicles.core.infrastructure.helpers.ItemFactory;
import nl.mtvehicles.core.infrastructure.helpers.TextUtils;
import nl.mtvehicles.core.infrastructure.models.MTVehicleSubCommand;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class VehicleFuel extends MTVehicleSubCommand {
    public VehicleFuel() {
        this.setPlayerCommand(true);
    }

    @Override
    public boolean execute(CommandSender sender, Command cmd, String s, String[] args) {
        if (!checkPermission("mtvehicles.benzine")) return true;

        Inventory inv = Bukkit.createInventory(null, 9, "Benzine menu");

        List<Integer> jerrycans = (List<Integer>) ConfigModule.defaultConfig.get(DefaultConfig.Option.JERRYCANS);
        assert jerrycans != null;

        for (int jerrycan : jerrycans) {
            inv.addItem(benzineItem(jerrycan, jerrycan));
        }

        player.openInventory(inv);

        return true;
    }

    public static ItemStack benzineItem(int maxFuel, int currentFuel) {
        ItemStack is = new ItemFactory(Material.getMaterial("DIAMOND_HOE")).setAmount(1).setDurability((short) 58).setNBT("mtvehicles.benzineval", "" + currentFuel).setNBT("mtvehicles.benzinesize", "" + maxFuel).toItemStack();
        ItemMeta im = is.getItemMeta();
        List<String> itemlore = new ArrayList<>();
        itemlore.add(TextUtils.colorize("&8"));
        itemlore.add(TextUtils.colorize("&7Jerrycan &e" + currentFuel + "&7/&e" + maxFuel + " &7liter"));
        assert im != null;
        im.setLore(itemlore);
        im.setUnbreakable(true);
        im.setDisplayName(TextUtils.colorize("&6Jerrycan " + maxFuel + "L"));
        is.setItemMeta(im);
        return is;
    }
}
