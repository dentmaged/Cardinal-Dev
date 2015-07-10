package in.twizmwaz.cardinal.command;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.event.RankChangeEvent;
import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.modules.nicks.NickModule;
import in.twizmwaz.cardinal.util.ChatUtils;
import in.twizmwaz.cardinal.util.TeamUtils;
import in.twizmwaz.cardinal.util.UUIDFetcher;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.CommandPermissions;

public class NickCommands {

    @SuppressWarnings("deprecation")
    @Command(aliases = "nick", desc = "Changes your nickname", usage = "<show|off|nick>", min = 0, max = 2, flags = "i")
    @CommandPermissions("cardinal.nick")
    public static void nick(final CommandContext args, CommandSender sender) throws CommandException {
        if (!(sender instanceof Player) && args.argsLength() == 1)
            throw new CommandException("Only players may run this command!");
        Player target = (Player) sender;
        if (args.argsLength() == 2) {
            target = Bukkit.getPlayer(args.getString(1));
        }

        if (target == null)
            throw new CommandException("That player does not exist!");
        Match match = GameHandler.getGameHandler().getMatch();
        String nick = args.getString(0, "show");
        if (nick.equalsIgnoreCase("show")) {
            if (match.getModules().getModule(NickModule.class).getNick(target) != null) {
                String prefix = target.getName() + ChatColor.YELLOW + " is";
                if (target.getName().equalsIgnoreCase(sender.getName()))
                    prefix = ChatColor.YELLOW + "You are";
                sender.sendMessage(prefix + " disguised as " + TeamUtils.getTeamColorByPlayer(target) + match.getModules().getModule(NickModule.class).getNick(target) + ChatColor.YELLOW + "!");
                return;
            } else {
                String prefix = ChatColor.RED + "That player is";
                if (target.getName().equalsIgnoreCase(sender.getName()))
                    prefix = ChatColor.RED + "You are";
                sender.sendMessage(prefix + " not disguised!");
                return;
            }
        }
        boolean check = !nick.equalsIgnoreCase("off"), taken = false;

        if (check) {
            sender.sendMessage(ChatColor.YELLOW + "Checking nickname " + TeamUtils.getTeamColorByPlayer(target) + nick.replace(".", "") + ChatColor.YELLOW + "...");

            try {
                taken = UUIDFetcher.getUUIDOf(nick) != null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (nick.equalsIgnoreCase("off")) {
            match.getModules().getModule(NickModule.class).removeNick(target);
            Bukkit.getPluginManager().callEvent(new RankChangeEvent(target, TeamUtils.getTeamByPlayer(target)));

            if (target.getName() != sender.getName())
                target.sendMessage(TeamUtils.getTeamColorByPlayer(target) + target.getName() + ChatColor.YELLOW + " is now undisguised!");
            target.sendMessage("You are now undisgusied!");
            return;
        }

        if (!taken) {
            boolean instant = args.hasFlag('i');
            if (instant) {
                if (target.getName() != sender.getName())
                    target.sendMessage(TeamUtils.getTeamColorByPlayer(target) + target.getName() + ChatColor.YELLOW + " is now disguised as " + TeamUtils.getTeamColorByPlayer(target) + nick + ChatColor.YELLOW + "!");
                target.sendMessage(ChatColor.YELLOW + "You are now disguised as " + TeamUtils.getTeamColorByPlayer(target) + nick.replace(".", "") + ChatColor.YELLOW + "!");
                ChatUtils.getAdminChannel().sendMessage("[" + ChatColor.GOLD + "A" + ChatColor.WHITE + "] " + TeamUtils.getTeamColorByPlayer(target) + target.getName() + ChatColor.YELLOW + " is disguised as " + TeamUtils.getTeamColorByPlayer(target) + nick.replace(".", "") + ChatColor.YELLOW + "!");
                match.getModules().getModule(NickModule.class).setNick(target, nick, instant);
                Bukkit.getPluginManager().callEvent(new RankChangeEvent(target, TeamUtils.getTeamByPlayer(target)));
            } else {
                if (target.getName() != sender.getName())
                    target.sendMessage(TeamUtils.getTeamColorByPlayer(target) + target.getName() + ChatColor.YELLOW + " will be disguised as " + TeamUtils.getTeamColorByPlayer(target) + nick.replace(".", "") + ChatColor.YELLOW + " when they next join the server!");
                target.sendMessage(ChatColor.YELLOW + "You will be disguised as " + TeamUtils.getTeamColorByPlayer(target) + nick + ChatColor.YELLOW + " when you next join the server!");
                ChatUtils.getAdminChannel().sendMessage("[" + ChatColor.GOLD + "A" + ChatColor.WHITE + "] " + TeamUtils.getTeamColorByPlayer(target) + target.getName() + ChatColor.YELLOW + " will be disguised as " + TeamUtils.getTeamColorByPlayer(target) + nick.replace(".", "") + ChatColor.YELLOW + " when they next join the server!");
                match.getModules().getModule(NickModule.class).setNick(target, nick, instant);
            }
        } else if (taken && check) {
            sender.sendMessage(ChatColor.RED + "That nickname is taken!");
        }
    }

}
