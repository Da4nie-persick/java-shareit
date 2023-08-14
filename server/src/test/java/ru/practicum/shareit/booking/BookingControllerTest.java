package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.service.BookingService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
public class BookingControllerTest {
    @Autowired
    private final ObjectMapper mapper = new ObjectMapper();
    @Autowired
    private MockMvc mvc;
    @MockBean
    BookingService bookingService;

    private BookingDtoResponse bookingDtoResponse = new BookingDtoResponse(
            1,
            LocalDateTime.of(2023, Month.SEPTEMBER, 20, 10, 10, 10),
            LocalDateTime.of(2023, Month.SEPTEMBER, 23, 11, 11, 11),
            new BookingDtoResponse.Item(1, "item"),
            new BookingDtoResponse.User(1, "user"),
            Status.APPROVED);

    @Test
    public void creteBooking() throws Exception {
        when(bookingService.create(any(), anyInt())).thenReturn(bookingDtoResponse);

        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(bookingDtoResponse))
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDtoResponse.getId()), Integer.class))
                .andExpect(jsonPath("$.start", is(String.format("%s", bookingDtoResponse.getStart()))))
                .andExpect(jsonPath("$.end", is(String.format("%s", bookingDtoResponse.getEnd()))))
                .andExpect(jsonPath("$.item.id", is(1)))
                .andExpect(jsonPath("$.booker.id", is(1)))
                .andExpect(jsonPath("$.status", is(bookingDtoResponse.getStatus().toString())));
    }

    @Test
    public void approvedOrRejectedBooking() throws Exception {
        when(bookingService.approvedOrRejected(any(), anyInt(), anyInt())).thenReturn(bookingDtoResponse);

        mvc.perform(patch("/bookings" + "/1?approved=true")
                        .content(mapper.writeValueAsString(bookingDtoResponse))
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDtoResponse.getId()), Integer.class))
                .andExpect(jsonPath("$.start", is(String.format("%s", bookingDtoResponse.getStart()))))
                .andExpect(jsonPath("$.end", is(String.format("%s", bookingDtoResponse.getEnd()))))
                .andExpect(jsonPath("$.item.id", is(1)))
                .andExpect(jsonPath("$.booker.id", is(1)))
                .andExpect(jsonPath("$.status", is(bookingDtoResponse.getStatus().toString())));
    }

    @Test
    public void getBookingId() throws Exception {
        when(bookingService.getBookingId(anyInt(), anyInt()))
                .thenReturn(bookingDtoResponse);

        mvc.perform(get("/bookings" + "/1")
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDtoResponse.getId()), Integer.class))
                .andExpect(jsonPath("$.start", is(String.format("%s", bookingDtoResponse.getStart()))))
                .andExpect(jsonPath("$.end", is(String.format("%s", bookingDtoResponse.getEnd()))))
                .andExpect(jsonPath("$.item.id", is(1)))
                .andExpect(jsonPath("$.booker.id", is(1)))
                .andExpect(jsonPath("$.status", is(bookingDtoResponse.getStatus().toString())));
    }

    @Test
    public void getAllByUserId() throws Exception {
        when(bookingService.getAllByUserId(anyInt(), anyString(), anyInt(), anyInt()))
                .thenReturn(List.of(bookingDtoResponse));

        mvc.perform(get("/bookings" + "/?state=ALL&from=0&size=10")
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(bookingDtoResponse.getId()), Integer.class))
                .andExpect(jsonPath("$[0].start", is(String.format("%s", bookingDtoResponse.getStart()))))
                .andExpect(jsonPath("$[0].end", is(String.format("%s", bookingDtoResponse.getEnd()))))
                .andExpect(jsonPath("$[0].item.id", is(1)))
                .andExpect(jsonPath("$[0].booker.id", is(1)))
                .andExpect(jsonPath("$[0].status", is(bookingDtoResponse.getStatus().toString())));
    }

    @Test
    public void getAllByOwner() throws Exception {
        when(bookingService.getAllByOwner(anyInt(), anyString(), anyInt(), anyInt()))
                .thenReturn(List.of(bookingDtoResponse));

        mvc.perform(get("/bookings" + "/owner?state=ALL&from=0&size=10")
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(bookingDtoResponse.getId()), Integer.class))
                .andExpect(jsonPath("$[0].start", is(String.format("%s", bookingDtoResponse.getStart()))))
                .andExpect(jsonPath("$[0].end", is(String.format("%s", bookingDtoResponse.getEnd()))))
                .andExpect(jsonPath("$[0].item.id", is(1)))
                .andExpect(jsonPath("$[0].booker.id", is(1)))
                .andExpect(jsonPath("$[0].status", is(bookingDtoResponse.getStatus().toString())));
    }
}