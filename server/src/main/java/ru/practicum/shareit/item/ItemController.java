package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithBookingDto;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public ItemDto create(@RequestHeader("X-Sharer-User-Id") Integer userId,
                          @Valid @RequestBody ItemDto itemDto) {
        return itemService.create(itemDto, userId);
    }

    @PatchMapping("/{id}")
    public ItemDto update(@RequestHeader("X-Sharer-User-Id") Integer userId,
                          @RequestBody ItemDto itemDto,
                          @PathVariable Integer id) {
        return itemService.update(id, itemDto, userId);
    }

    @GetMapping
    public List<ItemWithBookingDto> getItemByOwner(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                                   @RequestParam(defaultValue = "0", required = false) Integer from,
                                                   @RequestParam(defaultValue = "10", required = false) Integer size) {
        return itemService.getItemByOwner(userId, size, from);
    }

    @GetMapping("/{id}")
    public ItemWithBookingDto getItemId(@RequestHeader("X-Sharer-User-Id") Integer userId, @PathVariable Integer id) {
        return itemService.getItemId(userId, id);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItem(@RequestParam String text,
                                    @RequestParam(defaultValue = "0", required = false) Integer from,
                                    @RequestParam(defaultValue = "10", required = false) Integer size) {
        return itemService.searchItem(text, size, from);
    }

    @PostMapping("{itemId}/comment")
    public CommentDto addComment(@Valid @RequestBody CommentDto commentDto, @RequestHeader("X-Sharer-User-Id") Integer userId,
                                 @PathVariable Integer itemId) {
        return itemService.addComment(commentDto, userId, itemId);
    }
}
