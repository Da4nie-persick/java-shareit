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
    public List<ItemWithBookingDto> getItemByOwner(@RequestHeader("X-Sharer-User-Id") Integer userId) {
        return itemService.getItemByOwner(userId);
    }

    @GetMapping("/{id}")
    public ItemWithBookingDto getUserId(@RequestHeader("X-Sharer-User-Id") Integer userId, @PathVariable Integer id) {
        return itemService.getItemId(userId, id);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItem(@RequestParam String text) {
        return itemService.searchItem(text);
    }

    @PostMapping("{itemId}/comment")
    public CommentDto addComment(@Valid @RequestBody CommentDto commentDto, @RequestHeader("X-Sharer-User-Id") Integer userId,
                                 @PathVariable Integer itemId) {
        return itemService.addComment(commentDto, userId, itemId);
    }
}
