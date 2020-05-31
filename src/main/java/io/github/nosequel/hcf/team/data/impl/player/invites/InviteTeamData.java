package io.github.nosequel.hcf.team.data.impl.player.invites;

import com.google.gson.JsonObject;
import io.github.nosequel.hcf.team.data.impl.SaveableTeamData;
import io.github.nosequel.hcf.util.JsonBuilder;
import io.github.nosequel.hcf.util.StringUtils;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class InviteTeamData extends SaveableTeamData {

    private List<UUID> invites;

    public InviteTeamData() {
        this.invites = new ArrayList<>();
    }

    public InviteTeamData(JsonObject object) {
        this.invites = StringUtils.listFromString(object.get("invites").getAsString()).stream()
                .map(UUID::fromString)
                .collect(Collectors.toList());
    }

    /**
     * Invite a player to a team
     *
     * @param player the player
     */
    public void invite(Player player) {
        this.invites.add(player.getUniqueId());
    }

    /**
     * Check if a player has been invited to the team
     *
     * @param player the player
     * @return whether he has been invited or not
     */
    public boolean hasInvite(Player player) {
        return this.invites.contains(player.getUniqueId());
    }

    @Override
    public String getSavePath() {
        return "invites";
    }

    @Override
    public JsonObject toJson() {
        return new JsonBuilder()
                .addProperty("invites", StringUtils.fromList(invites))
                .get();
    }
}