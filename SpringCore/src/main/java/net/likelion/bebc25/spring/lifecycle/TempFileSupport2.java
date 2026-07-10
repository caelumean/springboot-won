package net.likelion.bebc25.spring.lifecycle;


import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

// 임시로 파일을 만들어서 파일에 로그를 출력
// 이 방식은 너무 스프링에 종속적이기 때문에
// 어노테이션 방식으로 하는 걸 추천한다
@Component
public class TempFileSupport2 implements InitializingBean, DisposableBean {
    @Value("resources/temp.log")
    private String filePath;    // 파일 경로

    public TempFileSupport2(){
        System.out.println("생성자 호출됨." + filePath);
    }

    // 네트워크, 파일시스템 연결 같은 무거운 작업을 수행할 초기화 메서드 정의
    // 비즈니스 메서드 호출 전에 실행해야 할 작업
    // 초기화 콜백
    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println(filePath + " 경로의 FileOutputStream 생성.");
    }

    public void writeLog(String msg){
        System.out.println(filePath + "에 로그 저장: " + msg);
    }


    // 소멸 콜백
    @Override
    public void destroy() throws Exception {
        System.out.println(filePath + " 경로의 FileOutPutStream 닫기.");

    }
}
