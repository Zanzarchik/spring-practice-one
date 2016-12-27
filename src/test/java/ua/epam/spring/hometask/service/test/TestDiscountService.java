package ua.epam.spring.hometask.service.test;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import ua.epam.spring.hometask.domain.Event;
import ua.epam.spring.hometask.domain.User;
import ua.epam.spring.hometask.service.impl.CustomDiscountService;
import ua.epam.spring.hometask.service.impl.discount.BirthdayDiscountStrategy;
import ua.epam.spring.hometask.service.impl.discount.EveryNTicketsDiscountStrategy;

import static org.junit.Assert.assertEquals;

@ContextConfiguration("classpath:test-spring-config.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class TestDiscountService {

    @Autowired private CustomDiscountService discountService;
    @Autowired private CustomDiscountService discountServiceWithoutDiscountStrategies;
    @Autowired private BirthdayDiscountStrategy birthdayDiscount;
    @Autowired private EveryNTicketsDiscountStrategy everyNTicketsDiscount;

    private User testUser;
    private Event testEvent = new Event("testEvent");
    private LocalDateTime currentData = LocalDateTime.now();
    private int everyTicketDiscountAmount = 50; //see test-spring-config
    private int birthdayDiscountAmount = 5;

    @Before
    public void init(){

        testUser = new User();
        testUser.setId(1L);
        testUser.setFirstName("testUser");
        testUser.setBirthdayData(currentData.minus(birthdayDiscount.getBirthdayDiscountPeriod(), ChronoUnit.DAYS));
    }

    @Test
    public void if_HasTwoDifferentDiscountStrategy_and_BothAreTriggered_then_getMaxAvailableDiscount(){

        int currentDiscount = discountService.getDiscount(testUser, testEvent, currentData, everyNTicketsDiscount.getRequiredNumberOfTickets());
        assertEquals("Get a discount when all discount strategies is triggered", everyTicketDiscountAmount, currentDiscount);
    }

    @Test
    public void if_HasTwoDifferentDiscountStrategy_and_OneIsTriggered_then_getTriggeredDiscount(){

        int currentDiscount = discountService.getDiscount(testUser, testEvent, currentData, everyNTicketsDiscount.getRequiredNumberOfTickets() - 1);
        assertEquals("Get a discount when only one discount strategies is triggered", birthdayDiscountAmount, currentDiscount);
    }

    @Test
    public void if_HasNoDiscountStrategy_then_getNoDiscount(){

        int currentDiscount = discountServiceWithoutDiscountStrategies.getDiscount(testUser, testEvent, currentData, everyNTicketsDiscount.getRequiredNumberOfTickets());
        assertEquals("Get a discount when only one discount strategies is triggered", 0, currentDiscount);
    }
}
