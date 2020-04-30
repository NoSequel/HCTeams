package io.github.nosequel.hcf.team;

import io.github.nosequel.hcf.data.Data;
import io.github.nosequel.hcf.team.data.TeamData;
import io.github.nosequel.hcf.team.data.impl.general.GeneralTeamData;
import io.github.nosequel.hcf.team.data.impl.player.PlayerTeamData;
import io.github.nosequel.hcf.team.enums.TeamType;
import jdk.internal.jline.internal.Nullable;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.ChatColor;

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

    private void setupData() {
        this.addData(new GeneralTeamData(this));
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
    public String getDisplayName() {
        final String formattedName = this.name.replace("_", "");

        switch (type) {
            case PLAYER_TEAM: { // todo: check if player is ally or in the team.
                return ChatColor.RED + formattedName;
            }

            case KOTH_TEAM: {
                return (color == null ? ChatColor.AQUA : color) + formattedName + ChatColor.GOLD + " KOTH";
            }

            case ROAD_TEAM: {
                return (color == null ? ChatColor.GOLD : color) + formattedName.replace("Road", " Road");
            }

            case SYSTEM_TEAM: {
                return (color == null ? ChatColor.WHITE : color) + formattedName;
            }

            case SAFEZONE_TEAM: {
                return (color == null ? ChatColor.GREEN : color) + formattedName;
            }
        }

        return "None";
    }
}