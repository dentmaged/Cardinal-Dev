package in.twizmwaz.cardinal.event;

import in.twizmwaz.cardinal.teams.Team;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class MatchEndEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final Team team;

    public MatchEndEvent(Team team) {
        this.team = team;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public Team getTeam() throws NullPointerException {
        try {
            return team;
        } catch (NullPointerException ex) {
            throw new NullPointerException("No valid winning team");
        }
    }
}
