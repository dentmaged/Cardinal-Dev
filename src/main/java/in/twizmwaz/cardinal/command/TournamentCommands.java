package in.twizmwaz.cardinal.command;

import in.twizmwaz.cardinal.Cardinal;
import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.chat.ChatConstant;
import in.twizmwaz.cardinal.chat.LocalizedChatMessage;
import in.twizmwaz.cardinal.event.TeamNameChangeEvent;
import in.twizmwaz.cardinal.match.MatchState;
import in.twizmwaz.cardinal.module.modules.startTimer.StartTimer;
import in.twizmwaz.cardinal.teams.Team;
import in.twizmwaz.cardinal.util.ChatUtils;
import in.twizmwaz.cardinal.util.TeamUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.NestedCommand;

public class TournamentCommands {

    public static final Map<String, String> registeredTeams = new HashMap<String, String>();

    @Command(aliases = { "ready", "r" }, desc = "Make your team ready.")
    public static void ready(final CommandContext cmd, CommandSender sender) throws CommandException {
        if (!(sender instanceof Player) && cmd.argsLength() == 0)
            throw new CommandException("Console cannot use this command!");

        if (GameHandler.getGameHandler().getMatch().getState().equals(MatchState.WAITING) || GameHandler.getGameHandler().getMatch().getState().equals(MatchState.STARTING)) {
            if (cmd.argsLength() > 0 && sender.hasPermission(new Permission("cardinal.ready.override", PermissionDefault.OP))) {
                if (cmd.getJoinedStrings(0).equalsIgnoreCase("*")) {
                    for (Team team : TeamUtils.getTeams()) {
                        team.setReady(true);
                        ChatUtils.getGlobalChannel().sendMessage(team.getCompleteName() + ChatColor.YELLOW + " is now ready");
                        if (Cardinal.getInstance().getConfig().getBoolean("observers-ready") ? TeamUtils.teamsReady() : TeamUtils.teamsNoObsReady()) {
                            GameHandler.getGameHandler().getMatch().start(600);
                        }
                    }
                } else {
                    Team team = TeamUtils.getTeamByPlayer((Player) sender);
                    if (!team.isReady()) {
                        team.setReady(true);
                        ChatUtils.getGlobalChannel().sendMessage(team.getCompleteName() + ChatColor.YELLOW + " is now ready");
                        if (Cardinal.getInstance().getConfig().getBoolean("observers-ready") ? TeamUtils.teamsReady() : TeamUtils.teamsNoObsReady()) {
                            GameHandler.getGameHandler().getMatch().start(600);
                        }
                    } else {
                        throw new CommandException("That team is already ready!");
                    }
                }
            } else {
                Team team = TeamUtils.getTeamByPlayer((Player) sender);
                if (!team.isReady()) {
                    team.setReady(true);
                    ChatUtils.getGlobalChannel().sendMessage(team.getCompleteName() + ChatColor.YELLOW + " is now ready");
                    if (Cardinal.getInstance().getConfig().getBoolean("observers-ready") ? TeamUtils.teamsReady() : TeamUtils.teamsNoObsReady()) {
                        GameHandler.getGameHandler().getMatch().start(600);
                    }
                } else {
                    throw new CommandException("Your team is already ready!");
                }
            }
        } else {
            throw new CommandException("You cannot ready up during or after a match");
        }
    }

    @Command(aliases = { "unready", "u" }, desc = "Make your team not ready.")
    public static void unready(final CommandContext cmd, CommandSender sender) throws CommandException {
        if (!(sender instanceof Player))
            throw new CommandException("Console cannot use this command!");
        if (GameHandler.getGameHandler().getMatch().getState().equals(MatchState.WAITING) || GameHandler.getGameHandler().getMatch().getState().equals(MatchState.STARTING)) {
            Team team = TeamUtils.getTeamByPlayer((Player) sender);
            if (team.isReady()) {
                team.setReady(false);
                ChatUtils.getGlobalChannel().sendMessage(team.getCompleteName() + ChatColor.YELLOW + " is no longer ready");
                if (GameHandler.getGameHandler().getMatch().getState().equals(MatchState.STARTING)) {
                    GameHandler.getGameHandler().getMatch().setState(MatchState.WAITING);
                    GameHandler.getGameHandler().getMatch().getModules().getModule(StartTimer.class).setCancelled(true);
                    ChatUtils.getGlobalChannel().sendMessage(ChatColor.RED + "Match start countdown cancelled because " + team.getCompleteName() + ChatColor.RED + " became un-ready.");
                }
            } else
                throw new CommandException("Your team is already not ready!");
        } else
            throw new CommandException("You cannot unready during or after a match");
    }

    @Command(aliases = { "register" }, desc = "Registers a team", min = 2)
    public static void register(final CommandContext cmd, CommandSender sender) throws CommandException {
        Team team = TeamUtils.getTeamByName(cmd.getString(0));
        if (team != null) {
            String name = cmd.getJoinedStrings(1);
            String locale = ChatUtils.getLocale(sender);
            sender.sendMessage(ChatColor.GRAY + new LocalizedChatMessage(ChatConstant.GENERIC_TEAM_REGISTER, team.getColor() + name + ChatColor.GRAY).getMessage(locale));
            TournamentCommands.registeredTeams.put(team.getName(), name);
            team.setName(name);
            Bukkit.getServer().getPluginManager().callEvent(new TeamNameChangeEvent(team));
        } else {
            throw new CommandException(new LocalizedChatMessage(ChatConstant.ERROR_NO_TEAM_MATCH).getMessage(ChatUtils.getLocale(sender)));
        }
    }

    @Command(aliases = { "clear" }, desc = "Clears all registered teams")
    public static void clear(final CommandContext cmd, CommandSender sender) throws CommandException {
        String locale = ChatUtils.getLocale(sender);
        sender.sendMessage(ChatColor.GRAY + new LocalizedChatMessage(ChatConstant.GENERIC_TEAM_CLEAR).getMessage(locale));
        for (Entry<String, String> entry : registeredTeams.entrySet()) {
            if (TeamUtils.getTeamByName(entry.getValue()) != null) {
                TeamUtils.getTeamByName(entry.getValue()).setName(entry.getKey());
            }
        }
        registeredTeams.clear();
    }

    public static class TournamentParentCommand {

        @Command(aliases = { "tournament", "tourney", "tm" }, desc = "Commands for tournaments.")
        @NestedCommand({ TournamentCommands.class })
        public static void tournament(final CommandContext args, CommandSender sender) throws CommandException {

        }

    }

}
