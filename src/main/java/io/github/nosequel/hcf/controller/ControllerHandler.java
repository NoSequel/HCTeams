package io.github.nosequel.hcf.controller;

import java.util.*;

public class ControllerHandler {

    private final Set<Controller> controllers = new HashSet<>();

    /**
     * Find a {@link Controller} by a {@link Class}
     *
     * @param clazz the class
     * @param <T>   the type of the controller
     * @return the controller
     */
    public <T extends Controller> T findController(Class<T> clazz) {
        return clazz.cast(this.controllers.stream()
                .filter(controller -> controller.getClass().equals(clazz))
                .findFirst().orElse(null));
    }

    /**
     * Register a new controller
     *
     * @param controller the controller
     * @param <T>        the type of the controller
     * @return the registered controller | or the previously registered controller.
     */
    @SuppressWarnings("unchecked")
    public <T extends Controller> T registerController(Controller controller) {
        if (this.controllers.add(controller)) {
            controller.enable();
            return (T) controller;
        }

        return (T) this.findController(controller.getClass());
    }
}