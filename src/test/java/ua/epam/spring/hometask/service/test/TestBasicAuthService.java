package ua.epam.spring.hometask.service.test;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.Map;

import ua.epam.spring.hometask.domain.User;
import ua.epam.spring.hometask.service.impl.BasicAuthService;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@ContextConfiguration("classpath:test-spring-config.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class TestBasicAuthService {

    @Autowired
    private BasicAuthService authService;

    private User persistenceUser;

    @Before
    public void init() {

        persistenceUser = new User();
        persistenceUser.setId(2L);
        persistenceUser.setEmail("test@email.com");
    }

    @Test
    public void testRegisterUser() {

        authService.registerUser(persistenceUser);

        assertNotNull(String.format("Try to register an %s", persistenceUser), authService.getAllRegisteredUsers().get(persistenceUser.getId()));
    }

    @Test
    public void testIsRegisteredUser() {

        authService.registerUser(persistenceUser);

        assertTrue("Check that an user was registered", authService.isRegisteredUser(persistenceUser));
    }
}
