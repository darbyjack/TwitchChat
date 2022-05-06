package me.glaremasters.twitchchat.config;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.yaml.NodeStyle;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import javax.annotation.Nullable;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public final class ConfigFactory {
    private @NotNull final Path dataFolder;

    public ConfigFactory(@NotNull Path dataFolder) {
        this.dataFolder = dataFolder;
    }

    public @NotNull Settings settings() {
        final var config = create(Settings.class, "settings.yml");
        return Objects.requireNonNullElseGet(config, Settings::new);
    }

    private @Nullable <T> T create(@NotNull final Class<T> clazz, @NotNull final String fileName) {
        try {
            if (!Files.exists(dataFolder)) {
                Files.createDirectories(dataFolder);
            }

            final var path = dataFolder.resolve(fileName);

            final var loader = loader(path);
            final var node = loader.load();
            final var config = node.get(clazz);

            if (!Files.exists(path)) {
                Files.createFile(path);
                node.set(clazz, config);
            }

            loader.save(node);
            return config;
        } catch (final IOException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    private @NotNull YamlConfigurationLoader loader(@NotNull final Path path) {
        return YamlConfigurationLoader.builder()
                .path(path)
                .defaultOptions(options -> options.shouldCopyDefaults(true))
                .nodeStyle(NodeStyle.BLOCK)
                .indent(2)
                .build();
    }
}
