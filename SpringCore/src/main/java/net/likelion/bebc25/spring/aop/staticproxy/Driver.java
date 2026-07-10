package net.likelion.bebc25.spring.aop.staticproxy;

public class Driver {
    private final Car car;

    // DI
    Driver(Car car){
        System.out.println("Constructor Injection 호출 " + car);
        this.car = car;
    }

    public void driveCar(){
        car.startEngine();
        car.drive();
        car.stopEngine();
    }
}
