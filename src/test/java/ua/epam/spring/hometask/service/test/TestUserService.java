package ua.epam.spring.hometask.service.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.Map;

import ua.epam.spring.hometask.domain.User;
import ua.epam.spring.hometask.service.api.UserService;
import ua.epam.spring.hometask.service.impl.CustomUserService;

@ContextConfiguration("classpath:test-spring-config.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class TestUserService {

    @Autowired
    private CustomUserService userService;
    private User persistenceUser;
    private User temporaryUser;

    @Before
    public void init() {

        temporaryUser = new User();
        temporaryUser.setId(1L);
        temporaryUser.setEmail("test_temporary@email.com");

        persistenceUser = new User();
        persistenceUser.setId(2L);
        persistenceUser.setEmail("test@email.com");

        Map<Long, User> users = new HashMap<>();
        users.put(persistenceUser.getId(), persistenceUser);
        userService.setUserStorage(users);
    }

    @Test
    public void testGetAllUser() {

        int actualSize = userService.getAll().size();
        assertEquals("Getting all users", 1, actualSize);
    }

    @Test
    public void testSaveUser() {

        int initialSize = userService.getAll().size();
        userService.save(temporaryUser);

        assertEquals("Try to save an " + temporaryUser, initialSize + 1, userService.getAll().size());
    }

    @Test
    public void testRemoveUser() {

        int initialSize = userService.getAll().size();
        userService.remove(persistenceUser);

        assertEquals("Try to remove an " + persistenceUser, initialSize - 1, userService.getAll().size());
    }

    @Test
    public void testGetUserByEmail() {

        User user = userService.getUserByEmail(persistenceUser.getEmail());

        assertNotNull("Try to find by email an ", user);
        assertEquals("Try to find by email an ", persistenceUser.getEmail(), user.getEmail());

    }

    @Test
    public void testGetUserById() {

        User user = userService.getById(2L);

        assertNotNull("Try to find by id an ", user);
        assertEquals("Try to find by id an ", persistenceUser.getId(), user.getId());
    }

    @Test
    public void testRegisterUser() {

        userService.registerUser(persistenceUser);

        assertNotNull(String.format("Try to register an %s", persistenceUser), userService.getAllRegisteredUsers().get(persistenceUser.getId()));
    }

    @Test
    public void testIsRegisteredUser() {

        userService.registerUser(persistenceUser);

        assertTrue("Check that an user was registered", userService.isRegisteredUser(persistenceUser));
    }
}
