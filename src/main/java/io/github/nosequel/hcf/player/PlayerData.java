package io.github.nosequel.hcf.player;

import io.github.nosequel.hcf.HCTeams;
import io.github.nosequel.hcf.data.Data;
import io.github.nosequel.hcf.util.command.annotation.Subcommand;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;


@Getter
@Setter
public class PlayerData {

    private final UUID uuid;
    private final Set<Data> data = new HashSet<>();
    private final PlayerDataController controller = HCTeams.getInstance().getHandler().findController(PlayerDataController.class);

    /**
     * Constructor for creating a new Profile
     * This constructor automatically adds the profile to the profile list
     *
     * @param uuid the uuid
     */
    public PlayerData(UUID uuid) {
        this.uuid = uuid;

        controller.getPlayerData().add(this);
    }

    /**
     * Find a data object by a class
     *
     * @param data the data class
     * @param <T>  the type of the data object
     * @return the found data object | or null
     */
    public <T extends Data> T findData(Class<T> data) {
        return data.cast(this.data.stream()
                .filter($data -> $data.getClass().equals(data))
                .findFirst().orElse(null));

    }

    /**
     * Add a data object to the player's data
     *
     * @param data the data object
     * @param <T>  the type of the data object
     * @return the data object
     */
    @SuppressWarnings("unchecked")
    public <T extends Data> T addData(T data) {
        if (this.getData().add(data)) {
            return data;
        }

        return (T) this.findData(data.getClass());
    }

    /**
     * Check whether a player has a data class
     *
     * @param data the data class
     * @param <T>  the type of the data class
     * @return whether the player has the data class or not.
     */
    public <T extends Data> boolean hasData(Class<T> data) {
        return this.findData(data) != null;
    }
}