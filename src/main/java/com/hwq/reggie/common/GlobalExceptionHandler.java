package com.hwq.reggie.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;

@ControllerAdvice(annotations = RestController.class)
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> handleException(SQLIntegrityConstraintViolationException ex) {

        log.error("print error message:"+ex.getMessage());
        if(ex.getMessage().contains("Duplicate entry"))
        {
         String[] split = ex.getMessage().split(" ");
         String msg = split[2] + "is already exsited";
         return R.error(msg);
        }
        return R.error("unknown mistake");
    }

    @ExceptionHandler(CustomException.class)
    public R<String> handleException(CustomException ce) {

        log.error("print error message:"+ce.getMessage());

            return R.error(ce.getMessage());


    }





}
