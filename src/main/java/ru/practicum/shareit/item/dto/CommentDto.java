package ru.practicum.shareit.item.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Getter
@Setter
public class CommentDto {
    private Integer id;
    @NotEmpty
    private String text;
    private String authorName;
    private LocalDateTime created;
}
