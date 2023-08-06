package ru.practicum.shareit.request;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.mapper.RequestMapper;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.request.service.ItemRequestServiceImpl;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.WARN)
public class RequestServiceTest {
    @Mock
    UserRepository userRepository;
    @Mock
    ItemRepository itemRepository;
    @Mock
    ItemRequestRepository requestRepository;
    ItemRequestService requestService;
    User user, user2;
    ItemRequest itemRequest1, itemRequest2;

    @BeforeEach
    public void beforeEach() {
        requestService = new ItemRequestServiceImpl(requestRepository, userRepository, itemRepository);
        user = new User(1, "user2", "user2@yandex.ru");
        user2 = new User(2, "user2", "user2@yandex.ru");
        itemRequest1 = new ItemRequest(1, "описание", user, LocalDateTime.now());
        itemRequest2 = new ItemRequest(2, "описание222", user2, LocalDateTime.now().plusMinutes(1));
    }

    @Test
    public void createItemRequestSuccessfulTest() {
        when(userRepository.findById(any()))
                .thenReturn(Optional.ofNullable(itemRequest1.getRequester()));

        when(requestRepository.findById(any()))
                .thenReturn(Optional.ofNullable(itemRequest1));

        when(requestRepository.save(any()))
                .thenReturn(itemRequest1);

        ItemRequestDto itemRequestDto = requestService.create(RequestMapper.toItemRequestDto(itemRequest1, null),
                itemRequest1.getRequester().getId());
        assertThat(itemRequestDto.getDescription(), equalTo(itemRequest1.getDescription()));
        assertThat(itemRequestDto.getCreated().toString(), equalTo(itemRequest1.getCreated().toString()));
    }

    @Test
    public void createUserNotFound() {
        Exception exception = Assertions.assertThrows(ObjectNotFoundException.class,
                () -> requestService.create(RequestMapper.toItemRequestDto(itemRequest1, null),
                        8));
        assertThat(exception.getMessage(), equalTo("Пользователь с данным id не найден!"));
    }

    @Test
    public void getRequestIdNotFoundUser() {
        Exception exception = Assertions.assertThrows(ObjectNotFoundException.class,
                () -> requestService.getRequestId(4, 1));
        assertThat(exception.getMessage(), equalTo("Пользователь не найден!"));
    }

    @Test
    public void getRequestIdNotFoundItem() {
        when(userRepository.existsById(user.getId()))
                .thenReturn(true);

        Exception exception = Assertions.assertThrows(ObjectNotFoundException.class,
                () -> requestService.getRequestId(1, 1));
        assertThat(exception.getMessage(), equalTo("Вещь не найдена!"));
    }

    @Test
    public void getRequestIdSuccessfulTest() {
        when(userRepository.existsById(user.getId()))
                .thenReturn(true);

        when(requestRepository.findById(any()))
                .thenReturn(Optional.ofNullable(itemRequest1));

        ItemRequestDto itemRequestDto = requestService.getRequestId(1, 1);
        assertThat(itemRequestDto.getId(), equalTo(itemRequest1.getId()));
        assertThat(itemRequestDto.getDescription(), equalTo(itemRequest1.getDescription()));
        assertThat(itemRequestDto.getCreated().toString(), equalTo(itemRequest1.getCreated().toString()));
    }

    @Test
    public void getRequestsAllOthersNotFoundUser() {
        Exception exception = Assertions.assertThrows(ObjectNotFoundException.class,
                () -> requestService.getRequestsAllOthers(7, 10, 1));
        assertThat(exception.getMessage(), equalTo("Пользователь не найден!"));
    }

    @Test
    public void getRequestsAllOthersInvalidSizeOrFrom() {
        when(userRepository.existsById(any()))
                .thenReturn(true);

        Exception exception = Assertions.assertThrows(IllegalArgumentException.class,
                () -> requestService.getRequestsAllOthers(itemRequest1.getRequester().getId(), 10, -9));
        assertThat(exception.getMessage(), equalTo("Page index must not be less than zero"));
    }

    @Test
    public void getRequestsAllOthers() {
        when(userRepository.existsById(any()))
                .thenReturn(true);

        when(requestRepository.findByRequesterIdIsNot(any(), any()))
                .thenReturn(List.of(itemRequest1));

        List<ItemRequestDto> list = requestService.getRequestsAllOthers(itemRequest2.getRequester().getId(), 10, 0);
        assertThat(list.size(), equalTo(1));
        assertThat(list.get(0).getDescription(), equalTo(itemRequest1.getDescription()));
        assertThat(list.get(0).getCreated().toString(), equalTo(itemRequest1.getCreated().toString()));
    }

    @Test
    public void getRequests() {
        when(userRepository.existsById(any()))
                .thenReturn(true);

        when(requestRepository.findByRequesterIdOrderByCreated(any()))
                .thenReturn(List.of(itemRequest1));

        List<ItemRequestDto> list = requestService.getRequests(itemRequest1.getRequester().getId());
        assertThat(list.size(), equalTo(1));
        assertThat(list.get(0).getDescription(), equalTo(itemRequest1.getDescription()));
        assertThat(list.get(0).getCreated().toString(), equalTo(itemRequest1.getCreated().toString()));
    }

    @Test
    public void getRequestsNotFoundUser() {
        Exception exception = Assertions.assertThrows(ObjectNotFoundException.class,
                () -> requestService.getRequests(itemRequest1.getRequester().getId()));
        assertThat(exception.getMessage(), equalTo("Пользователь не найден!"));
    }
}