package ru.practicum.shareit.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ErrorHandlerTest {
    private final ErrorHandler errorHandler = new ErrorHandler();

    @Test
    public void handleThrowable() {
        Exception exception = new Exception("INTERNAL_SERVER_ERROR");
        ErrorResponse response = errorHandler.handleThrowable(exception);
        assertEquals(response.getError(), exception.getMessage());
    }

    @Test
    public void handleObjectNotFoundException() {
        ObjectNotFoundException exception = new ObjectNotFoundException("NOT_FOUND");
        ErrorResponse response = errorHandler.handleThrowable(exception);
        assertEquals(response.getError(), exception.getMessage());
    }

    @Test
    public void handleBookingException() {
        BookingException exception = new BookingException("BAD_REQUEST");
        ErrorResponse response = errorHandler.handleThrowable(exception);
        assertEquals(response.getError(), exception.getMessage());
    }
}
