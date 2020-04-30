package io.github.nosequel.hcf.team.data.impl.player;

import com.google.gson.JsonObject;
import io.github.nosequel.hcf.team.Team;
import io.github.nosequel.hcf.team.data.impl.SaveableTeamData;
import io.github.nosequel.hcf.util.JsonBuilder;
import io.github.nosequel.hcf.util.StringUtils;
import lombok.Getter;

import java.util.*;

@Getter
public class PlayerTeamData implements SaveableTeamData {

    private final UUID leader;

    private final Set<UUID> members = new HashSet<>();
    private final Set<UUID> captains = new HashSet<>();
    private final Set<UUID> coLeaders = new HashSet<>();

    private final Team team;

    /**
     * Constructor for creating a new PlayerTeamData object
     *
     * @param team the team
     */
    public PlayerTeamData(Team team, UUID leaderUuid) {
        this.team = team;
        this.leader = leaderUuid;
    }

    /**
     * Promote a player to a higher role
     *
     * @param uuid the player
     * @return the new role of the player
     */
    public PlayerRole promotePlayer(UUID uuid) {
        if (this.members.remove(uuid)) {
            this.captains.add(uuid);
        } else if (this.captains.remove(uuid)) {
            this.coLeaders.add(uuid);
        } else {
            this.members.add(uuid);
        }

        return this.getRole(uuid);
    }

    /**
     * Promote a player to a higher role
     *
     * @param uuid the player
     * @return the new role of the player
     */
    public PlayerRole demotePlayer(UUID uuid) {
        if (this.captains.remove(uuid)) {
            this.members.add(uuid);
        } else if (this.coLeaders.remove(uuid)) {
            this.captains.add(uuid);
        } else {
            this.members.add(uuid);
        }

        return this.getRole(uuid);
    }

    /**
     * Get the PlayerRole of a player
     *
     * @param uuid the uuid of the player
     * @return the role
     */
    public PlayerRole getRole(UUID uuid) {
        return this.leader.equals(uuid) ?
                PlayerRole.LEADER : this.members.contains(uuid) ?
                PlayerRole.MEMBER : this.captains.contains(uuid) ?
                PlayerRole.CAPTAIN : this.coLeaders.contains(uuid) ?
                PlayerRole.CO_LEADER : null;
    }

    @Override
    public String getSavePath() {
        return "player";
    }

    @Override
    public JsonObject toJson() {
        return new JsonBuilder()
                .addProperty("members", StringUtils.fromList((List<?>) members))
                .addProperty("captains", StringUtils.fromList((List<?>) captains))
                .addProperty("coLeaders", StringUtils.fromList((List<?>) coLeaders))
                .addProperty("leader", leader.toString())
                .get();
    }
}