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

    @EventHandler
    public void onJerryCanClick(final PlayerInteractEvent event) {
        this.event = event;
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        ItemStack item = event.getItem();
        if (item == null || item.getType() == Material.AIR || !item.hasItemMeta()) return;

        NBTItem nbt;
        try {
            nbt = new NBTItem(item);
            if (!nbt.hasKey("mtvehicles.benzinesize")) return;
        } catch (Exception e) {
            return;
        }

        Block clickedBlock = event.getClickedBlock();
        if (clickedBlock == null || event.getHand() != EquipmentSlot.HAND) {
            event.getPlayer().sendMessage(TextUtils.colorize(ConfigModule.messagesConfig.getMessage(Message.WRONG_HAND)));
            return;
        }

        player = event.getPlayer();
        int currentFuel = Integer.parseInt(nbt.getString("mtvehicles.benzineval"));
        int maxFuel = Integer.parseInt(nbt.getString("mtvehicles.benzinesize"));

        this.setAPI(new JerryCanClickEvent(currentFuel, maxFuel));
        callAPI();
        if (isCancelled()) return;

        event.setCancelled(true);

        if (!ConfigModule.defaultConfig.canFillJerryCans(player, clickedBlock.getLocation())) return;

        if (isFuelStation(clickedBlock)) {
            if (player.isSneaking()) {
                fillWholeJerryCan(currentFuel, maxFuel);
            } else {
                fillJerryCan(currentFuel, maxFuel);
            }
        }
    }

    private boolean isFuelStation(Block block) {
        String blockType = block.getType().toString();
        return (blockType.contains("LEVER") && ConfigModule.defaultConfig.isFillJerryCansLeverEnabled())
                || (blockType.contains("TRIPWIRE_HOOK") && ConfigModule.defaultConfig.isFillJerryCansTripwireHookEnabled());
    }

    private void fillJerryCan(int currentFuel, int maxFuel) {
        if (currentFuel >= maxFuel) {
            ConfigModule.messagesConfig.sendMessage(player, Message.JERRYCAN_FULL);
            return;
        }

        if (canAffordFuel(1)) {
            player.setItemInHand(VehicleFuel.jerrycanItem(maxFuel, currentFuel + 1));
            playJerryCanSound();
        }
    }

    private void fillWholeJerryCan(int currentFuel, int maxFuel) {
        if (currentFuel >= maxFuel) {
            ConfigModule.messagesConfig.sendMessage(player, Message.JERRYCAN_FULL);
            return;
        }

        int difference = maxFuel - currentFuel;
        if (canAffordFuel(difference)) {
            player.setItemInHand(VehicleFuel.jerrycanItem(maxFuel, maxFuel));
            playJerryCanSound();
        }
    }

    private boolean canAffordFuel(int litres) {
        if (!ConfigModule.defaultConfig.isFillJerryCanPriceEnabled()) return true;
        double price = litres * ConfigModule.defaultConfig.getFillJerryCanPrice();
        return DependencyModule.vault.withdrawMoneyPlayer(player, price);
    }

    @VersionSpecific
    private void playJerryCanSound() {
        if (!ConfigModule.defaultConfig.jerryCanPlaySound()) return;

        String soundName = VersionModule.getServerVersion().is1_12() ? "BLOCK_NOTE_PLING" : "BLOCK_NOTE_BLOCK_PLING";
        try {
            player.getWorld().playSound(player.getLocation(), Sound.valueOf(soundName), 3.0F, 0.5F);
        } catch (IllegalArgumentException e) {
            Main.logSevere("Could not play sound '" + soundName + "'.");
            e.printStackTrace();
        }
    }
}
