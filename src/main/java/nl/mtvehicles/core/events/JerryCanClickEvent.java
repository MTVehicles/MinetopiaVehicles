package nl.mtvehicles.core.events;

import nl.mtvehicles.core.commands.vehiclesubs.VehicleFuel;
import nl.mtvehicles.core.infrastructure.helpers.NBTUtils;
import nl.mtvehicles.core.infrastructure.helpers.TextUtils;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import nl.mtvehicles.core.infrastructure.modules.DependencyModule;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class JerryCanClickEvent implements Listener {
    @EventHandler
    public void onJerryCanClick(final PlayerInteractEvent e) {
        final Player p = e.getPlayer();
        final Action action = e.getAction();
        final ItemStack item = e.getItem();

        if (e.getItem() == null
                || (!e.getItem().hasItemMeta()
                || !(NBTUtils.contains(item, "mtvehicles.benzinesize")))
                || e.getClickedBlock() == null
        ) return;

        e.setCancelled(true);

        if (e.getHand() != EquipmentSlot.HAND) {
            e.getPlayer().sendMessage(TextUtils.colorize(ConfigModule.messagesConfig.getMessage("wrongHand")));
            return;
        }

        if (!action.equals(Action.RIGHT_CLICK_BLOCK)) return;

        if (!ConfigModule.defaultConfig.isFillJerryCansEnabled()) return;

        Block clickedBlock = e.getClickedBlock();

        if (!DependencyModule.worldGuard.isInAtLeastOneRegion(clickedBlock.getLocation(), ConfigModule.defaultConfig.gasStationList())) return;

        if (clickedBlock.getType().toString().contains("LEVER") && ConfigModule.defaultConfig.isFillJerryCansLeverEnabled() && hasFillJerryCansPermission(p)) {
            fillJerryCan(p, item);
        } else if (clickedBlock.getType().toString().contains("TRIPWIRE_HOOK") && ConfigModule.defaultConfig.isFillJerryCansTripwireHookEnabled() && hasFillJerryCansPermission(p)) {
            fillJerryCan(p, item);
        }
    }

    private boolean hasFillJerryCansPermission(Player p){
        if (!ConfigModule.defaultConfig.hasFillJerryCansPermission(p)) {
            p.sendMessage(TextUtils.colorize(ConfigModule.messagesConfig.getMessage("noPerms")));
            return false;
        } else {
            return true;
        }
    }

    private void fillJerryCan(Player p, ItemStack item){
        Integer benval = Integer.parseInt(NBTUtils.getString(item, "mtvehicles.benzineval"));
        Integer bensize = Integer.parseInt(NBTUtils.getString(item, "mtvehicles.benzinesize"));

        if ((benval + 1) <= bensize){
            double price = getFuelPrice();
            if (makePlayerPay(p, price)){
                p.setItemInHand(VehicleFuel.benzineItem(bensize, benval + 1));
                p.sendMessage(String.format(ConfigModule.messagesConfig.getMessage("transactionSuccessful"), DependencyModule.vault.getMoneyFormat(price)));
                //p.getWorld().playSound(p.getLocation(), "minecraft:entity.player.swim", 3.0F, 0.5F);
                //I'm not sure whether sounds/their names have been changed through the versions... It would be nice having a sound here.
            }
        }
        else if (benval == bensize) { ConfigModule.messagesConfig.sendMessage(p, "jerrycanFull"); }
    }

    private boolean makePlayerPay(Player p, double price){ //returns true if payed/doesn't have to, false if didn't pay/error
        if (!ConfigModule.defaultConfig.isFillJerryCanPriceEnabled()) return true; //it isn't enabled, so just fill the jerrycan...

        return DependencyModule.vault.withdrawMoneyPlayer(p, price);
    }

    private double getFuelPrice(){
        return ConfigModule.defaultConfig.getFillJerryCanPrice();
    }

    private double getFuelPrice(int litres){ //Might be used later
        return litres * ConfigModule.defaultConfig.getFillJerryCanPrice();
    }
}
