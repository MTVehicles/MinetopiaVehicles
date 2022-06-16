package nl.mtvehicles.core.listeners;

import de.tr7zw.changeme.nbtapi.NBTItem;
import nl.mtvehicles.core.events.VehicleVoucherEvent;
import nl.mtvehicles.core.infrastructure.dataconfig.MessagesConfig;
import nl.mtvehicles.core.infrastructure.enums.InventoryTitle;
import nl.mtvehicles.core.infrastructure.enums.Message;
import nl.mtvehicles.core.infrastructure.utils.ItemUtils;
import nl.mtvehicles.core.infrastructure.models.MTVListener;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

/**
 * On right click with a voucher
 */
public class VehicleVoucherListener extends MTVListener {
    public static HashMap<Player, String> voucher = new HashMap<>();

    public VehicleVoucherListener(){
        super(new VehicleVoucherEvent());
    }

    @EventHandler
    public void onVoucherRedeem(PlayerInteractEvent event) {
        this.event = event;
        player = event.getPlayer();
        final Action action = event.getAction();
        final ItemStack item = event.getItem();

        if (item == null || item.getType() != Material.PAPER) return;
        NBTItem nbt = new NBTItem(item);
        if (!nbt.hasKey("mtvehicles.item")) return;

        String carUUID = nbt.getString("mtvehicles.item");

        VehicleVoucherEvent api = (VehicleVoucherEvent) getAPI();
        api.setVoucherUUID(carUUID);
        callAPI();
        if (isCancelled()) return;

        carUUID = api.getVoucherUUID();

        if (event.getHand() != EquipmentSlot.HAND) {
            event.setCancelled(true);
            player.sendMessage(ConfigModule.messagesConfig.getMessage(Message.WRONG_HAND));
            return;
        }

        if (action.equals(Action.RIGHT_CLICK_BLOCK) || action.equals(Action.RIGHT_CLICK_AIR)) {
            Inventory inv = Bukkit.createInventory(null, 27, InventoryTitle.VOUCHER_REDEEM_MENU.getStringTitle());
            voucher.put(player, carUUID);
            MessagesConfig msg = ConfigModule.messagesConfig;
            inv.setItem(11, ItemUtils.getMenuItem(
                    "RED_WOOL",
                    "WOOL",
                    (short) 14,
                    1,
                    "&c" + msg.getMessage(Message.CANCEL),
                    "&7" + msg.getMessage(Message.CANCEL_ACTION), "&7" + msg.getMessage(Message.CANCEL_VOUCHER)
            ));
            inv.setItem(15, ItemUtils.getMenuItem(
                    "LIME_WOOL",
                    "WOOL",
                    (short) 5,
                    1,
                    "&a"  + msg.getMessage(Message.CONFIRM),
                    "&7" + msg.getMessage(Message.CONFIRM_ACTION), "&7" + msg.getMessage(Message.CONFIRM_VOUCHER)
            ));
            player.openInventory(inv);
        }
    }
}
