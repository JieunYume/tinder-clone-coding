package com.hot6.pnureminder.controller;

import com.hot6.pnureminder.dto.error.ErrorDto;
import com.hot6.pnureminder.dto.error.UnknownErrorDto;
import com.hot6.pnureminder.exception.CustomException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.hot6.pnureminder.exception.ErrorCode.INTERNAL_SERVER_ERROR;


@RestControllerAdvice
public class ExceptionHandlers {
    @ExceptionHandler({ CustomException.class })
    protected ResponseEntity handleCustomException(CustomException ex) {
        return new ResponseEntity(new ErrorDto(ex.getErrorCode().getStatus(), ex.getErrorCode().getMessage()), HttpStatus.valueOf(ex.getErrorCode().getStatus()));
    }

    @ExceptionHandler({ Exception.class })
    protected ResponseEntity handleServerException(Exception ex) {
        System.out.println(ex.getMessage());
        ex.printStackTrace();
        return new ResponseEntity(new UnknownErrorDto(INTERNAL_SERVER_ERROR.getStatus(), INTERNAL_SERVER_ERROR.getMessage(), ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
