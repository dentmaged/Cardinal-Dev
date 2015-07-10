package in.twizmwaz.cardinal.command;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.chat.ChatConstant;
import in.twizmwaz.cardinal.chat.LocalizedChatMessage;
import in.twizmwaz.cardinal.module.modules.utils.UtilModule;
import in.twizmwaz.cardinal.util.ChatUtils;
import in.twizmwaz.cardinal.util.TeamUtils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.CommandPermissions;

public class FreezeCommands {

    @Command(aliases = { "freeze", "f" }, desc = "Freezes a player", usage = "<player>", min = 1, max = 1)
    @CommandPermissions("cardinal.freeze")
    public static void freeze(final CommandContext args, CommandSender sender) throws CommandException {
        Player target = Bukkit.getPlayer(args.getString(0));
        if (target == null)
            throw new CommandException(new LocalizedChatMessage(ChatConstant.ERROR_PLAYER_NOT_FOUND).getMessage(ChatUtils.getLocale(sender)));
        GameHandler.getGameHandler().getMatch().getModules().getModule(UtilModule.class).freeze(target, sender);
        ChatUtils.getAdminChannel().sendMessage("[" + ChatColor.GOLD + "A" + ChatColor.WHITE + "] " + (sender instanceof Player ? TeamUtils.getTeamColorByPlayer((Player) sender) + ((Player) sender).getDisplayName() : ChatColor.YELLOW + "*Console") + ChatColor.RED + " froze " + TeamUtils.getTeamColorByPlayer(target) + target.getName() + ChatColor.RED + "!");
    }

    @Command(aliases = { "unfreeze", "unf" }, desc = "Freezes a player", usage = "<player>", min = 1, max = 1)
    @CommandPermissions("cardinal.freeze")
    public static void unfreeze(final CommandContext args, CommandSender sender) throws CommandException {
        Player target = Bukkit.getPlayer(args.getString(0));
        if (target == null)
            throw new CommandException(new LocalizedChatMessage(ChatConstant.ERROR_PLAYER_NOT_FOUND).getMessage(ChatUtils.getLocale(sender)));
        GameHandler.getGameHandler().getMatch().getModules().getModule(UtilModule.class).unfreeze(target);
        ChatUtils.getAdminChannel().sendMessage("[" + ChatColor.GOLD + "A" + ChatColor.WHITE + "] " + (sender instanceof Player ? TeamUtils.getTeamColorByPlayer((Player) sender) + ((Player) sender).getDisplayName() : ChatColor.YELLOW + "*Console") + ChatColor.RED + " unfroze " + TeamUtils.getTeamColorByPlayer(target) + target.getName() + ChatColor.RED + "!");
    }

}
