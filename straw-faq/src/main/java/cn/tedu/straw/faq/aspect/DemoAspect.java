package cn.tedu.straw.faq.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class DemoAspect {
    @Pointcut("execution(public * cn.tedu.straw.faq.controller" + ".TagController.tags(..))")

    public void pointCut(){}

    @Before("pointCut()")
    public void before(){

        System.out.println("前置通知");
    }

    @After("pointCut()")
    public void after(){
        System.out.println("後置通知");
    }

    @Around("pointCut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("環繞通知運行之前");
        // ProceedingJoinPoint类型的参数具有调用切面方法的功能
        Object obj=joinPoint.proceed();
        System.out.println("環繞通知執行之後");
        return obj;
    }
}
