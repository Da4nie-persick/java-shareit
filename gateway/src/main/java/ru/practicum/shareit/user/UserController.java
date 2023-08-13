package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.Valid;

@Controller
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {
    private final UserClient userClient;

    @PostMapping
    public ResponseEntity<Object> createUser(@Valid @RequestBody UserDto userDto) {
        return userClient.createUser(userDto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable long id,
                                         @RequestBody UserDto userDto) {
        return userClient.update(id, userDto);
    }

    @GetMapping
    public ResponseEntity<Object> allUser() {
        return userClient.allUser();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getUserId(@PathVariable long id) {
        return userClient.getUserId(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable long id) {
        return userClient.deleteUser(id);
    }
}