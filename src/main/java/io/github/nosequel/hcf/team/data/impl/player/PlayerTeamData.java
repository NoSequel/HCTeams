package io.github.nosequel.hcf.team.data.impl.player;

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.github.nosequel.hcf.team.Team;
import io.github.nosequel.hcf.team.data.impl.SaveableTeamData;
import io.github.nosequel.hcf.util.JsonBuilder;
import io.github.nosequel.hcf.util.JsonUtils;
import io.github.nosequel.hcf.util.StringUtils;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
public class PlayerTeamData implements SaveableTeamData {

    private UUID leader;

    private final Set<UUID> members = new HashSet<>();
    private final Set<UUID> captains = new HashSet<>();
    private final Set<UUID> coLeaders = new HashSet<>();

    private final int balance = 0;

    private final Team team;

    /**
     * Constructor for creating a new PlayerTeamData object
     *
     * @param team the team it's assigned to
     */
    public PlayerTeamData(Team team, UUID leaderUuid) {
        this.team = team;
        this.leader = leaderUuid;
    }

    /**
     * Constructor for loading a PlayerTeamData object from a JsonObject
     *
     * @param team   the team it's assigned to
     * @param object the object it has to read the data from
     */
    public PlayerTeamData(Team team, JsonObject object) {
        this.team = team;
        this.leader = UUID.fromString(object.get("leader").getAsString());

        ImmutableMap.of(
                "members", this.members,
                "captains", this.captains,
                "coLeaders", this.coLeaders
        ).forEach((key, value) -> {
            final String $key = object.get("key").getAsString();
            final JsonArray jsonArray = JsonUtils.getParser().parse($key).getAsJsonArray();

            jsonArray.forEach(element -> value.add(UUID.fromString(element.getAsString())));
        });

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

    /**
     * Get all total members of the team
     *
     * @return a list of uuids of all members
     */
    public List<UUID> getAllMembers() {
        final List<UUID> members = new ArrayList<>(this.captains);
        members.addAll(this.members);
        members.addAll(this.coLeaders);
        members.add(this.leader);

        return members;
    }

    /**
     * Get all online members
     *
     * @return the online members
     */
    public List<Player> getOnlineMembers() {
        return this.getAllMembers().stream()
                .map(Bukkit::getPlayer)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    /**
     * Broadcast a message to all the members
     *
     * @param message the message
     */
    public void broadcast(String message) {
        this.getOnlineMembers().forEach(player -> player.sendMessage(StringUtils.translate(message)));
    }

    @Override
    public String getSavePath() {
        return "player_data";
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

    /**
     * Check whether a player is in the team
     *
     * @param player the player
     * @return whether he's in the team
     */
    public boolean contains(Player player) {
        return this.members.contains(player.getUniqueId()) || this.captains.contains(player.getUniqueId()) || this.coLeaders.contains(player.getUniqueId()) || this.leader.equals(player.getUniqueId());
    }
}