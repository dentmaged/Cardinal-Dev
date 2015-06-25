package in.twizmwaz.cardinal.module.modules.utils;

import in.twizmwaz.cardinal.command.TournamentCommands;
import in.twizmwaz.cardinal.event.CycleCompleteEvent;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.util.TeamUtils;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;

public class UtilModule implements Module {

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    @SuppressWarnings("unchecked")
    @EventHandler
    public void onCycleComplete(CycleCompleteEvent event) {
        for (TeamModule<Player> team : TeamUtils.getTeams()) {
            if (TournamentCommands.registeredTeams.get(team.getName()) != null) {
                team.setName(TournamentCommands.registeredTeams.get(team.getName()));
            }
        }
    }

}
