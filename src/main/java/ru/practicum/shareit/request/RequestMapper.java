package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestDto;

public class RequestMapper {
    public static ItemRequestDto toItemRequestDto(ItemRequest itemRequest) {
        ItemRequestDto.User requester = new ItemRequestDto.User();
        if (itemRequest.getRequester() != null) {
            requester.setId(itemRequest.getRequester().getId());
        }
        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setId(itemRequest.getId());
        itemRequestDto.setDescription(itemRequestDto.getDescription());
        itemRequestDto.setRequester(requester);
        itemRequestDto.setCreated(itemRequest.getCreated());
        return itemRequestDto;
    }
}
