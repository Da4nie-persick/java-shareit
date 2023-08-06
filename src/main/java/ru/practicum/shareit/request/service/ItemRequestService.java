package ru.practicum.shareit.request.service;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

public interface ItemRequestService {
    @Transactional
    ItemRequestDto create(ItemRequestDto itemRequestDto, Integer userId);

    @Transactional
    List<ItemRequestDto> getRequests(Integer requesterId);

    @Transactional
    List<ItemRequestDto> getRequestsAllOthers(Integer userId, Integer size, Integer from);

    @Transactional
    ItemRequestDto getRequestId(Integer userId, Integer itemId);
}
