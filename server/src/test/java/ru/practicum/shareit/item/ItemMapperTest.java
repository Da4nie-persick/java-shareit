package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithBookingDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

public class ItemMapperTest {
    private Item item;
    private ItemDto itemDto;
    private User user;
    private List<Comment> list;
    private Booking last, next;

    @BeforeEach
    public void beforeEach() {
        user = new User(1, "user1", "email@yandex.ru");
        itemDto = new ItemDto(1, "itemDto", "description item1", true, null);
        item = new Item(1, "item1", "description item1", true, user, null);
        list = List.of(new Comment(null, "вот такой коментарий", item, user, LocalDateTime.now()));
        last = new Booking(null, LocalDateTime.now().minusMinutes(10), LocalDateTime.now(), item, user, Status.APPROVED);
        next = new Booking(null, LocalDateTime.now().plusMinutes(2), LocalDateTime.now().plusMinutes(5), item, user, Status.APPROVED);
    }

    @Test
    public void toItemTest() {
        Item test = ItemMapper.toItem(itemDto, user);
        assertThat(test.getId(), equalTo(1));
        assertThat(test.getName(), equalTo("itemDto"));
        assertThat(test.getDescription(), equalTo("description item1"));
        assertThat(test.getAvailable(), equalTo(true));
        assertThat(test.getOwner().getName(), equalTo("user1"));
    }

    @Test
    public void toItemDtoTest() {
        ItemDto test = ItemMapper.toItemDto(item);
        assertThat(test.getId(), equalTo(1));
        assertThat(test.getName(), equalTo("item1"));
        assertThat(test.getDescription(), equalTo("description item1"));
        assertThat(test.getAvailable(), equalTo(true));
    }

    @Test
    public void toItemWithBookingDtoTest() {
        ItemWithBookingDto test = ItemMapper.toItemWithBookingDto(list, last, next, item);
        assertThat(test.getId(), equalTo(1));
        assertThat(test.getName(), equalTo("item1"));
        assertThat(test.getDescription(), equalTo("description item1"));
        assertThat(test.getAvailable(), equalTo(true));
        assertThat(test.getLastBooking().getBookerId(), equalTo(1));
        assertThat(test.getNextBooking().getBookerId(), equalTo(1));
        assertThat(test.getComments(), hasSize(1));
        assertThat(test.getComments().get(0).getText(), equalTo("вот такой коментарий"));
    }
}