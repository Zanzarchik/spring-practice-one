package ua.epam.spring.hometask.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import ua.epam.spring.hometask.domain.Event;
import ua.epam.spring.hometask.domain.User;
import ua.epam.spring.hometask.service.api.DiscountService;
import ua.epam.spring.hometask.service.api.DiscountStrategy;

public class CustomDiscountService implements DiscountService {

    private List<DiscountStrategy> discountStrategies;

    public List<DiscountStrategy> getDiscountStrategies() {
        return new ArrayList<>(discountStrategies);
    }

    public void setDiscountStrategies(@Nonnull List<DiscountStrategy> discountStrategies) {
        this.discountStrategies = new ArrayList<>(discountStrategies);
    }

    @Override
    public byte getDiscount(@Nullable User user, @Nonnull Event event, @Nonnull LocalDateTime airDateTime, long numberOfTickets) {

        return (byte) discountStrategies.stream()
                .mapToInt(strategy -> strategy == null ? 0 : strategy.getDiscount(user, event, airDateTime, numberOfTickets))
                .max()
                .orElse(0);
    }
}
