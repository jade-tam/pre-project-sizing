package vn.gtel.pm2.sizing;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
@Slf4j
public class SizingApplication {

    public static void main(String[] args) {
        SpringApplication.run(SizingApplication.class, args);
    }

    @EventListener
    public void onApplicationEvent(ContextRefreshedEvent event) {
        try {
            System.out.println("==================================================");
            System.out.println("|               GTEL Application                  |");
            System.out.println("|               Develop by Gtel Team              |");
            System.out.println("==================================================");

            log.info("==================================================");
            log.info("|               GTEL Application                  |");
            log.info("|               Develop by Gtel Team              |");
            log.info("==================================================");
        } catch (Exception e) {

        }
    }
}
