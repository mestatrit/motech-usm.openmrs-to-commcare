package org.motechproject.openmrs.ws.resource;

import org.motechproject.openmrs.ws.resource.model.RoleListResult;
import org.motechproject.openmrs.ws.resource.model.User;
import org.motechproject.openmrs.ws.resource.model.UserListResult;

public interface UserResource {

    UserListResult getAllUsers();

    UserListResult queryForUsersByUsername(String username);

    User createUser(User user);

    void updateUser(User user);

    RoleListResult getAllRoles();
}
