package nl.mtvehicles.core.events;

import nl.mtvehicles.core.infrastructure.dataconfig.MessagesConfig;
import nl.mtvehicles.core.infrastructure.helpers.ItemUtils;
import nl.mtvehicles.core.infrastructure.helpers.NBTUtils;
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

public class VehicleVoucherEvent implements Listener {
    @EventHandler
    public void onVoucherRedeem(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        Action action = e.getAction();
        ItemStack item = e.getItem();

        if (item == null || item.getType() != Material.PAPER) return;

        if (!NBTUtils.contains(item, "mtvehicles.item")) return;

        if (e.getHand() != EquipmentSlot.HAND) {
            e.setCancelled(true);
            p.sendMessage(ConfigModule.messagesConfig.getMessage("wrongHand"));
            return;
        }

        if (action.equals(Action.RIGHT_CLICK_BLOCK) || action.equals(Action.RIGHT_CLICK_AIR)) {
            Inventory inv = Bukkit.createInventory(null, 27, "Voucher Redeem Menu");
            MessagesConfig msg = ConfigModule.messagesConfig;
            inv.setItem(11, ItemUtils.woolItem("WOOL", "RED_WOOL", 1, (short) 14, "&c" + msg.getMessage("cancel"), String.format("&7%s@&7%s", msg.getMessage("cancelAction"), msg.getMessage("cancelVoucher"))));
            inv.setItem(15, ItemUtils.woolItem("WOOL", "LIME_WOOL", 1, (short) 5, "&a"  + msg.getMessage("confirm"), String.format("&7%s@&7%s", msg.getMessage("confirmAction"), msg.getMessage("confirmVoucher"))));
            p.openInventory(inv);
        }
    }
}