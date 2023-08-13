package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.booking.dto.BookingNotObjectsDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemWithBookingDto {
    private Integer id;
    @NotBlank(message = "Имя вещи не может быть пустым!")
    private String name;
    @NotBlank(message = "Описание вещи не может быть пустым!")
    private String description;
    @NotNull(message = "Статус вещи не может быть пустым!")
    private Boolean available;
    private BookingNotObjectsDto lastBooking;
    private BookingNotObjectsDto nextBooking;
    private List<CommentDto> comments;
}
