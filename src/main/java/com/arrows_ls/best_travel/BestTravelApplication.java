package com.arrows_ls.best_travel;

import com.arrows_ls.best_travel.domain.repositories.mongo.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BestTravelApplication implements CommandLineRunner {

	@Autowired
	private AppUserRepository appUserRepository;

	public static void main(String[] args) {
		SpringApplication.run(BestTravelApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		this.appUserRepository.findAll().forEach(System.out::println);
		System.out.println(this.appUserRepository.findByUsername("ragnar777").orElseThrow());
	}
}
