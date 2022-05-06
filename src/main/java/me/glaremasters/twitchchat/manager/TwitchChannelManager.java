package me.glaremasters.twitchchat.manager;

import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import me.glaremasters.twitchchat.TwitchChat;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class TwitchChannelManager {
    private final TwitchChat plugin;
    private final Map<String, Set<UUID>> channels = new HashMap<>();

    public TwitchChannelManager(TwitchChat plugin) {
        this.plugin = plugin;

        init();
    }

    public void init() {
        this.plugin.twitch().getEventManager().onEvent(ChannelMessageEvent.class, event -> {
            handleMessage(event.getChannel().getName(), event.getUser().getName(), event.getMessage());
        });
    }

    public void addChannel(final String channel) {
        channels.put(channel, new HashSet<>());
    }

    public void removeChannel(final String channel) {
        channels.remove(channel);
    }

    public void addPlayerToChannel(final String channel, final UUID uuid) {
        channels.get(channel).add(uuid);
    }

    public void removePlayerFromChannel(final String channel, final UUID uuid) {
        channels.get(channel).remove(uuid);
    }

    public Set<UUID> players(final String channel) {
        return channels.get(channel);
    }

    public void handleMessage(final String channel, final String user, final String message) {
        final Component content = Component.text(plugin.config().settings().format().replace("{channel}", channel).replace("{user}", user).replace("{message}", message));
        players(channel).forEach(uuid -> Bukkit.getPlayer(uuid).sendMessage(content));
    }

    public Map<String, Set<UUID>> channels() {
        return channels;
    }
}
