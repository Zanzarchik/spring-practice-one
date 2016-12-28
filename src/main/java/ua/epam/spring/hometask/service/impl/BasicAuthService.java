package ua.epam.spring.hometask.service.impl;

import java.util.HashMap;
import java.util.Map;

import ua.epam.spring.hometask.domain.User;
import ua.epam.spring.hometask.service.api.AuthService;

public class BasicAuthService implements AuthService {

    private Map<Long, User> registeredUsers = new HashMap<>();
    private Map<Long, String> insecurePasswordMapping = new HashMap<>();

    @Override
    public boolean isRegisteredUser(User user) {
        return registeredUsers.containsKey(user.getId());
    }

    @Override
    public User registerUser(User user) {
        registeredUsers.put(user.getId(), user);
        return user;
    }

    @Override
    public boolean login(User user, String password) {

        String errorMessage = "Invalid username/password pair";

        if (!isRegisteredUser(user)){
            throw new SecurityException(errorMessage);
        }

        String correctPassword = insecurePasswordMapping.get(user.getId());
        if (correctPassword == null || correctPassword.intern() != password.intern()){
            throw new SecurityException(errorMessage);
        }

        return true;
    }

    public Map<Long, User> getAllRegisteredUsers() {
        return registeredUsers;
    }
}
