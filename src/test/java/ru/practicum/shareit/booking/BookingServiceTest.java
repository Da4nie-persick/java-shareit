package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.exception.BookingException;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.WARN)
public class BookingServiceTest {
    @Mock
    UserRepository userRepository;
    @Mock
    ItemRepository itemRepository;
    @Mock
    BookingRepository bookingRepository;
    BookingService bookingService;
    User user, otherUser;
    Item item;
    Booking booking1, booking2;

    @BeforeEach
    public void beforeEach() {
        bookingService = new BookingServiceImpl(bookingRepository, userRepository, itemRepository);
        user = new User(1, "user2", "user2@yandex.ru");
        otherUser = new User(2, "Misha", "mIsha@yandex.ru");
        item = new Item(1, "item1", "description item1", true, user, null);
        booking1 = new Booking(null,
                LocalDateTime.of(2022, Month.FEBRUARY, 2, 10, 10),
                LocalDateTime.of(2022, Month.FEBRUARY, 2, 10, 11),
                item,
                user,
                Status.REJECTED);

        booking2 = new Booking(null,
                LocalDateTime.of(2024, Month.FEBRUARY, 9, 10, 10),
                LocalDateTime.of(2024, Month.FEBRUARY, 4, 10, 18),
                item,
                user,
                Status.WAITING);
    }

    @Test
    public void getAllByUserIdStateAll() {
        when(userRepository.existsById(user.getId()))
                .thenReturn(true);

        when(bookingRepository.findAllByBookerIdOrderByStartDesc(anyInt(), any()))
                .thenReturn(List.of(booking1, booking2));

        List<BookingDtoResponse> list = bookingService.getAllByUserId(user.getId(), "ALL", 10, 0);

        Assertions.assertEquals(2, list.size());
        Assertions.assertEquals("item1", list.get(0).getItem().getName());
        Assertions.assertEquals("user2", list.get(0).getBooker().getName());
        Assertions.assertEquals("item1", list.get(1).getItem().getName());
        Assertions.assertEquals("user2", list.get(1).getBooker().getName());
    }

    @Test
    public void getAllByUserIdStateCurrent() {
        when(userRepository.existsById(user.getId()))
                .thenReturn(true);

        when(bookingRepository.findAllByBookerIdCurrent(anyInt(), any(), any(), any()))
                .thenReturn(List.of(booking1, booking2));

        List<BookingDtoResponse> list = bookingService.getAllByUserId(user.getId(), "CURRENT", 10, 0);

        Assertions.assertEquals(2, list.size());
        Assertions.assertEquals("item1", list.get(0).getItem().getName());
        Assertions.assertEquals("user2", list.get(0).getBooker().getName());
        Assertions.assertEquals("item1", list.get(1).getItem().getName());
        Assertions.assertEquals("user2", list.get(1).getBooker().getName());
    }

    @Test
    public void getAllByUserIdStatePast() {
        when(userRepository.existsById(user.getId()))
                .thenReturn(true);

        when(bookingRepository.findAllByBookerIdPast(anyInt(), any(), any()))
                .thenReturn(List.of(booking1));

        List<BookingDtoResponse> list = bookingService.getAllByUserId(user.getId(), "PAST", 10, 0);

        Assertions.assertEquals(1, list.size());
        Assertions.assertEquals("item1", list.get(0).getItem().getName());
        Assertions.assertEquals("user2", list.get(0).getBooker().getName());
    }

    @Test
    public void getAllByUserIdStateFuture() {
        Exception exception = Assertions.assertThrows(ObjectNotFoundException.class,
                () -> bookingService.getAllByUserId(otherUser.getId(), "FUTURE", 10, 0));

        Assertions.assertEquals("Пользователь не найден!", exception.getMessage());
    }

    @Test
    public void getAllByUserIdStateWaiting() {
        when(userRepository.existsById(user.getId()))
                .thenReturn(true);

        when(bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(anyInt(), any(), any()))
                .thenReturn(List.of(booking2));

        List<BookingDtoResponse> list = bookingService.getAllByUserId(user.getId(), "WAITING", 10, 0);

        Assertions.assertEquals(1, list.size());
        Assertions.assertEquals("item1", list.get(0).getItem().getName());
        Assertions.assertEquals("user2", list.get(0).getBooker().getName());
    }

    @Test
    public void getAllByUserIdStateRejected() {
        when(userRepository.existsById(user.getId()))
                .thenReturn(true);

        when(bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(anyInt(), any(), any()))
                .thenReturn(List.of(booking1));

        List<BookingDtoResponse> list = bookingService.getAllByUserId(user.getId(), "REJECTED", 10, 0);

        Assertions.assertEquals(1, list.size());
        Assertions.assertEquals("item1", list.get(0).getItem().getName());
        Assertions.assertEquals("user2", list.get(0).getBooker().getName());
    }

    @Test
    public void getAllByUserIdInvalidState() {
        when(userRepository.existsById(user.getId()))
                .thenReturn(true);
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class,
                () -> bookingService.getAllByUserId(user.getId(), "Staiii", 10, 0));

