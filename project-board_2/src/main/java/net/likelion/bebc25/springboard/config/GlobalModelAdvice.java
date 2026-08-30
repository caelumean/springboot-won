package net.likelion.bebc25.springboard.config;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import java.net.InetAddress;

@ControllerAdvice
public class GlobalModelAdvice {

    @ModelAttribute("serverName")
    public String getServerName() {
        String hostname = System.getenv("HOSTNAME");
        if (hostname == null || hostname.isBlank()) {
            try {
                hostname = InetAddress.getLocalHost().getHostName();
            } catch (Exception e) {
                hostname = "board-app";
            }
        }
        return hostname;
    }
}