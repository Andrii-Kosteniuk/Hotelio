package com.hotelio.common.mapper;

import com.hotelio.common.dto.AddressRequest;
import com.hotelio.common.dto.CreateHotelRequest;
import com.hotelio.common.dto.HotelResponse;
import com.hotelio.common.dto.PageResponse;
import com.hotelio.hotel.domain.Address;
import com.hotelio.hotel.domain.Hotel;
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
                request.starRating(),
                request.pricePerNight()
        );
    }

    default Address toAddress(AddressRequest request) {
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
