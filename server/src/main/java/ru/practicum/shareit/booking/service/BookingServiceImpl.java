package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.exception.BookingException;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    @Transactional
    public BookingDtoResponse create(BookingDtoRequest bookingDto, Integer userId) {
        Item item = itemRepository.findById(bookingDto.getItemId())
                .orElseThrow(() -> new ObjectNotFoundException("Вещь не найдена!"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("Пользователь не найден!"));
        if (item.getOwner().getId().equals(userId)) {
            throw new ObjectNotFoundException("Владелец не может забронировать вещь!");
        }
        if (!item.getAvailable()) {
            throw new BookingException("Забронирован!");
        }
        if (bookingDto.getStart().isBefore(LocalDateTime.now())
                || bookingDto.getEnd().isBefore(bookingDto.getStart())
                || (bookingDto.getEnd()).equals(bookingDto.getStart())) {
            throw new BookingException("Проверьте время!");
        }
        Booking booking = BookingMapper.toBooking(bookingDto, item, user);

        return BookingMapper.toBookingDto(bookingRepository.save(booking));
    }

    @Override
    @Transactional
    public BookingDtoResponse approvedOrRejected(Boolean approved, Integer bookingId, Integer userId) {
        if (approved == null) {
            throw new BookingException("Не корректный approved");
        }
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ObjectNotFoundException("Бронь не найдена!"));
        if (!booking.getItem().getOwner().getId().equals(userId)) {
            throw new ObjectNotFoundException("Пользователь не является владельцем!");
        }
        if (!booking.getStatus().equals(Status.WAITING)) {
            throw new BookingException("Статус бронирования не WAITING");
        }
        booking.setStatus(approved ? Status.APPROVED : Status.REJECTED);
        return BookingMapper.toBookingDto(bookingRepository.save(booking));
    }

    @Override
    @Transactional(readOnly = true)
    public BookingDtoResponse getBookingId(Integer bookingId, Integer userId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ObjectNotFoundException("Бронь не найдена!"));
        if (booking.getBooker().getId().equals(userId) || booking.getItem().getOwner().getId().equals(userId)) {
            return BookingMapper.toBookingDto(booking);
        }
        throw new ObjectNotFoundException("Не имеет доступ к информации по бронированию!");
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingDtoResponse> getAllByUserId(Integer userId, String state, Integer size, Integer from) {
        if (!userRepository.existsById(userId)) {
            throw new ObjectNotFoundException("Пользователь не найден!");
        }
        if (Arrays.stream(State.values()).noneMatch(s -> s.toString().equals(state))) {
            throw new IllegalArgumentException("Unknown state: " + state);
        }
        List<Booking> bookingList = new ArrayList<>();
        Pageable pageable = PageRequest.of(from / size, size);
        switch (State.valueOf(state)) {
            case ALL:
                bookingList = bookingRepository.findAllByBookerIdOrderByStartDesc(userId, pageable);
                break;
            case CURRENT:
                bookingList = bookingRepository.findAllByBookerIdCurrent(userId, LocalDateTime.now(), LocalDateTime.now(), pageable);
                break;
            case PAST:
                bookingList = bookingRepository.findAllByBookerIdPast(userId, LocalDateTime.now(), pageable);
                break;
            case FUTURE:
                bookingList = bookingRepository.findAllByBookerIdFuture(userId, LocalDateTime.now(), pageable);
                break;
            case WAITING:
                bookingList = bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(userId, Status.WAITING, pageable);
                break;
            case REJECTED:
                bookingList = bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(userId, Status.REJECTED, pageable);
                break;
        }
        return bookingList.stream().map(BookingMapper::toBookingDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingDtoResponse> getAllByOwner(Integer userId, String state, Integer size, Integer from) {
        if (!userRepository.existsById(userId)) {
            throw new ObjectNotFoundException("Пользователь не найден!");
        }
        if (Arrays.stream(State.values()).noneMatch(s -> s.toString().equals(state))) {
            throw new IllegalArgumentException("Unknown state: " + state);
        }
        List<Booking> bookingList = new ArrayList<>();
        Pageable pageable = PageRequest.of(from / size, size);
        switch (State.valueOf(state)) {
            case ALL:
                bookingList = bookingRepository.getAll(userId, pageable);
                break;
            case CURRENT:
                bookingList = bookingRepository.getCurrent(userId, LocalDateTime.now(), pageable);
                break;
            case PAST:
                bookingList = bookingRepository.getPast(userId, LocalDateTime.now(), pageable);
                break;
            case FUTURE:
                bookingList = bookingRepository.getFuture(userId, LocalDateTime.now(), pageable);
                break;
            case WAITING:
                bookingList = bookingRepository.getWaitingOrReject(userId, Status.WAITING.toString(), pageable);
                break;
            case REJECTED:
                bookingList = bookingRepository.getWaitingOrReject(userId, Status.REJECTED.toString(), pageable);
                break;
        }
        return bookingList.stream().map(BookingMapper::toBookingDto).collect(Collectors.toList());
    }
}
