package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Integer id;
    @NotBlank(message = "Имя пользователя не может быть пустым!")
    private String name;
    @NotBlank(message = "Email не может быть пустым!")
    @Email(message = "Email должен бфть корректным!")
    private String email;
}
