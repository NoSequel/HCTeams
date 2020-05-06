package io.github.nosequel.hcf.data;

import java.util.List;

public interface Datable<T extends Data> {

    /**
     * Get the registered data classes
     *
     * @return the registered data classes
     */
    List<Class<T>> getRegisteredData();

}