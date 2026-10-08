package com.hotelio.hotel.repository;

import com.hotelio.hotel.domain.Address;
import com.hotelio.hotel.domain.Hotel;
import com.hotelio.hotel.domain.HotelStatus;
import com.hotelio.hotel.dto.HotelSearchCriteria;
import com.hotelio.hotel.infrastructure.HotelRepositoryImpl;
import com.hotelio.room.domain.Room;
import com.hotelio.room.domain.RoomStatus;
import com.hotelio.room.domain.RoomType;
import com.hotelio.room.dto.RoomSearchCriteria;
import com.hotelio.room.infrastructure.RoomRepositoryImpl;
import com.hotelio.room.repository.RoomRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({HotelRepositoryImpl.class, RoomRepositoryImpl.class})
class HotelRepositoryTests {

    private static final Pageable PAGEABLE = PageRequest.of(0, 10);

    @Autowired
    HotelRepository hotelRepository;
    @Autowired
    RoomRepository roomRepository;

    @Test
    void shouldFindHotelById() {
        // Arrange
        var hotel = createHotel("Hotel 1", HotelStatus.ACTIVE, createTestAddress("Poland", "Zakopane"), 5);

        hotelRepository.save(hotel);

        UUID hotelId = hotel.getId();

        // Act
        Optional<Hotel> result = hotelRepository.findById(hotelId);

        // Assert
        assertTrue(result.isPresent());

        var foundHotel = result.orElseThrow();

        assertEquals(hotelId, foundHotel.getId());
        assertEquals("Hotel 1", foundHotel.getName());
        assertEquals(HotelStatus.ACTIVE, foundHotel.getStatus());
    }

    @Test
    void shouldVerifyIfHotelExistsByName() {
        // Arrange
        var hotel = createHotel("Hotel 1", HotelStatus.ACTIVE, createTestAddress("Poland", "Zakopane"), 5);

        hotelRepository.save(hotel);
        String hotelName = hotel.getName();

        // Act
        var result = hotelRepository.existsByName(hotelName);

        // Assert
        assertTrue(result);
    }

    @Test
    void shouldReturnFalseWhenHotelNameDoesNotExist() {
        // Arrange
        var hotel = createHotel("Hotel 1", HotelStatus.ACTIVE, createTestAddress("Poland", "Zakopane"), 5);

        hotelRepository.save(hotel);

        // Act
        var result = hotelRepository.existsByName("Hotel 2");

        // Assert
        assertFalse(result);
    }

    @Test
    void shouldReturnOnlyActiveHotels() {
        // Arrange
        Hotel firstHotel = createHotel("Hotel 1", HotelStatus.ACTIVE, createTestAddress("Poland", "Zakopane"), 5);
        Hotel secondHotel = createHotel("Hotel 2", HotelStatus.PENDING_REVIEW, createTestAddress("Poland", "Wroclaw"), 5);

        hotelRepository.save(firstHotel);
        hotelRepository.save(secondHotel);


        HotelSearchCriteria criteria = emptyCriteria();

        // Act
        Page<Hotel> result = hotelRepository.findAllHotels(criteria, PAGEABLE);

        // Assert
        assertEquals(1, result.getTotalElements());
        assertEquals(firstHotel.getId(), result.getContent().getFirst().getId());

    }


    @Test
    void shouldFindHotelsByCityAndCountryIgnoringCase() {
        // Arrange
        Hotel firstHotel = createHotel("Hotel 1", HotelStatus.ACTIVE, createTestAddress("Poland", "Zakopane"), 5);
        Hotel secondHotel = createHotel("Hotel 2", HotelStatus.ACTIVE, createTestAddress("Poland", "Wroclaw"), 4);

        hotelRepository.save(firstHotel);
        hotelRepository.save(secondHotel);

        HotelSearchCriteria criteria = new HotelSearchCriteria(
                "ZAKOPANE",
                "poland",
                null,
                null,
                null,
                null
        );

        // Act
        Page<Hotel> result = hotelRepository.findAllHotels(criteria, PAGEABLE);

        // Assert
        assertEquals(1, result.getTotalElements());
        assertEquals(firstHotel.getId(), result.getContent().getFirst().getId());
    }

