package coloryr.allmusic.side.bc;

import coloryr.allmusic.AllMusic;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class EventBC implements Listener {
    @EventHandler
    public void onPlayerquit(final PlayerDisconnectEvent event) {
        AllMusic.removeNowPlayPlayer(event.getPlayer().getName());
    }

    @EventHandler
    public void onLoginEvent(PostLoginEvent event) {
        AllMusic.joinPlay(event.getPlayer().getName());
    }
}
