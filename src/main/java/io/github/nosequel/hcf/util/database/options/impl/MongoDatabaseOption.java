package io.github.nosequel.hcf.util.database.options.impl;

import io.github.nosequel.hcf.util.database.options.DatabaseOption;
import lombok.Getter;

@Getter
public class MongoDatabaseOption extends DatabaseOption {

    private final String hostname;
    private final int port;

    private String username;
    private String password;
    private String authenticateDatabase;
    private boolean authenticate;

    /**
     * Constructor for creating a new MongoDatabaseOption
     *
     * @param hostname               the ip address
     * @param username               the username
     * @param password               the passsword
     * @param authenticationDatabase the database to authenticate with
     * @param port                   the port
     */
    public MongoDatabaseOption(String hostname, String username, String password, String authenticationDatabase, int port) {
        this.hostname = hostname;
        this.port = port;
        this.username = username;
        this.authenticateDatabase = authenticationDatabase;

        if(!password.isEmpty()) {
            this.password = password;
            this.authenticate = true;
        }
    }

    @Override
    public Object[] getOptions() {
        return new Object[] { hostname, username, password, authenticate, port };
    }
}