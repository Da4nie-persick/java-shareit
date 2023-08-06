package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import javax.persistence.EntityManager;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class BookingServiceIntegrationTest {
    private final BookingService bookingService;
    private final EntityManager manager;

    @Test
    public void getAllByUserId() {
        User user3 = new User(null, "user2", "user2@yandex.ru");
        User booker = new User(null, "booker", "user2@ygmai.ru");
        List<User> sourceUser = List.of(user3, booker);

        Item item1 = new Item(null, "item1", "description item1", true, user3, null);
        Item item2 = new Item(null, "item2", "description item1", true, user3, null);
        List<Item> sourceItem = List.of(item1, item2);

        Booking booking1 = new Booking(null, LocalDateTime.now(), LocalDateTime.now().plusMinutes(8), item1, booker, Status.APPROVED);
        Booking booking2 = new Booking(null, LocalDateTime.now(), LocalDateTime.now().plusMinutes(10), item2, booker, Status.APPROVED);
        List<Booking> sourceBooking = List.of(booking1, booking2);

        for (User user : sourceUser) {
            manager.persist(user);
        }

        for (Item item : sourceItem) {
            manager.persist(item);
        }

        for (Booking booking : sourceBooking) {
            manager.persist(booking);
        }
        manager.flush();

        List<BookingDtoResponse> list = bookingService.getAllByUserId(booker.getId(), "ALL", 10, 0);

        assertThat(list, hasSize(sourceBooking.size()));
        for (Booking booking : sourceBooking) {
            assertThat(list, Matchers.hasItem(allOf(
                    hasProperty("id", notNullValue()),
                    hasProperty("item", instanceOf(BookingDtoResponse.Item.class)),
                    hasProperty("item", hasProperty("name", equalTo(booking.getItem().getName()))),
                    hasProperty("booker", instanceOf(BookingDtoResponse.User.class)),
                    hasProperty("booker", hasProperty("name", equalTo(booking.getBooker().getName()))))));
        }
    }
}