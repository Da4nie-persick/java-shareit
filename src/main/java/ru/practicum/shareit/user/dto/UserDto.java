package ru.practicum.shareit.user.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class UserDto {
    private Integer id;
    @NotBlank(message = "Имя пользователя не может быть пустым!")
    private String name;
    @NotBlank(message = "Email не может быть пустым!")
    @Email(message = "Email должен бфть корректным!")
    private String email;
}
