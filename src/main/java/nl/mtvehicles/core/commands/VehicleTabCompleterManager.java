package nl.mtvehicles.core.commands;

import nl.mtvehicles.core.commands.vehiclesubs.VehicleEdit;
import nl.mtvehicles.core.infrastructure.modules.CommandModule;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Tab completer for /mtv command
 */
public class VehicleTabCompleterManager implements org.bukkit.command.TabCompleter {
    
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
            }
        }
        return null;
    }

    private List<String> getApplicableTabCompleters(String arg, Collection<String> completions) {
        return StringUtil.copyPartialMatches(arg, completions, new ArrayList<>(completions.size()));
    }
}
