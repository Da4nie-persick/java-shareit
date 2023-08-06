package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

@DataJpaTest
public class BookingRepositoryTest {
    @Autowired
    private TestEntityManager em;

    @Autowired
    BookingRepository bookingRepository;

    User owner = new User(null, "user2", "user2@yandex.ru");
    User booker = new User(null, "booker", "user2@ygmai.ru");

    Item item1 = new Item(null, "item1", "description item1", true, owner, null);
    Item item2 = new Item(null, "item2", "description item1", true, owner, null);

    @Test
    public void contextLoads() {
        Assertions.assertNotNull(em);
    }

    @Test
    public void findAllByBookerIdPast() {
        Booking booking1 = new Booking(null, LocalDateTime.now(), LocalDateTime.now().plusMinutes(7), item1, booker, Status.APPROVED);
        Booking booking2 = new Booking(null, LocalDateTime.now(), LocalDateTime.now().plusMinutes(7), item2, booker, Status.APPROVED);
        em.persist(owner);
        em.persist(booker);
        em.persist(item1);
        em.persist(item2);
        em.persist(booking1);
        em.persist(booking2);
        List<Booking> bookings = bookingRepository.findAllByBookerIdPast(booker.getId(),
                LocalDateTime.now().plusMinutes(9),
                PageRequest.of(0 / 10, 10));
        assertThat(bookings, hasSize(2));
        assertThat(bookings.get(0), equalTo(booking1));
        assertThat(bookings.get(1), equalTo(booking2));
    }

    @Test
    public void findAllByBookerIdFuture() {
        Booking booking1 = new Booking(null, LocalDateTime.now(), LocalDateTime.now().plusMinutes(7), item1, booker, Status.APPROVED);
        Booking booking2 = new Booking(null, LocalDateTime.now(), LocalDateTime.now().plusMinutes(7), item2, booker, Status.APPROVED);
        em.persist(owner);
        em.persist(booker);
        em.persist(item1);
        em.persist(item2);
        em.persist(booking1);
        em.persist(booking2);
        List<Booking> bookings = bookingRepository.findAllByBookerIdFuture(booker.getId(),
                LocalDateTime.now().minusMinutes(1),
                PageRequest.of(0 / 10, 10));
        assertThat(bookings, hasSize(2));
        assertThat(bookings.get(0), equalTo(booking1));
        assertThat(bookings.get(1), equalTo(booking2));
    }

    @Test
    public void findAllByBookerIdCurrent() {
        Booking booking1 = new Booking(null,
                LocalDateTime.of(2022, Month.FEBRUARY, 2, 10, 10),
                LocalDateTime.of(2022, Month.FEBRUARY, 2, 10, 11),
                item1,
                booker,
                Status.APPROVED);

        Booking booking2 = new Booking(null,
                LocalDateTime.of(2022, Month.FEBRUARY, 9, 10, 10),
                LocalDateTime.of(2022, Month.FEBRUARY, 4, 10, 18),
                item2,
                booker,
                Status.APPROVED);

        em.persist(owner);
        em.persist(booker);
        em.persist(item1);
        em.persist(item2);
        em.persist(booking1);
        em.persist(booking2);

        List<Booking> bookings = bookingRepository.findAllByBookerIdCurrent(booker.getId(),
                LocalDateTime.of(2022, Month.FEBRUARY, 2, 10, 10),
                LocalDateTime.of(2022, Month.FEBRUARY, 2, 10, 13),
                PageRequest.of(0 / 10, 10));
        assertThat(bookings, hasSize(1));
        assertThat(bookings.get(0), equalTo(booking1));
    }

    @Test
    public void getAllTest() {
        Booking booking1 = new Booking(null, LocalDateTime.now(), LocalDateTime.now().plusMinutes(7), item1, booker, Status.APPROVED);
        Booking booking2 = new Booking(null, LocalDateTime.now(), LocalDateTime.now().plusMinutes(7), item2, booker, Status.APPROVED);

        em.persist(owner);
        em.persist(booker);
        em.persist(item1);
        em.persist(item2);
        em.persist(booking1);
        em.persist(booking2);

        List<Booking> bookings = bookingRepository.getAll(owner.getId(), PageRequest.of(0 / 10, 10));
        assertThat(bookings, hasSize(2));
        assertThat(bookings.get(0), equalTo(booking1));
        assertThat(bookings.get(1), equalTo(booking2));
    }

