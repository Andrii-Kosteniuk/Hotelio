package com.hotelio.hotel.service;


import com.hotelio.common.dto.PageResponse;
import com.hotelio.common.exception.RequestParameterNotValidException;
import com.hotelio.common.exception.ResourceAlreadyExistsException;
import com.hotelio.common.exception.ResourceNotFoundException;
import com.hotelio.hotel.domain.Address;
import com.hotelio.hotel.domain.Hotel;
import com.hotelio.hotel.domain.HotelStatus;
import com.hotelio.hotel.dto.*;
import com.hotelio.hotel.mapper.HotelMapper;
import com.hotelio.hotel.mapper.HotelSearchMapper;
import com.hotelio.hotel.repository.HotelRepository;
import com.hotelio.hotel.validation.HotelSearchValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HotelServiceTests {

    @Mock
    HotelRepository hotelRepository;
    @Mock
    HotelMapper hotelMapper;
    @Mock
    HotelSearchMapper searchMapper;
    @Mock
    HotelSearchValidator searchValidator;

    @InjectMocks
    HotelServiceImpl hotelService;

    @Test
    void shouldCreateHotel() {
        // Arrange
        UUID hotelId = UUID.randomUUID();

        Hotel hotel = getHotel(hotelId);
        var request = getHotelRequest();
        var hotelResponse = getHotelResponse(hotelId);

        when(hotelRepository.existsByName("Hotel 1")).thenReturn(false);
        when(hotelMapper.toHotel(request)).thenReturn(hotel);
        when(hotelRepository.save(hotel)).thenReturn(hotel);
        when(hotelMapper.toHotelResponse(hotel)).thenReturn(hotelResponse);

        // Act
        var response = hotelService.createHotel(request);


        // Assert
        assertNotNull(response);
        assertEquals(response.id(), hotelId);
        verify(hotelRepository).save(hotel);

    }

    @Test
    void shouldThrowExceptionWhenHotelAlreadyExists() {
        // Arrange
        CreateHotelRequest request = getHotelRequest();

        when(hotelRepository.existsByName(request.name()))
                .thenReturn(true);

        // Act & Assert
        ResourceAlreadyExistsException exception =
                assertThrows(
                        ResourceAlreadyExistsException.class,
                        () -> hotelService.createHotel(request)
                );

        assertEquals("Hotel with name 'Hotel 1' already exists", exception.getMessage());

        verify(hotelRepository).existsByName(request.name());

        verify(hotelRepository, never()).save(any());
        verifyNoInteractions(hotelMapper);
    }


    @Test
    void shouldGetHotelById() {
        // Arrange
        UUID hotelId = UUID.randomUUID();

        Hotel hotel = getHotel(hotelId);
        var hotelResponse = getHotelResponse(hotelId);

        when(hotelRepository.findById(hotelId)).thenReturn(Optional.of(hotel));
        when(hotelMapper.toHotelResponse(hotel)).thenReturn(hotelResponse);

        // Act
        var response = hotelService.getHotel(hotelId);


        // Assert
        assertNotNull(response);
        assertEquals(response.id(), hotelId);
        assertEquals(response.name(), hotel.getName());
        verify(hotelRepository).findById(hotelId);

    }

    @Test
    void shouldThrowExceptionWhenHotelByIdNotFound() {
        // Arrange
        UUID hotelId = UUID.randomUUID();

        // Act & Assert
        ResourceNotFoundException exception =
                assertThrows(
                        ResourceNotFoundException.class,
                        () -> hotelService.getHotel(hotelId)
                );

        assertEquals("Hotel not found with id %s:".formatted(hotelId), exception.getMessage());
        verify(hotelRepository).findById(hotelId);
        verifyNoInteractions(hotelMapper);
    }

    @Test
    void shouldReturnHotelsWhenSearchCriteriaMatch() {
        // Arrange
        UUID hotelId = UUID.randomUUID();
        HotelSearchRequest request = mock(HotelSearchRequest.class);
        HotelSearchCriteria criteria = mock(HotelSearchCriteria.class);

        Pageable pageable = PageRequest.of(0, 10);

        Hotel hotel = getHotel(hotelId);

        Page<Hotel> hotelPage = new PageImpl<>(
                List.of(hotel),
                pageable,
                1
        );

        @SuppressWarnings("unchecked")
        PageResponse<HotelResponse> expectedResponse =
                mock(PageResponse.class);

        when(searchMapper.toCriteria(request))
                .thenReturn(criteria);

        when(hotelRepository.findAllHotels(criteria, pageable))
                .thenReturn(hotelPage);

        when(hotelMapper.toPageResponse(hotelPage))
                .thenReturn(expectedResponse);

        // Act
        PageResponse<HotelResponse> actualResponse =
                hotelService.search(request, pageable);

        // Assert
        assertSame(expectedResponse, actualResponse);

        InOrder inOrder = inOrder(
                searchValidator,
                searchMapper,
                hotelRepository,
                hotelMapper
        );

        inOrder.verify(searchValidator).validate(request);
        inOrder.verify(searchMapper).toCriteria(request);
        inOrder.verify(hotelRepository)
                .findAllHotels(criteria, pageable);
        inOrder.verify(hotelMapper).toPageResponse(hotelPage);
    }

    @Test
    void shouldReturnEmptyPageWhenNoHotelsMatch() {
        // Arrange
        HotelSearchRequest request = mock(HotelSearchRequest.class);
        HotelSearchCriteria criteria = mock(HotelSearchCriteria.class);

        Pageable pageable = PageRequest.of(0, 10);

        Page<Hotel> emptyPage = Page.empty(pageable);

        @SuppressWarnings("unchecked")
        PageResponse<HotelResponse> expectedResponse =
                mock(PageResponse.class);

        when(searchMapper.toCriteria(request))
                .thenReturn(criteria);

        when(hotelRepository.findAllHotels(criteria, pageable))
                .thenReturn(emptyPage);

        when(hotelMapper.toPageResponse(emptyPage))
                .thenReturn(expectedResponse);

        // Act
        PageResponse<HotelResponse> actualResponse =
                hotelService.search(request, pageable);

        // Assert
        assertSame(expectedResponse, actualResponse);

        verify(searchValidator).validate(request);
        verify(hotelRepository).findAllHotels(criteria, pageable);
        verify(hotelMapper).toPageResponse(emptyPage);
    }

    @Test
    void shouldNotSearchWhenValidationFails() {
        // Arrange
        HotelSearchRequest request = mock(HotelSearchRequest.class);
        Pageable pageable = PageRequest.of(0, 10);

        doThrow(new RequestParameterNotValidException("Invalid search criteria")).when(searchValidator).validate(request);

        // Act & Assert
        assertThrows(
                RequestParameterNotValidException.class,
                () -> hotelService.search(request, pageable)
        );

        verify(searchValidator).validate(request);

        verifyNoInteractions(
                searchMapper,
                hotelRepository,
                hotelMapper
        );
    }


    private HotelResponse getHotelResponse(UUID hotelId) {
        return new HotelResponse(
                hotelId,
                "Hotel 1",
                "Hotel description",
                createHotelAddressResponse(),
                5);
    }

    private CreateHotelRequest getHotelRequest() {
        return new CreateHotelRequest(
                "Hotel 1",
                "Hotel description",
                createHotelAddressRequest(),
                5);
    }

    private HotelAddressRequest createHotelAddressRequest() {
        return new HotelAddressRequest(
                "Poland",
                "Wroclaw",
                "Wroclaw",
                "Sucha",
                1,
                "55-555"
        );
    }

    private HotelAddressResponse createHotelAddressResponse() {
        return new HotelAddressResponse(
                "Poland",
                "Wroclaw",
                "Wroclaw",
                "Sucha",
                1,
                "55-555"
        );
    }

    private Hotel getHotel(UUID hotelId) {
        return new Hotel(
                hotelId,
                "Hotel 1",
                "Hotel description",
                Address.builder()
                        .country("Poland")
                        .city("Wroclaw")
                        .district("Wroclaw")
                        .street("Sucha")
                        .zipCode("55-555")
                        .build(),
                5,
                HotelStatus.ACTIVE,
                null,
                null,
                null,
                null,
                1L
        );
    }

}
