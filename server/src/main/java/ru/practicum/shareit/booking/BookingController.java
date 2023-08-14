package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;

@Validated
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public BookingDtoResponse create(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                     @RequestBody BookingDtoRequest bookingDto) {
        return bookingService.create(bookingDto, userId);
    }

    @PatchMapping(value = "/{bookingId}")
    public BookingDtoResponse approvedOrRejected(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                                 @RequestParam Boolean approved,
                                                 @PathVariable Integer bookingId) {
        return bookingService.approvedOrRejected(approved, bookingId, userId);
    }

    @GetMapping(value = "/{bookingId}")
    public BookingDtoResponse getBookingId(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                           @PathVariable Integer bookingId) {
        return bookingService.getBookingId(bookingId, userId);
    }

    @GetMapping
    public List<BookingDtoResponse> getAllByUserId(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                                   @RequestParam String state,
                                                   @RequestParam Integer from,
                                                   @RequestParam Integer size) {
        return bookingService.getAllByUserId(userId, state, size, from);
    }

    @GetMapping(value = "/owner")
    public List<BookingDtoResponse> getAllByOwner(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                                  @RequestParam String state,
                                                  @RequestParam Integer from,
                                                  @RequestParam Integer size) {
        return bookingService.getAllByOwner(userId, state, size, from);
    }
}