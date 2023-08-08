package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDto create(ItemRequestDto itemRequestDto, Integer userId);

    List<ItemRequestDto> getRequests(Integer requesterId);

    List<ItemRequestDto> getRequestsAllOthers(Integer userId, Integer size, Integer from);

    ItemRequestDto getRequestId(Integer userId, Integer itemId);
}
