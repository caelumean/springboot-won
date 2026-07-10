package net.likelion.bebc25.spring.componentscan;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

// 빈의 이름을 hCar로 지정됨
@Component(/*value=*/"hCar")
// 같은 Car 타입의 Bean이 Gasoline,Hybrid가 있어서 구분을 무엇을 쓸지 모를 때
// 어떤 걸 메인으로 할지 정해줄때 Primary를 쓴다.
@Primary
public class HybridCar implements Car {
    @Override
    public void startEngine(){
        System.out.println("시스템 전원을 켜서 하이브리드 시동을 켭니다.");
    }
    @Override
    public void drive(){
        System.out.println("가솔린과 전기를 사용하여 주행합니다.");
    }
    @Override
    public void stopEngine(){
        System.out.println("하이브리드 시스템 종료 처리를 합니다.");
    }
}
