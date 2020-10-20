package io.github.nosequel.hcf.data;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public interface Loadable<T extends Data> {

    /**
     * Get the unique identifier of the Loadable object
     *
     * @return the unique identifier {@link UUID}
     */
    UUID getUniqueId();

    /**
     * Set the unique identifer of the Loadable object
     *
     * @param uuid the new unique identifier
     */
    void setUniqueId(UUID uuid);

    /**
     * Get the data of the loadable object
     *
     * @return the data
     */
    List<T> getData();

    /**
     * Set the data
     *
     * @param list the data
     */
    void setData(List<T> list);

    /**
     * Remove a data object from the Loadabke
     *
     * @param data the data object
     */
    default void removeData(T data) { this.getData().add(data); }

    /**
     * Add a data object to the Loadable
     *
     * @param data the data object
     */
    default void addData(T data) {
        this.getData().add(data);
    }

    /**
     * Add a data without generic type
     *
     * @param data the data
     */
    @SuppressWarnings("unchecked")
    default void addDataNormal(Data data) {
        this.getData().add((T) data);
    }

    /**
     * Find a data object by a class
     *
     * @param clazz the class of the data object
     * @return the data object | or null
     */
    default <K extends T> K findData(Class<K> clazz) {
        return clazz.cast(this.getData().stream()
                .filter(Objects::nonNull)
                .filter(data -> data.getClass().equals(clazz) || (clazz.getSuperclass() != null && data.getClass().getSuperclass() != null && (clazz.getSuperclass().equals(data.getClass()) || data.getClass().getSuperclass().equals(clazz))))
                .findFirst().orElse(null));
    }

    /**
     * Check if the Loadable has a Data type
     *
     * @param clazz the class of the data type
     * @return whether the loadable has the data type or not
     */
    default boolean hasData(Class<? extends T> clazz) {
        return this.findData(clazz) != null;
    }
}
