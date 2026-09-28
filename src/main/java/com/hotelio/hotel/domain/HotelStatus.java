package com.hotelio.hotel.domain;

import java.util.Map;
import java.util.Set;

public enum HotelStatus {
    PENDING_REVIEW,
    ACTIVE,
    REJECTED,
    SUSPENDED;


    private static final Map<HotelStatus, Set<HotelStatus>> ALLOWED_TRANSITIONS = Map.of(
            PENDING_REVIEW, Set.of(ACTIVE, REJECTED),
            ACTIVE, Set.of(SUSPENDED),
            REJECTED, Set.of(),
            SUSPENDED, Set.of(ACTIVE)
    );

    public boolean canTransitionTo(HotelStatus target) {
        return ALLOWED_TRANSITIONS.getOrDefault(this, Set.of()).contains(target);
    }
}
