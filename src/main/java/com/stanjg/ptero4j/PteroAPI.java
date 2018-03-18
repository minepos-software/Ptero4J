package com.stanjg.ptero4j;

import com.stanjg.ptero4j.controllers.ServersController;
import com.stanjg.ptero4j.controllers.TestController;
import com.stanjg.ptero4j.controllers.UsersController;

import java.io.IOException;

public class PteroAPI {

    private static PteroAPI instance;
    public static PteroAPI getInstance() {
        return instance;
    }

    private String baseURL, key;

    private UsersController usersController;
    private ServersController serversController;

    public PteroAPI(String baseURL, String key) {
        if (instance != null)
            return;

        instance = this;

        this.baseURL = baseURL.endsWith("/") ? baseURL  + "api/application" : baseURL + "/api/application";
        this.key = "Bearer " + key;

        try {
            new TestController(this.baseURL, this.key).testConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.usersController = new UsersController(this.baseURL, this.key);
        this.serversController = new ServersController(this.baseURL, this.key);
    }

    public UsersController getUsersController() {
        return this.usersController;
    }

    public ServersController getServersController() {
        return serversController;
    }
}
