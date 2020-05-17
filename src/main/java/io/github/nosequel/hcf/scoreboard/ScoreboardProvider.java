package io.github.nosequel.hcf.scoreboard;

import io.github.nosequel.hcf.HCTeams;
import io.github.nosequel.hcf.timers.TimerController;
import io.github.nosequel.hcf.util.StringUtils;
import io.github.thatkawaiisam.assemble.AssembleAdapter;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ScoreboardProvider implements AssembleAdapter {

    private final TimerController timerController = HCTeams.getInstance().getHandler().findController(TimerController.class);

    @Override
    public String getTitle(Player player) {
        return ChatColor.GOLD + ChatColor.BOLD.toString() + "Squads" + ChatColor.RED + " [Map 1]";
    }

    @Override
    public List<String> getLines(Player player) {
        final List<String> strings = new ArrayList<>();

        timerController.getTimers().stream()
                .filter(timer -> timer.isOnCooldown(player))
                .map(timer -> timer.getScoreboardTag() + ChatColor.GRAY + ": " + ChatColor.RED + StringUtils.getFormattedTime(timer.getDuration(player), timer.isTrailing()))
                .forEach(strings::add);

        if (!strings.isEmpty()) {
            strings.add(0, "&7&m-------------------");
            strings.add("");
            strings.add(ChatColor.GRAY + ChatColor.ITALIC.toString() + "vesta.rip");
            strings.add("&7&m-------------------");
        }

        return strings;
    }
}