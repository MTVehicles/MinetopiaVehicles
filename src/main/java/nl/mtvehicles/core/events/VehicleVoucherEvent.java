package nl.mtvehicles.core.events;

import nl.mtvehicles.core.infrastructure.helpers.ItemUtils;
import nl.mtvehicles.core.infrastructure.helpers.NBTUtils;
import nl.mtvehicles.core.Main;
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

        if (!(NBTUtils.contains(item, "mtvehicles.item"))) return;

        if (e.getHand() != EquipmentSlot.HAND) {
            e.setCancelled(true);
            p.sendMessage(ConfigModule.messagesConfig.getMessage("wrongHand"));
            return;
        }

        if (action.equals(Action.RIGHT_CLICK_BLOCK) || action.equals(Action.RIGHT_CLICK_AIR)) {
            Inventory inv = Bukkit.createInventory(null, 27, "Voucher Redeem Menu");
            inv.setItem(11, ItemUtils.woolItem("WOOL", "RED_WOOL", 1, (short) 14, "&cNee", "&7Als je op deze knop drukt@&7zal het keuzemenu sluiten@&7en zul je je item houden."));
            inv.setItem(15, ItemUtils.woolItem("WOOL", "LIME_WOOL", 1, (short) 5, "&aJa", "&7Als je op deze knop drukt@&7zul je je voertuig ontvangen@&7en zul je niet meer in staat@&7zijn om je voertuig te verkopen."));
            p.openInventory(inv);
        }
    }
}