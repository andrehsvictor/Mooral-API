package andrehsvictor.mooral;

import org.springframework.boot.SpringApplication;

public class TestMooralApplication {

	public static void main(String[] args) {
		SpringApplication.from(MooralApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
