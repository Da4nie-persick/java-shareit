package ru.practicum.shareit.request;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

@DataJpaTest
public class RequestRepositoryTest {
    @Autowired
    private TestEntityManager em;

    @Autowired
    ItemRequestRepository repository;

    User user = new User(null, "user4", "user2@ygmai.ru");
    User user2 = new User(null, "user", "u@gmai.ru");

    @Test
    public void contextLoads() {
        Assertions.assertNotNull(em);
    }

    @Test
    public void findByRequesterIdOrderByCreatedTest() {
        ItemRequest itemRequest1 = new ItemRequest(null, "колбаса", user, LocalDateTime.now());
        ItemRequest itemRequest2 = new ItemRequest(null, "печенье", user, LocalDateTime.now());
        em.persist(user);
        em.persist(itemRequest1);
        em.persist(itemRequest2);
        List<ItemRequest> items = repository.findByRequesterIdOrderByCreated(user.getId());
        assertThat(items, hasSize(2));
        assertThat(items.get(0), equalTo(itemRequest1));
        assertThat(items.get(1), equalTo(itemRequest2));
    }

    @Test
    public void findByRequesterIdIsNot() {
        ItemRequest itemRequest1 = new ItemRequest(null, "колбаса", user, LocalDateTime.now());
        ItemRequest itemRequest2 = new ItemRequest(null, "печенье", user2, LocalDateTime.now());
        em.persist(user);
        em.persist(user2);
        em.persist(itemRequest1);
        em.persist(itemRequest2);
        List<ItemRequest> items = repository.findByRequesterIdIsNot(user.getId(), PageRequest.of(0 / 10, 10));
        assertThat(items, hasSize(1));
        assertThat(items.get(0), equalTo(itemRequest2));
    }
}