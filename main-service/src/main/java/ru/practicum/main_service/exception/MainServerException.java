package ru.practicum.main_service.exception;

public class MainServerException extends RuntimeException {
    public MainServerException(String message) {
        super(message);
    }
}
