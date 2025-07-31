package io.github.andrehsvictor.mooral_api;

import org.springframework.boot.SpringApplication;

public class TestMooralApiApplication {

	public static void main(String[] args) {
		SpringApplication.from(MooralApiApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
