package io.github.nosequel.hcf.util.database;

import io.github.nosequel.hcf.controller.Controller;
import io.github.nosequel.hcf.util.database.handler.DataHandler;
import io.github.nosequel.hcf.util.database.options.DatabaseOption;
import io.github.nosequel.hcf.util.database.type.DataType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DatabaseController implements Controller {

    private final DataType<?, ?> type;
    private final DatabaseOption option;

    private DataHandler dataHandler;

    /**
     * Constructor for creating a new DatabaseController
     *
     * @param option      the options of the database
     * @param type        the type of the data
     */
    public DatabaseController(DatabaseOption option, DataType<?, ?> type) {
        this.type = type;
        this.option = option;
    }
}