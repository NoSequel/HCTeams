package io.github.nosequel.hcf.listeners.team;

import io.github.nosequel.hcf.HCTeams;
import io.github.nosequel.hcf.player.PlayerData;
import io.github.nosequel.hcf.player.PlayerDataController;
import io.github.nosequel.hcf.player.data.deathban.DeathbanData;
import io.github.nosequel.hcf.player.data.deathban.impl.PlayerDeathbanData;

import io.github.nosequel.hcf.player.data.deathban.impl.natural.NaturalDeathbanData;
import io.github.nosequel.hcf.player.data.deathban.impl.natural.NaturalDeathbanType;

import io.github.nosequel.hcf.team.Team;
import io.github.nosequel.hcf.team.TeamController;
import io.github.nosequel.hcf.team.data.impl.player.DTRData;
import io.github.nosequel.hcf.team.data.impl.player.PlayerTeamData;
import io.github.nosequel.hcf.util.NumberUtil;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DeathListeners implements Listener {

    private final PlayerDataController controller = HCTeams.getInstance().getHandler().findController(PlayerDataController.class);
    private final TeamController teamController = HCTeams.getInstance().getHandler().findController(TeamController.class);

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        final Player player = event.getEntity();
        final PlayerData playerData = controller.findPlayerData(player.getUniqueId());
        final Team team = teamController.findTeam(player);
        final DeathbanData data;

        if (player.getKiller() != null) {
            data = new PlayerDeathbanData(player.getKiller().getUniqueId(), 30000);
        } else {
            data = new NaturalDeathbanData(NaturalDeathbanType.UNDEFINED, 30000);
        }

        if (teamController.findTeam(player) != null) {
            final PlayerTeamData teamData = team.findData(PlayerTeamData.class);
            final DTRData dtrData = team.findData(DTRData.class);

            teamData.broadcast(ChatColor.RED + "Member Death: " + ChatColor.WHITE + player.getName() + ChatColor.YELLOW + " (" + dtrData.getDtr() + " -> " + NumberUtil.round(dtrData.getDtr()-1.0D, 1) + ")");
            dtrData.setDtr(dtrData.getDtr()-1.0D);
        }

        player.getInventory().clear();
        playerData.addData(data);
        data.kickPlayer(player);
        event.setDeathMessage(null);
    }
}