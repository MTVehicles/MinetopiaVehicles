package nl.mtvehicles.core.listeners;

import de.tr7zw.changeme.nbtapi.NBTItem;
import nl.mtvehicles.core.commands.vehiclesubs.VehicleFuel;
import nl.mtvehicles.core.infrastructure.enums.Message;
import nl.mtvehicles.core.infrastructure.helpers.TextUtils;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import nl.mtvehicles.core.infrastructure.modules.DependencyModule;
import nl.mtvehicles.core.infrastructure.modules.VersionModule;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class JerryCanClickListener implements Listener {
    @EventHandler
    public void onJerryCanClick(final PlayerInteractEvent e) {
        final Player p = e.getPlayer();
        final Action action = e.getAction();
        final ItemStack item = e.getItem();

        if (e.isCancelled()) return;
        if (!VersionModule.getServerVersion().isOld()){
            if (((org.bukkit.event.Cancellable) e).isCancelled()) return;
        }

        if (e.getItem() == null) return;

        if (!e.getItem().hasItemMeta()
                || !(new NBTItem(e.getItem())).hasKey("mtvehicles.benzinesize")
                || e.getClickedBlock() == null
        ) return;

        e.setCancelled(true);

        if (e.getHand() != EquipmentSlot.HAND) {
            e.getPlayer().sendMessage(TextUtils.colorize(ConfigModule.messagesConfig.getMessage(Message.WRONG_HAND)));
            return;
        }

        if (!action.equals(Action.RIGHT_CLICK_BLOCK)) return;

        Block clickedBlock = e.getClickedBlock();

        if (!ConfigModule.defaultConfig.canFillJerryCans(p, clickedBlock.getLocation())) return;
        final boolean isSneaking = p.isSneaking();

        if (clickedBlock.getType().toString().contains("LEVER") && ConfigModule.defaultConfig.isFillJerryCansLeverEnabled()) {
            if (isSneaking) fillWholeJerryCan(p, item);
            else fillJerryCan(p, item);
        } else if (clickedBlock.getType().toString().contains("TRIPWIRE_HOOK") && ConfigModule.defaultConfig.isFillJerryCansTripwireHookEnabled()) {
            if (isSneaking) fillWholeJerryCan(p, item);
            else fillJerryCan(p, item);
        }
    }

    private void fillJerryCan(Player p, ItemStack item){
        int benval = (new NBTItem(item)).getInteger("mtvehicles.benzineval");
        int bensize = (new NBTItem(item)).getInteger("mtvehicles.benzinesize");

        if (benval == bensize) ConfigModule.messagesConfig.sendMessage(p, Message.JERRYCAN_FULL);

        if ((benval + 1) <= bensize){
            double price = getFuelPrice();
            if (makePlayerPay(p, price)){
                p.setItemInHand(VehicleFuel.benzineItem(bensize, benval + 1));
                p.sendMessage(String.format(ConfigModule.messagesConfig.getMessage(Message.TRANSACTION_SUCCESSFUL), DependencyModule.vault.getMoneyFormat(price)));
                playJerryCanSound(p);
            }
        }
    }

    private void fillWholeJerryCan(Player p, ItemStack item){
        int benval = (new NBTItem(item)).getInteger("mtvehicles.benzineval");
        int bensize = (new NBTItem(item)).getInteger("mtvehicles.benzinesize");
        if (benval == bensize) ConfigModule.messagesConfig.sendMessage(p, Message.JERRYCAN_FULL);

        int difference = bensize - benval;
        double price = getFuelPrice(difference);
        if (makePlayerPay(p, price)){
            p.setItemInHand(VehicleFuel.benzineItem(bensize, bensize));
            p.sendMessage(String.format(ConfigModule.messagesConfig.getMessage(Message.TRANSACTION_SUCCESSFUL), DependencyModule.vault.getMoneyFormat(price)));
            playJerryCanSound(p);
        }
    }

    private boolean makePlayerPay(Player p, double price){ //returns true if payed/doesn't have to, false if didn't pay/error
        if (!ConfigModule.defaultConfig.isFillJerryCanPriceEnabled()) return true; //it isn't enabled, so just fill the jerrycan...

        return DependencyModule.vault.withdrawMoneyPlayer(p, price);
    }

    private double getFuelPrice(){
        return ConfigModule.defaultConfig.getFillJerryCanPrice();
    }

    private double getFuelPrice(int litres){
        return litres * ConfigModule.defaultConfig.getFillJerryCanPrice();
    }

    private void playJerryCanSound(Player p){
        if (!ConfigModule.defaultConfig.jerryCanPlaySound()) return;

        if (VersionModule.getServerVersion().is1_12()) { //1.12 has different names
            try {
                p.getWorld().playSound(p.getLocation(), Sound.valueOf("BLOCK_NOTE_PLING"), 3.0F, 0.5F);
            } catch (IllegalArgumentException e) {
                e.printStackTrace(); //The sound could not be played, hmmm.
            }
        } else {
            try {
                p.getWorld().playSound(p.getLocation(), Sound.valueOf("BLOCK_NOTE_BLOCK_PLING"), 3.0F, 0.5F);
            } catch (IllegalArgumentException e) {
                e.printStackTrace(); //The sound could not be played, hmmm.
            }
        }
    }
}
