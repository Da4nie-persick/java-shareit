package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemDto {
    private Integer id;
    @NotBlank(message = "Имя вещи не может быть пустым!")
    private String name;
    @NotBlank(message = "Описание вещи не может быть пустым!")
    private String description;
    @NotNull(message = "Статус вещи не может быть пустым!")
    private Boolean available;
    private Integer requestId;
}
