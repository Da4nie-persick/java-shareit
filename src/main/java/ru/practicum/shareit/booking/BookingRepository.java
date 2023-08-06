package ru.practicum.shareit.booking;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Integer> {
    List<Booking> findAllByBookerIdOrderByStartDesc(Integer userId, Pageable pageable);

    @Query(value = "select * from bookings b join items i on b.item_id = i.id where booker_id = ?1 " +
            "and end_date < ?2 order by start_date desc", nativeQuery = true)
    List<Booking> findAllByBookerIdPast(Integer userId, LocalDateTime end, Pageable pageable);

    @Query(value = "select * from bookings b join items i on b.item_id = i.id where booker_id = ?1 " +
            "and start_date > ?2 order by start_date desc", nativeQuery = true)
    List<Booking> findAllByBookerIdFuture(Integer userId, LocalDateTime start, Pageable pageable);

    @Query(value = "select * from bookings b join items i on b.item_id = i.id where booker_id = ?1 " +
            "and ?2 between start_date and end_date order by start_date desc", nativeQuery = true)
    List<Booking> findAllByBookerIdCurrent(Integer userId, LocalDateTime start, LocalDateTime end, Pageable pageable);

    List<Booking> findAllByBookerIdAndStatusOrderByStartDesc(Integer userId, Status status, Pageable pageable);

    List<Booking> findByBookerIdAndEndBeforeOrderByEndDesc(Integer userId, LocalDateTime end);

    @Query(value = "select * from bookings b join items i on b.item_id = i.id where owner_id = ?1 order by start_date desc",
            nativeQuery = true)
    List<Booking> getAll(Integer userId, Pageable pageable);

    @Query(value = "select * from bookings b join items i on b.item_id = i.id where owner_id = ?1 " +
            "and end_date < ?2 order by start_date desc", nativeQuery = true)
    List<Booking> getPast(Integer userId, LocalDateTime end, Pageable pageable);

    @Query(value = "select * from bookings b join items i on b.item_id = i.id where owner_id = ?1 " +
            "and start_date > ?2 order by start_date desc", nativeQuery = true)
    List<Booking> getFuture(Integer userId, LocalDateTime start, Pageable pageable);

    @Query(value = "select * from bookings b join items i on b.item_id = i.id where owner_id = ?1 " +
            "and ?2 between start_date and end_date order by start_date desc", nativeQuery = true)
    List<Booking> getCurrent(Integer userId, LocalDateTime dateTime, Pageable pageable);

    @Query(value = "select * from bookings b join items i on b.item_id = i.id where owner_id = ?1 " +
            "and status = ?2 order by start_date desc", nativeQuery = true)
    List<Booking> getWaitingOrReject(Integer userId, String status, Pageable pageable);

    Booking findFirstByItemIdAndStatusAndStartIsBefore(Integer itemId, Status status, LocalDateTime start, Sort sort);

    Booking findFirstByItemIdAndStatusAndStartIsAfter(Integer itemId, Status status, LocalDateTime now, Sort sort);
}
