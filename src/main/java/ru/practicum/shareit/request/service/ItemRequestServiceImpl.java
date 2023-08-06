package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.request.mapper.RequestMapper;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    @Transactional
    public ItemRequestDto create(ItemRequestDto itemRequestDto, Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("Пользователь с данным id не найден!"));
        ItemRequest createRequest = itemRequestRepository.save(RequestMapper.toItemRequest(itemRequestDto, user));
        return RequestMapper.toItemRequestDtoCreate(createRequest);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemRequestDto> getRequests(Integer requesterId) {
        if (!userRepository.existsById(requesterId)) {
            throw new ObjectNotFoundException("Пользователь не найден!");
        }
        List<ItemRequest> itemRequestList = itemRequestRepository.findByRequesterIdOrderByCreated(requesterId);
        return itemRequestList.stream()
                .map(itemRequest -> {
                    List<Item> itemsList = itemRepository.findByRequestId(itemRequest.getId(), Sort.by("id").descending());
                    return RequestMapper.toItemRequestDto(itemRequest, itemsList);
                }).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemRequestDto> getRequestsAllOthers(Integer userId, Integer size, Integer from) {
        if (!userRepository.existsById(userId)) {
            throw new ObjectNotFoundException("Пользователь не найден!");
        }
        List<ItemRequest> itemRequestList = itemRequestRepository.findByRequesterIdIsNot(userId,
                PageRequest.of(from , size, Sort.by("created")));
        return itemRequestList.stream()
                .map(itemRequest -> {
                    List<Item> itemsList = itemRepository.findByRequestId(itemRequest.getId(), Sort.by("id").descending());
                    return RequestMapper.toItemRequestDto(itemRequest, itemsList);
                }).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ItemRequestDto getRequestId(Integer userId, Integer id) {
        if (!userRepository.existsById(userId)) {
            throw new ObjectNotFoundException("Пользователь не найден!");
        }
        ItemRequest itemRequest = itemRequestRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Запрос не найден!"));

        List<Item> itemsList = itemRepository.findByRequestId(itemRequest.getId(), Sort.by("id").descending());
        return RequestMapper.toItemRequestDto(itemRequest, itemsList);
    }
}
