package net.likelion.bebc25.oop.after;

public class Driver {
    private Car car;

    // DI
    Driver(Car car){
        this.car = car;
    }

    public void dreveCar(){
        car.startEngine();
        car.drive();
        car.stopEngine();
    }
}
