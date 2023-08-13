package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import javax.validation.Valid;

@Controller
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemRequestController {
    private final ItemRequestClient itemRequestClient;

    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader("X-Sharer-User-Id") long userId,
                                         @RequestBody @Valid ItemRequestDto itemRequestDto) {
        log.info("Creating itemRequest={}, userId={}", itemRequestDto, userId);
        return itemRequestClient.create(userId, itemRequestDto);
    }

    @GetMapping
    public ResponseEntity<Object> getRequests(@RequestHeader("X-Sharer-User-Id") long requesterId) {
        log.info("Get itemRequests itemRequestId={}", requesterId);
        return itemRequestClient.getRequests(requesterId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getRequestsAllOthers(@RequestHeader("X-Sharer-User-Id") long userId,
                                                       @RequestParam(defaultValue = "0", required = false) int from,
                                                       @RequestParam(defaultValue = "10", required = false) int size) {
        log.info("Get itemRequests userId={}, from={}, size={}", userId, from, size);
        return itemRequestClient.getRequestsAllOthers(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getRequestId(@RequestHeader("X-Sharer-User-Id") long userId,
                                               @PathVariable long requestId) {
        log.info("Get itemRequests userId={}, requestId={}", userId, requestId);
        return itemRequestClient.getRequestId(userId, requestId);
    }
}