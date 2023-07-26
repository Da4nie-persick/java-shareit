package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.dto.BookingNotObjectsDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithBookingDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.util.List;
import java.util.stream.Collectors;

public class ItemMapper {
    public static final ItemDto toItemDto(Item item) {
        ItemDto.ItemRequest itemRequest = new ItemDto.ItemRequest();
        if (item.getRequest() != null) {
            itemRequest.setId(itemRequest.getId());
        }
        ItemDto itemDto = new ItemDto();
        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setAvailable(item.getAvailable());
        itemDto.setRequest(itemRequest);
        return itemDto;
    }

    public static final Item toItem(ItemDto itemDto, User user) {
        Item item = new Item();
        item.setId(itemDto.getId());
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setAvailable(itemDto.getAvailable());
        item.setOwner(user);
        return item;
    }

    public static ItemWithBookingDto toItemWithBookingDto(List<Comment> comments,
                                                          Booking last,
                                                          Booking next,
                                                          Item item) {
        ItemWithBookingDto itemWithBookingDto = new ItemWithBookingDto();
        itemWithBookingDto.setId(item.getId());
        itemWithBookingDto.setName(item.getName());
        itemWithBookingDto.setDescription(item.getDescription());
        itemWithBookingDto.setAvailable(item.getAvailable());

        if (last != null) {
            itemWithBookingDto.setLastBooking(new BookingNotObjectsDto(last.getId(), last.getBooker().getId()));
        } else {
            itemWithBookingDto.setLastBooking(null);
        }

        if (next != null) {
            itemWithBookingDto.setNextBooking(new BookingNotObjectsDto(next.getId(), next.getBooker().getId()));
        } else {
            itemWithBookingDto.setNextBooking(null);
        }

        List<CommentDto> commentDtoList = comments.stream()
                .map(comment -> {
                    CommentDto commentDto = new CommentDto();
                    commentDto.setId(comment.getId());
                    commentDto.setText(comment.getText());
                    commentDto.setAuthorName(comment.getAuthor().getName());
                    commentDto.setCreated(comment.getCreated());
                    return commentDto;
                }).collect(Collectors.toList());
        itemWithBookingDto.setComments(commentDtoList);

        return itemWithBookingDto;
    }
}
