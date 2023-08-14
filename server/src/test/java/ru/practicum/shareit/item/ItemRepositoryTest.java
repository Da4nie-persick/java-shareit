package ru.practicum.shareit.item;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

@DataJpaTest
public class ItemRepositoryTest {
    @Autowired
    private TestEntityManager em;

    @Autowired
    ItemRepository itemRepository;

    User user = new User(null, "user4", "user2@ygmai.ru");
    User user2 = new User(null, "us", "dasha141099@yandex.ru");

    @Test
    public void contextLoads() {
        Assertions.assertNotNull(em);
    }

    @Test
    public void searchItem() {
        Item item1 = new Item(null, "item1", "description item1", true, user, null);
        Item item2 = new Item(null, "name", "item2", true, user, null);
        em.persist(user);
        em.persist(item1);
        em.persist(item2);
        List<Item> items = itemRepository.searchItem("ITeM", PageRequest.of(0 / 10, 10));
        assertThat(items, hasSize(2));
        assertThat(items.get(0), equalTo(item1));
        assertThat(items.get(1), equalTo(item2));
    }

    @Test
    public void getAllByOwnerId() {
        Item item1 = new Item(null, "item1", "description item1", true, user, null);
        Item item2 = new Item(null, "name", "item2", true, user2, null);
        em.persist(user);
        em.persist(user2);
        em.persist(item1);
        em.persist(item2);
        List<Item> items = itemRepository.getAllByOwnerId(user2.getId(), PageRequest.of(0 / 10, 10));
        assertThat(items, hasSize(1));
        assertThat(items.get(0), equalTo(item2));
    }

    @Test
    public void findByRequestId() {
        Item item1 = new Item(null, "item1", "description item1", true, user, null);
        Item item2 = new Item(null, "name", "item2", true, user, null);
        em.persist(user);
        em.persist(item1);
        em.persist(item2);
        List<Item> items = itemRepository.findByRequestId(6, Sort.by("id").descending());
        assertThat(items, hasSize(0));
    }
}
