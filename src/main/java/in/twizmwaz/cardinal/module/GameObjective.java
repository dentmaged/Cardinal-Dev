package in.twizmwaz.cardinal.module;

import in.twizmwaz.cardinal.module.modules.scoreboard.GameObjectiveScoreboardHandler;
import in.twizmwaz.cardinal.teams.Team;

public interface GameObjective extends Module {

    public Team getTeam();

    public String getName();

    public String getId();

    public boolean isTouched();

    public boolean isComplete();

    public boolean showOnScoreboard();

    public GameObjectiveScoreboardHandler getScoreboardHandler();

}
