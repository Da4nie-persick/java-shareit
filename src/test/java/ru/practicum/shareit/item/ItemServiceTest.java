package ru.practicum.shareit.item;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.exception.BookingException;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithBookingDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ItemServiceTest {
    @Mock
    ItemRepository itemRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    BookingRepository bookingRepository;
    @Mock
    CommentRepository commentRepository;
    ItemService itemService;
    User user, otherUser;
    Item item;
    Comment comment;
    Booking last, next;

    @BeforeEach
    public void beforeEach() {
        itemService = new ItemServiceImpl(itemRepository, userRepository, bookingRepository, commentRepository);
        user = new User(1, "user2", "user2@yandex.ru");
        otherUser = new User(2, "Misha", "mIsha@yandex.ru");
        item = new Item(null, "item1", "description item1", true, user, null);
        comment = new Comment(null, "вот такой коментарий", item, user, LocalDateTime.now());
        last = new Booking(null, LocalDateTime.now().minusMinutes(10), LocalDateTime.now(), item, user, Status.APPROVED);
        next = new Booking(null, LocalDateTime.now().plusMinutes(2), LocalDateTime.now().plusMinutes(5), item, otherUser, Status.APPROVED);
    }

    @Test
    public void createItemNotFountException() {
        when(userRepository.findById(any())).thenReturn(Optional.empty());
        Exception exception = Assertions.assertThrows(ObjectNotFoundException.class,
                () -> itemService.create(ItemMapper.toItemDto(item), 3));
        Assertions.assertEquals("Пользователь не найден!", exception.getMessage());
    }

    @Test
    public void createItemSuccessfulTest() {
        when(itemRepository.save(any())).thenReturn(item);
        when(userRepository.findById(any())).thenReturn(Optional.ofNullable(item.getOwner()));
        ItemDto itemDto = itemService.create(ItemMapper.toItemDto(item), user.getId());
        Assertions.assertEquals(itemDto.getDescription(), item.getDescription());
    }

    @Test
    public void updateItemNotFountException() {
        when(itemRepository.findById(any())).thenReturn(Optional.empty());
        Exception exception = Assertions.assertThrows(ObjectNotFoundException.class,
                () -> itemService.update(1, ItemMapper.toItemDto(item), user.getId()));
        Assertions.assertEquals("Вещь с данным id не найдена!", exception.getMessage());
    }

    @Test
    public void notUpdateOtherOwner() {
        when(itemRepository.findById(any())).thenReturn(Optional.of(item));
        Exception exception = Assertions.assertThrows(ObjectNotFoundException.class,
                () -> itemService.update(1, ItemMapper.toItemDto(item), otherUser.getId()));
        Assertions.assertEquals("Пользователь не найден!", exception.getMessage());
    }

    @Test
    public void updateTest() {
        when(itemRepository.findById(any())).thenReturn(Optional.of(item));
        when(itemRepository.save(any())).thenReturn(item);
        ItemDto itemDto1 = new ItemDto(null, "item2", "description item1", true, null);
        ItemDto itemDto = itemService.update(item.getId(), itemDto1, user.getId());
        Assertions.assertEquals("item2", itemDto.getName());
        Assertions.assertEquals("description item1", itemDto.getDescription());
    }

    @Test
    public void getItemIdNotFountException() {
        when(itemRepository.findById(any())).thenReturn(Optional.empty());

        Exception exception = Assertions.assertThrows(ObjectNotFoundException.class,
                () -> itemService.getItemId(1, 1));
        Assertions.assertEquals("Вещь с данным id не найдена!", exception.getMessage());
    }

    @Test
    public void getItemIdSuccessfulTest() {
        when(itemRepository.findById(any()))
                .thenReturn(Optional.of(item));

        when(bookingRepository.findFirstByItemIdAndStatusAndStartIsBefore(
                eq(item.getId()), any(), any(), any()))
                .thenReturn(last);

        when(bookingRepository.findFirstByItemIdAndStatusAndStartIsAfter(
                eq(item.getId()), any(), any(), any()))
                .thenReturn(next);

        when(commentRepository.findByItemIdOrderByCreatedDesc(item.getId()))
                .thenReturn(List.of(comment));

        ItemWithBookingDto itemWithBookingDto = itemService.getItemId(1, item.getId());
        Assertions.assertEquals(itemWithBookingDto.getName(), item.getName());
        Assertions.assertEquals(itemWithBookingDto.getDescription(), item.getDescription());
        Assertions.assertEquals(itemWithBookingDto.getLastBooking().getBookerId(), 1);
        Assertions.assertEquals(itemWithBookingDto.getNextBooking().getBookerId(), 2);
        Assertions.assertEquals(itemWithBookingDto.getComments().size(), 1);
    }

    @Test
    public void getItemByOwnerSuccessfulTest() {
        when(itemRepository.getAllByOwnerId(any(), any()))
                .thenReturn(List.of(item));

        when(bookingRepository.findFirstByItemIdAndStatusAndStartIsBefore(
                eq(item.getId()), any(), any(), any()))
                .thenReturn(last);

        when(bookingRepository.findFirstByItemIdAndStatusAndStartIsAfter(
                eq(item.getId()), any(), any(), any()))
                .thenReturn(next);

        when(commentRepository.findByItemIdOrderByCreatedDesc(item.getId()))
                .thenReturn(List.of(comment));

        List<ItemWithBookingDto> list = itemService.getItemByOwner(1, 10, 0);
        Assertions.assertEquals(list.size(), 1);
        Assertions.assertEquals(list.get(0).getName(), item.getName());
        Assertions.assertEquals(list.get(0).getDescription(), item.getDescription());
        Assertions.assertEquals(list.get(0).getLastBooking().getBookerId(), 1);
        Assertions.assertEquals(list.get(0).getNextBooking().getBookerId(), 2);
        Assertions.assertEquals(list.get(0).getComments().size(), 1);
    }

    @Test
    public void searchItemSuccessfulTest() {
        when(itemRepository.searchItem(any(), any()))
                .thenReturn(List.of(item));

        List<ItemDto> list = itemService.searchItem("iTem1", 10, 0);
        Assertions.assertEquals(list.size(), 1);
        Assertions.assertEquals(list.get(0).getName(), item.getName());
        Assertions.assertEquals(list.get(0).getDescription(), item.getDescription());
    }

    @Test
    public void searchItemSuccessfulTest2() {
        List<ItemDto> list = itemService.searchItem("", 10, 0);
        Assertions.assertEquals(list.size(), 0);
    }

    @Test
    public void addCommentSuccessfulTest() {
        when(userRepository.findById(any()))
                .thenReturn(Optional.of(otherUser));

        when(itemRepository.findById(any()))
                .thenReturn(Optional.of(item));

        when(bookingRepository.findByBookerIdAndEndBeforeOrderByEndDesc(
                any(), any())).thenReturn(List.of(last));

        when(commentRepository.save(any()))
                .thenReturn(comment);

        CommentDto commentDto = itemService.addComment(CommentMapper.toCommentDto(comment), user.getId(), item.getId());
        Assertions.assertEquals(commentDto.getText(), comment.getText());
        Assertions.assertEquals(commentDto.getAuthorName(), comment.getAuthor().getName());
    }

    @Test
    public void addCommentUserNotFoundException() {
        when(userRepository.findById(any()))
                .thenReturn(Optional.empty());

        Exception exception = Assertions.assertThrows(ObjectNotFoundException.class,
                () -> itemService.addComment(CommentMapper.toCommentDto(comment), user.getId(), item.getId()));
        Assertions.assertEquals("Пользователь не найден!", exception.getMessage());
    }

    @Test
    public void addCommentItemNotFoundException() {
        when(userRepository.findById(any()))
                .thenReturn(Optional.of(otherUser));

        Exception exception = Assertions.assertThrows(ObjectNotFoundException.class,
                () -> itemService.addComment(CommentMapper.toCommentDto(comment), user.getId(), item.getId()));
        Assertions.assertEquals("Вещь с данным id не найдена!", exception.getMessage());
    }

    @Test
    public void addCommentBookingNotFoundException() {
        when(userRepository.findById(any()))
                .thenReturn(Optional.of(otherUser));

        when(itemRepository.findById(any()))
                .thenReturn(Optional.of(item));

        Exception exception = Assertions.assertThrows(BookingException.class,
                () -> itemService.addComment(CommentMapper.toCommentDto(comment), user.getId(), item.getId()));
        Assertions.assertEquals("Пользователь не бронирует!", exception.getMessage());
    }
}