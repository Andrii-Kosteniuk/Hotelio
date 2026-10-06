package com.dev.hotelio.common.mapper;

import com.dev.hotelio.common.dto.hotel.HotelAddressRequest;
import com.dev.hotelio.common.dto.hotel.CreateHotelRequest;
import com.dev.hotelio.common.dto.hotel.HotelResponse;
import com.dev.hotelio.common.dto.PageResponse;
import com.dev.hotelio.hotel.domain.Address;
import com.dev.hotelio.hotel.domain.Hotel;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring")
public interface HotelMapper {

    HotelResponse toHotelResponse(Hotel hotel);

    default PageResponse<HotelResponse> toPageResponse(Page<Hotel> hotels){
        return new PageResponse<>(
                hotels.map(this::toHotelResponse).getContent(),
                hotels.getNumber(),
                hotels.getSize(),
                hotels.getTotalElements(),
                hotels.getTotalPages(),
                hotels.isFirst(),
                hotels.isLast()
        );
    }

    default Hotel toHotel(CreateHotelRequest request) {

        return Hotel.createNew(
                request.name(),
                request.description(),
                toAddress(request.address()),
                request.starRating()
        );
    }

    default Address toAddress(HotelAddressRequest request) {
        return Address.builder()
                .country(request.country())
                .city(request.city())
                .district(request.district())
                .street(request.street())
                .buildingNumber(request.buildingNumber())
                .zipCode(request.zipCode())
                .build();
    }


}
