package io.github.nosequel.hcf.team;

import io.github.nosequel.hcf.data.Datable;
import io.github.nosequel.hcf.controller.Controller;
import io.github.nosequel.hcf.team.claim.Claim;
import io.github.nosequel.hcf.team.claim.ClaimPriority;
import io.github.nosequel.hcf.team.data.TeamData;
import io.github.nosequel.hcf.team.data.impl.claim.ClaimTeamData;
import io.github.nosequel.hcf.team.enums.TeamType;
import io.github.nosequel.hcf.util.Cuboid;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Getter
@Setter
public class TeamController implements Controller, Datable<TeamData> {

    private final List<Team> teams = new ArrayList<>();
    private final List<Class<TeamData>> registeredData = new ArrayList<>();

    public void enable() {
        new Team(null, "Spawn", TeamType.SAFEZONE_TEAM,
                new Claim(
                        new Cuboid(
                                new Location(Bukkit.getWorlds().get(0), 100, 100, 100),
                                new Location(Bukkit.getWorlds().get(0), -100, -100, -100)
                        ),
                        ClaimPriority.NORMAL
                )
        );

        new Team(null, "Wilderness", TeamType.WILDERNESS_TEAM,
                new Claim(
                        new Cuboid(
                                new Location(Bukkit.getWorlds().get(0), 2000, 2000, 2000),
                                new Location(Bukkit.getWorlds().get(0), -2000, -2000, -2000)
                        ),
                        ClaimPriority.NORMAL
                )
        );
    }

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
     * Find a team by a location of a player
     *
     * @param location the location
     * @return the found team
     */
    public Team findTeam(Location location) {
        final Optional<Claim> claim = this.teams.stream()
                .filter(team -> team.findData(ClaimTeamData.class) != null)
                .map(team -> team.findData(ClaimTeamData.class).getClaim())
                .filter($claim -> $claim.getCuboid().isLocationInCuboid(location))
                .findFirst();

        return claim.map(Claim::getTeam).orElse(null);
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