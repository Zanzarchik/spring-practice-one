package ua.epam.spring.hometask.service.impl.discount;

import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import ua.epam.spring.hometask.domain.Event;
import ua.epam.spring.hometask.domain.User;
import ua.epam.spring.hometask.service.api.DiscountStrategy;
import ua.epam.spring.hometask.service.api.UserService;

public class EveryNTicketsDiscountStrategy implements DiscountStrategy {

    private int requiredNumberOfTickets;
    private int discountAmount;

    public EveryNTicketsDiscountStrategy(int requiredNumberOfTickets, int discountAmount) {

        if (requiredNumberOfTickets <= 0) {
            throw new IllegalArgumentException("Number of tickets required for discount should be positive. Actual value is " + requiredNumberOfTickets);
        }

        this.requiredNumberOfTickets = requiredNumberOfTickets;
        this.discountAmount = discountAmount;
    }

    public int getRequiredNumberOfTickets() {
        return requiredNumberOfTickets;
    }

    @Override
    public int getDiscount(@Nullable User user, @Nonnull Event event, @Nonnull LocalDateTime airDateTime, long amountOfPurchasedTickets) {

        if (amountOfPurchasedTickets >= requiredNumberOfTickets) {
            return discountAmount;
        }

        if (user == null){
            return 0;
        }

        int currentTicketAmount = user.getTickets().size();
        if ((currentTicketAmount % requiredNumberOfTickets + amountOfPurchasedTickets) >= requiredNumberOfTickets) {
            return discountAmount;
        }

        return 0;
    }

    private boolean hasEnoughTickets(int ticketAmount) {
        return true;
    }
}
