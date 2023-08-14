package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.User;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class RequestServiceIntegrationTest {
    private final ItemRequestService itemRequestService;
    private final EntityManager manager;

    @Test
    public void getRequests() {
        User user3 = new User(null, "user2", "user2@yandex.ru");
        User user4 = new User(null, "booker", "user2@ygmai.ru");

        List<User> sourceUser = List.of(user3, user4);

        ItemRequest itemRequest1 = new ItemRequest(
                null,
                "колбаса",
                user3,
                LocalDateTime.of(2023, Month.SEPTEMBER, 20, 10, 10));

        ItemRequest itemRequest2 = new ItemRequest(
                null,
                "печенье",
                user3,
                LocalDateTime.of(2023, Month.SEPTEMBER, 20, 11, 11));

        List<ItemRequest> sourceItemRequest = List.of(itemRequest1, itemRequest2);

        for (User user : sourceUser) {
            manager.persist(user);
        }

        for (ItemRequest itemRequest : sourceItemRequest) {
            manager.persist(itemRequest);
        }

        manager.flush();
        TypedQuery<User> query = manager.createQuery("Select u from User u where u.email = :email", User.class);
        User userNew = query.setParameter("email", user4.getEmail()).getSingleResult();
        List<ItemRequestDto> list = itemRequestService.getRequestsAllOthers(userNew.getId(), 10, 0);
        assertThat(list, hasSize(sourceItemRequest.size()));
        for (ItemRequest itemRequest : sourceItemRequest) {
            assertThat(list, Matchers.hasItem(allOf(
                    hasProperty("id", notNullValue()),
                    hasProperty("description", equalTo(itemRequest.getDescription())),
                    hasProperty("created", equalTo(itemRequest.getCreated()))
            )));
        }
    }
}