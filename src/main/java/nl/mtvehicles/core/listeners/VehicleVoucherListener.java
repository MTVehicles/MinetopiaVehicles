package nl.mtvehicles.core.listeners;

import de.tr7zw.changeme.nbtapi.NBTItem;
import nl.mtvehicles.core.events.VehicleVoucherEvent;
import nl.mtvehicles.core.infrastructure.dataconfig.MessagesConfig;
import nl.mtvehicles.core.infrastructure.enums.Message;
import nl.mtvehicles.core.infrastructure.helpers.ItemUtils;
import nl.mtvehicles.core.infrastructure.models.MTVListener;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class VehicleVoucherListener extends MTVListener {

    public VehicleVoucherListener(){
        super(new VehicleVoucherEvent());
    }

    @EventHandler
    public void onVoucherRedeem(PlayerInteractEvent e) {
        final Player p = e.getPlayer();
        final Action action = e.getAction();
        final ItemStack item = e.getItem();

        if (e.isCancelled()) return;

        if (item == null || item.getType() != Material.PAPER) return;
        NBTItem nbt = new NBTItem(item);

        if (!nbt.hasKey("mtvehicles.item")) return;

        if (e.getHand() != EquipmentSlot.HAND) {
            e.setCancelled(true);
            p.sendMessage(ConfigModule.messagesConfig.getMessage(Message.WRONG_HAND));
            return;
        }

        if (action.equals(Action.RIGHT_CLICK_BLOCK) || action.equals(Action.RIGHT_CLICK_AIR)) {
            Inventory inv = Bukkit.createInventory(null, 27, "Voucher Redeem Menu");
            MessagesConfig msg = ConfigModule.messagesConfig;
            inv.setItem(11, ItemUtils.woolItem("WOOL", "RED_WOOL", 1, (short) 14, "&c" + msg.getMessage(Message.CANCEL), String.format("&7%s@&7%s", msg.getMessage(Message.CANCEL_ACTION), msg.getMessage(Message.CANCEL_VOUCHER))));
            inv.setItem(15, ItemUtils.woolItem("WOOL", "LIME_WOOL", 1, (short) 5, "&a"  + msg.getMessage(Message.CONFIRM), String.format("&7%s@&7%s", msg.getMessage(Message.CONFIRM_ACTION), msg.getMessage(Message.CONFIRM_VOUCHER))));
            p.openInventory(inv);
        }
    }
}