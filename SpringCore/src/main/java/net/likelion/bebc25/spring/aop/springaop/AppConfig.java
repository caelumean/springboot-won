package net.likelion.bebc25.spring.aop.springaop;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

// 설정을 엮어주는 작업을 이 곳에 한다.
// 스프링 컨테이너에 알려주는 앱 설정 클래스
@Configuration  // 설정 파일용이라는 것을 어노테이션으로 알려줘야한다.
// 스프링 컨테이너에 @Aspect 어노테이션이 붙은 빈(Bean)들을 찾아서 프록시 처리를 하도록 지시
@EnableAspectJAutoProxy
public class AppConfig {

    @Bean   // 스프링 빈으로 등록(메소드명인 car가 빈의 이름이 된다.)
    public Car car(){
        return new GasolineCar();
//        return new HybridCar();
    }

    @Bean
    public Driver driveCar(Car car){
        return new Driver(car); // DI
    }

    @Bean
    public LoggingAspect loggingAspect(){
        return new LoggingAspect();
    }
}
