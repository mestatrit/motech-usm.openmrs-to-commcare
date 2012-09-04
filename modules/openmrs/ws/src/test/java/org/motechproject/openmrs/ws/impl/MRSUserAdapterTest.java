package org.motechproject.openmrs.ws.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.motechproject.mrs.exception.UserAlreadyExistsException;
import org.motechproject.mrs.model.MRSPerson;
import org.motechproject.mrs.model.MRSUser;
import org.motechproject.mrs.services.MRSUserAdapter;
import org.motechproject.openmrs.ws.HttpException;
import org.motechproject.openmrs.ws.resource.UserResource;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.google.gson.JsonElement;

public class MRSUserAdapterTest {

    @Mock
    private MRSPersonAdapterImpl personAdapter;

    @Mock
    private UserResource userResource;

    private MRSUserAdapterImpl impl;

    @Before
    public void setUp() {
        initMocks(this);
        impl = new MRSUserAdapterImpl(userResource, personAdapter);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void shouldThrowUnsupportedExceptionOnChangeCurrentPassword() {
        impl.changeCurrentUserPassword(null, null);
    }

//    @Test
//    public void shouldFilterAdminDaemonFromAllUsersList() throws IOException, HttpException {
//        String json = TestUtils.parseJsonFileAsString("json/user-list-full-response.json");
//
//        when(client.getJson(null)).thenReturn(json);
//
//        List<MRSUser> users = impl.getAllUsers();
//
//        assertEquals(1, users.size());
//    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionOnNullUsername() {
        impl.getUserByUserName(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionOnEmptyUsername() {
        impl.getUserByUserName("");
    }

    private MRSUser makeExpectedUser() {
        return new MRSUser().id("UUU").userName("test_user").systemId("3-4").person(null).securityRole("Provider");
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionOnNullUser() throws UserAlreadyExistsException {
        impl.saveUser(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionOnNullUsernameOnSave() throws UserAlreadyExistsException {
        impl.saveUser(new MRSUser().userName(null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenSettingPasswordWithNullUsername() {
        impl.setNewPasswordForUser(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenSettingPasswordWithEmptyUsername() {
        impl.setNewPasswordForUser("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionOnUpdateWithNullUser() {
        impl.updateUser(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionOnUpdateWithUserNullId() {
        impl.updateUser(new MRSUser().id(null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionOnUpdateWithUserEmptyId() {
        impl.updateUser(new MRSUser().id(""));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionOnUpdateWithPersonNullId() {
        MRSPerson person = new MRSPerson().id(null);
        impl.updateUser(new MRSUser().id("a").person(person));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionOnUpdateWithPersonEmptyId() {
        MRSPerson person = new MRSPerson().id("");
        impl.updateUser(new MRSUser().id("a").person(person));
    }
}
