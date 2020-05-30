package io.github.nosequel.hcf.scoreboard.provider.impl;

import io.github.nosequel.hcf.HCTeams;
import io.github.nosequel.hcf.scoreboard.provider.BoardProvider;
import io.github.nosequel.hcf.timers.TimerController;
import io.github.nosequel.hcf.util.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class TimerBoardProvider implements BoardProvider {

    private final TimerController timerController = HCTeams.getInstance().getHandler().findController(TimerController.class);

    @Override
    public List<String> getStrings(Player player) {
        return timerController.getTimers().stream()
                .filter(timer -> timer.isOnCooldown(player))
                .map(timer -> timer.getScoreboardTag() + ChatColor.GRAY + ": " + ChatColor.RED + StringUtils.getFormattedTime(timer.getDuration(player), timer.isTrailing()))
                .collect(Collectors.toList());
    }
}
