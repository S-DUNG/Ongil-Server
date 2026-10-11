package sdung.ongil;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class OngilApplication {

    public static void main(String[] args) {
        SpringApplication.run(OngilApplication.class, args);
    }

}
