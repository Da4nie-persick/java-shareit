package ru.practicum.shareit.booking.service;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;

import java.util.List;

public interface BookingService {
    BookingDtoResponse create(BookingDtoRequest bookingDto, Integer userId);

    BookingDtoResponse approvedOrRejected(Boolean approved, Integer bookingId, Integer userId);

    BookingDtoResponse getBookingId(Integer bookingId, Integer userId);

    List<BookingDtoResponse> getAllByUserId(Integer userId, String state, Integer size, Integer from);

    List<BookingDtoResponse> getAllByOwner(Integer userId, String state, Integer size, Integer from);
}
