package in.twizmwaz.cardinal.command;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.chat.ChatConstant;
import in.twizmwaz.cardinal.chat.LocalizedChatMessage;
import in.twizmwaz.cardinal.module.modules.permissions.PermissionModule;
import in.twizmwaz.cardinal.teams.Team;
import in.twizmwaz.cardinal.util.ChatUtils;
import in.twizmwaz.cardinal.util.TeamUtils;

import java.util.concurrent.Callable;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.LazyMetadataValue;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.CommandPermissions;

public class ChatCommands {

    @Command(aliases = {"g", "global", "shout"}, desc = "Talk in global chat.", usage = "<message>")
    @CommandPermissions("cardinal.chat.global")
    public static void global(final CommandContext cmd, CommandSender sender) throws CommandException {
        String locale = ChatUtils.getLocale(sender);
        if (sender instanceof Player) {
            if (cmd.argsLength() == 0) {
                ((Player) sender).setMetadata("default-channel", new LazyMetadataValue(GameHandler.getGameHandler().getPlugin(), LazyMetadataValue.CacheStrategy.NEVER_CACHE, new Channel(ChatUtils.ChannelType.GLOBAL)));
                sender.sendMessage(ChatColor.YELLOW + new LocalizedChatMessage(ChatConstant.UI_DEFAULT_CHANNEL_GLOBAL).getMessage(locale));
            }
            if (cmd.argsLength() > 0) {
                if (GameHandler.getGameHandler().getGlobalMute() && !PermissionModule.isStaff(((Player) sender)))
                    throw new CommandException(ChatConstant.ERROR_GLOBAL_MUTE_ENABLED.asMessage().getMessage(ChatUtils.getLocale(sender)));
                String message = assembleMessage(cmd), username = "";
                for (Player player : ChatUtils.getGlobalChannel().getMembers()) {
                    username = ChatUtils.getUsername((Player) sender, player);
                    player.sendMessage(username + ChatColor.RESET + ": " + message);
                }
                Bukkit.getConsoleSender().sendMessage(username + ChatColor.RESET + ": " + message);
            }
        } else throw new CommandException("Console cannot use this command.");
    }

    @Command(aliases = {"a", "admin"}, desc = "Talk in admin chat.", usage = "<message>")
    @CommandPermissions("cardinal.chat.admin")
    public static void admin(final CommandContext cmd, CommandSender sender) throws CommandException {
        String locale = ChatUtils.getLocale(sender);
        if (sender instanceof Player) {
            if (cmd.argsLength() == 0) {
                ((Player) sender).setMetadata("default-channel", new LazyMetadataValue(GameHandler.getGameHandler().getPlugin(), LazyMetadataValue.CacheStrategy.NEVER_CACHE, new Channel(ChatUtils.ChannelType.ADMIN)));
                sender.sendMessage(ChatColor.YELLOW + new LocalizedChatMessage(ChatConstant.UI_DEFAULT_CHANNEL_ADMIN).getMessage(locale));
            }
            if (cmd.argsLength() > 0) {
                String message = assembleMessage(cmd), username = "";
                for (Player player : ChatUtils.getAdminChannel().getMembers()) {
                    username = ChatUtils.getUsername((Player) sender, player);
                    player.sendMessage("[" + ChatColor.GOLD + "A" + ChatColor.WHITE + "] " + username + ChatColor.RESET + ": " + message);
                }
                Bukkit.getConsoleSender().sendMessage("[" + ChatColor.GOLD + "A" + ChatColor.WHITE + "] " + username + ChatColor.RESET + ": " + message);
            }
        } else throw new CommandException("Console cannot use this command.");
    }

    @Command(aliases = {"t"}, desc = "Talk in team chat.", usage = "<message>")
    @CommandPermissions("cardinal.chat.team")
    public static void team(final CommandContext cmd, CommandSender sender) throws CommandException {
        String locale = ChatUtils.getLocale(sender);
        if (sender instanceof Player) {
            if (cmd.argsLength() == 0) {
                ((Player) sender).setMetadata("default-channel", new LazyMetadataValue(GameHandler.getGameHandler().getPlugin(), LazyMetadataValue.CacheStrategy.NEVER_CACHE, new Channel(ChatUtils.ChannelType.TEAM)));
                sender.sendMessage(ChatColor.YELLOW + new LocalizedChatMessage(ChatConstant.UI_DEFAULT_CHANNEL_TEAM).getMessage(locale));
            }
            if (cmd.argsLength() > 0) {
                if (GameHandler.getGameHandler().getGlobalMute() && !PermissionModule.isStaff(((Player) sender)))
                    throw new CommandException(ChatConstant.ERROR_GLOBAL_MUTE_ENABLED.asMessage().getMessage(ChatUtils.getLocale(sender)));
                Team team = TeamUtils.getTeamByPlayer((Player) sender);
                String message = assembleMessage(cmd), username = "";
                for (Player player : TeamUtils.getTeamChannel(team).getMembers()) {
                    username = ChatUtils.getUsername((Player) sender, player);
                    player.sendMessage(team.getColor() + "[" + team.getName() + "] " + username + ChatColor.RESET + ": " + message);
                }
                Bukkit.getConsoleSender().sendMessage(team.getColor() + "[" + team.getName() + "] " + username + ChatColor.RESET + ": " + message);
            }
        } else throw new CommandException("Console cannot use this command.");
    }

    private static String assembleMessage(CommandContext context) {
        return context.getJoinedStrings(0);
    }

    public static class Channel implements Callable {

        private final ChatUtils.ChannelType channel;

        protected Channel(final ChatUtils.ChannelType channel) {
            this.channel = channel;
        }

        @Override
        public Object call() throws Exception {
            return channel;
        }

    }

}
