package ua.epam.spring.hometask.service.api;

import ua.epam.spring.hometask.domain.User;

public interface AuthService {

    boolean isRegisteredUser(User user);
    User registerUser(User user);
    boolean login(User user, String password);
}
