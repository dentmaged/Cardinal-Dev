package in.twizmwaz.cardinal.util;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.chat.ChatMessage;
import in.twizmwaz.cardinal.module.ModuleCollection;
import in.twizmwaz.cardinal.module.modules.chatChannels.AdminChannel;
import in.twizmwaz.cardinal.module.modules.chatChannels.GlobalChannel;
import in.twizmwaz.cardinal.module.modules.chatChannels.TeamChannel;
import in.twizmwaz.cardinal.module.modules.nicks.NickModule;
import in.twizmwaz.cardinal.rank.Rank;

import java.util.Locale;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChatUtils {

    public static void sendWarningMessage(Player player, String msg) {
        if (msg != null) player.sendMessage(ChatColor.YELLOW + " \u26A0 " + ChatColor.RED + msg);
    }

    public static void sendWarningMessage(Player player, ChatMessage msg) {
        if (msg != null)
            player.sendMessage(ChatColor.YELLOW + " \u26A0 " + ChatColor.RED + msg.getMessage(player.getLocale()));
    }

    public static String getWarningMessage(String msg) {
        if (msg == null) return null;
        else return ChatColor.YELLOW + " \u26A0 " + ChatColor.RED + ChatColor.translateAlternateColorCodes('`', msg);
    }

    public static String getLocale(CommandSender sender) {
        return sender instanceof Player ? ((Player) sender).getLocale() : Locale.getDefault().toString();
    }

    public static GlobalChannel getGlobalChannel() {
        return GameHandler.getGameHandler().getMatch().getModules().getModule(GlobalChannel.class);
    }

    public static AdminChannel getAdminChannel() {
        return GameHandler.getGameHandler().getMatch().getModules().getModule(AdminChannel.class);
    }

    public static ModuleCollection<TeamChannel> getTeamChannels() {
        return GameHandler.getGameHandler().getMatch().getModules().getModules(TeamChannel.class);
    }

    public static String getUsername(CommandSender sender) {
        return ((sender instanceof Player) ? TeamUtils.getTeamColorByPlayer((Player) sender) + ((Player) sender).getDisplayName() : ChatColor.YELLOW + "*Console");
    }

    public static String getUsername(Player player, Player viewer) {
        String username = TeamUtils.getTeamColorByPlayer(player) + "";
        if (GameHandler.getGameHandler().getMatch().getModules().getModule(NickModule.class).getNick(player) != null && player.hasPermission("cardinal.staff"))
            if (GameHandler.getGameHandler().getMatch().getModules().getModule(NickModule.class).isActive(player))
                username += Rank.getPlayerPrefix(player.getUniqueId()) + username + ChatColor.STRIKETHROUGH + player.getName() + ChatColor.RESET + " " + player.getDisplayName();
            else
                username += Rank.getPlayerPrefix(player.getUniqueId()) + username + ChatColor.STRIKETHROUGH + player.getName() + ChatColor.RESET + " " + ChatColor.ITALIC + GameHandler.getGameHandler().getMatch().getModules().getModule(NickModule.class).getNick(player);
        else
            username += player.getDisplayName();
        return username;
    }

    public static enum ChannelType {
        GLOBAL, ADMIN, TEAM
    }

}
