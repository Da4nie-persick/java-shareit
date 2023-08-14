package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.booking.dto.BookingNotObjectsDto;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemWithBookingDto {
    private Integer id;
    private String name;
    private String description;
    private Boolean available;
    private BookingNotObjectsDto lastBooking;
    private BookingNotObjectsDto nextBooking;
    private List<CommentDto> comments;
}