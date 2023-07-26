package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.booking.model.Status;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
public class BookingDtoResponse {
    private Integer id;
    @NotNull
    @FutureOrPresent(message = "Броннирование не должно быть в прошлом")
    private LocalDateTime start;
    @NotNull
    @Future(message = "Броннирование не должно быть в прошлом")
    private LocalDateTime end;
    private Item item;
    private User booker;
    private Status status;

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Item {
        private final Integer id;
        private final String name;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class User {
        private final Integer id;
        private final String name;
    }
}


