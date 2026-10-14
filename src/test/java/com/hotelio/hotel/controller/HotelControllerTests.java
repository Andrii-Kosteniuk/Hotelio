package com.hotelio.hotel.controller;

import com.hotelio.common.dto.PageResponse;
import com.hotelio.hotel.domain.HotelStatus;
import com.hotelio.hotel.dto.*;
import com.hotelio.hotel.service.HotelService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HotelController.class)
class HotelControllerTests {

    @MockitoBean
    HotelService hotelService;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;


    @Test
    void testCreateHotel() throws Exception {
        UUID hotelId = UUID.randomUUID();

        var request = new CreateHotelRequest("Hotel 1", "Some description", createHotelAddressRequest(), 5);
        var response = new HotelResponse(hotelId, "Hotel 1", "Some description", createHotelAddressResponseWithCity("Wroclaw"), 5, HotelStatus.PENDING_REVIEW);

        when(hotelService.createHotel(request)).thenReturn(response);

        mockMvc.perform(post("/api/v1/hotels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Hotel 1"));

    }

    @Test
    void testGetHotelById() throws Exception {
        UUID hotelId = UUID.randomUUID();

        var response = new HotelResponse(hotelId, "Hotel 1", "Some description", createHotelAddressResponseWithCity("Warshaw"), 5, HotelStatus.PENDING_REVIEW);

        when(hotelService.getHotel(hotelId)).thenReturn(response);

        mockMvc.perform(get("/api/v1/hotels/" + hotelId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Hotel 1"));

    }


    @Test
    void testSearchHotels() throws Exception {

        UUID hotelId = UUID.randomUUID();

        Pageable pageable = PageRequest.of(0, 20, Sort.by("name").ascending());
        var request = makeHotelSearchRequestWitCity("Wroclaw");
        var response = new HotelResponse(hotelId, "Hotel 1", "Some description", createHotelAddressResponseWithCity("Wroclaw"), 5, HotelStatus.PENDING_REVIEW);

        PageResponse<HotelResponse> expectedResponse =
                new PageResponse<>(List.of(response), 0, 20, 1, 1, true, true);

        when(hotelService.search(request, pageable)).thenReturn(expectedResponse);

        mockMvc.perform(get("/api/v1/hotels")
                        .param("city", "Wroclaw")
                        .param("page", "0")
                        .param("size", "20")
                        .param("sort", "name")
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content[0].id").value(hotelId.toString()))
                .andExpect(jsonPath("$.content[0].name").value("Hotel 1"))
                .andExpect(jsonPath("$.content[0].starRating").value(5))
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.totalPages").value(1));


        verify(hotelService).search(request, pageable);
    }

    private static HotelSearchRequest makeHotelSearchRequestWitCity(String city) {
        return new HotelSearchRequest(city,
                null, null, null, null, null, null, null, null, null, null, null);
    }

    private HotelAddressResponse createHotelAddressResponseWithCity(String city) {
        return new HotelAddressResponse("Poland", city, "Wroclaw", "Street", 101, "22-555");
    }

    private HotelAddressRequest createHotelAddressRequest() {
        return new HotelAddressRequest("Poland", "Wroclaw", "Wroclaw", "Street", 101, "22-555");
    }


}
