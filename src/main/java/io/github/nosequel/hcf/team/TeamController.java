package io.github.nosequel.hcf.team;

import io.github.nosequel.hcf.data.Datable;
import io.github.nosequel.hcf.controller.Controller;
import io.github.nosequel.hcf.team.data.TeamData;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class TeamController implements Controller, Datable<TeamData> {

    private final List<Team> teams = new ArrayList<>();
    private final List<Class<TeamData>> registeredData = new ArrayList<>();

    /**
     * Find a team by a UUID
     *
     * @param uuid the uuid
     * @return the found team | or null
     */
    public Team findTeam(UUID uuid) {
        return this.teams.stream()
                .filter(team -> team.getUuid().equals(uuid))
                .findFirst().orElse(null);
    }

    /**
     * Find a data object by a class
     *
     * @param data the class
     * @param <T>  the type of the data object
     * @return the found data | or null
     */
    public <T extends TeamData> T findData(Class<T> data) {
        return data.cast(this.registeredData.stream()
                .filter($data -> $data.equals(data))
                .findFirst().orElse(null));
    }
}