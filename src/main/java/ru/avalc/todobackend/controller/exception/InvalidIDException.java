package ru.avalc.todobackend.controller.exception;

/**
 * @author Alexei Valchuk, 05.06.2023, email: a.valchukav@gmail.com
 */

public class InvalidIDException extends RuntimeException {

    public InvalidIDException(String message) {
        super(message);
    }

    public InvalidIDException(String message, Throwable cause) {
        super(message, cause);
    }
}
