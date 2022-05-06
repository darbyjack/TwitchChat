package me.glaremasters.twitchchat.config;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
public final class Settings {
    private String token = "";
    private String format = "[Twitch] {channel} {user}: {message}";

    public String token() {
        return token;
    }

    public String format() {
        return format;
    }
}
