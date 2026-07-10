package net.likelion.bebc25.spring.di.setter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// 설정을 엮어주는 작업을 이 곳에 한다.
// 스프링 컨테이너에 알려주는 앱 설정 클래스
@Configuration  // 설정 파일용이라는 것을 어노테이션으로 알려줘야한다.
public class AppConfig {

    @Bean   // 스프링 빈으로 등록(메소드명인 car가 빈의 이름이 된다.)
    public Car car(){
//        return new GasolineCar();
        return new HybridCar();
    }

    @Bean
    public Driver driver(Car car){
        Driver driver = new Driver();   // DI
        driver.setCar(car); // setter
        return driver;

    }
}

// 위에 코드를 보고
// 스프링 컨테이너가 하는 일
//AppConfig config = new AppConfig();
//Car car = config.car();
//Driver driver = config.driver(car);
