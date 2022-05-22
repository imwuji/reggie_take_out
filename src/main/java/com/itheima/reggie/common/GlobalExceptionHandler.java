package com.itheima.reggie.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;

@ResponseBody
@Slf4j
@ControllerAdvice(annotations = {RestController.class})
public class GlobalExceptionHandler {


    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> exceptionHandler(SQLIntegrityConstraintViolationException e) {

        if (e.getMessage().contains("Duplicate entry")) {
            String[] strings = e.getMessage().split(" ");
            String msg = strings[2] + "已经存在";
            return R.error(msg);
        }

        log.error("fail:{}", e.getMessage());
        return R.error("fail");
    }
}
