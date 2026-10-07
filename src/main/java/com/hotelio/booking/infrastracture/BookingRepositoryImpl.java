package com.hotelio.booking.infrastracture;

import com.hotelio.booking.model.Booking;
import com.hotelio.booking.model.BookingStatus;
import com.hotelio.booking.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.EnumSet;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class BookingRepositoryImpl implements BookingRepository {

    private static final EnumSet<BookingStatus> BLOCKING_STATUSES =
            EnumSet.of(
                    BookingStatus.PENDING,
                    BookingStatus.CONFIRMED);

    private final JpaBookingRepository repository;

    @Override
    public Booking save(Booking booking) {
        return repository.save(booking);
    }

    @Override
    public boolean existsOverlappingBooking(UUID roomId, LocalDate checkIn, LocalDate checkOut) {
        return repository.existsOverlappingBooking(roomId, checkIn, checkOut, BLOCKING_STATUSES);
    }
}