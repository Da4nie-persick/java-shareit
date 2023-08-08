package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.model.Status;

import java.time.LocalDateTime;
import java.time.Month;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
public class BookingDtoTest {
    @Autowired
    JacksonTester<BookingDtoResponse> json;

    @Test
    public void bookingDtoTest() throws Exception {
        BookingDtoResponse bookingDtoResponse = new BookingDtoResponse(
                1,
                LocalDateTime.of(2023, Month.SEPTEMBER, 20, 10, 10, 10),
                LocalDateTime.of(2023, Month.SEPTEMBER, 23, 11, 11, 11),
                new BookingDtoResponse.Item(1, "item"),
                new BookingDtoResponse.User(1, "user"),
                Status.APPROVED);
        JsonContent<BookingDtoResponse> bookingDtoResponseJsonContent = json.write(bookingDtoResponse);
        assertThat(bookingDtoResponseJsonContent).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(bookingDtoResponseJsonContent).extractingJsonPathNumberValue("$.item.id").isEqualTo(1);
        assertThat(bookingDtoResponseJsonContent).extractingJsonPathStringValue("$.start");
        assertThat(bookingDtoResponseJsonContent).extractingJsonPathStringValue("$.end");
    }
}