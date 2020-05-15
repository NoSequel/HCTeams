package io.github.nosequel.hcf.util.command.adapter.defaults;

import io.github.nosequel.hcf.util.command.adapter.TypeAdapter;
import org.bukkit.command.CommandSender;

import java.util.UUID;

public class UUIDTypeAdapter implements TypeAdapter<UUID> {

    @Override
    public UUID convert(CommandSender sender, String source) {
        return UUID.fromString(source);
    }

    @Override
    public Class<UUID> getType() {
        return UUID.class;
    }
}
