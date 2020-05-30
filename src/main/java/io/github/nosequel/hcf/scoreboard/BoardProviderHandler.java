package io.github.nosequel.hcf.scoreboard;

import io.github.nosequel.hcf.scoreboard.provider.impl.TimerBoardProvider;
import io.github.nosequel.hcf.scoreboard.provider.BoardProvider;
import io.github.thatkawaiisam.assemble.AssembleAdapter;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BoardProviderHandler implements AssembleAdapter {

    private final List<BoardProvider> providers = new ArrayList<>(Arrays.asList(
            new TimerBoardProvider()
    ));

    @Override
    public String getTitle(Player player) {
        return ChatColor.DARK_PURPLE + "Vesta" + ChatColor.GRAY + ChatColor.BOLD + " ｜ " + ChatColor.WHITE + "Squads";
    }

    @Override
    public List<String> getLines(Player player) {
        final List<String> strings = new ArrayList<>();

        providers.forEach(provider -> strings.addAll(provider.getStrings(player)));

        if (!strings.isEmpty()) {
            strings.add(0, "&7&m-------------------");
            strings.add("");
            strings.add(ChatColor.GRAY + ChatColor.ITALIC.toString() + "vesta.rip");
            strings.add("&7&m-------------------");
        }

        return strings;
    }
}