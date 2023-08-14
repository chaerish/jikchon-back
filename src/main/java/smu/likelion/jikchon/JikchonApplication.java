package smu.likelion.jikchon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class JikchonApplication {

	public static void main(String[] args) {
		SpringApplication.run(JikchonApplication.class, args);
	}

}
