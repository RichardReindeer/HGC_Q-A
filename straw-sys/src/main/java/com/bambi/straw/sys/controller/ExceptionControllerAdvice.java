package com.bambi.straw.sys.controller;


import com.bambi.straw.commons.service.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 统一异常处理类
 * @RestControllerAdvice表示统一处理所有控制器的增强
 */
@RestControllerAdvice
@Slf4j
public class ExceptionControllerAdvice {

    //这个类实际上可以对当前控制层所有方法进行前置后置的增强
    //但是现在只处理异常

    //当控制器发生异常时，匹配下面的异常处理
    @ExceptionHandler
    //这个方法用于处理控制器发生ServiceException
    public R handlerServiceException(ServiceException e){
        log.error("发生业务异常",e);
        return R.failed(e);
    }

    //这个方法中可以编写多种异常的处理
    //现在项目中我们主要需要处理的异常就是ServiceException
    //其他异常都有Exception父类来处理就可以了
    @ExceptionHandler
    public R handlerException(Exception e){
        log.error("其他异常",e);
        return R.failed(e);
    }
}
