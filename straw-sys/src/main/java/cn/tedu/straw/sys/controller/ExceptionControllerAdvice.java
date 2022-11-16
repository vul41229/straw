package cn.tedu.straw.sys.controller;

import cn.tedu.straw.commons.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ExceptionControllerAdvice {
    @ExceptionHandler
    public String handlerServiceException(ServiceException e){
        log.error("業務異常",e);
        return e.getMessage();
    }

    @ExceptionHandler
    public String handlerException(Exception e){
        log.error("其他異常",e);
        return e.getMessage();
    }
}
