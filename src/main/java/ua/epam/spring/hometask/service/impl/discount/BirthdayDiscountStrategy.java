package ua.epam.spring.hometask.service.impl.discount;

import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import ua.epam.spring.hometask.domain.Event;
import ua.epam.spring.hometask.domain.User;
import ua.epam.spring.hometask.service.api.DiscountStrategy;

public class BirthdayDiscountStrategy implements DiscountStrategy {

    private int birthdayDiscountPeriod;
    private int discount;

    public BirthdayDiscountStrategy(int birthdayDiscountPeriod, int discount) {
        if (birthdayDiscountPeriod <= 0) {
            throw new IllegalArgumentException("Birthday discount period should be positive value");
        }
        this.birthdayDiscountPeriod = birthdayDiscountPeriod;
        this.discount = discount;
    }

    public int getBirthdayDiscountPeriod() {
        return birthdayDiscountPeriod;
    }

    @Override
    public int getDiscount(@Nullable User user, @Nonnull Event event, @Nonnull LocalDateTime airDateTime, long numberOfTickets) {

        if (user == null || user.getBirthdayData()==null){
            return 0;
        }
        if (Math.abs(user.getBirthdayData().getDayOfYear() - airDateTime.getDayOfYear()) <= birthdayDiscountPeriod ){
            return discount;
        }

        return 0;
    }
}
