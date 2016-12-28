package ua.epam.spring.hometask.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import ua.epam.spring.hometask.domain.User;
import ua.epam.spring.hometask.service.api.UserService;

public class CustomUserService implements UserService {

    private Map<Long, User> userStorage = new HashMap<>();

    public void setUserStorage(Map<Long, User> userStorage) {
        this.userStorage = userStorage;
    }

    @Nullable
    @Override
    public User getUserByEmail(@Nonnull String email) {

        Optional<User> user = userStorage.entrySet().stream()
                .map(Map.Entry::getValue)
                .filter(u -> u.getEmail().equals(email)).findFirst();
        return user.orElse(null);
    }

    @Override
    public User save(@Nonnull User object) {
        userStorage.put(object.getId(), object);
        return object;
    }

    @Override
    public void remove(@Nonnull User object) {
        userStorage.remove(object.getId());
    }

    @Override
    public User getById(@Nonnull Long id) {
        return userStorage.get(id);
    }

    @Nonnull
    @Override
    public Collection<User> getAll() {
        List<User> users = new ArrayList<>(userStorage.values());
        return users;
    }
}
