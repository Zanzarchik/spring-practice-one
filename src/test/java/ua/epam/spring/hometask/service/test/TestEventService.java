package ua.epam.spring.hometask.service.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import ua.epam.spring.hometask.domain.Auditorium;
import ua.epam.spring.hometask.domain.Event;
import ua.epam.spring.hometask.service.impl.CustomEventService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:test-spring-config.xml")
public class TestEventService {

    @Autowired
    private CustomEventService eventService;
    private Event persistenceEvent;
    private Event duplicatePersistenceEvent;
    private Event duplicatePersistenceEventWithDifferentAuditorium;
    private Event transientEvent;

    private final static String FIND_BY_NAME_TEMPLATE_MESSAGE = "Try to find an event with name <%s>";
    private final static String FIND_BY_ID_TEMPLATE_MESSAGE = "Try to find an event with id <%s>";

    @Before
    public void init() {

        LocalDateTime currentDateTime = LocalDateTime.now();
        Auditorium hallOne = new Auditorium();
        hallOne.setName("Hall #1");

        Auditorium hallTwo = new Auditorium();
        hallOne.setName("Hall #2");

        Map<Long, Event> testEvents = new HashMap<>();

        persistenceEvent = new Event("test_event_one");
        persistenceEvent.setId(1L);
        persistenceEvent.addAirDateTime(currentDateTime, hallOne);
        testEvents.put(persistenceEvent.getId(), persistenceEvent);

        transientEvent = new Event("test_event_two");
        transientEvent.setId(2L);
        transientEvent.addAirDateTime(currentDateTime, hallTwo);

        duplicatePersistenceEvent = new Event("test_event_one");
        duplicatePersistenceEvent.setId(3L);
        duplicatePersistenceEvent.addAirDateTime(currentDateTime, hallOne);

        duplicatePersistenceEventWithDifferentAuditorium = new Event("test_event_one");
        duplicatePersistenceEventWithDifferentAuditorium.setId(4L);
        duplicatePersistenceEventWithDifferentAuditorium.addAirDateTime(currentDateTime, hallTwo);

        eventService.setEventStorage(testEvents);
    }

    @Test
    public void testGetEventByName() {

        Event event = eventService.getByName(persistenceEvent.getName());

        assertNotNull(String.format(FIND_BY_NAME_TEMPLATE_MESSAGE, persistenceEvent.getName()), event);
        assertEquals(String.format(FIND_BY_NAME_TEMPLATE_MESSAGE, persistenceEvent.getName()), persistenceEvent.getName(), event.getName());

    }

    @Test
    public void testGetEventById() {

        Event event = eventService.getById(persistenceEvent.getId());

        assertNotNull(String.format(FIND_BY_ID_TEMPLATE_MESSAGE, persistenceEvent.getId()), event);
        assertEquals(String.format(FIND_BY_ID_TEMPLATE_MESSAGE, persistenceEvent.getId()), persistenceEvent.getId(), event.getId());
    }

    @Test
    public void testSaveUniqueEvent() {

        int initialEventCount = eventService.getAll().size();
        eventService.save(transientEvent);

        assertEquals(String.format("Try to save an %s", transientEvent), initialEventCount + 1, eventService.getAll().size());
    }

    @Test
    public void testSaveEvent_whenEqualEvent_andEqualEventDate_andDifferentAuditorium_thenSuccessfullySaveOccurred() {

        int initialEventCount = eventService.getAll().size();
        eventService.save(duplicatePersistenceEventWithDifferentAuditorium);

        assertEquals(String.format("Try to save a duplicate %s with different auditorium", duplicatePersistenceEventWithDifferentAuditorium),
                initialEventCount + 1, eventService.getAll().size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveEvent_whenEqualEvent_andEqualEventDate_andEqualAuditorium_thenExceptionOccurred(){
       eventService.save(duplicatePersistenceEvent);
    }

    @Test
    public void testRemoveEvent() {

        int initialEventCount = eventService.getAll().size();
        eventService.remove(persistenceEvent);

        assertEquals(String.format("Try to remove an %s", persistenceEvent), initialEventCount - 1, eventService.getAll().size());
    }
}
