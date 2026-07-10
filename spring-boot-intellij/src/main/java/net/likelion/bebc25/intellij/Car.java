package net.likelion.bebc25.intellij;

import org.springframework.stereotype.Component;

@Component
public interface Car {
    void startEngine();
    void drive();
    void stopEngine();
}
