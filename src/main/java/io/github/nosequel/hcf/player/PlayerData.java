package io.github.nosequel.hcf.player;

import io.github.nosequel.hcf.HCTeams;
import io.github.nosequel.hcf.data.Data;
import io.github.nosequel.hcf.data.Loadable;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Getter
@Setter
public class PlayerData implements Loadable<Data> {

    private final UUID uniqueId;
    private final List<Data> data = new ArrayList<>();
    private final PlayerDataController controller = HCTeams.getInstance().getHandler().findController(PlayerDataController.class);

    /**
     * Constructor for creating a new Profile
     * This constructor automatically adds the profile to the profile list
     *
     * @param uniqueId the uuid
     */
    public PlayerData(UUID uniqueId) {
        this.uniqueId = uniqueId;

        controller.getPlayerData().add(this);
    }
}