package nl.mtvehicles.core.commands;

import nl.mtvehicles.core.Main;
import nl.mtvehicles.core.infrastructure.modules.CommandModule;
import org.bukkit.util.StringUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class VehicleTabCompleterManager implements org.bukkit.command.TabCompleter {
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (strings.length == 1) {
            return getApplicableTabCompleters(strings[0], CommandModule.subcommands.keySet());
        }
        return null;
    }

    private List<String> getApplicableTabCompleters(String arg, Collection<String> completions) {
        return StringUtil.copyPartialMatches(arg, completions, new ArrayList<>(completions.size()));
    }
}
