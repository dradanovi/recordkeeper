package hr.java.web.radanovic.recordkeeper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import hr.java.web.radanovic.recordkeeper.configuration.SwaggerConfig;

@SpringBootApplication
@EnableJpaRepositories("hr.java.web.radanovic.recordkeeper.repository")
@Import(SwaggerConfig.class)
public class Recordkeeper2Application {

	public static void main(String[] args) {
		SpringApplication.run(Recordkeeper2Application.class, args);
	}

}
