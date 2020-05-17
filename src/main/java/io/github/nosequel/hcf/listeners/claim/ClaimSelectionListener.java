package io.github.nosequel.hcf.listeners.claim;

import io.github.nosequel.hcf.HCTeams;
import io.github.nosequel.hcf.controller.Controllable;
import io.github.nosequel.hcf.player.PlayerData;
import io.github.nosequel.hcf.player.PlayerDataController;
import io.github.nosequel.hcf.player.data.ClaimSelectionData;
import io.github.nosequel.hcf.team.Team;
import io.github.nosequel.hcf.team.TeamController;
import io.github.nosequel.hcf.team.claim.selection.ClaimSelection;
import io.github.nosequel.hcf.team.data.impl.claim.ClaimTeamData;
import io.github.nosequel.hcf.team.data.impl.player.PlayerTeamData;
import io.github.nosequel.hcf.team.enums.TeamType;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class ClaimSelectionListener implements Listener, Controllable<PlayerDataController> {

    private final PlayerDataController controller = this.getController();
    private final TeamController teamController = HCTeams.getInstance().getHandler().findController(TeamController.class);

    @EventHandler
    public void onClick(PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        final PlayerData playerData = controller.findPlayerData(player.getUniqueId());

        if (playerData != null && playerData.hasData(ClaimSelectionData.class)) {
            final ClaimSelectionData data = playerData.findData(ClaimSelectionData.class);
            final ClaimSelection claimSelection = data.getClaimSelection();
            final Action action = event.getAction();

            switch (action) {
                case LEFT_CLICK_BLOCK: {
                    claimSelection.setLocation1(event.getClickedBlock().getLocation());
                } break;

                case RIGHT_CLICK_BLOCK: {
                    claimSelection.setLocation2(event.getClickedBlock().getLocation());
                } break;

                case RIGHT_CLICK_AIR: {
                    if (player.isSneaking()) {
                        playerData.getData().remove(data);
                        player.sendMessage(ChatColor.GRAY + "You have cancelled your claiming task.");
                    }
                } break;

                case LEFT_CLICK_AIR: {
                    if (player.isSneaking()) {
                        if (claimSelection.getLocation1() == null || claimSelection.getLocation2() == null) {
                            player.sendMessage(ChatColor.GRAY + "One or more positions hasn't been set.");
                            return;
                        }

                        if (!teamController.findTeam(claimSelection.getLocation1()).getType().equals(TeamType.WILDERNESS_TEAM) || !teamController.findTeam(claimSelection.getLocation1()).getType().equals(TeamType.WILDERNESS_TEAM)) {
                            player.sendMessage(ChatColor.GRAY + "The current selection contains non-wilderness regions.");
                        }

                    }

                    final Team team = claimSelection.getTeam();
                    claimSelection.apply();
                    playerData.getData().remove(data);

                    if (team.getType().equals(TeamType.PLAYER_TEAM)) {
                        final PlayerTeamData playerTeamData = team.findData(PlayerTeamData.class);
                        final ClaimTeamData claimTeamData = team.findData(ClaimTeamData.class);

                        playerTeamData.broadcast(ChatColor.GRAY + "Your team now has a claim of " + claimTeamData.getClaim().getCuboid().getChunks() + " chunks.");
                    }
                } break;
            }

            event.setCancelled(true);
        }
    }
}