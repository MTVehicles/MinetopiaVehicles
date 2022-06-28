package nl.mtvehicles.core.listeners;

import de.tr7zw.changeme.nbtapi.NBTItem;
import nl.mtvehicles.core.Main;
import nl.mtvehicles.core.commands.vehiclesubs.VehicleFuel;
import nl.mtvehicles.core.events.JerryCanClickEvent;
import nl.mtvehicles.core.infrastructure.annotations.VersionSpecific;
import nl.mtvehicles.core.infrastructure.enums.Message;
import nl.mtvehicles.core.infrastructure.utils.TextUtils;
import nl.mtvehicles.core.infrastructure.models.MTVListener;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import nl.mtvehicles.core.infrastructure.modules.DependencyModule;
import nl.mtvehicles.core.infrastructure.modules.VersionModule;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

/**
 * On right click with a jerry can - its refuelling (the gas stations feature)
 */
public class JerryCanClickListener extends MTVListener {

    private Action action;
    private ItemStack item;

    private int currentFuel;
    private int maxFuel;

    @EventHandler
    public void onJerryCanClick(final PlayerInteractEvent event) {
        this.event = event;
        player = event.getPlayer();
        action = event.getAction();
        item = event.getItem();

        Block clickedBlock = event.getClickedBlock();

        if (!action.equals(Action.RIGHT_CLICK_BLOCK)) return;
        if (item == null || item.getType() == Material.AIR) return;
        try {
            if (!item.hasItemMeta()
                    || !(new NBTItem(item)).hasKey("mtvehicles.benzinesize")
                    || clickedBlock == null
            ) return;
        } catch (NullPointerException e){ //If NBT-API was unable to get NBT,
            return; //the item is not acceptable then
        }
        if (event.getHand() != EquipmentSlot.HAND) {
            event.getPlayer().sendMessage(TextUtils.colorize(ConfigModule.messagesConfig.getMessage(Message.WRONG_HAND)));
            return;
        }

        NBTItem nbt = new NBTItem(item);
        currentFuel = Integer.parseInt(nbt.getString("mtvehicles.benzineval"));
        maxFuel = Integer.parseInt(nbt.getString("mtvehicles.benzinesize"));

        this.setAPI(new JerryCanClickEvent(currentFuel, maxFuel));
        callAPI();
        if (isCancelled()) return;

        event.setCancelled(true);

        if (!ConfigModule.defaultConfig.canFillJerryCans(player, clickedBlock.getLocation())) return;
        final boolean isSneaking = player.isSneaking();

        if (clickedBlock.getType().toString().contains("LEVER") && ConfigModule.defaultConfig.isFillJerryCansLeverEnabled()) {
            if (isSneaking) fillWholeJerryCan();
            else fillJerryCan();
        } else if (clickedBlock.getType().toString().contains("TRIPWIRE_HOOK") && ConfigModule.defaultConfig.isFillJerryCansTripwireHookEnabled()) {
            if (isSneaking) fillWholeJerryCan();
            else fillJerryCan();
        }
    }

    private void fillJerryCan(){
        if (currentFuel == maxFuel) ConfigModule.messagesConfig.sendMessage(player, Message.JERRYCAN_FULL);

        if ((currentFuel + 1) <= maxFuel){
            double price = getFuelPrice();
            if (makePlayerPay(price)){
                player.setItemInHand(VehicleFuel.jerrycanItem(maxFuel, currentFuel + 1));
                playJerryCanSound();
            }
        }
    }

    private void fillWholeJerryCan(){
        if (currentFuel == maxFuel) ConfigModule.messagesConfig.sendMessage(player, Message.JERRYCAN_FULL);

        int difference = maxFuel - currentFuel;
        double price = getFuelPrice(difference);
        if (makePlayerPay(price)){
            player.setItemInHand(VehicleFuel.jerrycanItem(maxFuel, maxFuel));
            playJerryCanSound();
        }
    }

    private boolean makePlayerPay(double price){ //returns true if payed/doesn't have to, false if didn't pay/error
        if (!ConfigModule.defaultConfig.isFillJerryCanPriceEnabled()) return true; //it isn't enabled, so just fill the jerrycan...

        return DependencyModule.vault.withdrawMoneyPlayer(player, price);
    }

    private double getFuelPrice(){
        return ConfigModule.defaultConfig.getFillJerryCanPrice();
    }

    private double getFuelPrice(int litres){
        return litres * ConfigModule.defaultConfig.getFillJerryCanPrice();
    }

    @VersionSpecific
    private void playJerryCanSound(){
        if (!ConfigModule.defaultConfig.jerryCanPlaySound()) return;

        if (VersionModule.getServerVersion().is1_12()) { //1.12 has different names
            try {
                player.getWorld().playSound(player.getLocation(), Sound.valueOf("BLOCK_NOTE_PLING"), 3.0F, 0.5F);
            } catch (IllegalArgumentException e) {
                Main.logSevere("Could not play sound 'BLOCK_NOTE_PLING'.");
                e.printStackTrace(); //The sound could not be played, hmmm.
            }
        } else {
            try {
                player.getWorld().playSound(player.getLocation(), Sound.valueOf("BLOCK_NOTE_BLOCK_PLING"), 3.0F, 0.5F);
            } catch (IllegalArgumentException e) {
                Main.logSevere("Could not play sound 'BLOCK_NOTE_BLOCK_PLING'.");
                e.printStackTrace(); //The sound could not be played, hmmm.
            }
        }
    }
}
