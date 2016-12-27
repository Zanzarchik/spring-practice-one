package ua.epam.spring.hometask.service.test;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import ua.epam.spring.hometask.domain.Auditorium;
import ua.epam.spring.hometask.domain.Event;
import ua.epam.spring.hometask.domain.EventRating;
import ua.epam.spring.hometask.domain.Ticket;
import ua.epam.spring.hometask.domain.User;
import ua.epam.spring.hometask.service.api.DiscountService;
import ua.epam.spring.hometask.service.api.UserService;
import ua.epam.spring.hometask.service.impl.CustomBookingService;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@ContextConfiguration("classpath:test-spring-config.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class TestBookingService {

    private static final byte DISCOUNT = (byte)50;
    private static final double VIP_RATIO = 2;
    private static final double BASE_PRICE = 100;
    private static final double HIGH_RATING_INDEX = 2;
    private static final LocalDateTime CURRENT_DATA = LocalDateTime.now();

    @Autowired
    @InjectMocks
    private CustomBookingService bookingService;

    @Mock
    private UserService userService;
    @Mock
    private DiscountService discountService;

    private Map<String, User> users;
    private Event testEvent;
    private LocalDateTime airData;
    private Set<Long> vipSeats;
    private Auditorium auditorium;

    @Before
    public void init() {

        createUsers();
        createVipSeats();
        createAuditoriums();
        createEvents();
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testBookTicketsForRegisteredUser() {

        User user = users.get("registeredUser");
        NavigableSet<Ticket> tickets = createTickets(user, 1);
        when(userService.isRegisteredUser(user)).thenReturn(true);

        bookingService.bookTickets(tickets);

        assertEquals("Try to book tickets for registered user", tickets.size(), bookingService.getTickets(user.getId()).size());
    }

    @Test
    public void testBookTicketsForAnonymousUser() {

        User user = users.get("anonymousUser");
        NavigableSet<Ticket> tickets = createTickets(user, 1);
        when(userService.isRegisteredUser(user)).thenReturn(false);

        bookingService.bookTickets(tickets);

        assertEquals("Try to book tickets for registered user", 0, bookingService.getTickets(user.getId()).size());
    }

    @Test
    public void if_BaseSeats_and_NoDiscount_and_LowRating_then_ReturnBasePrice() {

        testEvent.setRating(EventRating.LOW);
        User user = users.get("anonymousUser");
        when(userService.isRegisteredUser(user)).thenReturn(false);

        Set<Long> seats = new HashSet<>();
        seats.add(5L);
        seats.add(6L);

        double actualPrice = bookingService.getTicketsPrice(testEvent, airData, user, seats, auditorium);

        assertEquals("Get a ticket price when an user buys a ticket on base seats for an event with a base rating and without a discount",
                BASE_PRICE * seats.size(),
                actualPrice, 0);
    }

    @Test
    public void if_VipSeats_andNoDiscount_and_LowRating_then_ReturnVipPrice(){

        testEvent.setRating(EventRating.LOW);
        User user = users.get("anonymousUser");
        when(userService.isRegisteredUser(user)).thenReturn(false);

        Set<Long> seats = new HashSet<>();
        seats.add(1L);
        seats.add(2L);

        double actualPrice = bookingService.getTicketsPrice(testEvent, airData, user, seats, auditorium);

        assertEquals("Get a ticket price when an user buys a ticket on VIP seats for an event with a base rating and without a discount",
                BASE_PRICE * VIP_RATIO * seats.size(),
                actualPrice, 0);
    }

    @Test
    public void if_VipSeats_and_BaseSeats_and_NoDiscount_and_LowRating_then_ReturnSumVipBasePrice(){


        testEvent.setRating(EventRating.LOW);
        User user = users.get("anonymousUser");
        when(userService.isRegisteredUser(user)).thenReturn(false);

        Set<Long> seats = new HashSet<>();
        seats.add(1L);
        seats.add(6L);

        double actualPrice = bookingService.getTicketsPrice(testEvent, airData, user, seats, auditorium);

        assertEquals("Get a ticket price when an user buys a ticket on VIP and base seats for an event with a base rating and without a discount",
                BASE_PRICE * VIP_RATIO + BASE_PRICE,
                actualPrice, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void if_NoSeats_then_ThrowException(){

        testEvent.setRating(EventRating.LOW);
        User user = users.get("anonymousUser");
        when(userService.isRegisteredUser(user)).thenReturn(false);

        Set<Long> seats = new HashSet<>();
        bookingService.getTicketsPrice(testEvent, airData, user, seats, auditorium);
    }

    @Test
    public void if_BaseSeats_and_HasDiscount_and_LowRating_then_ReturnPriceWithDiscount(){

        testEvent.setRating(EventRating.LOW);
        User user = users.get("anonymousUser");
        when(userService.isRegisteredUser(user)).thenReturn(false);

        Set<Long> seats = new HashSet<>();
        seats.add(5L);
        seats.add(6L);
        when(discountService.getDiscount(user, testEvent, airData, seats.size())).thenReturn(DISCOUNT);

        double actualPrice = bookingService.getTicketsPrice(testEvent, airData, user, seats, auditorium);
        double expectedPrice = BASE_PRICE * seats.size() - (BASE_PRICE * seats.size() * DISCOUNT / 100);

        assertEquals("Get a ticket price when an user buys a ticket on base seats for an event with a base rating and with a discount",
                expectedPrice,
                actualPrice, 0);
    }

    @Test
    public void if_BaseSeats_and_NoDiscount_and_HighRating_then_ReturnRatingPrice(){

        testEvent.setRating(EventRating.HIGH);
        User user = users.get("anonymousUser");
        when(userService.isRegisteredUser(user)).thenReturn(false);

        Set<Long> seats = new HashSet<>();
        seats.add(5L);
        seats.add(6L);
        when(discountService.getDiscount(user, testEvent, airData, seats.size())).thenReturn((byte)0);

        double actualPrice = bookingService.getTicketsPrice(testEvent, airData, user, seats, auditorium);

        assertEquals("Get a ticket price when an user buys a ticket on base seats for an event with a high rating and without discount",
                BASE_PRICE * seats.size() * HIGH_RATING_INDEX,
                actualPrice, 0);
    }

    @Test
    public void if_VipSeats_and_HasDiscount_and_HighRating_then_ReturnRatingVipPriceWithDiscount(){

        testEvent.setRating(EventRating.HIGH);
        User user = users.get("anonymousUser");
        when(userService.isRegisteredUser(user)).thenReturn(false);

        Set<Long> seats = new HashSet<>();
        seats.add(5L);
        seats.add(6L);
        when(discountService.getDiscount(user, testEvent, airData, seats.size())).thenReturn(DISCOUNT);

        double actualPrice = bookingService.getTicketsPrice(testEvent, airData, user, seats, auditorium);
        double expectedPrice = HIGH_RATING_INDEX * BASE_PRICE * seats.size() - (HIGH_RATING_INDEX * BASE_PRICE * seats.size() * DISCOUNT / 100);

        assertEquals("Get a ticket price when an user buys a ticket on vip seats for an event with a high rating and with a discount",
                expectedPrice,
                actualPrice, 0);
    }

    private void createUsers() {

        users = new HashMap<>();

        User registeredUser = new User();
        registeredUser.setId(1L);
        registeredUser.setFirstName("registeredUser");
        users.put("registeredUser", registeredUser);

        User anonymousUser = new User();
        anonymousUser.setId(2L);
        anonymousUser.setFirstName("anonymousUser");
        users.put("anonymousUser", anonymousUser);
    }

    private void createVipSeats() {

        vipSeats = new HashSet<>();
        vipSeats.add(1L);
        vipSeats.add(2L);
        vipSeats.add(3L);
        vipSeats.add(4L);
    }

    private void createAuditoriums() {

        auditorium = new Auditorium();
        auditorium.setName("Green Hall");
        auditorium.setNumberOfSeats(8);
        auditorium.setVipSeats(vipSeats);
        auditorium.setVipSeatRatio(VIP_RATIO);
    }

    private void createEvents() {

        airData = CURRENT_DATA.minus(4L, ChronoUnit.DAYS);
        NavigableSet<LocalDateTime> eventAirData = new TreeSet<>();
        eventAirData.add(airData);

        NavigableMap<LocalDateTime, Auditorium> auditoriums = new TreeMap<>();
        auditoriums.put(CURRENT_DATA, auditorium);

        testEvent = new Event("testEvent");
        testEvent.setId(1L);
        testEvent.setAirDates(eventAirData);
        testEvent.setAuditoriums(auditoriums);
        testEvent.setBasePrice(BASE_PRICE);
    }

    private NavigableSet<Ticket> createTickets(User user, long seat) {

        Ticket ticket = new Ticket(user, testEvent, LocalDateTime.now(), seat);
        NavigableSet<Ticket> tickets = new TreeSet<>();
        tickets.add(ticket);
        return tickets;
    }
}