        Assertions.assertEquals("Unknown state: Staiii", exception.getMessage());
    }

    @Test
    public void getAllByUserIdInvalidSize() {
        when(userRepository.existsById(user.getId()))
                .thenReturn(true);
        Exception exception = Assertions.assertThrows(BookingException.class,
                () -> bookingService.getAllByUserId(user.getId(), "REJECTED", -1, 0));

        Assertions.assertEquals("Не корректные size or from", exception.getMessage());
    }

    @Test
    public void createBookingUserIsOwnerItem() {
        BookingDtoRequest bookingRequest = new BookingDtoRequest(
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now().plusMinutes(1),
                item.getId());

        Booking booking = BookingMapper.toBooking(bookingRequest, item, user);

        when(userRepository.findById(booking.getBooker().getId()))
                .thenReturn(Optional.ofNullable(booking.getBooker()));

        when(itemRepository.findById(bookingRequest.getItemId()))
                .thenReturn(Optional.ofNullable(item));

        when(bookingRepository.save(booking)).thenReturn(booking);

        Exception exception = Assertions.assertThrows(ObjectNotFoundException.class,
                () -> bookingService.create(bookingRequest, 1));

        Assertions.assertEquals("Владелец не может забронировать вещь!", exception.getMessage());
    }

    @Test
    public void createBooked() {
        item.setAvailable(false);
        BookingDtoRequest bookingRequest = new BookingDtoRequest(
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now().plusMinutes(1),
                item.getId());

        Booking booking = BookingMapper.toBooking(bookingRequest, item, otherUser);

        when(userRepository.findById(booking.getBooker().getId()))
                .thenReturn(Optional.ofNullable(booking.getBooker()));

        when(itemRepository.findById(bookingRequest.getItemId()))
                .thenReturn(Optional.ofNullable(item));

        when(bookingRepository.save(booking)).thenReturn(booking);

        Exception exception = Assertions.assertThrows(BookingException.class,
                () -> bookingService.create(bookingRequest, otherUser.getId()));

        Assertions.assertEquals("Забронирован!", exception.getMessage());
    }

    @Test
    public void createBookingItemNotFoundTest() {
        BookingDtoRequest bookingRequest = new BookingDtoRequest(
                LocalDateTime.now().minusMinutes(90),
                LocalDateTime.now().plusMinutes(1),
                item.getId());

        Exception exception = Assertions.assertThrows(ObjectNotFoundException.class,
                () -> bookingService.create(bookingRequest, otherUser.getId()));

        Assertions.assertEquals("Вещь не найдена!", exception.getMessage());
    }

    @Test
    public void createBookingUserNotFoundTest() {
        BookingDtoRequest bookingRequest = new BookingDtoRequest(
                LocalDateTime.now().minusMinutes(90),
                LocalDateTime.now().plusMinutes(1),
                item.getId());

        when(itemRepository.findById(bookingRequest.getItemId()))
                .thenReturn(Optional.ofNullable(item));

        Exception exception = Assertions.assertThrows(ObjectNotFoundException.class,
                () -> bookingService.create(bookingRequest, otherUser.getId()));

        Assertions.assertEquals("Пользователь не найден!", exception.getMessage());
    }

    @Test
    public void approvedOrRejectedTest() {
        when(bookingRepository.save(any())).thenReturn(booking2);

        when(bookingRepository.findById(booking2.getId())).thenReturn(Optional.ofNullable(booking2));

        BookingDtoResponse bookingDto = bookingService.approvedOrRejected(true, booking2.getId(), user.getId());
        Assertions.assertEquals("item1", bookingDto.getItem().getName());
        Assertions.assertEquals("user2", bookingDto.getBooker().getName());
    }

    @Test
    public void approvedOrRejectedDifferentStatus() {
        when(bookingRepository.save(any())).thenReturn(booking1);

        when(bookingRepository.findById(booking1.getId())).thenReturn(Optional.ofNullable(booking1));

        Exception exception = Assertions.assertThrows(BookingException.class,
                () -> bookingService.approvedOrRejected(true, booking1.getId(), user.getId()));

        Assertions.assertEquals("Статус бронирования не WAITING", exception.getMessage());
    }

    @Test
    public void approvedOrRejectedBookingNotFound() {
        Exception exception = Assertions.assertThrows(ObjectNotFoundException.class,
                () -> bookingService.approvedOrRejected(true, booking2.getId(), user.getId()));
        Assertions.assertEquals("Бронь не найдена!", exception.getMessage());
    }

    @Test
    public void approvedOrRejectedUserNotOwnerTest() {
        when(bookingRepository.save(any())).thenReturn(booking2);

        when(bookingRepository.findById(booking2.getId())).thenReturn(Optional.ofNullable(booking2));

        Exception exception = Assertions.assertThrows(ObjectNotFoundException.class,
                () -> bookingService.approvedOrRejected(true, booking2.getId(), otherUser.getId()));
        Assertions.assertEquals("Пользователь не является владельцем!", exception.getMessage());
    }

    @Test
    public void approvedOrRejectedApprovedInvalidTest() {
        when(bookingRepository.findById(booking2.getId())).thenReturn(Optional.ofNullable(booking2));

        Exception exception = Assertions.assertThrows(BookingException.class,
                () -> bookingService.approvedOrRejected(null, booking2.getId(), otherUser.getId()));
        Assertions.assertEquals("Не корректный approved", exception.getMessage());
    }

    @Test
    public void getBookingId() {
        when(bookingRepository.findById(booking1.getId())).thenReturn(Optional.ofNullable(booking1));

        BookingDtoResponse bookingDtoResponse = bookingService.getBookingId(booking1.getId(), user.getId());

        Assertions.assertEquals(booking1.getBooker().getName(), bookingDtoResponse.getBooker().getName());
        Assertions.assertEquals(booking1.getItem().getName(), bookingDtoResponse.getItem().getName());
    }

    @Test
    public void getBookingIdNotOwner() {
        when(bookingRepository.findById(booking1.getId())).thenReturn(Optional.ofNullable(booking1));

        Exception exception = Assertions.assertThrows(ObjectNotFoundException.class,
                () -> bookingService.getBookingId(booking1.getId(), otherUser.getId()));

        Assertions.assertEquals("Не имеет доступ к информации по бронированию!", exception.getMessage());
    }

    @Test
    public void getBookingIdBookedNotFoundTest() {
        Exception exception = Assertions.assertThrows(ObjectNotFoundException.class,
                () -> bookingService.getBookingId(booking1.getId(), otherUser.getId()));

        Assertions.assertEquals("Бронь не найдена!", exception.getMessage());
    }

    @Test
    public void getAllByOwnerStateAll() {
        when(userRepository.existsById(user.getId()))
                .thenReturn(true);

        when(bookingRepository.getAll(anyInt(), any()))
                .thenReturn(List.of(booking1, booking2));

        List<BookingDtoResponse> list = bookingService.getAllByOwner(user.getId(), "ALL", 10, 0);

        Assertions.assertEquals(2, list.size());
        Assertions.assertEquals("item1", list.get(0).getItem().getName());
        Assertions.assertEquals("user2", list.get(0).getBooker().getName());
        Assertions.assertEquals("item1", list.get(1).getItem().getName());
        Assertions.assertEquals("user2", list.get(1).getBooker().getName());
    }

    @Test
    public void getAllByOwnerStateCurrentInvalidSizeAndFrom() {
        when(userRepository.existsById(user.getId()))
                .thenReturn(true);

        when(bookingRepository.findAllByBookerIdCurrent(anyInt(), any(), any(), any()))
                .thenReturn(List.of(booking1, booking2));

        Exception exception = Assertions.assertThrows(BookingException.class,
                () -> bookingService.getAllByOwner(user.getId(), "CURRENT", 10, -2));

        Assertions.assertEquals("Не корректные size or from", exception.getMessage());
    }

    @Test
    public void getAllByOwnerStatePast() {
        when(userRepository.existsById(user.getId()))
                .thenReturn(true);

        when(bookingRepository.getPast(anyInt(), any(), any()))
                .thenReturn(List.of(booking1));

        List<BookingDtoResponse> list = bookingService.getAllByOwner(user.getId(), "PAST", 10, 0);

        Assertions.assertEquals(1, list.size());
        Assertions.assertEquals("item1", list.get(0).getItem().getName());
        Assertions.assertEquals("user2", list.get(0).getBooker().getName());
    }

    @Test
    public void getAllByOwnerStateFuture() {
        when(userRepository.existsById(user.getId()))
                .thenReturn(true);

        when(bookingRepository.getFuture(anyInt(), any(), any()))
                .thenReturn(List.of(booking2));

        List<BookingDtoResponse> list = bookingService.getAllByOwner(user.getId(), "FUTURE", 10, 0);

        Assertions.assertEquals(1, list.size());
        Assertions.assertEquals("item1", list.get(0).getItem().getName());
        Assertions.assertEquals("user2", list.get(0).getBooker().getName());
    }

    @Test
    public void getAllByOwnerStateWaitingInvalidUserId() {
        when(userRepository.existsById(user.getId()))
                .thenReturn(true);

        when(bookingRepository.getWaitingOrReject(anyInt(), any(), any()))
                .thenReturn(List.of(booking2));

        Exception exception = Assertions.assertThrows(ObjectNotFoundException.class,
                () -> bookingService.getAllByOwner(otherUser.getId(), "WAITING", 10, 0));

        Assertions.assertEquals("Пользователь не найден!", exception.getMessage());
    }

    @Test
    public void getAllByOwnerStateRejected() {
        when(userRepository.existsById(user.getId()))
                .thenReturn(true);

        when(bookingRepository.getWaitingOrReject(anyInt(), any(), any()))
                .thenReturn(List.of(booking1));

        List<BookingDtoResponse> list = bookingService.getAllByOwner(user.getId(), "REJECTED", 10, 0);

        Assertions.assertEquals(1, list.size());
        Assertions.assertEquals("item1", list.get(0).getItem().getName());
        Assertions.assertEquals("user2", list.get(0).getBooker().getName());
    }
}