package io.github.nosequel.hcf.team;

import io.github.nosequel.hcf.controller.Controllable;
import io.github.nosequel.hcf.data.Data;
import io.github.nosequel.hcf.data.Loadable;
import io.github.nosequel.hcf.team.claim.Claim;
import io.github.nosequel.hcf.team.data.TeamData;
import io.github.nosequel.hcf.team.data.impl.CosmeticTeamData;
import io.github.nosequel.hcf.team.data.impl.claim.ClaimTeamData;
import io.github.nosequel.hcf.team.data.impl.player.PlayerTeamData;
import io.github.nosequel.hcf.team.data.impl.player.invites.InviteTeamData;
import io.github.nosequel.hcf.team.enums.TeamType;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class Team implements Controllable<TeamController>, Loadable<TeamData> {

    private final TeamController teamController = this.getController();
    private final TeamType type;
    private final List<TeamData> data = new ArrayList<>();

    private ChatColor color;

    private UUID uniqueId;
    private String name;

    /**
     * Constructor for creating a new Team object
     *
     * @param uuid the uuid of the team
     * @param name the name of the team
     * @param type the team type
     */
    public Team(UUID uuid, String name, TeamType type) {
        this.uniqueId = uuid == null ? UUID.randomUUID() : uuid;
        this.name = name;
        this.type = type;

        setupData();
        teamController.getTeams().add(this);
    }

    /**
     * Constructor for creating a new Player Team object
     * This defaults the TeamType to PLAYER_TEAM
     *
     * @param uuid       the uuid of the team
     * @param name       the name of the team
     * @param leaderUuid the leader of the team
     */
    public Team(UUID uuid, String name, UUID leaderUuid) {
        this(uuid, name, TeamType.PLAYER_TEAM);

        this.addData(new PlayerTeamData(this, leaderUuid));
    }

    /**
     * Constructor for creating a new Team object
     * This constructor automatically adds a ClaimTeamData to the data list
     *
     * @param uuid  the uuid of the team
     * @param name  the name of the team
     * @param type  the type of the claim
     * @param claim the allocated region of the team
     */
    public Team(UUID uuid, String name, TeamType type, Claim claim) {
        this(uuid, name, type);

        this.addData(new ClaimTeamData(this, claim));
    }

    private void setupData() {
        this.addData(new CosmeticTeamData(this));

        if(this.type.equals(TeamType.PLAYER_TEAM)) {
            this.addData(new InviteTeamData());
        }
    }

    /**
     * Set the team's claim to a new claim
     *
     * @param claim the claim
     */
    public void addClaim(Claim claim) {
        if(this.findData(ClaimTeamData.class) != null) {
            this.getData().remove(this.findData(ClaimTeamData.class));
        }

        this.addData(new ClaimTeamData(this, claim));
    }

    /**
     * Check whether a player can interact with the team's claim
     *
     * @param player the player
     * @return whether he can interact
     */
    public boolean canInteract(Player player) {
        if (player.hasPermission("hcteams.bypass.interact") || player.getGameMode().equals(GameMode.CREATIVE)) {
            return true;
        }

        if (this.getType().equals(TeamType.PLAYER_TEAM)) {
            return this.findData(PlayerTeamData.class).contains(player);
        }

        return this.type.canInteract;
    }

    /**
     * Disband the current team
     */
    public void disband() {
        if(this.type.equals(TeamType.PLAYER_TEAM)) {
            final PlayerTeamData playerTeamData = this.findData(PlayerTeamData.class);
            playerTeamData.broadcast(ChatColor.RED + "Your current team has been disbanded.");
        }

        teamController.getTeams().remove(this);
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