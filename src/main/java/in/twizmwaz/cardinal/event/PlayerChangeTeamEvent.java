package in.twizmwaz.cardinal.event;

import in.twizmwaz.cardinal.teams.Team;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerChangeTeamEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private final Player player;
    private final boolean forced;
    private Team newTeam;
    private Team oldTeam;
    private boolean cancelled;

    public PlayerChangeTeamEvent(Player player, boolean forced, Team newTeam, Team oldTeam) {
        this.player = player;
        this.forced = forced;
        this.newTeam = newTeam;
        this.oldTeam = oldTeam;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public Player getPlayer() {
        return player;
    }

    public Team getNewTeam() {
        return newTeam;
    }

    public Team getOldTeam() {
        return oldTeam;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean isCancelled) {
        this.cancelled = isCancelled;
    }
}
