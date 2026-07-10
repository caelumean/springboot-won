package net.likelion.bebc25.spring.aop.dynamicproxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class TimeCheckInvocationHandler implements InvocationHandler {
    // 진짜 Car 객체
    private final Car target;

    TimeCheckInvocationHandler(Car target){
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("[동적 프록시] 메서드 실행 전: " + method.getName());

        long start = System.currentTimeMillis();
        // target의 메서드 호출
        Object result = method.invoke(target, args);
        long end = System.currentTimeMillis();

        System.out.println("[동적 프록시] 메서드 실행 후: " + method.getName() + "(시간: " + (end - start) + "ms)");

        return result;
    }
}
