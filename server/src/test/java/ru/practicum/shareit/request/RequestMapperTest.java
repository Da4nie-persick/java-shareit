package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.mapper.RequestMapper;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

public class RequestMapperTest {
    private ItemRequest itemRequest;
    private ItemRequestDto itemRequestDto;
    private User user;
    private List<Item> list;

    @BeforeEach
    public void beforeEach() {
        user = new User(1, "user1", "email@yandex.ru");

        itemRequest = new ItemRequest(1, "описание", user, LocalDateTime.now());

        itemRequestDto = new ItemRequestDto(2,
                "описание222",
                LocalDateTime.now().plusMinutes(1),
                List.of(new ItemDto(
                        null,
                        "item1",
                        "description item1",
                        true,
                        null))
        );

        list = List.of(new Item(
                null,
                "item1",
                "description item1",
                true,
                user,
                itemRequest));
    }

    @Test
    public void toItemRequestTest() {
        ItemRequest test = RequestMapper.toItemRequest(itemRequestDto, user);
        assertThat(test.getId(), equalTo(2));
        assertThat(test.getDescription(), equalTo("описание222"));
        assertThat(test.getRequester().getName(), equalTo("user1"));
    }

    @Test
    public void toItemRequestDtoCreateTest() {
        ItemRequestDto test = RequestMapper.toItemRequestDtoCreate(itemRequest);
        assertThat(test.getId(), equalTo(1));
        assertThat(test.getDescription(), equalTo("описание"));
    }

    @Test
    public void toItemRequestDtoTest() {
        ItemRequestDto test = RequestMapper.toItemRequestDto(itemRequest, list);
        assertThat(test.getId(), equalTo(1));
        assertThat(test.getDescription(), equalTo("описание"));
        assertThat(test.getItems(), hasSize(1));
        assertThat(test.getItems().get(0).getName(), equalTo("item1"));
        assertThat(test.getItems().get(0).getDescription(), equalTo("description item1"));
    }
}
