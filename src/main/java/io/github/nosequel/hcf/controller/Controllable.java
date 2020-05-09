package io.github.nosequel.hcf.controller;

import io.github.nosequel.hcf.HCTeams;

import java.lang.reflect.ParameterizedType;
import java.util.Arrays;

public interface Controllable<T extends Controller> {

    /**
     * Get the controller which controls the object
     *
     * @return the controller
     */
    @SuppressWarnings("unchecked")
    default T getController() {
        final ParameterizedType interfaceClass = (ParameterizedType) Arrays.stream(this.getClass().getGenericInterfaces())
                .filter(type -> type.getTypeName().contains(Controllable.class.getSimpleName()))
                .findFirst().orElse(null);

        if (interfaceClass != null) {
            final Class<T> genericTypeClass = (Class<T>) interfaceClass.getActualTypeArguments()[0];

            return HCTeams.getInstance().getHandler().findController(genericTypeClass);
        }

        throw new IllegalStateException("No interface by name Controllable found.");
    }
}