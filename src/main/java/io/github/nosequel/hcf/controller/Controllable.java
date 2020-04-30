package io.github.nosequel.hcf.controller;

import io.github.nosequel.hcf.HCTeams;

public interface Controllable<T extends Controller> {

    /**
     * Get the controller which controls the object
     *
     * @return the controller
     */
    default T getController() {
        return HCTeams.getInstance().getHandler().findController(this.getType());
    }

    /**
     * Get the type of the controller
     *
     * @return the type
     */
    Class<T> getType();

}
