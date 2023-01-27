package nl.mtvehicles.core.listeners;

import com.sk89q.worldguard.protection.flags.StateFlag;
import net.milkbowl.vault.economy.Economy;
import nl.mtvehicles.core.infrastructure.dependencies.WorldGuardUtils;
import nl.mtvehicles.core.infrastructure.enums.Message;
import nl.mtvehicles.core.infrastructure.models.MTVListener;
import nl.mtvehicles.core.infrastructure.models.Vehicle;
import nl.mtvehicles.core.infrastructure.models.VehicleUtils;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import org.bukkit.Bukkit;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.util.HashMap;
import java.util.Objects;

public class SignClickListener extends MTVListener{
    HashMap<String, String> VehicleList = VehicleLeaveListener.VehicleList;

    @EventHandler
    public void onFuelSignClick(PlayerInteractEvent event) throws FileNotFoundException {
        Player player = event.getPlayer();
        //Check if a sign is clicked on
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && Objects.requireNonNull(event.getClickedBlock()).getType().toString().contains("WALL_SIGN")){
            Sign sign = (Sign) event.getClickedBlock().getState();
            if (sign.getLine(0).equalsIgnoreCase("[Refuel]")) {
                WorldGuardUtils WUtils = new WorldGuardUtils();
                if(WUtils.isInRegionWithFlag(event.getClickedBlock().getLocation(), "mtv-gasstation", StateFlag.State.ALLOW)){
                    float price = Float.parseFloat(sign.getLine(2));
                    final String licensePlate = VehicleList.get(player.getName());
                    Vehicle vehicle = VehicleUtils.getVehicle(licensePlate);
                    assert vehicle != null;
                    int fuel = (int) vehicle.getFuel();
                    //Initialize the Economy Plugin
                    RegisteredServiceProvider<Economy> rsp = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
                    assert rsp != null;
                    Economy economy = rsp.getProvider();

                    if (economy.withdrawPlayer(player, (100 - fuel) * price).transactionSuccess()) {
                        //Set -The fuel level to 100%
                        File file = new File("plugins/MTVehicles/", "vehicleData.yml");
                        FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
                        try {
                            cfg.load(file);
                        } catch (IOException | InvalidConfigurationException e) {
                            e.printStackTrace();
                        }
                        double maxfuel = 100;
                        cfg.set("vehicle." + licensePlate + ".benzine", maxfuel);
                        try {
                            cfg.save(file);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    } else {
                        ConfigModule.messagesConfig.sendMessage(player, Message.NOT_ENOUGH_MONEY);
                    }
                }
            }
        }
    }
}

