package ru.practicum.shareit.booking.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
public class BookingDtoRequest {
    @NotNull
    @FutureOrPresent(message = "Броннирование не должно быть в прошлом")
    private LocalDateTime start;
    @NotNull
    @Future(message = "Броннирование не должно быть в прошлом")
    private LocalDateTime end;
    private Integer itemId;
}