    @Test
    void shouldFindHotelsWithinStarRatingRange() {
        // Arrange
        Hotel expected = createHotel("Hotel 1", HotelStatus.ACTIVE, createTestAddress("Poland", "Zakopane"), 4);

        Hotel secondHotel = createHotel("Hotel 1", HotelStatus.ACTIVE, createTestAddress("Poland", "Zakopane"), 3);

        hotelRepository.save(expected);
        hotelRepository.save(secondHotel);

        HotelSearchCriteria criteria = new HotelSearchCriteria(
                null,
                null,
                null,
                4,
                4,
                null
        );

        // Act
        Page<Hotel> result =
                hotelRepository.findAllHotels(criteria, PAGEABLE);

        // Assert
        assertEquals(1, result.getTotalElements());
        assertEquals(
                expected.getId(),
                result.getContent().getFirst().getId()
        );
    }

    @Test
    void shouldFindHotelsByNameIgnoringCase() {
        // Arrange
        Hotel firstHotel = createHotel("Hotel 1", HotelStatus.ACTIVE, createTestAddress("Poland", "Zakopane"), 4);

        Hotel secondHotel = createHotel("Hotel 2", HotelStatus.ACTIVE, createTestAddress("Poland", "Zakopane"), 3);

        hotelRepository.save(firstHotel);
        hotelRepository.save(secondHotel);

        HotelSearchCriteria criteria = new HotelSearchCriteria(
                null,
                null,
                "Hotel 2",
                null,
                null,
                null
        );

        // Act
        Page<Hotel> result =
                hotelRepository.findAllHotels(criteria, PAGEABLE);

        // Assert
        assertEquals(1, result.getTotalElements());
        assertEquals(
                secondHotel.getId(),
                result.getContent().getFirst().getId()
        );
    }



    @Test
    void shouldNotMatchWhenRoomFiltersAreSatisfiedByDifferentRooms() {
        // Arrange
        Hotel hotel = createHotel("Hotel 1", HotelStatus.ACTIVE, createTestAddress("Poland", "Wroclaw"), 5);

        Room firstRoom = Room.builder()
                .hotel(hotel)
                .type(RoomType.DOUBLE)
                .status(RoomStatus.ACTIVE)
                .capacity(2)
                .bedCount(2)
                .pricePerNight(new BigDecimal("300.00"))
                .build();


        Room secondRoom = Room.builder()
                .hotel(hotel)
                .type(RoomType.SUITE)
                .status(RoomStatus.ACTIVE)
                .capacity(4)
                .bedCount(2)
                .pricePerNight(new BigDecimal("150.00"))
                .build();

        hotelRepository.save(hotel);
        roomRepository.save(firstRoom);
        roomRepository.save(secondRoom);


        RoomSearchCriteria roomCriteria = new RoomSearchCriteria(
                RoomType.DOUBLE,
                4,
                null,
                null,
                new BigDecimal("150.00"),
                null,
                null
        );

        HotelSearchCriteria criteria = new HotelSearchCriteria(
                null,
                null,
                null,
                null,
                null,
                roomCriteria
        );

        // Act
        Page<Hotel> result =
                hotelRepository.findAllHotels(criteria, PAGEABLE);

        // Assert
        assertTrue(result.isEmpty());
    }

    private Hotel createHotel(String name, HotelStatus status, Address address, int starRating) {
        Hotel hotel = Hotel.builder()
                .name(name)
                .description("Test hotel description")
                .status(status)
                .starRating(starRating)
                .address(createTestAddress(address.getCountry(), address.getCity()))
                .build();

        return hotelRepository.save(hotel);
    }

    private Address createTestAddress( String country, String city) {
        return Address.builder()
                .country(country)
                .city(city)
                .district("Małopolskie")
                .street("Krupówki")
                .buildingNumber(10)
                .zipCode("34-500")
                .build();
    }

    private HotelSearchCriteria emptyCriteria() {
        return new HotelSearchCriteria(
                null,
                null,
                null,
                null,
                null,
                null
        );
    }
}
