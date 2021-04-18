package nl.mtvehicles.core.Utils;

import nl.mtvehicles.core.Main;
import org.bukkit.Bukkit;

public class RunnableUtils {

    public static void catchAsync(Runnable runnable) {
        if (Main.paper) {
            if (Bukkit.isPrimaryThread())
                runnable.run();
            else
                Bukkit.getScheduler().runTask(Main.instance, runnable);
        } else runnable.run();
    }

}
