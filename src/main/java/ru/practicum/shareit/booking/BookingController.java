package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Validated
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public BookingDtoResponse create(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                     @Valid @RequestBody BookingDtoRequest bookingDto) {
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
                                                   @RequestParam(defaultValue = "ALL") String state,
                                                   @RequestParam(defaultValue = "0", required = false) @PositiveOrZero Integer from,
                                                   @RequestParam(defaultValue = "10", required = false) @Positive Integer size) {
        return bookingService.getAllByUserId(userId, state, size, from);
    }

    @GetMapping(value = "/owner")
    public List<BookingDtoResponse> getAllByOwner(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                                  @RequestParam(defaultValue = "ALL") String state,
                                                  @RequestParam(defaultValue = "0", required = false) @PositiveOrZero Integer from,
                                                  @RequestParam(defaultValue = "10", required = false) @Positive Integer size) {
        return bookingService.getAllByOwner(userId, state, size, from);
    }
}
