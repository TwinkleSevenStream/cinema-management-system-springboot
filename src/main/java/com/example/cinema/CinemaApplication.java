package     com.example.cinema;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

//开启对定时任务的支持
@EnableScheduling

//表明这是一个webApp, 并且会自动扫描本包即所有子包。检查到注解后会自动装配到bean，实现整个架构。
@SpringBootApplication
public class CinemaApplication {
    public static void main(String[] args) {
        SpringApplication.run(CinemaApplication.class, args);
    }
}


