package io.github.nosequel.hcf.team.data.impl.player;

public enum PlayerRole {

    MEMBER(1),
    CAPTAIN(2),
    CO_LEADER(3),
    LEADER(4);

    public int priority;

    PlayerRole(int priority) {
        this.priority = priority;
    }

    public boolean isHigher(PlayerRole role) {
        return priority > role.priority;
    }

}
