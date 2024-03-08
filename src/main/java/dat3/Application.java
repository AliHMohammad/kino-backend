package dat3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

//Remove the exclude when/if security is needed
@SpringBootApplication( exclude = {SecurityAutoConfiguration.class} )
//@SpringBootApplication( )
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
