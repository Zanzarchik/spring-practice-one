package ua.epam.spring.hometask.service.test.discount;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.util.NavigableSet;
import java.util.TreeSet;

import ua.epam.spring.hometask.domain.Event;
import ua.epam.spring.hometask.domain.Ticket;
import ua.epam.spring.hometask.domain.User;
import ua.epam.spring.hometask.service.api.UserService;
import ua.epam.spring.hometask.service.impl.discount.EveryNTicketsDiscountStrategy;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class TestEveryNTicketsDiscountStrategy {

    @Mock
    private UserService userService;

    private LocalDateTime currentData = LocalDateTime.now();
    private int requiredNumberOfTickets = 10;
    private int discountAmount = 5;
    private User testUser;
    private EveryNTicketsDiscountStrategy discountStrategy;

    @Before
    public void init() {

        testUser = new User();
        testUser.setId(1L);
        testUser.setFirstName("testUser");

        discountStrategy = new EveryNTicketsDiscountStrategy(requiredNumberOfTickets, discountAmount);

    }

    @Test
    public void if_UserIsRegistered_and_BuyEnoughTickets_then_getDiscount() {

        int currentDiscount = discountStrategy.getDiscount(testUser, new Event("testEvent"), currentData, requiredNumberOfTickets);

        assertEquals("Get a discount for registered user who buys required amount of tickets", discountAmount, currentDiscount);
    }

    @Test
    public void if_UserIsReqistered_and_AlreadyHasEnoughTickets_and_BuyNotEnoughTickets_then_getNoDiscount() {

        testUser.setTickets(generateRequiredAmountOfTickets(requiredNumberOfTickets));
        int currentDiscount = discountStrategy.getDiscount(testUser, new Event("testEvent"), currentData, requiredNumberOfTickets - 1);

        assertEquals("Get a discount for registered user " +
                "who already has enough amount of tickets " +
                "and buys tickets " +
                "whose number is less than required", 0, currentDiscount);
    }

    @Test
    public void if_UserIsRegistered_and_HasNotEnoughTickets_and_BuyNotEnoughTickets_andSummaryHasEnoughTickets_then_getDiscount() {

        int missingTicketNumber = 1;
        testUser.setTickets(generateRequiredAmountOfTickets(requiredNumberOfTickets - missingTicketNumber));
        int currentDiscount = discountStrategy.getDiscount(testUser, new Event("testEvent"), currentData, missingTicketNumber);

        assertEquals("Get a discount for registered user " +
                "who has not enough amount of tickets " +
                "and buys tickets " +
                "whose number is less than required " +
                "and finally a sum of total tickets equal required amount of tickets", discountAmount, currentDiscount);
    }

    @Test
    public void if_UserIsRegistered_and_HasNotEnoughTickets_and_BuyNotEnoughTickets_andSummaryHasMoreTicketsThanRequires_then_getDiscount() {

        int missingTicketNumber = 1;
        testUser.setTickets(generateRequiredAmountOfTickets(requiredNumberOfTickets - missingTicketNumber));
        int currentDiscount = discountStrategy.getDiscount(testUser, new Event("testEvent"), currentData, missingTicketNumber + 1);

        assertEquals("Get a discount for registered user " +
                "who has not enough amount of tickets " +
                "and buys tickets " +
                "whose number is less than required " +
                "and finally a sum of total tickets is more than required amount of tickets", discountAmount, currentDiscount);
    }

    @Test
    public void if_UserIsRegistered_and_HasNotEnoughTickets_and_BuyNotEnoughTickets_andSummaryHasLessTicketsThanRequires_then_getNoDiscount() {

        int missingTicketNumber = 1;
        testUser.setTickets(generateRequiredAmountOfTickets(requiredNumberOfTickets - missingTicketNumber));
        int currentDiscount = discountStrategy.getDiscount(testUser, new Event("testEvent"), currentData, missingTicketNumber - 1);

        assertEquals("Get a discount for registered user " +
                "who has not enough amount of tickets " +
                "and buys tickets " +
                "whose number is less than required " +
                "and finally a sum of total tickets is less than required amount of tickets", 0, currentDiscount);
    }

    @Test
    public void if_UserIsUnreqistered_and_BuyEnoughTickets_then_getDiscount() {

        int currentDiscount = discountStrategy.getDiscount(null, new Event("testEvent"), currentData, requiredNumberOfTickets);

        assertEquals("Get a discount for unregistered user who buys required amount of tickets", discountAmount, currentDiscount);
    }

    private NavigableSet<Ticket> generateRequiredAmountOfTickets(int ticketsAmount) {

        NavigableSet<Ticket> tickets = new TreeSet<>();
        for (int i = 0; i < ticketsAmount; i++) {
            tickets.add(new Ticket(testUser, new Event("testEvent" + i), currentData, 1));
        }

        return tickets;
    }
}
