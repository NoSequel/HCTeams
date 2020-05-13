package io.github.nosequel.hcf.team;

import io.github.nosequel.hcf.data.Datable;
import io.github.nosequel.hcf.controller.Controller;
import io.github.nosequel.hcf.team.claim.Claim;
import io.github.nosequel.hcf.team.claim.ClaimPriority;
import io.github.nosequel.hcf.team.data.TeamData;
import io.github.nosequel.hcf.team.data.impl.claim.ClaimTeamData;
import io.github.nosequel.hcf.team.data.impl.player.PlayerTeamData;
import io.github.nosequel.hcf.team.enums.TeamType;
import io.github.nosequel.hcf.util.Cuboid;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;

@Getter
@Setter
public class TeamController implements Controller, Datable<TeamData> {

    private final List<Team> teams = new ArrayList<>();
    private final List<Class<TeamData>> registeredData = new ArrayList<>();

    public void enable() {
        final Claim spawnClaim = new Claim(new Cuboid(new Location(Bukkit.getWorlds().get(0), 100, 100, 100), new Location(Bukkit.getWorlds().get(0), -100, -100, -100)), ClaimPriority.NORMAL);
        final Claim wildernessClaim = new Claim(new Cuboid(new Location(Bukkit.getWorlds().get(0), 2000, 2000, 2000), new Location(Bukkit.getWorlds().get(0), -2000, -2000, -2000)), ClaimPriority.NORMAL);

        spawnClaim.setDeathban(false);

        new Team(null, "Spawn", TeamType.SAFEZONE_TEAM, spawnClaim);
        new Team(null, "Wilderness", TeamType.WILDERNESS_TEAM, wildernessClaim);
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
     * Find a team by a name {@link String}
     *
     * @param name the name
     * @return the foun team
     */
    public Team findTeam(String name) {
        return this.teams.stream()
                .filter(team -> team.getName().equalsIgnoreCase(name))
                .findFirst().orElse(null);
    }

    /**
     * Find a team by a player
     *
     * @param player the player
     * @return the team
     */
    public Team findTeam(Player player) {
        final Optional<PlayerTeamData> team = this.teams.stream()
                .filter($team -> $team.findData(PlayerTeamData.class) != null)
                .map($team -> $team.findData(PlayerTeamData.class))
                .filter(data -> data.contains(player))
                .findFirst();

        return team.map(PlayerTeamData::getTeam).orElse(null);
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