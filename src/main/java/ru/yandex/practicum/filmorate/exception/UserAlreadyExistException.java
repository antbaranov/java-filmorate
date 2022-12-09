package ru.yandex.practicum.filmorate.exception;

public class UserAlreadyExistException extends RuntimeException {
    public UserAlreadyExistException(String string) {
        super(string);
    }

}
