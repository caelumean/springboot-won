package net.likelion.bebc25.spring.componentscan;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class Driver {
    private Car car;

//    Driver(){
//        System.out.println("Driver 기본생성자 호출됨.");
//    }

    // DI
    // Qualifier - Primary 무시하고 필요한 쪽에 지정한 것을 쓴다.
    // 필요한 빈을 직접 지정하는 방식
    // Autowired - 의존성 주입하라는 어노테이션 / 생성자가 하나만 있을 경우에는 생략 가능
//    @Autowired
//    Driver(@Qualifier("gasolineCar") Car car){
//        System.out.println("Constructor Injection 호출" + car);
//        this.car = car;
//    }

    public void setCar(Car car) {
        System.out.println("Setting Injection 호출됨.");
        this.car = car;
    }

    public void driveCar(int maxSpeed){
        car.startEngine();
        car.drive();
        car.stopEngine();
    }
}
