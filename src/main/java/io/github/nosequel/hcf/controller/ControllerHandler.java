package io.github.nosequel.hcf.controller;

import lombok.Getter;

import java.util.*;

@Getter
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

    /**
     * Unregister a controller
     *
     * @param controller the controller to get unregistered
     */
    public void unregisterController(Class<? extends Controller> controller) {
        if (this.findController(controller) != null) {
            this.controllers.remove(this.findController(controller));
        }

        System.out.println("Tried unregistering controller which isn't registered");
    }
}