package in.twizmwaz.cardinal.util;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.GameObjective;
import in.twizmwaz.cardinal.module.ModuleCollection;
import in.twizmwaz.cardinal.module.modules.chatChannels.TeamChannel;
import in.twizmwaz.cardinal.module.modules.hill.HillObjective;
import in.twizmwaz.cardinal.module.modules.wools.WoolObjective;
import in.twizmwaz.cardinal.teams.Team;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class TeamUtils {

    public static Team getTeamWithFewestPlayers(Match match) {
        Team result = null;
        double percent = Double.POSITIVE_INFINITY;
        for (Team team : getTeams()) {
            if (!team.isObserver() && (team.size() / (double) team.getMax()) < percent) {
                result = team;
                percent = team.size() / (double) team.getMax();
            }
        }
        return result;
    }

    public static Team getTeamByName(String name) {
        if (name == null) return null;
        for (Team team : GameHandler.getGameHandler().getMatch().getModules().getModules(Team.class)) {
            if (team.getName().replaceAll(" ", "").toLowerCase().startsWith(name.replaceAll(" ", "").toLowerCase())) {
                return team;
            }
        }
        return null;
    }

    public static Team getTeamById(String id) {
        for (Team team : GameHandler.getGameHandler().getMatch().getModules().getModules(Team.class)) {
            if (team.getId().replaceAll(" ", "").equalsIgnoreCase(id.replaceAll(" ", ""))) {
                return team;
            }
        }
        for (Team team : GameHandler.getGameHandler().getMatch().getModules().getModules(Team.class)) {
            if (team.getId().replaceAll(" ", "").toLowerCase().startsWith(id.replaceAll(" ", "").toLowerCase())) {
                return team;
            }
        }
        for (Team team : GameHandler.getGameHandler().getMatch().getModules().getModules(Team.class)) {
            if (team.getId().replaceAll(" ", "-").equalsIgnoreCase(id.replaceAll(" ", "-"))) {
                return team;
            }
        }
        for (Team team : GameHandler.getGameHandler().getMatch().getModules().getModules(Team.class)) {
            if (team.getId().replaceAll(" ", "-").toLowerCase().startsWith(id.replaceAll(" ", "-").toLowerCase())) {
                return team;
            }
        }
        return null;
    }

    public static Team getTeamByPlayer(Player player) {
        for (Team team : GameHandler.getGameHandler().getMatch().getModules().getModules(Team.class)) {
            if (team.contains(player)) {
                return team;
            }
        }
        return null;
    }

    public static ModuleCollection<Team> getTeams() {
        return GameHandler.getGameHandler().getMatch().getModules().getModules(Team.class);
    }

    public static ModuleCollection<GameObjective> getObjectives(Team team) {
        ModuleCollection<GameObjective> objectives = new ModuleCollection<>();
        for (GameObjective objective : GameHandler.getGameHandler().getMatch().getModules().getModules(GameObjective.class)) {
            if (objective instanceof WoolObjective) {
                if (objective.getTeam() == team) {
                    objectives.add(objective);
                }
            } else if (objective.getTeam() != team && !(objective instanceof HillObjective)) {
                objectives.add(objective);
            }
        }
        return objectives;
    }

    public static ModuleCollection<GameObjective> getShownObjectives(Team team) {
        ModuleCollection<GameObjective> objectives = new ModuleCollection<>();
        for (GameObjective objective : getObjectives(team)) {
            if (objective.showOnScoreboard()) {
                objectives.add(objective);
            }
        }
        return objectives;
    }

    public static TeamChannel getTeamChannel(Team team) {
        for (TeamChannel channel : GameHandler.getGameHandler().getMatch().getModules().getModules(TeamChannel.class)) {
            if (channel.getTeam() == team) return channel;
        }
        return null;
    }

    public static ChatColor getTeamColorByPlayer(OfflinePlayer player) {
        if (player.isOnline()) {
            Team team = getTeamByPlayer(player.getPlayer());
            if (team != null) return team.getColor();
            else return ChatColor.DARK_AQUA;
        } else return ChatColor.DARK_AQUA;
    }

    public static boolean teamsReady() {
        for (Team team : getTeams()) {
            if (!team.isReady()) return false;
        }
        return true;
    }

    public static boolean teamsNoObsReady() {
        for (Team team : getTeams()) {
            if (!team.isReady() && !team.isObserver()) return false;
        }
        return true;
    }
}
