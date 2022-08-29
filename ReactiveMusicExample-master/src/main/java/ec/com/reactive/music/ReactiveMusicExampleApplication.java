package ec.com.reactive.music;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@SpringBootApplication
public class ReactiveMusicExampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReactiveMusicExampleApplication.class, args);
	}

}
