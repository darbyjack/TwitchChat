package me.glaremasters.twitchchat.commands;

import com.github.twitch4j.TwitchClient;
import dev.triumphteam.cmd.core.BaseCommand;
import dev.triumphteam.cmd.core.annotation.Command;
import dev.triumphteam.cmd.core.annotation.SubCommand;
import dev.triumphteam.cmd.core.annotation.Suggestion;
import me.glaremasters.twitchchat.TwitchChat;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command("twitch")
public class TwitchChatCommands extends BaseCommand {
    private final TwitchChat plugin;
    private final TwitchClient client;

    public TwitchChatCommands(final TwitchChat plugin) {
        this.plugin = plugin;
        this.client = plugin.twitch();
    }

    @SubCommand("add")
    public void subscribe(final CommandSender sender, final String channel) {
        client.getChat().joinChannel(channel);
        plugin.channelManager().addChannel(channel);
        sender.sendMessage("Now listening to chat from: " + channel);
    }

    @SubCommand("remove")
    public void unsubscribe(final CommandSender sender, @Suggestion("channels") final String channel) {
        client.getChat().leaveChannel(channel);
        plugin.channelManager().removeChannel(channel);
        sender.sendMessage("No longer listening to chat from: " + channel);
    }

    @SubCommand("listen")
    public void watch(final CommandSender sender, @Suggestion("channels") final String channel) {
        final Player player = (Player) sender;
        plugin.channelManager().addPlayerToChannel(channel, player.getUniqueId());
    }

    @SubCommand("unlisten")
    public void unwatch(final CommandSender sender, @Suggestion("channels") final String channel) {
        final Player player = (Player) sender;
        plugin.channelManager().removePlayerFromChannel(channel, player.getUniqueId());
    }
}
