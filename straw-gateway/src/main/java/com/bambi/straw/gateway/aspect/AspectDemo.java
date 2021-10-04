package com.bambi.straw.gateway.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

/**
 * 下面的注解表示当前类是一个编写AOP增强的类，
 * 不写这个注解，这个类中编写切入点或增强代码无效
 */
@Aspect
@Component
@Slf4j
public class AspectDemo {
    //声明切入点: 指定要增强的方法
    @Pointcut("execution(public * com.bambi.straw.gateway.controller.TestController.test(..))")
    //下面这个方法不需要定义任何内容
    //只是使用方法名定义了当前切入点的名称
    public void pointCut(){

    }

    //利用方法的名称即切入点的名称对这个方法进行增强
    // 前置增强(在方法运行前运行代码)
    //@Before中填写方法名称，需要加()
    @Before("pointCut()")
    public void before(){
        log.debug("运行了aspect前置增强");
    }

    //后置增强
    @After("pointCut()")
    public void after(){
        log.debug("运行了aspect的后置增强");
    }

    //环绕增强
    @Around("pointCut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        log.debug("环绕增强开始");
        //运行指定的方法
        //方法执行后会有返回值，如果不接收并返回是没有办法得到返回值的
        //拦截器同理
        Object re = joinPoint.proceed();
        log.info("这是开始调用了,环绕增强结束");
        return re;
    }

    @AfterThrowing("pointCut()")
    public void afterThrowing(){
        log.error("发生了异常，开始走这个异常增强");
    }

}
