package io.github.nosequel.hcf.player;

import io.github.nosequel.hcf.controller.Controller;
import io.github.nosequel.hcf.data.Data;
import io.github.nosequel.hcf.data.DataController;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class PlayerDataController implements Controller, DataController<PlayerData, Data> {

    private final List<PlayerData> playerData = new ArrayList<>();
    private final List<Class<? extends Data>> registeredData = new ArrayList<>();

    /**
     * Find a {@link PlayerData} instance by an UUID
     *
     * @param uuid the uuid
     * @return the found player data | or null
     */
    public PlayerData findPlayerData(UUID uuid) {
        return this.playerData.stream()
                .filter($playerData -> $playerData.getUniqueId().equals(uuid))
                .findFirst().orElse(null);
    }

    @Override
    public void load(PlayerData loadable) {
    }
}