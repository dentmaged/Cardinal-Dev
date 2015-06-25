package in.twizmwaz.cardinal.util;

import in.twizmwaz.cardinal.chat.ChatConstant;
import in.twizmwaz.cardinal.chat.LocalizedChatMessage;
import in.twizmwaz.cardinal.command.TournamentCommands;
import in.twizmwaz.cardinal.event.TeamNameChangeEvent;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;

import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;

public class RegisterUtils {

    public static void register(CommandContext cmd, CommandSender sender) throws CommandException {
        TeamModule<Player> team = TeamUtils.getTeamByName(cmd.getString(0));
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

    public static void clear(CommandContext cmd, CommandSender sender) {
        String locale = ChatUtils.getLocale(sender);
        sender.sendMessage(ChatColor.GRAY + new LocalizedChatMessage(ChatConstant.GENERIC_TEAM_CLEAR).getMessage(locale));
        for (Entry<String, String> entry : TournamentCommands.registeredTeams.entrySet()) {
            if (TeamUtils.getTeamByName(entry.getValue()) != null) {
                TeamUtils.getTeamByName(entry.getValue()).setName(entry.getKey());
            }
        }
        TournamentCommands.registeredTeams.clear();
    }

}
