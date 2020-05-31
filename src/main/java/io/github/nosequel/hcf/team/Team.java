package io.github.nosequel.hcf.team;

import io.github.nosequel.hcf.HCTeams;
import io.github.nosequel.hcf.controller.Controllable;
import io.github.nosequel.hcf.data.Loadable;
import io.github.nosequel.hcf.team.claim.Claim;
import io.github.nosequel.hcf.team.data.TeamData;
import io.github.nosequel.hcf.team.data.impl.GeneralData;
import io.github.nosequel.hcf.team.data.impl.claim.ClaimTeamData;
import io.github.nosequel.hcf.team.data.impl.player.DTRData;
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
import java.util.logging.Level;

@Getter
@Setter
public class Team implements Controllable<TeamController>, Loadable<TeamData> {

    private final TeamController teamController = this.getController();

    private List<TeamData> data = new ArrayList<>();
    private GeneralData generalData;
    private ChatColor color;
    private UUID uniqueId;

    public Team(UUID uuid) {
        this.uniqueId = uuid == null ? UUID.randomUUID() : uuid;

        this.teamController.getTeams().add(this);
    }

    /**
     * Constructor for creating a new Team object
     *
     * @param uuid the uuid of the team
     * @param name the name of the team
     * @param type the team type
     */
    public Team(UUID uuid, String name, TeamType type) {
        this(uuid);

        this.generalData = new GeneralData(name, type);

        this.setupData();

        HCTeams.getInstance().getLogger().log(Level.INFO, "Creating new team" + (this.hasData(GeneralData.class) ? " called " + this.findData(GeneralData.class).getName() : ""));
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

        this.addData(new PlayerTeamData(leaderUuid));
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

        this.addData(new ClaimTeamData(claim));
    }

    private void setupData() {
        this.addData(generalData);

        if (this.generalData.getType().equals(TeamType.PLAYER_TEAM)) {
            this.addData(new InviteTeamData());
            this.addData(new DTRData(1.1D));
        }
    }

    /**
     * Set the team's claim to a new claim
     *
     * @param claim the claim
     */
    public void addClaim(Claim claim) {
        if (this.findData(ClaimTeamData.class) != null) {
            this.getData().remove(this.findData(ClaimTeamData.class));
        }

        this.addData(new ClaimTeamData(claim));
    }

    /**
     * Check whether a player can interact with the team's claim
     *
     * @param player the player
     * @return whether he can interact
     */
    public boolean canInteract(Player player) {
        if (player.hasPermission("hcteams.bypass.interact") && player.getGameMode().equals(GameMode.CREATIVE)) {
            return true;
        }

        if (this.getGeneralData().getType().equals(TeamType.PLAYER_TEAM)) {
            return this.findData(PlayerTeamData.class).contains(player) || this.findData(DTRData.class).isRaidable();
        }

        return this.getGeneralData().getType().canInteract;
    }

    /**
     * Disband the current team
     */
    public void disband() {
        if (this.getGeneralData().getType().equals(TeamType.PLAYER_TEAM)) {
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
        return this.getGeneralData().getType().formatName(this, player);
    }

    /**
     * Get the formatted name of the team
     *
     * @return the formatted name
     */
    public String getFormattedName() {
        return this.getGeneralData().getName().replace("_", " ");
    }
}