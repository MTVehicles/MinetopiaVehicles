package nl.mtvehicles.core.infrastructure.models;

import nl.mtvehicles.core.infrastructure.modules.VersionModule;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;

import javax.annotation.Nullable;
import java.lang.reflect.Method;

public abstract class MTVListener implements Listener {

    protected Event event;
    protected Player player;

    private MTVEvent api;

    protected MTVListener(){
        this.api = null;
    }

    protected MTVListener(MTVEvent api){
        this.api = api;
    }

    protected boolean isCancelled(){
        if (event == null) throw new NullPointerException("Cannot check if event is cancelled if event is null.");

        if (getAPI().isCancelled()) return true;

        if (VersionModule.getServerVersion().is1_12()){
            try {
                Method method = event.getClass().getDeclaredMethod("isCancelled");
                return (boolean) method.invoke(event);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        if (!(event instanceof org.bukkit.event.Cancellable)) return false;
        return ((org.bukkit.event.Cancellable) event).isCancelled();
    }

    protected void setAPI(MTVEvent event){
        this.api = event;
    }

    protected MTVEvent getAPI(){
        if (api == null) throw new NullPointerException("Event API not specified for this listener.");

        return api;
    }

    protected void callAPI(){
        if (api == null) throw new NullPointerException("Event API not specified for this listener.");

        api.setPlayer(player);
        api.call();
    }

    /**
     * Call the event with a custom player
     * @param player Custom player (may be null if no player is specified)
     */
    protected void callAPI(@Nullable Player player){
        if (api == null) throw new NullPointerException("Event API not specified for this listener.");

        api.setPlayer(player);
        api.call();
    }
}
