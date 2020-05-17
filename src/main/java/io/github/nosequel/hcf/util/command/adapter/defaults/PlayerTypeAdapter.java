package io.github.nosequel.hcf.util.command.adapter.defaults;

import io.github.nosequel.hcf.util.command.adapter.TypeAdapter;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PlayerTypeAdapter implements TypeAdapter<Player> {

    @Override
    public Player convert(CommandSender sender, String source) {
        if (source.equalsIgnoreCase("@SELF")) {
            return (Player) sender;
        }

        Player player = Bukkit.getPlayer(source);

        if (player == null) {
            player = Bukkit.getPlayer(UUID.fromString(source));
        }

        return player;
    }

    @Override
    public Class<Player> getType() {
        return Player.class;
    }
}
