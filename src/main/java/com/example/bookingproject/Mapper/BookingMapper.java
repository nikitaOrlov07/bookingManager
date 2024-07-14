package com.example.bookingproject.Mapper;

import com.example.bookingproject.Dto.BookingDto;
import com.example.bookingproject.Model.BookingEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
public class BookingMapper {
    public static BookingDto getBookingDtoFromBookingEntity(BookingEntity bookingEntity)
    {
        // Amenities
        String amenities = String.join(",", bookingEntity.getAmenities()); // write the list items to a String variable and separate them with a comma
        // Conditions
        String conditions = String.join(",", bookingEntity.getConditions());
        BookingDto bookingDto = BookingDto.builder()
                .id(bookingEntity.getId())
                .attachments(bookingEntity.getAttachments())
                .conditions(conditions)
                .author(bookingEntity.getAuthor())
                .numberOfRooms(bookingEntity.getNumberOfRooms())
                .companyName(bookingEntity.getCompanyName())
                .currency(bookingEntity.getCurrency())
                .price(bookingEntity.getPrice())
                .type(bookingEntity.getType())
                .title(bookingEntity.getTitle())
                .capacity(bookingEntity.getCapacity())
                .bookingTime(bookingEntity.getBookingTime())
                .description(bookingEntity.getDescription())
                .amenities(amenities)
                .country(bookingEntity.getCountry())
                .address(bookingEntity.getAddress())
                .city(bookingEntity.getCity())
                .build();
        return bookingDto;
    }
    public static BookingEntity getBookingEntityFromBookingDto(BookingDto bookingDto)
    {
        // Amenities
        String[] amenitiesItems = bookingDto.getAmenities().split(",\\s*"); // comma with space or just comma
        List<String>  amenities = new ArrayList<>(Arrays.asList(amenitiesItems));
        // Conditions
        String[] conditionItems = bookingDto.getConditions().split(",\\s*"); // comma with space or just comma
        List<String>  conditions = new ArrayList<>(Arrays.asList(conditionItems));

        BookingEntity bookingEntity = BookingEntity.builder()
                .attachments(bookingDto.getAttachments())
                .author(bookingDto.getAuthor())
                .numberOfRooms(bookingDto.getNumberOfRooms())
                .companyName(bookingDto.getCompanyName())
                .currency(bookingDto.getCurrency())
                .price(bookingDto.getPrice())
                .type(bookingDto.getType())
                .title(bookingDto.getTitle())
                .bookingTime(bookingDto.getBookingTime())
                .capacity(bookingDto.getCapacity())
                .description(bookingDto.getDescription())
                .amenities(amenities)
                .conditions(conditions)
                .country(bookingDto.getCountry())
                .address(bookingDto.getAddress())
                .city(bookingDto.getCity())
                .build();
        return  bookingEntity;
    }
}
