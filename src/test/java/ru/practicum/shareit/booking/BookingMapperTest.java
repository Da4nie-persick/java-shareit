package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.time.Month;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class BookingMapperTest {
    private Item item;
    private User user;
    private BookingDtoRequest bookingDtoRequest;
    private Booking booking;

    @BeforeEach
    public void beforeEach() {
        user = new User(1, "user1", "email@yandex.ru");
        item = new Item(1, "item1", "description item1", true, user, null);
        bookingDtoRequest = new BookingDtoRequest(
                LocalDateTime.of(2023, Month.FEBRUARY, 9, 10, 10),
                LocalDateTime.of(2023, Month.FEBRUARY, 9, 10, 13),
                item.getId());

        booking = new Booking(1,
                LocalDateTime.of(2023, Month.FEBRUARY, 9, 10, 9),
                LocalDateTime.of(2023, Month.FEBRUARY, 9, 10, 13),
                item,
                user,
                Status.APPROVED);
    }

    @Test
    public void toBookingTest() {
        Booking test = BookingMapper.toBooking(bookingDtoRequest, item, user);
        assertThat(test.getStart(), equalTo(LocalDateTime.of(2023, Month.FEBRUARY, 9, 10, 10)));
        assertThat(test.getEnd(), equalTo(LocalDateTime.of(2023, Month.FEBRUARY, 9, 10, 13)));
        assertThat(test.getItem().getName(), equalTo("item1"));
        assertThat(test.getBooker().getName(), equalTo("user1"));
        assertThat(test.getStatus(), equalTo(Status.WAITING));
    }

    @Test
    public void toBookingDtoTest() {
        BookingDtoResponse test = BookingMapper.toBookingDto(booking);
        assertThat(test.getStart(), equalTo(LocalDateTime.of(2023, Month.FEBRUARY, 9, 10, 9)));
        assertThat(test.getEnd(), equalTo(LocalDateTime.of(2023, Month.FEBRUARY, 9, 10, 13)));
        assertThat(test.getItem().getName(), equalTo("item1"));
        assertThat(test.getBooker().getName(), equalTo("user1"));
        assertThat(test.getStatus(), equalTo(Status.APPROVED));
    }
}