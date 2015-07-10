package in.twizmwaz.cardinal.module.modules.utils;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.command.TournamentCommands;
import in.twizmwaz.cardinal.event.CycleCompleteEvent;
import in.twizmwaz.cardinal.event.MatchEndEvent;
import in.twizmwaz.cardinal.event.MatchStartEvent;
import in.twizmwaz.cardinal.event.PlayerChangeTeamEvent;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.ModuleCollection;
import in.twizmwaz.cardinal.module.modules.spawn.SpawnModule;
import in.twizmwaz.cardinal.teams.Team;
import in.twizmwaz.cardinal.util.ChatUtils;
import in.twizmwaz.cardinal.util.TeamUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerMoveEvent;

public class UtilModule implements Module {

    private static Map<UUID, UUID> frozenPlayers = new HashMap<UUID, UUID>();

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler
    public void onCycleComplete(CycleCompleteEvent event) {
        for (Team team : TeamUtils.getTeams()) {
            if (TournamentCommands.registeredTeams.get(team.getName()) != null) {
                team.setName(TournamentCommands.registeredTeams.get(team.getName()));
            }
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (isFrozen(event.getPlayer())) {
            if (event.getFrom().getX() != event.getTo().getX() || event.getFrom().getY() != event.getTo().getZ() || event.getFrom().getX() != event.getTo().getZ()) {
                Location loc = event.getFrom();
                loc.setPitch(event.getTo().getPitch());
                loc.setYaw(event.getTo().getYaw());
                event.getPlayer().teleport(loc);
            }
        }
    }

    @EventHandler
    public void onMatchStart(MatchStartEvent event) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.getInventory().setHeldItemSlot(0);
        }
    }

    @EventHandler
    public void onMatchEnd(MatchEndEvent event) {
        ModuleCollection<SpawnModule> spawns = new ModuleCollection<SpawnModule>();
        Team team = TeamUtils.getTeamById("observers");
        for (SpawnModule spawnModule : GameHandler.getGameHandler().getMatch().getModules().getModules(SpawnModule.class)) {
            if (spawnModule.getTeam() == team) spawns.add(spawnModule);
        }
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.teleport(spawns.getRandom().getLocation());
        }
    }

    @EventHandler
    public void onPlayerChangeTeam(PlayerChangeTeamEvent event) {
        ChatUtils.getAdminChannel().sendMessage(event.getPlayer().getDisplayName() + ChatColor.GRAY + " joined " + event.getNewTeam().getCompleteName());
    }

    public void freeze(Player target, CommandSender freezer) {
        frozenPlayers.put(target.getUniqueId(), (freezer instanceof Player ? ((Player) freezer).getUniqueId() : UUID.randomUUID()));
    }

    public void unfreeze(Player target) {
        frozenPlayers.remove(target.getUniqueId());
    }

    public boolean isFrozen(Player player) {
        return frozenPlayers.get(player.getUniqueId()) != null;
    }

}
