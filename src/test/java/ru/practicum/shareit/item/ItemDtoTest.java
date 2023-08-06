package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.ItemDto;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
public class ItemDtoTest {
    @Autowired
    JacksonTester<ItemDto> json;

    @Test
    public void itemDtoTest() throws Exception {
        ItemDto itemDto = new ItemDto(1, "item1", "description item1", true, null);
        JsonContent<ItemDto> itemDtoJsonContent = json.write(itemDto);
        assertThat(itemDtoJsonContent).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(itemDtoJsonContent).extractingJsonPathStringValue("$.name").isEqualTo("item1");
        assertThat(itemDtoJsonContent).extractingJsonPathStringValue("$.description").isEqualTo("description item1");
        assertThat(itemDtoJsonContent).extractingJsonPathBooleanValue("$.available").isEqualTo(true);
    }
}