package me.glaremasters.twitchchat;

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import dev.triumphteam.cmd.bukkit.BukkitCommandManager;
import dev.triumphteam.cmd.core.suggestion.SuggestionKey;
import me.glaremasters.twitchchat.commands.TwitchChatCommands;
import me.glaremasters.twitchchat.config.ConfigFactory;
import me.glaremasters.twitchchat.manager.TwitchChannelManager;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class TwitchChat extends JavaPlugin {
    private BukkitCommandManager<CommandSender> manager;
    private TwitchClient twitchClient;
    private TwitchChannelManager channelManager;
    private BukkitAudiences adventure;
    private ConfigFactory configFactory;

    @Override
    public void onEnable() {
        this.adventure = BukkitAudiences.create(this);
        this.configFactory = new ConfigFactory(getDataFolder().toPath());

        OAuth2Credential credential = new OAuth2Credential("twitch", config().settings().token());
        this.twitchClient = TwitchClientBuilder.builder().withEnableChat(true).withChatAccount(credential).build();

        this.manager = BukkitCommandManager.create(this);

        this.channelManager = new TwitchChannelManager(this);

        this.manager.registerSuggestion(SuggestionKey.of("channels"), (sender, context) -> channelManager.channels().keySet().stream().toList());
        this.manager.registerCommand(new TwitchChatCommands(this));
    }

    @Override
    public void onDisable() {
        if (this.adventure != null) {
            this.adventure.close();
            this.adventure = null;
        }
    }

    public @NonNull BukkitAudiences adventure() {
        if (this.adventure == null) {
            throw new IllegalStateException("Tried to access Adventure when the plugin was disabled!");
        }
        return this.adventure;
    }

    public BukkitCommandManager<CommandSender> manager() {
        return manager;
    }

    public TwitchClient twitch() {
        return twitchClient;
    }

    public TwitchChannelManager channelManager() {
        return channelManager;
    }

    public ConfigFactory config() {
        return configFactory;
    }
}
