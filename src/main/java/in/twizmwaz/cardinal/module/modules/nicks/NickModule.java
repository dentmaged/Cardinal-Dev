package in.twizmwaz.cardinal.module.modules.nicks;

import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.util.MapEntry;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerQuitEvent;

public class NickModule implements Module {

    private static Map<UUID, Entry<String, Boolean>> nicknames = new HashMap<UUID, Entry<String, Boolean>>();

    public void removeNick(Player player) {
        nicknames.remove(player.getUniqueId());
    }

    public void setNick(Player player, String nick, boolean instant) {
        nicknames.put(player.getUniqueId(), new MapEntry<String, Boolean>(nick, instant));
    }

    public String getNick(Player player) {
        return nicknames.get(player.getUniqueId()).getKey();
    }

    public boolean isInstant(Player player) {
        return nicknames.get(player.getUniqueId()).getValue();
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (isInstant(event.getPlayer()))
            nicknames.remove(event.getPlayer().getUniqueId());
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

}