    @Test
    public void getPastTest() {
        Booking booking1 = new Booking(null, LocalDateTime.now(), LocalDateTime.now().plusMinutes(7), item1, booker, Status.APPROVED);
        Booking booking2 = new Booking(null, LocalDateTime.now(), LocalDateTime.now().plusMinutes(90), item2, booker, Status.APPROVED);
        em.persist(owner);
        em.persist(booker);
        em.persist(item1);
        em.persist(item2);
        em.persist(booking1);
        em.persist(booking2);
        List<Booking> bookings = bookingRepository.getPast(owner.getId(),
                LocalDateTime.now().plusMinutes(9),
                PageRequest.of(0 / 10, 10));
        assertThat(bookings, hasSize(1));
        assertThat(bookings.get(0), equalTo(booking1));
    }

    @Test
    public void getFutureTest() {
        Booking booking1 = new Booking(null,
                LocalDateTime.of(2022, Month.FEBRUARY, 2, 10, 10),
                LocalDateTime.of(2022, Month.FEBRUARY, 2, 10, 11),
                item1,
                booker,
                Status.APPROVED);

        Booking booking2 = new Booking(null,
                LocalDateTime.of(2022, Month.FEBRUARY, 1, 10, 10),
                LocalDateTime.of(2022, Month.FEBRUARY, 4, 10, 18),
                item2,
                booker,
                Status.APPROVED);

        em.persist(owner);
        em.persist(booker);
        em.persist(item1);
        em.persist(item2);
        em.persist(booking1);
        em.persist(booking2);
        List<Booking> bookings = bookingRepository.getFuture(owner.getId(),
                LocalDateTime.of(2022, Month.FEBRUARY, 2, 10, 0),
                PageRequest.of(0 / 10, 10));
        assertThat(bookings, hasSize(1));
        assertThat(bookings.get(0), equalTo(booking1));
    }

    @Test
    public void getCurrentTest() {
        Booking booking1 = new Booking(null,
                LocalDateTime.of(2022, Month.FEBRUARY, 2, 10, 10),
                LocalDateTime.of(2022, Month.FEBRUARY, 2, 10, 19),
                item1,
                booker,
                Status.APPROVED);

        Booking booking2 = new Booking(null,
                LocalDateTime.of(2022, Month.FEBRUARY, 1, 10, 10),
                LocalDateTime.of(2022, Month.FEBRUARY, 4, 10, 18),
                item2,
                booker,
                Status.APPROVED);

        em.persist(owner);
        em.persist(booker);
        em.persist(item1);
        em.persist(item2);
        em.persist(booking1);
        em.persist(booking2);
        List<Booking> bookings = bookingRepository.getCurrent(owner.getId(),
                LocalDateTime.of(2022, Month.FEBRUARY, 2, 10, 12),
                PageRequest.of(0 / 10, 10));
        assertThat(bookings, hasSize(2));
        assertThat(bookings.get(0), equalTo(booking1));
        assertThat(bookings.get(1), equalTo(booking2));
    }

    @Test
    public void getWaitingOrRejectTest() {
        Booking booking1 = new Booking(null, LocalDateTime.now(), LocalDateTime.now().plusMinutes(7), item1, booker, Status.WAITING);
        Booking booking2 = new Booking(null, LocalDateTime.now(), LocalDateTime.now().plusMinutes(90), item2, booker, Status.REJECTED);
        em.persist(owner);
        em.persist(booker);
        em.persist(item1);
        em.persist(item2);
        em.persist(booking1);
        em.persist(booking2);
        List<Booking> bookingsR = bookingRepository.getWaitingOrReject(owner.getId(),
                "REJECTED",
                PageRequest.of(0 / 10, 10));
        assertThat(bookingsR, hasSize(1));
        assertThat(bookingsR.get(0), equalTo(booking2));

        List<Booking> bookingsW = bookingRepository.getWaitingOrReject(owner.getId(),
                "WAITING",
                PageRequest.of(0 / 10, 10));
        assertThat(bookingsW, hasSize(1));
        assertThat(bookingsW.get(0), equalTo(booking1));
    }
}