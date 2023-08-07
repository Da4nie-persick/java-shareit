package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequestDto create(@Valid @RequestBody ItemRequestDto itemRequestDto,
                                 @RequestHeader("X-Sharer-User-Id") Integer userId) {
        return itemRequestService.create(itemRequestDto, userId);
    }

    @GetMapping
    public List<ItemRequestDto> getRequests(@RequestHeader("X-Sharer-User-Id") Integer requesterId) {
        return itemRequestService.getRequests(requesterId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getRequestsAllOthers(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                                     @RequestParam(defaultValue = "0", required = false) Integer from,
                                                     @RequestParam(defaultValue = "10", required = false) Integer size) {
        return itemRequestService.getRequestsAllOthers(userId, size, from);
    }

    @GetMapping("/{id}")
    public ItemRequestDto getRequestId(@RequestHeader("X-Sharer-User-Id") Integer userId, @PathVariable Integer id) {
        return itemRequestService.getRequestId(userId, id);
    }
}