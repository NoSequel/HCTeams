package io.github.nosequel.hcf.tasks.impl;

import io.github.nosequel.hcf.HCTeams;
import io.github.nosequel.hcf.tasks.Task;
import io.github.nosequel.hcf.team.TeamController;
import io.github.nosequel.hcf.team.data.impl.player.DTRData;

public class DTRTask extends Task {

    private final TeamController teamController = HCTeams.getInstance().getHandler().findController(TeamController.class);

    public DTRTask() {
        super(0);
    }

    @Override
    public void tick() throws Exception {
        teamController.getTeams().stream()
                .filter(team -> team.hasData(DTRData.class))
                .map(team -> team.findData(DTRData.class))
                .filter(data -> data.getDtr() != data.getMaxDtr() && System.currentTimeMillis() - data.getLastRegen() >= 150000)
                .forEach(data -> {
                    data.setLastRegen(System.currentTimeMillis());
                    data.setDtr(data.getDtr() + 0.1);
                });
    }

    @Override
    public String getName() {
        return "DTR";
    }
}