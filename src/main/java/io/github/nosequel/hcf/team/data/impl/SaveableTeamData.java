package io.github.nosequel.hcf.team.data.impl;

import io.github.nosequel.hcf.controller.Controllable;
import io.github.nosequel.hcf.data.impl.SaveableData;
import io.github.nosequel.hcf.team.TeamController;
import io.github.nosequel.hcf.team.data.TeamData;

public interface SaveableTeamData extends TeamData, SaveableData, Controllable<TeamController> {

    default Class<TeamController> getType() {
        return TeamController.class;
    }
}
