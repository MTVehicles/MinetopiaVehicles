package nl.mtvehicles.core.commands;

import lombok.Getter;
import nl.mtvehicles.core.commands.vehiclesubs.VehicleEdit;
import nl.mtvehicles.core.infrastructure.modules.CommandModule;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * Tab completer for /mtv command
 */
public class VehicleTabCompleterManager implements org.bukkit.command.TabCompleter {

    @Getter
    private static HashMap<String, String> vehicleList = new HashMap<>();
    
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (strings.length == 1) {
            return getApplicableTabCompleters(strings[0], CommandModule.subcommands.keySet());
        } else if (strings.length > 1) {
            String subCommand = strings[0].toLowerCase();
            
            if (subCommand.equals("edit")) {
                if (strings.length == 2) {
                    List<String> playerNames = new ArrayList<>();
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        playerNames.add(player.getName());
                    }                    
                    List<String> suggestions = new ArrayList<>(playerNames);
                    suggestions.addAll(VehicleEdit.getEditCommands());
                    return getApplicableTabCompleters(strings[1], suggestions);
                } else if (strings.length == 3) {
                    if (Bukkit.getPlayer(strings[1]) != null) {
                        return getApplicableTabCompleters(strings[2], VehicleEdit.getEditCommands());
                    }
                }
            } else if (subCommand.equals("give")) {
                if (strings.length == 2) {
                    List<String> playerNames = new ArrayList<>();
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        playerNames.add(player.getName());
                    }
                    return getApplicableTabCompleters(strings[1], playerNames);
                } else if (strings.length == 3) {
                    if (Bukkit.getPlayer(strings[1]) != null) {
                        return getApplicableTabCompleters(strings[2], vehicleList.keySet());
                    }
                } else if (strings.length == 4 && commandSender.hasPermission("mtvehicles.givevoucher")) {
                    List<String> completions = Arrays.asList("--voucher:true", "--voucher:false");
                    return getApplicableTabCompleters(strings[3], completions);
                }
            } else if (subCommand.equals("buy")) {
                if (strings.length == 2) {
                    return getApplicableTabCompleters(strings[1], vehicleList.keySet());
                } else if (strings.length == 3 && commandSender.hasPermission("mtvehicles.buyvoucher")) {
                    List<String> completions = Arrays.asList("--voucher:true", "--voucher:false");
                    return getApplicableTabCompleters(strings[2], completions);
                }
            }
        }
        return null;
    }

    private List<String> getApplicableTabCompleters(String arg, Collection<String> completions) {
        return StringUtil.copyPartialMatches(arg, completions, new ArrayList<>(completions.size()));
    }

    public static void loadVehicleList(){
        if (!vehicleList.isEmpty()) vehicleList.clear();

        List<Map<?, ?>> vehicles = ConfigModule.vehiclesConfig.getVehicles();
        for (Map<?, ?> configVehicle : vehicles) {
            List<Map<?, ?>> skins = (List<Map<?, ?>>) configVehicle.get("cars");
            for (Map<?, ?> skin : skins) {
                String skinName = ((String) skin.get("name")).replace(" ", "_").toUpperCase(Locale.ROOT);
                String skinUuid = (String) skin.get("uuid");
                vehicleList.put(skinName, skinUuid);
            }
        }
    }

}
