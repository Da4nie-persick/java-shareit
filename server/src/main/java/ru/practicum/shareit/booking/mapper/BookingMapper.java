package ru.practicum.shareit.booking.mapper;

import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

public class BookingMapper {
    public static final Booking toBooking(BookingDtoRequest bookingDtoRequest, Item item, User user) {
        Booking booking = new Booking();
        booking.setStart(bookingDtoRequest.getStart());
        booking.setEnd(bookingDtoRequest.getEnd());
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStatus(Status.WAITING);
        return booking;
    }

    public static final BookingDtoResponse toBookingDto(Booking booking) {
        BookingDtoResponse bookingDtoResponse = new BookingDtoResponse();
        bookingDtoResponse.setId(booking.getId());
        bookingDtoResponse.setStart(booking.getStart());
        bookingDtoResponse.setEnd(booking.getEnd());
        bookingDtoResponse.setItem(new BookingDtoResponse.Item(booking.getItem().getId(),
                booking.getItem().getName()));
        bookingDtoResponse.setBooker(new BookingDtoResponse.User(booking.getBooker().getId(),
                booking.getBooker().getName()));
        bookingDtoResponse.setStatus(booking.getStatus());
        return bookingDtoResponse;
    }
}
