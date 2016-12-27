package ua.epam.spring.hometask.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.SortedMap;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import ua.epam.spring.hometask.domain.Auditorium;
import ua.epam.spring.hometask.domain.Event;
import ua.epam.spring.hometask.service.api.EventService;

public class CustomEventService implements EventService {

    private Map<Long, Event> eventStorage = new HashMap<>();

    public void setEventStorage(Map<Long, Event> eventStorage) {
        this.eventStorage = eventStorage;
    }

    @Nullable
    @Override
    public Event getByName(@Nonnull String name) {
        Optional<Event> events = eventStorage.entrySet().stream()
                .map(Map.Entry::getValue)
                .filter(e -> e.getName().equals(name))
                .findFirst();
        return events.orElse(null);
    }

    @Override
    public Event save(@Nonnull Event event) throws IllegalArgumentException {
        if (hasSameEvent(event)) {
            throw new IllegalArgumentException(String.format("Try to save an %s but the event with same datetime and same auditorium already exists", event));
        }
        eventStorage.put(event.getId(), event);
        return event;
    }

    private boolean hasSameEvent(Event event) {

        List<Event> events = eventStorage.entrySet().stream()
                .filter(entry -> entry.getValue().equals(event))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());

        return events.size() != 0 && events.stream()
                .map(Event::getAuditoriums)
                .filter(entry -> event.getAuditoriums().entrySet().stream()
                        .filter(e -> entry.containsKey(e.getKey())
                                && entry.get(e.getKey()).equals(e.getValue()))
                        .count() > 0)
                .count() > 0;
    }

    @Override
    public void remove(@Nonnull Event object) {
        eventStorage.remove(object.getId());
    }

    @Override
    public Event getById(@Nonnull Long id) {
        return eventStorage.get(id);
    }

    @Nonnull
    @Override
    public Collection<Event> getAll() {
        List<Event> events = new ArrayList<>(eventStorage.values());
        return events;
    }
}
