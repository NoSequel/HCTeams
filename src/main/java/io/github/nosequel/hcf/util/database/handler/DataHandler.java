package io.github.nosequel.hcf.util.database.handler;

import io.github.nosequel.hcf.data.DataController;
import io.github.nosequel.hcf.data.Loadable;

public interface DataHandler {

    /**
     * Save a Loadable inside of a Collection
     *
     * @param loadable   the loadable
     * @param collection the collection
     */
    void save(Loadable<?> loadable, String collection);

    /**
     * Load a DataType from an object
     *
     * @param controller the controller
     * @param loadable   the loadable
     */
    void load(DataController<?, ?> controller, Loadable<?> loadable, String collectionName);

    /**
     * Load all documents from a collection
     *
     * @param controller     the controller
     * @param collectionName the collection
     */
    void loadAll(DataController<?, ?> controller, String collectionName, Class<? extends Loadable<?>> loadableType);

}
