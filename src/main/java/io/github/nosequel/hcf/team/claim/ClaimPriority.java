package io.github.nosequel.hcf.team.claim;

public enum ClaimPriority {

    LOW(1),
    NORMAL(2),
    HIGH(3);

    private final int priority;

    /**
     * Constructor for creating a new ClaimPriority enumeration with a priority.
     *
     * @param priority
     */
    ClaimPriority(int priority) {
        this.priority = priority;
    }

    /**
     * Check if the paramaterized ClaimPriority is higher than the concurrent one
     *
     * @param priority the priority
     * @return whether it's higher or not
     */
    public boolean isHigherPriority(ClaimPriority priority) {
        return priority.priority > this.priority;
    }
}
