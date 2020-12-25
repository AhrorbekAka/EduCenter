package ecma.demo.educenter;

import ecma.demo.educenter.entity.User;
import ecma.demo.educenter.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.HashSet;

@SpringBootApplication
@EnableScheduling
public class EduCenterApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(EduCenterApplication.class, args);
    }

    @Autowired
    private UserRepository userRepository;
    @Override
    public void run(String... args) throws Exception {
//        this.userRepository.save(new User("asdf", "Abror", "Nematov", "+998955155885", new HashSet<>()));
    }
}
