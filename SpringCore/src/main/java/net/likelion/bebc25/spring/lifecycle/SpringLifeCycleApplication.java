package net.likelion.bebc25.spring.lifecycle;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class SpringLifeCycleApplication {
    void main(){
        try{
            AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
//            TempFileSupport support = context.getBean(TempFileSupport.class);
            TempFileSupport2 support = context.getBean(TempFileSupport2.class);
            support.writeLog("사용자가 로그인 함.");
            context.close();    // 스프링 컨테이너 종료
        }catch(Exception e){
            e.printStackTrace();;
        }

    }
}