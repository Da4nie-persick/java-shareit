package ru.practicum.shareit.request.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ItemRequestDto {
    private Integer id;
    private String description;
    private User requester;
    private LocalDateTime created;

    @Data
    public static class User {
        private Integer id;
    }
}
