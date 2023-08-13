package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithBookingDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.User;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)

public class ItemServiceIntegrationTest {
    private final ItemService itemService;
    private final EntityManager manager;

    @Test
    public void getItemByOwner() {
        User user = new User(null, "user4", "user2@ygmai.ru");

        ItemDto itemDto = new ItemDto(null, "item1", "description item1", true, null);
        ItemDto itemDto2 = new ItemDto(null, "item2", "description item1", true, null);

        List<ItemDto> source = List.of(itemDto, itemDto2);
        manager.persist(user);

        for (ItemDto item : source) {
            manager.persist(ItemMapper.toItem(item, user));
        }

        manager.flush();
        TypedQuery<User> query = manager.createQuery("Select u from User u where u.email = :email", User.class);
        User userNew = query.setParameter("email", user.getEmail()).getSingleResult();
        List<ItemWithBookingDto> list = itemService.getItemByOwner(userNew.getId(), 10, 0);
        assertThat(list, hasSize(2));
        for (ItemDto itemList : source) {
            assertThat(list, Matchers.hasItem(allOf(
                    hasProperty("id", notNullValue()),
                    hasProperty("name", equalTo(itemList.getName())),
                    hasProperty("description", equalTo(itemList.getDescription())))));
        }
    }
}