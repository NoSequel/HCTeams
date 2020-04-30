package io.github.nosequel.hcf.team;

import io.github.nosequel.hcf.data.Data;
import io.github.nosequel.hcf.team.data.TeamData;
import io.github.nosequel.hcf.team.data.impl.player.PlayerTeamData;
import io.github.nosequel.hcf.team.enums.TeamType;
import jdk.internal.jline.internal.Nullable;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class Team {

    private final TeamType type;
    private final List<Data> data = new ArrayList<>();

    @Nullable
    private ChatColor color;

    private UUID uuid;
    private String name;

    /**
     * Constructor for creating a new Team object
     *
     * @param uuid the uuid of the team
     * @param name the name of the team
     * @param type the team type
     */
    public Team(@Nullable UUID uuid, String name, TeamType type) {
        this.uuid = uuid == null ? UUID.randomUUID() : uuid;
        this.name = name;
        this.type = type;

        this.setupData();
    }

    /**
     * Constructor for creating a new Player Team object
     * This defaults the TeamType to PLAYER_TEAM
     *
     * @param uuid       the uuid of the team
     * @param name       the name of the team
     * @param leaderUuid the leader of the team
     */
    public Team(@Nullable UUID uuid, String name, UUID leaderUuid) {
        this(uuid, name, TeamType.PLAYER_TEAM);
        this.addData(new PlayerTeamData(this, leaderUuid));
    }

    /**
     * Sets up all data objects for the team
     */
    private void setupData() {
    }

    /**
     * Add a new Data object to the data list of the team
     *
     * @param data the data object
     * @param <T>  the type of the data
     * @return the data object
     */
    public <T extends TeamData> T addData(T data) {
        this.data.add(data);

        return data;
    }

    /**
     * Find a Data object by a class {@link Data}
     *
     * @param data the class
     * @param <T>  the type of the Data object
     * @return the data object
     */
    public <T extends TeamData> T findData(Class<T> data) {
        return data.cast(this.data.stream()
                .filter($data -> $data.getClass().equals(data))
                .findFirst().orElse(null));
    }

    /**
     * Get the display name of the team
     *
     * @return the display name
     */
    public String getDisplayName(Player player) {
        return this.type.formatName(this, player);
    }

    /**
     * Get the formatted name of the team
     *
     * @return the formatted name
     */
    public String getFormattedName() {
        return this.name.replace("_", " ");
    }

}