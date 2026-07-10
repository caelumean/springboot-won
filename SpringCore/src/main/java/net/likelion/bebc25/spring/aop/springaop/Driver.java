package net.likelion.bebc25.spring.aop.springaop;

public class Driver {
    private final Car car;

    // DI
    Driver(Car car){
        System.out.println("Constructor Injection 호출");
        this.car = car;
    }

    public void driveCar(int maxSpeed){
        car.startEngine();
        car.drive();
        car.stopEngine();
    }
}
