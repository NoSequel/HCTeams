package io.github.nosequel.hcf.controller;

import io.github.nosequel.hcf.HCTeams;

public interface Controllable<T extends Controller> {

    /**
     * Get the controller which controls the object
     *
     * @return the controller
     */
    @SuppressWarnings("unchecked")
    default T getController() {
        final Class<T> genericTypeClass = (Class<T>) this.getClass().getTypeParameters()[0].getGenericDeclaration();
        return HCTeams.getInstance().getHandler().findController(genericTypeClass);
    }
}