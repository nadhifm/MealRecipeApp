package com.example.mealrecipeapp.utils;
public class Resource<T> {
    public enum Status { SUCCESS, ERROR, LOADING }

    private final Status status;
    private final T data;
    private final String message;

    private Resource(Status status, T data, String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }

    public static <T> Resource<T> success(T data) {
        return new Resource<>(Status.SUCCESS, data, null);
    }

    public static <T> Resource<T> error(String message, T data) {
        return new Resource<>(Status.ERROR, data, message);
    }

    public static <T> Resource<T> loading(T data) {
        return new Resource<>(Status.LOADING, data, null);
    }

    // Getters
    public Status getStatus() {
        return this.status;
    }

    public T getData() {
        return this.data;
    }

    public String getMessage() {
        return this.message;
    }
}
