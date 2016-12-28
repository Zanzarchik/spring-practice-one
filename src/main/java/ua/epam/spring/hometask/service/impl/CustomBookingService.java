package ua.epam.spring.hometask.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import ua.epam.spring.hometask.domain.Auditorium;
import ua.epam.spring.hometask.domain.Event;
import ua.epam.spring.hometask.domain.EventRating;
import ua.epam.spring.hometask.domain.Ticket;
import ua.epam.spring.hometask.domain.User;
import ua.epam.spring.hometask.service.api.AuthService;
import ua.epam.spring.hometask.service.api.BookingService;
import ua.epam.spring.hometask.service.api.DiscountService;
import ua.epam.spring.hometask.service.api.UserService;

public class CustomBookingService implements BookingService {

    private AuthService authService;
    private DiscountService discountService;
    private Map<Long, Set<Ticket>> tickets = new HashMap<>();
    private Map<EventRating, Double> ratingIndex = new HashMap<>();

    public CustomBookingService(Map<EventRating, Double> ratingIndex) {
        this.ratingIndex = ratingIndex;
    }

    @Autowired
    public void setUserService(AuthService authService) {
        this.authService = authService;
    }

    @Autowired
    public void setDiscountService(DiscountService discountService) {
        this.discountService = discountService;
    }

    @Override
    public double getTicketsPrice(@Nonnull Event event,
                                  @Nonnull LocalDateTime dateTime,
                                  @Nullable User user,
                                  @Nonnull Set<Long> seats,
                                  @Nonnull Auditorium auditorium) {

        if (seats.size()==0){
            throw new IllegalArgumentException("Seats should contain at least one element");
        }

        int discount = discountService.getDiscount(user, event, dateTime, seats.size());
        double priceWithoutDiscount = getTicketPriceWithoutDiscount(event, seats, auditorium);
        double discountSum = priceWithoutDiscount * discount / 100;

        return priceWithoutDiscount - discountSum;
    }

    @Override
    public void bookTickets(@Nonnull Set<Ticket> tickets) {

        User user = tickets.stream()
                .map(Ticket::getUser)
                .findFirst().orElseThrow(() -> new IllegalArgumentException("User was not set for ticket"));

        if (authService.isRegisteredUser(user)) {
            this.tickets.put(user.getId(), tickets);
        }
    }

    @Nonnull
    @Override
    public Set<Ticket> getPurchasedTicketsForEvent(@Nonnull Event event, @Nonnull LocalDateTime dateTime) {
        return null;
    }

    public Set<Ticket> getTickets(Long userId) {

        if (tickets.containsKey(userId)) {
            return new HashSet<>(tickets.get(userId));
        }
        return new HashSet<>();
    }

    private double getTicketPriceWithoutDiscount(Event event, Set<Long> seats, Auditorium auditorium) {

        Set<Long> vipSeats = auditorium.getVipSeats();
        double totalPrice = 0;

        for (Long seat : seats) {
            if (vipSeats.contains(seat)){
                totalPrice += event.getBasePrice() * ratingIndex.get(event.getRating()) * auditorium.getVipSeatRatio();
            }else {
                totalPrice += event.getBasePrice() * ratingIndex.get(event.getRating());
            }
        }

        return totalPrice;
    }
}
