package by.vstu.electronicjournal.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.transaction.NotSupportedException;
import java.io.IOException;
import java.text.ParseException;
import java.util.NoSuchElementException;

@RestController
@ControllerAdvice
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ControllerExceptions {

    @ExceptionHandler({EntityNotFoundException.class,
            IllegalArgumentException.class,
            MissingServletRequestParameterException.class,
            HttpRequestMethodNotSupportedException.class,
            JsonProcessingException.class,
            HttpMessageNotReadableException.class,
            IllegalAccessException.class,
            ParseException.class,
            NoSuchElementException.class,
            EmptyResultDataAccessException.class,
            EntityExistsException.class,
            NullPointerException.class,
            NotSupportedException.class,
            ClassCastException.class,
            IOException.class
    })
    public String exceptionHandler(Exception e) {
        return e.getMessage() != null ? e.getMessage() : e.getLocalizedMessage();
    }
}
