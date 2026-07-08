package net.likelion.bebc25.oop.before;

public class Driver {
    private GasolineCar car= new GasolineCar();

    public void dreveCar(){
        car.startEngine();
        car.drive();
        car.stopEngine();
    }
}
