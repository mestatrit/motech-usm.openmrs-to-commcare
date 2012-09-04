package org.motechproject.openmrs.ws.resource.impl;

import org.apache.log4j.Logger;
import org.motechproject.mrs.exception.MRSException;
import org.motechproject.openmrs.ws.HttpException;
import org.motechproject.openmrs.ws.OpenMrsInstance;
import org.motechproject.openmrs.ws.RestClient;
import org.motechproject.openmrs.ws.resource.UserResource;
import org.motechproject.openmrs.ws.resource.model.Person;
import org.motechproject.openmrs.ws.resource.model.Person.PersonSerializer;
import org.motechproject.openmrs.ws.resource.model.Role;
import org.motechproject.openmrs.ws.resource.model.Role.RoleSerializer;
import org.motechproject.openmrs.ws.resource.model.RoleListResult;
import org.motechproject.openmrs.ws.resource.model.User;
import org.motechproject.openmrs.ws.resource.model.UserListResult;
import org.motechproject.openmrs.ws.util.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Component
public class UserResourceImpl implements UserResource {
    private static final Logger LOGGER = Logger.getLogger(UserResourceImpl.class);

    private final RestClient restClient;
    private final OpenMrsInstance openmrsInstance;

    @Autowired
    public UserResourceImpl(RestClient restClient, OpenMrsInstance openmrsInstace) {
        this.restClient = restClient;
        this.openmrsInstance = openmrsInstace;
    }

    @Override
    public UserListResult getAllUsers() {
        String responseJson = null;

        try {
            responseJson = restClient.getJson(openmrsInstance.toInstancePath("/user?v=full"));
        } catch (HttpException e) {
            LOGGER.error("Failed to get all users from OpenMRS: " + e.getMessage());
            throw new MRSException(e);
        }
        UserListResult result = (UserListResult) JsonUtils.readJson(responseJson, UserListResult.class);

        return result;
    }

    @Override
    public UserListResult queryForUsersByUsername(String username) {
        String responseJson = null;
        try {
            responseJson = restClient.getJson(openmrsInstance.toInstancePathWithParams("/user?q={username}&v=full",
                    username));
        } catch (HttpException e) {
            LOGGER.error("Failed to retrieve user by username: " + username + " with error: " + e.getMessage());
            throw new MRSException(e);
        }

        UserListResult results = (UserListResult) JsonUtils.readJson(responseJson, UserListResult.class);
        return results;
    }

    @Override
    public User createUser(User user) {
        Gson gson = getGsonWithAdapters();

        String responseJson = null;
        try {
            responseJson = restClient.postForJson(openmrsInstance.toInstancePath("/user"), gson.toJson(user));
        } catch (HttpException e) {
            LOGGER.error("Failed to save user: " + e.getMessage());
            throw new MRSException(e);
        }

        User saved = (User) JsonUtils.readJson(responseJson, User.class);
        return saved;
    }

    private Gson getGsonWithAdapters() {
        Gson gson = new GsonBuilder().registerTypeAdapter(Person.class, new PersonSerializer())
                .registerTypeAdapter(Role.class, new RoleSerializer()).create();
        return gson;
    }

    @Override
    public RoleListResult getAllRoles() {
        String responseJson = null;
        try {
            responseJson = restClient.getJson(openmrsInstance.toInstancePath("/role?v=full"));
        } catch (HttpException e) {
            LOGGER.error("Failed to retrieve the list of roles: " + e.getMessage());
            throw new MRSException(e);
        }
        RoleListResult result = (RoleListResult) JsonUtils.readJson(responseJson, RoleListResult.class);
        return result;
    }

    @Override
    public void updateUser(User user) {
        Gson gson = getGsonWithAdapters();
        String requestJson = gson.toJson(user);

        try {
            restClient.postWithEmptyResponseBody(
                    openmrsInstance.toInstancePathWithParams("/user/{uuid}", user.getUuid()), requestJson);
        } catch (HttpException e) {
            LOGGER.error("Failed to update user with username: " + user.getUsername());
            throw new MRSException(e);
        }
    }

}
