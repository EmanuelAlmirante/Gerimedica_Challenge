package com.gerimedica.error;

import com.gerimedica.exception.TechnicalException;

public class TechnicalError extends RestError {
    private String message;

    private TechnicalError() {
    }

    public TechnicalError(Exception exception) {
        super(exception);
        this.message = exception.getMessage();
    }

    public String getMessage() {
        return message;
    }

    @Override
    public TechnicalException getException() {
        return new TechnicalException(message);
    }
}
