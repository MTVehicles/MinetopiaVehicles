package nl.mtvehicles.core.infrastructure.models;

import nl.mtvehicles.core.infrastructure.modules.VersionModule;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;

import java.lang.reflect.Method;

public abstract class MTVListener implements Listener {

    protected Event event;
    protected Player player;

    final private MTVEvent api;

    protected MTVListener(MTVEvent api){
        this.api = api;
    }

    protected boolean isCancelled(){
        if (event == null) throw new NullPointerException("Cannot check if event is cancelled if event is null.");

        if (getAPI().isCancelled()) return true;

        if (VersionModule.getServerVersion().isOld()){
            try {
                Method method = event.getClass().getDeclaredMethod("isCancelled");
                return (boolean) method.invoke(event);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        if (!(event instanceof Cancellable)) return false;
        return ((Cancellable) event).isCancelled();
    }

    protected MTVEvent getAPI(){
        return api;
    }

    protected void callAPI(){
        api.setPlayer(player);
        api.call();
    }
}
