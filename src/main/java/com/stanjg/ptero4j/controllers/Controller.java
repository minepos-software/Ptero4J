package com.stanjg.ptero4j.controllers;

import com.stanjg.ptero4j.PteroAdminAPI;
import com.stanjg.ptero4j.PteroUserAPI;
import com.stanjg.ptero4j.entities.objects.misc.Logger;
import com.stanjg.ptero4j.util.HTTPMethod;
import com.stanjg.ptero4j.util.PteroUtils;
import okhttp3.*;
import org.json.JSONObject;

import java.io.IOException;

public abstract class Controller {

    private PteroAdminAPI adminAPI;
    private PteroUserAPI userAPI;

    private OkHttpClient client;
    private String baseURL, key;

    private final Logger logger;

    private static final MediaType JSON = MediaType.parse("application/json");

    public Controller(PteroAdminAPI api, String baseURL, String key, Logger logger) {
        this.adminAPI = api;
        this.baseURL = baseURL;
        this.key = key;
        this.logger = logger;

        this.client = new OkHttpClient();
    }

    public Controller(PteroUserAPI api, String baseURL, String key, Logger logger) {
        this.userAPI = api;
        this.baseURL = baseURL;
        this.key = key;
        this.logger = logger;

        this.client = new OkHttpClient();
    }

    protected Response makeApiCall(String endpoint, HTTPMethod method) throws IOException {
        return makeApiCall(endpoint, method, new JSONObject());
    }

    protected Response makeApiCall(String endpoint, HTTPMethod method, JSONObject data) throws IOException {
        Response response = null;

        try {

            switch (method) {

                case GET:
                    Request.Builder getBuilder = new Request.Builder()
                            .url(baseURL + endpoint);
                    addHeaders(getBuilder);

                    response = client.newCall(getBuilder.build()).execute();
                    response.body().close();
                    return response;

                case POST:
                    RequestBody postBody = RequestBody.create(JSON, data.toString());

                    Request.Builder postBuilder = new Request.Builder()
                            .url(baseURL + endpoint)
                            .post(postBody);
                    addHeaders(postBuilder);

                    response = client.newCall(postBuilder.build()).execute();
                    response.body().close();
                    return response;

                case PUT:
                    RequestBody putBody = RequestBody.create(JSON, data.toString());

                    Request.Builder putBuilder = new Request.Builder()
                            .url(baseURL + endpoint)
                            .put(putBody);
                    addHeaders(putBuilder);

                    response = client.newCall(putBuilder.build()).execute();
                    response.body().close();
                    return response;

                case PATCH:
                    RequestBody patchBody = RequestBody.create(JSON, data.toString());

                    Request.Builder patchBuilder = new Request.Builder()
                            .url(baseURL + endpoint)
                            .patch(patchBody);
                    addHeaders(patchBuilder);

                    response = client.newCall(patchBuilder.build()).execute();
                    response.body().close();
                    return response;

                case DELETE:
                    Request.Builder deleteBuilder = new Request.Builder()
                            .url(baseURL + endpoint)
                            .delete();
                    addHeaders(deleteBuilder);

                    response = client.newCall(deleteBuilder.build()).execute();
                    response.body().close();
                    return response;
            }

        } catch (Exception exc) {
            exc.printStackTrace();
        }

        throw new RuntimeException("Invalid Method");
    }

    private void addHeaders(Request.Builder request) {
        request.addHeader("Authorization", key)
                .addHeader("Accept", "application/vnd.pterodactyl.v1+json")
                .addHeader("User-Agent", "Ptero4J/v0.1");
    }

    protected PteroAdminAPI getAdminAPI() {
        return adminAPI;
    }

    protected PteroUserAPI getUserAPI() {
        return userAPI;
    }

    protected Logger getLogger() {
        return logger;
    }

    protected void logError(Response response) {
        getLogger().error(PteroUtils.getErrorMessage(response));
    }
}
