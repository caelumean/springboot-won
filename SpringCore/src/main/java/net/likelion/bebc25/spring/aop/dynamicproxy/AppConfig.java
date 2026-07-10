package net.likelion.bebc25.spring.aop.dynamicproxy;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Proxy;

// 설정을 엮어주는 작업을 이 곳에 한다.
// 스프링 컨테이너에 알려주는 앱 설정 클래스
@Configuration  // 설정 파일용이라는 것을 어노테이션으로 알려줘야한다.
public class AppConfig {

    @Bean   // 스프링 빈으로 등록(메소드명인 car가 빈의 이름이 된다.)
    public Car car(){
        Car target = new HybridCar();

        // 동적 프록시 생성
        // LogProxy를 만들어준다.
        Car proxyCar = (Car)Proxy.newProxyInstance(
                Car.class.getClassLoader(),     // 클래스 로더
                new Class[]{Car.class},         // 구현할 인터페이스 목록
                new TimeCheckInvocationHandler(target)  // 로직을 구현한 핸들러

        );

        return proxyCar;

    }

    @Bean
    public Driver driveCar(Car car){
        return new Driver(car); // DI
    }
}

