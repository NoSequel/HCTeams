package io.github.nosequel.hcf.util.command.adapter.defaults;

import io.github.nosequel.hcf.util.command.adapter.TypeAdapter;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class OfflinePlayerTypeAdapter implements TypeAdapter<OfflinePlayer> {

    @Override
    public OfflinePlayer convert(CommandSender sender, String source) {
        if (source.equalsIgnoreCase("@SELF")) {
            return (Player) sender;
        }

        OfflinePlayer player = Bukkit.getOfflinePlayer(source);

        if (player == null) {
            player = Bukkit.getOfflinePlayer(UUID.fromString(source));
        }

        return player;
    }

    @Override
    public Class<OfflinePlayer> getType() {
        return OfflinePlayer.class;
    }

}
