package com.gopay.app.contracts;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
@EqualsAndHashCode
@SuppressWarnings("PMD.TooManyMethods")
public class GenericResponse<T> {

    private final boolean success;
    private final List<Error> errors;
    private final T data;

    public GenericResponse(final List<Error> errors) {
        this.success = false;
        this.errors = new ArrayList<>(errors);
        this.data = null;

    }

    public GenericResponse(final T data) {
        this.success = true;
        this.errors = null;
        this.data = data;

    }

    public GenericResponse(final T data, final List<Error> errors) {
        this.success = false;
        this.errors = new ArrayList<>(errors);
        this.data = data;
    }


}
