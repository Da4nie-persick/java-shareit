package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.booking.model.Status;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookingDtoResponse {
    private Integer id;
    private LocalDateTime start;
    private LocalDateTime end;
    private Item item;
    private User booker;
    private Status status;

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Item {
        private Integer id;
        private String name;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class User {
        private Integer id;
        private String name;
    }
}