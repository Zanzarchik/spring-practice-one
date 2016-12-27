package ua.epam.spring.hometask.service.test.discount;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import ua.epam.spring.hometask.domain.Event;
import ua.epam.spring.hometask.domain.User;
import ua.epam.spring.hometask.service.impl.discount.BirthdayDiscountStrategy;

import static org.junit.Assert.assertEquals;

public class TestBirthdayDiscountStrategy {

    private BirthdayDiscountStrategy discountStrategy;
    private User testUser;
    private LocalDateTime currentData;
    private int testDiscount = 5;
    private int birthdayDiscountPeriod = 3;
    private int numberOfPurchasedTickets = 1;

    @Before
    public void init() {

        currentData = LocalDateTime.now();
        discountStrategy = new BirthdayDiscountStrategy(birthdayDiscountPeriod, testDiscount);

        testUser = new User();
        testUser.setFirstName("testUser");
        testUser.setId(1L);
    }

    @Test
    public void test_whenUserHasBirthdayWithinDiscountPeriod_thenGiveDiscount() {

        testUser.setBirthdayData(currentData.minus(birthdayDiscountPeriod - 1, ChronoUnit.DAYS));
        int currentDiscount = discountStrategy.getDiscount(testUser, new Event("testEvent"), currentData, numberOfPurchasedTickets);

        assertEquals("Get a discount when user's birthday data is included into discount period", testDiscount, currentDiscount);
    }

    @Test
    public void test_whenUserHasBirthdayEqualsHighBoundaryOfDiscountPeriod_thenGivenDiscount(){

        testUser.setBirthdayData(currentData.minus(birthdayDiscountPeriod, ChronoUnit.DAYS));
        int currentDiscount = discountStrategy.getDiscount(testUser, new Event("testEvent"), currentData, numberOfPurchasedTickets);

        assertEquals("Get a discount when user's birthday data equals a high bound of discount period", testDiscount, currentDiscount);
    }

    @Test
    public void test_whenUserHasBirthdayOutOfDiscountPeriod_thenNoDiscount(){

        testUser.setBirthdayData(currentData.minus(birthdayDiscountPeriod + 1, ChronoUnit.DAYS));
        int currentDiscount = discountStrategy.getDiscount(testUser, new Event("testEvent"), currentData, numberOfPurchasedTickets);

        assertEquals("Get a discount when user's birthday data is not included into discount period", 0, currentDiscount);
    }
}
