package net.likelion.bebc25.spring.aop.staticproxy;

public class LogProxy implements Car{
    // 진짜 Car 객체
    private final Car target;

    public LogProxy(Car target){
        this.target = target;
    }
    @Override
    public void startEngine() {
        System.out.println("[메서드 실행 전] 엔진을 점검합니다.");
        target.startEngine();

    }

    @Override
    public void drive() {
        System.out.println("[메서드 실행 전후] 안전벨트를 맵니다.");
        target.drive();
        System.out.println("[메서드 실행 전후] 안전벨트를 풉니다.");

    }

    @Override
    public void stopEngine() {
        target.stopEngine();
        System.out.println("[메서드 실행 후] 하차합니다.");
    }
}
