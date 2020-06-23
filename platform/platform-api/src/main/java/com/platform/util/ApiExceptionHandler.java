package com.platform.util;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.SQLException;

@ControllerAdvice
public class ApiExceptionHandler {

    private static Logger logger = Logger.getLogger(ApiExceptionHandler.class);
    /**
     * 自定义异常
     */
    @ExceptionHandler(NullPointerException.class)
    public void handleRRException(NullPointerException e) {
        logger.info(e.getMessage());
        e.printStackTrace();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public void handleRRException(IllegalArgumentException e) {
        logger.info(e.getMessage());
        e.printStackTrace();
    }

    @ExceptionHandler(IndexOutOfBoundsException.class)
    public void handleRRException(IndexOutOfBoundsException e) {
        logger.info(e.getMessage());
        e.printStackTrace();
    }

    @ExceptionHandler(SQLException.class)
    public void handleRRException(SQLException e) {
        logger.info(e.getMessage());
        e.printStackTrace();
    }
}
