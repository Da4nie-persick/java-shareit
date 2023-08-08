package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.exception.BookingException;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.item.CommentRepository;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemWithBookingDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    @Override
    @Transactional
    public ItemDto create(ItemDto itemDto, Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("Пользователь не найден!"));
        Item item = ItemMapper.toItem(itemDto, user);
        return ItemMapper.toItemDto(itemRepository.save(item));
    }

    @Override
    @Transactional
    public ItemDto update(Integer itemId, ItemDto itemDto, Integer userId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ObjectNotFoundException("Вещь с данным id не найдена!"));
        if (!item.getOwner().getId().equals(userId)) {
            throw new ObjectNotFoundException("Пользователь не найден!");
        }

        if (itemDto.getName() != null) {
            item.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            item.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }
        return ItemMapper.toItemDto(itemRepository.save(item));
    }

    @Override
    @Transactional(readOnly = true)
    public ItemWithBookingDto getItemId(Integer userId, Integer id) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Вещь с данным id не найдена!"));
        List<Comment> comments = commentRepository.findByItemIdOrderByCreatedDesc(item.getId());
        if (item.getOwner().getId().equals(userId)) {
            Booking last = getLastBooking(item.getId());
            Booking next = getNextBooking(item.getId());
            return ItemMapper.toItemWithBookingDto(comments, last, next, item);
        } else {
            return ItemMapper.toItemWithBookingDto(comments, null, null, item);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemDto> searchItem(String query, Integer size, Integer from) {
        if (query.isEmpty()) {
            return new ArrayList<>();
        }
        return itemRepository.searchItem(query, PageRequest.of(from, size)).stream()
                .map(item -> ItemMapper.toItemDto(item))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemWithBookingDto> getItemByOwner(Integer userId, Integer size, Integer from) {
        return itemRepository.getAllByOwnerId(userId, PageRequest.of(from, size, Sort.by("id")))
                .stream()
                .map(item -> {
                    List<Comment> comments = commentRepository.findByItemIdOrderByCreatedDesc(item.getId());
                    Booking last = getLastBooking(item.getId());
                    Booking next = getNextBooking(item.getId());
                    return ItemMapper.toItemWithBookingDto(comments, last, next, item);
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CommentDto addComment(CommentDto commentDto, Integer userId, Integer itemId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("Пользователь не найден!"));
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ObjectNotFoundException("Вещь с данным id не найдена!"));
        List<Booking> bookingList = bookingRepository.findByBookerIdAndEndBeforeOrderByEndDesc(userId,
                LocalDateTime.now());
        if (bookingList.isEmpty()) {
            throw new BookingException("Пользователь не бронирует!");
        }
        Comment comment = CommentMapper.toComment(commentDto, item, user);
        comment.setCreated(LocalDateTime.now());
        return CommentMapper.toCommentDto(commentRepository.save(comment));
    }

    private Booking getLastBooking(Integer itemId) {
        final Sort last = Sort.by(Sort.Direction.DESC, "start");
        return bookingRepository.findFirstByItemIdAndStatusAndStartIsBefore(itemId, Status.APPROVED, LocalDateTime.now(), last);
    }

    private Booking getNextBooking(Integer itemId) {
        final Sort next = Sort.by(Sort.Direction.ASC, "start");
        return bookingRepository.findFirstByItemIdAndStatusAndStartIsAfter(itemId, Status.APPROVED, LocalDateTime.now(), next);
    }
}